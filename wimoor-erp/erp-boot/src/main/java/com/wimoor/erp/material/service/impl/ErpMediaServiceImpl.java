package com.wimoor.erp.material.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.service.impl.StorageService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.material.mapper.ErpMediaMapper;
import com.wimoor.erp.material.mapper.ErpMediaRefMapper;
import com.wimoor.erp.material.pojo.entity.ErpMedia;
import com.wimoor.erp.material.pojo.entity.ErpMediaRef;
import com.wimoor.erp.material.service.IErpMediaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

/**
 * 媒体资源核心服务实现。
 *
 * 注意：缩略图生成失败不应阻断主流程；MD5 去重仅在同一 shopid 下生效。
 *
 * @author wimoor
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ErpMediaServiceImpl extends ServiceImpl<ErpMediaMapper, ErpMedia> implements IErpMediaService {

    private static final Set<String> ALLOWED_IMAGE_TYPES = new HashSet<>(Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/webp", "image/gif"
    ));
    private static final Set<String> ALLOWED_VIDEO_TYPES = new HashSet<>(Arrays.asList(
            "video/mp4", "video/quicktime", "video/x-msvideo"
    ));
    private static final long MAX_IMAGE_SIZE = 20L * 1024 * 1024;   // 20MB
    private static final long MAX_VIDEO_SIZE = 200L * 1024 * 1024;  // 200MB

    private final StorageService storageService;
    private final ErpMediaRefMapper mediaRefMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErpMedia upload(MultipartFile file, Integer mediaType, Integer usageType, Integer source, UserInfo userInfo) {
        if (file == null || file.isEmpty()) {
            throw new ERPBizException("上传文件不能为空");
        }
        if (userInfo == null || userInfo.getCompanyid() == null) {
            throw new ERPBizException("缺少用户上下文");
        }
        if (mediaType == null) {
            mediaType = ErpMedia.MEDIA_TYPE_IMAGE;
        }
        validateFile(file, mediaType);

        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new ERPBizException("读取上传文件失败: " + e.getMessage());
        }

        String md5 = md5Hex(bytes);
        ErpMedia existing = checkDuplicate(userInfo.getCompanyid(), md5);
        if (existing != null) {
            log.info("media duplicate hit, shopid={}, md5={}", userInfo.getCompanyid(), md5);
            return existing;
        }

        String contentType = file.getContentType() == null ? "application/octet-stream" : file.getContentType();
        String originalName = file.getOriginalFilename() == null ? "unknown" : file.getOriginalFilename();
        String ext = extOf(originalName);
        String objectName = buildObjectName(userInfo.getCompanyid(), md5, ext);

        // 1. 上传原文件
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            storageService.putObject(storageService.getBucketName(), objectName, is);
        } catch (Exception e) {
            throw new ERPBizException("上传到对象存储失败: " + e.getMessage());
        }

        // 2. 生成缩略图（仅图片，失败不阻塞）
        String thumbLocation = null;
        Integer[] dim = new Integer[]{null, null};
        if (mediaType == ErpMedia.MEDIA_TYPE_IMAGE) {
            try {
                ByteArrayOutputStream thumbOs = new ByteArrayOutputStream();
                Thumbnails.of(new ByteArrayInputStream(bytes))
                        .size(300, 300)
                        .outputFormat("jpg")
                        .toOutputStream(thumbOs);
                String thumbName = objectName + ".thumb.jpg";
                try (InputStream tis = new ByteArrayInputStream(thumbOs.toByteArray())) {
                    storageService.putObject(storageService.getBucketName(), thumbName, tis);
                }
                thumbLocation = thumbName;
                java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(new ByteArrayInputStream(bytes));
                if (img != null) {
                    dim[0] = img.getWidth();
                    dim[1] = img.getHeight();
                }
            } catch (Exception ex) {
                log.warn("thumbnail generation failed, objectName={}, err={}", objectName, ex.getMessage());
            }
        }

        // 3. 入库
        ErpMedia media = new ErpMedia();
        media.setShopid(userInfo.getCompanyid());
        media.setMediaType(mediaType);
        media.setUsageType(usageType == null ? ErpMedia.USAGE_FINISHED : usageType);
        media.setSource(source == null ? ErpMedia.SOURCE_SELF_SHOT : source);
        media.setLocation(objectName);
        media.setThumbLocation(thumbLocation);
        media.setName(originalName);
        media.setContentType(contentType);
        media.setFileSize((long) bytes.length);
        media.setMd5(md5);
        media.setWidth(dim[0]);
        media.setHeight(dim[1]);
        media.setProcessStatus(ErpMedia.PROCESS_NONE);
        media.setCreator(userInfo.getId());
        Date now = new Date();
        media.setCreateTime(now);
        media.setUpdateTime(now);
        this.save(media);
        return media;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ErpMedia> uploadBatch(MultipartFile zipFile, Integer usageType, Integer source, UserInfo userInfo) {
        if (zipFile == null || zipFile.isEmpty()) {
            throw new ERPBizException("ZIP 文件不能为空");
        }
        List<ErpMedia> results = new ArrayList<>();
        try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                String name = entry.getName();
                String lower = name.toLowerCase();
                if (!(lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                        || lower.endsWith(".png") || lower.endsWith(".webp"))) {
                    continue;
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buf = new byte[8192];
                int n;
                while ((n = zis.read(buf)) > 0) {
                    baos.write(buf, 0, n);
                }
                byte[] data = baos.toByteArray();
                MultipartFile wrapped = new BytesMultipartFile(name, guessContentType(lower), data);
                results.add(upload(wrapped, ErpMedia.MEDIA_TYPE_IMAGE, usageType, source, userInfo));
            }
        } catch (IOException e) {
            throw new ERPBizException("解析 ZIP 失败: " + e.getMessage());
        }
        return results;
    }

    @Override
    public ErpMedia checkDuplicate(String shopid, String md5) {
        if (shopid == null || md5 == null) {
            return null;
        }
        return this.baseMapper.selectByMd5(shopid, md5);
    }

    @Override
    public ErpMedia getByMd5(String shopid, String md5) {
        return checkDuplicate(shopid, md5);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String mediaId, boolean force, UserInfo userInfo) {
        ErpMedia media = this.getById(mediaId);
        if (media == null) {
            return false;
        }
        if (userInfo != null && userInfo.getCompanyid() != null
                && !userInfo.getCompanyid().equals(media.getShopid())) {
            throw new ERPBizException("无权删除其它租户的媒体");
        }
        List<ErpMediaRef> refs = mediaRefMapper.selectByMediaId(mediaId);
        if (refs != null && !refs.isEmpty()) {
            if (!force) {
                throw new ERPBizException("该媒体存在 " + refs.size() + " 条活跃关联，无法删除");
            }
            mediaRefMapper.delete(new LambdaQueryWrapper<ErpMediaRef>().eq(ErpMediaRef::getMediaId, mediaId));
        }
        // 对象存储删除尽力而为
        try {
            if (media.getLocation() != null) {
                storageService.removeObject(storageService.getBucketName(), media.getLocation());
            }
            if (media.getThumbLocation() != null) {
                storageService.removeObject(storageService.getBucketName(), media.getThumbLocation());
            }
        } catch (Exception e) {
            log.warn("storage cleanup failed for media={}, err={}", mediaId, e.getMessage());
        }
        return this.removeById(mediaId);
    }

    // ============= 工具方法 =============

    private void validateFile(MultipartFile file, int mediaType) {
        long size = file.getSize();
        String ct = file.getContentType();
        if (mediaType == ErpMedia.MEDIA_TYPE_IMAGE) {
            if (size > MAX_IMAGE_SIZE) {
                throw new ERPBizException("图片大小超过 20MB");
            }
            if (ct != null && !ALLOWED_IMAGE_TYPES.contains(ct.toLowerCase())) {
                throw new ERPBizException("不支持的图片类型: " + ct);
            }
        } else {
            if (size > MAX_VIDEO_SIZE) {
                throw new ERPBizException("视频大小超过 200MB");
            }
            if (ct != null && !ALLOWED_VIDEO_TYPES.contains(ct.toLowerCase())) {
                throw new ERPBizException("不支持的视频类型: " + ct);
            }
        }
    }

    static String md5Hex(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return Hex.encodeHexString(md.digest(bytes));
        } catch (Exception e) {
            throw new ERPBizException("MD5 计算失败: " + e.getMessage());
        }
    }

    static String extOf(String filename) {
        if (filename == null) return "";
        int idx = filename.lastIndexOf('.');
        return idx >= 0 ? filename.substring(idx).toLowerCase() : "";
    }

    static String buildObjectName(String shopid, String md5, String ext) {
        // 同一 shopid 同一 md5 → 同一对象路径（天然去重）
        String safeShop = shopid == null ? "default" : shopid.replaceAll("[^A-Za-z0-9_-]", "");
        return "media/" + safeShop + "/" + md5.substring(0, 2) + "/" + md5 + ext;
    }

    private static String guessContentType(String lower) {
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".webp")) return "image/webp";
        if (lower.endsWith(".gif")) return "image/gif";
        return "image/jpeg";
    }

    /** 内部使用：将 ZIP 内字节流包装成 MultipartFile。 */
    static class BytesMultipartFile implements MultipartFile {
        private final String name;
        private final String contentType;
        private final byte[] bytes;

        BytesMultipartFile(String name, String contentType, byte[] bytes) {
            this.name = name;
            this.contentType = contentType;
            this.bytes = bytes;
        }

        @Override public String getName() { return name; }
        @Override public String getOriginalFilename() { return name; }
        @Override public String getContentType() { return contentType; }
        @Override public boolean isEmpty() { return bytes == null || bytes.length == 0; }
        @Override public long getSize() { return bytes == null ? 0 : bytes.length; }
        @Override public byte[] getBytes() { return bytes; }
        @Override public InputStream getInputStream() { return new ByteArrayInputStream(bytes); }
        @Override public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(dest)) {
                fos.write(bytes);
            }
        }
    }
}
