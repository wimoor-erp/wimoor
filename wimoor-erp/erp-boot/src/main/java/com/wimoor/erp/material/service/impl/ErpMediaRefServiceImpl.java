package com.wimoor.erp.material.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.material.mapper.ErpMediaMapper;
import com.wimoor.erp.material.mapper.ErpMediaMapper.ErpMediaWithRef;
import com.wimoor.erp.material.mapper.ErpMediaRefMapper;
import com.wimoor.erp.material.pojo.entity.ErpMedia;
import com.wimoor.erp.material.pojo.entity.ErpMediaRef;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IErpMediaRefService;
import com.wimoor.erp.material.service.IMaterialService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 媒体-商品关联服务实现。
 *
 * 核心规则：
 *  - 唯一性：同一 media+material+platform+marketplace_id+slot_position 不可重复（DB 唯一键兜底）。
 *  - 主图同步：is_main=1 变更时同步 t_erp_material.image（通过 mediaService 获取 location/url）。
 *  - 主图降级：取消主图/删除主图时，自动将 sort_order 最小的展示图升为主图。
 *  - 首图主图：SKU 第一张展示图自动 is_main=1（assign 时检测）。
 *
 * @author wimoor
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ErpMediaRefServiceImpl extends ServiceImpl<ErpMediaRefMapper, ErpMediaRef>
        implements IErpMediaRefService {

    private final ErpMediaMapper mediaMapper;
    @Autowired
    @Lazy
    private IMaterialService materialService;
    private final com.wimoor.amazon.api.AmzProductMediaFeignClient amazonClient;
    @Autowired(required = false)
    private RedisTemplate<Object, Object> redisTemplate;

    private static final String CACHE_LIST_PREFIX = "erp:media:list:";
    private static final String CACHE_POOL_PREFIX = "erp:media:pool:";
    private static final String CACHE_MAIN_PREFIX = "erp:media:main:";
    private static final long CACHE_TTL_SECONDS = 3600L;

    @SuppressWarnings("unchecked")
    private <T> T cacheGet(String key) {
        if (redisTemplate == null) return null;
        try {
            return (T) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.warn("Redis cache get failed key={}, fall back to DB. err={}", key, e.getMessage());
            return null;
        }
    }

    private void cachePut(String key, Object value) {
        if (redisTemplate == null || value == null) return;
        try {
            redisTemplate.opsForValue().set(key, value, CACHE_TTL_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("Redis cache put failed key={}, err={}", key, e.getMessage());
        }
    }

    private void evictMaterial(String materialId) {
        if (redisTemplate == null || materialId == null) return;
        try {
            redisTemplate.delete(CACHE_LIST_PREFIX + materialId);
            redisTemplate.delete(CACHE_POOL_PREFIX + materialId);
            redisTemplate.delete(CACHE_MAIN_PREFIX + materialId);
        } catch (Exception e) {
            log.warn("Redis cache evict failed materialId={}, err={}", materialId, e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErpMediaRef assign(String mediaId, String materialId, int refType, Integer picClass,
                              String platform, String marketplaceId, String slot, UserInfo userInfo) {
        if (mediaId == null || materialId == null) {
            throw new ERPBizException("mediaId/materialId 不能为空");
        }
        ErpMedia media = mediaMapper.selectById(mediaId);
        if (media == null) {
            throw new ERPBizException("媒体不存在: " + mediaId);
        }
        if (userInfo != null && userInfo.getCompanyid() != null
                && !userInfo.getCompanyid().equals(media.getShopid())) {
            throw new ERPBizException("无权操作其他租户的媒体");
        }
        Material material = materialService.getById(materialId);
        if (material == null) {
            throw new ERPBizException("商品不存在: " + materialId);
        }

        // 查询是否已存在该唯一组合
        LambdaQueryWrapper<ErpMediaRef> qw = new LambdaQueryWrapper<>();
        qw.eq(ErpMediaRef::getMediaId, mediaId)
          .eq(ErpMediaRef::getMaterialId, materialId)
          .eq(ErpMediaRef::getPlatform, platform == null ? "" : platform)
          .eq(ErpMediaRef::getMarketplaceId, marketplaceId == null ? "" : marketplaceId)
          .eq(ErpMediaRef::getSlotPosition, slot == null ? "" : slot);
        ErpMediaRef existing = this.getOne(qw);
        if (existing != null) {
            return existing;
        }

        ErpMediaRef ref = new ErpMediaRef();
        ref.setMediaId(mediaId);
        ref.setMaterialId(materialId);
        ref.setShopid(media.getShopid());
        ref.setRefType(refType);
        ref.setPicClass(picClass);
        ref.setSortOrder(nextSortOrder(materialId, refType));
        ref.setPlatform(platform);
        ref.setMarketplaceId(marketplaceId);
        ref.setSlotPosition(slot);
        ref.setCreator(userInfo == null ? null : userInfo.getId());
        ref.setCreateTime(new Date());

        // 首图自动主图：SKU 展示图且当前 material 还没有主图
        if (refType == ErpMediaRef.REF_TYPE_SKU_DISPLAY) {
            ErpMediaRef currentMain = this.baseMapper.selectMainByMaterial(materialId, refType);
            if (currentMain == null) {
                ref.setIsMain(ErpMediaRef.MAIN_YES);
            } else {
                ref.setIsMain(ErpMediaRef.MAIN_NO);
            }
        } else {
            ref.setIsMain(ErpMediaRef.MAIN_NO);
        }

        this.save(ref);

        // 同步主图字段
        if (ErpMediaRef.MAIN_YES == (ref.getIsMain() == null ? 0 : ref.getIsMain())) {
            syncMaterialImage(materialId, media);
        }
        evictMaterial(materialId);
        return ref;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ErpMediaRef> batchAssign(List<String> mediaIds, String materialId, int refType,
                                        Integer picClass, String platform, String marketplaceId,
                                        UserInfo userInfo) {
        List<ErpMediaRef> result = new ArrayList<>();
        if (mediaIds == null || mediaIds.isEmpty()) {
            return result;
        }
        for (String mid : mediaIds) {
            result.add(assign(mid, materialId, refType, picClass, platform, marketplaceId, null, userInfo));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unassign(String refId, UserInfo userInfo) {
        ErpMediaRef ref = this.getById(refId);
        if (ref == null) {
            return false;
        }
        boolean wasMain = ref.getIsMain() != null && ref.getIsMain() == ErpMediaRef.MAIN_YES;
        String materialId = ref.getMaterialId();
        Integer refType = ref.getRefType();
        boolean removed = this.removeById(refId);
        if (removed && wasMain && refType != null && refType == ErpMediaRef.REF_TYPE_SKU_DISPLAY) {
            promoteFallbackMain(materialId);
        }
        if (removed) evictMaterial(materialId);
        return removed;
    }

    @Override
    public List<ErpMediaWithRef> list(String materialId) {
        String key = CACHE_LIST_PREFIX + materialId;
        List<ErpMediaWithRef> cached = cacheGet(key);
        if (cached != null) return cached;
        List<ErpMediaWithRef> data = mediaMapper.selectByMaterialId(materialId, ErpMediaRef.REF_TYPE_SKU_DISPLAY);
        cachePut(key, data);
        return data;
    }

    @Override
    public List<ErpMediaWithRef> pool(String materialId) {
        String key = CACHE_POOL_PREFIX + materialId;
        List<ErpMediaWithRef> cached = cacheGet(key);
        if (cached != null) return cached;
        List<ErpMediaWithRef> data = mediaMapper.selectByMaterialId(materialId, ErpMediaRef.REF_TYPE_SPU_POOL);
        cachePut(key, data);
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setMain(String refId, UserInfo userInfo) {
        ErpMediaRef target = this.getById(refId);
        if (target == null) {
            throw new ERPBizException("关联记录不存在: " + refId);
        }
        if (target.getRefType() == null || target.getRefType() != ErpMediaRef.REF_TYPE_SKU_DISPLAY) {
            throw new ERPBizException("仅 SKU 展示图可设置为主图");
        }
        // 旧主图降级
        this.update(new LambdaUpdateWrapper<ErpMediaRef>()
                .eq(ErpMediaRef::getMaterialId, target.getMaterialId())
                .eq(ErpMediaRef::getRefType, ErpMediaRef.REF_TYPE_SKU_DISPLAY)
                .set(ErpMediaRef::getIsMain, ErpMediaRef.MAIN_NO));
        // 新主图升级
        target.setIsMain(ErpMediaRef.MAIN_YES);
        boolean ok = this.updateById(target);
        if (ok) {
            ErpMedia media = mediaMapper.selectById(target.getMediaId());
            syncMaterialImage(target.getMaterialId(), media);
            evictMaterial(target.getMaterialId());
        }
        return ok;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sort(List<String> refIds, UserInfo userInfo) {
        if (refIds == null || refIds.isEmpty()) {
            return true;
        }
        int order = 0;
        String firstMaterialId = null;
        for (String id : refIds) {
            ErpMediaRef r = this.getById(id);
            if (r != null && firstMaterialId == null) firstMaterialId = r.getMaterialId();
            this.update(new LambdaUpdateWrapper<ErpMediaRef>()
                    .eq(ErpMediaRef::getId, id)
                    .set(ErpMediaRef::getSortOrder, order));
            order += 10;
        }
        if (firstMaterialId != null) evictMaterial(firstMaterialId);
        return true;
    }

    @Override
    public boolean updateUsage(String refId, int picClass, UserInfo userInfo) {
        return this.update(new LambdaUpdateWrapper<ErpMediaRef>()
                .eq(ErpMediaRef::getId, refId)
                .set(ErpMediaRef::getPicClass, picClass));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncFromAmazon(String materialId, String sku, String authorityId, String marketplaceId,
                              UserInfo userInfo) {
        if (materialId == null || sku == null || authorityId == null) {
            throw new ERPBizException("materialId/sku/authorityId 不能为空");
        }
        // 通过 Feign 获取 Amazon 端缓存的媒体
        List<com.wimoor.amazon.api.AmzProductMediaDTO> remote;
        try {
            remote = amazonClient.listBySku(authorityId, marketplaceId, sku);
        } catch (Exception e) {
            throw new ERPBizException("调用 Amazon 服务失败: " + e.getMessage());
        }
        if (remote == null || remote.isEmpty()) {
            return 0;
        }
        int imported = 0;
        for (com.wimoor.amazon.api.AmzProductMediaDTO dto : remote) {
            if (dto.getUrl() == null) continue;
            // 简化：使用 url 作为去重 key 写入 t_erp_media（不做下载落地）
            ErpMedia media = new ErpMedia();
            media.setShopid(userInfo == null ? null : userInfo.getCompanyid());
            media.setMediaType(ErpMedia.MEDIA_TYPE_IMAGE);
            media.setUsageType(ErpMedia.USAGE_FINISHED);
            media.setSource(ErpMedia.SOURCE_AMAZON_SYNC);
            media.setUrl(dto.getUrl());
            media.setName(dto.getVariant());
            media.setWidth(dto.getWidth());
            media.setHeight(dto.getHeight());
            media.setProcessStatus(ErpMedia.PROCESS_NONE);
            media.setCreator(userInfo == null ? null : userInfo.getId());
            Date now = new Date();
            media.setCreateTime(now);
            media.setUpdateTime(now);
            mediaMapper.insert(media);

            // 创建关联（默认到 SPU 池，platform=amazon，slot=variant）
            ErpMediaRef ref = new ErpMediaRef();
            ref.setMediaId(media.getId());
            ref.setMaterialId(materialId);
            ref.setShopid(media.getShopid());
            ref.setRefType(ErpMediaRef.REF_TYPE_SPU_POOL);
            ref.setPicClass(ErpMediaRef.PIC_CLASS_FINISHED);
            ref.setSortOrder(nextSortOrder(materialId, ErpMediaRef.REF_TYPE_SPU_POOL));
            ref.setIsMain(ErpMediaRef.MAIN_NO);
            ref.setPlatform(ErpMediaRef.PLATFORM_AMAZON);
            ref.setMarketplaceId(marketplaceId);
            ref.setSlotPosition(dto.getVariant());
            ref.setCreator(userInfo == null ? null : userInfo.getId());
            ref.setCreateTime(now);
            this.save(ref);
            imported++;
        }
        if (imported > 0) evictMaterial(materialId);
        return imported;
    }

    // ============= 内部辅助 =============

    /** 获取下一个 sort_order = 当前最大值 + 10 */
    private int nextSortOrder(String materialId, int refType) {
        LambdaQueryWrapper<ErpMediaRef> qw = new LambdaQueryWrapper<>();
        qw.eq(ErpMediaRef::getMaterialId, materialId)
          .eq(ErpMediaRef::getRefType, refType)
          .orderByDesc(ErpMediaRef::getSortOrder)
          .last("LIMIT 1");
        ErpMediaRef top = this.getOne(qw);
        return top == null || top.getSortOrder() == null ? 0 : top.getSortOrder() + 10;
    }

    /** 同步主图字段：将 media 的 ID 写入 t_erp_material.image */
    private void syncMaterialImage(String materialId, ErpMedia media) {
        if (media == null) return;
        Material material = materialService.getById(materialId);
        if (material == null) return;
        material.setImage(media.getId());
        materialService.updateById(material);
    }

    /** 主图删除后，从剩余展示图中按 sort_order 升序找出新主图。 */
    private void promoteFallbackMain(String materialId) {
        LambdaQueryWrapper<ErpMediaRef> qw = new LambdaQueryWrapper<>();
        qw.eq(ErpMediaRef::getMaterialId, materialId)
          .eq(ErpMediaRef::getRefType, ErpMediaRef.REF_TYPE_SKU_DISPLAY)
          .orderByAsc(ErpMediaRef::getSortOrder)
          .last("LIMIT 1");
        ErpMediaRef next = this.getOne(qw);
        if (next == null) {
            // 没有任何展示图了 → 清空 Material.image
            Material m = materialService.getById(materialId);
            if (m != null) {
                m.setImage(null);
                materialService.updateById(m);
            }
            return;
        }
        next.setIsMain(ErpMediaRef.MAIN_YES);
        this.updateById(next);
        ErpMedia media = mediaMapper.selectById(next.getMediaId());
        syncMaterialImage(materialId, media);
    }
}
