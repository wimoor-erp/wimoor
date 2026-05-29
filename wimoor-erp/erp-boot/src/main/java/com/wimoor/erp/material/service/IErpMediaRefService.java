package com.wimoor.erp.material.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.material.mapper.ErpMediaMapper.ErpMediaWithRef;
import com.wimoor.erp.material.pojo.entity.ErpMediaRef;

/**
 * 媒体-商品关联服务。
 *
 * @author wimoor
 */
public interface IErpMediaRefService extends IService<ErpMediaRef> {

    /**
     * 关联媒体到商品。
     *
     * @param mediaId    媒体ID
     * @param materialId 商品ID
     * @param refType    {@link ErpMediaRef#REF_TYPE_SPU_POOL} 或 {@link ErpMediaRef#REF_TYPE_SKU_DISPLAY}
     * @param picClass   分类（仅 SKU 展示图有效）
     * @param platform   平台标识（NULL=通用）
     * @param marketplaceId 站点ID（可空）
     * @param slot       图片位（MAIN/PT01..）
     * @return 新创建/已存在的 ref 记录
     */
    ErpMediaRef assign(String mediaId, String materialId, int refType,
                       Integer picClass, String platform, String marketplaceId, String slot,
                       UserInfo userInfo);

    /**
     * 批量分配：将 SPU 池里多张图分配到一个 SKU。
     */
    List<ErpMediaRef> batchAssign(List<String> mediaIds, String materialId, int refType,
                                  Integer picClass, String platform, String marketplaceId,
                                  UserInfo userInfo);

    /**
     * 取消分配。
     */
    boolean unassign(String refId, UserInfo userInfo);

    /**
     * 查询 SKU 级展示图（含 media 详情）。
     */
    List<ErpMediaWithRef> list(String materialId);

    /**
     * 查询 SPU 级图片池。
     */
    List<ErpMediaWithRef> pool(String materialId);

    /**
     * 设置主图（自动降级旧主图，并同步 t_erp_material.image）。
     */
    boolean setMain(String refId, UserInfo userInfo);

    /**
     * 批量更新排序。
     */
    boolean sort(List<String> refIds, UserInfo userInfo);

    /**
     * 更新 ref 的 pic_class（用途分类）。
     */
    boolean updateUsage(String refId, int picClass, UserInfo userInfo);

    /**
     * 从 Amazon 同步：将 t_amz_product_media 中指定 SKU 的图片导入到 SPU 池。
     *
     * @return 新导入的 ref 数量
     */
    int syncFromAmazon(String materialId, String sku, String authorityId, String marketplaceId,
                       UserInfo userInfo);
}
