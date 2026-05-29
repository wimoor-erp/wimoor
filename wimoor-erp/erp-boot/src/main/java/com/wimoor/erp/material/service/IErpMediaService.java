package com.wimoor.erp.material.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.material.pojo.entity.ErpMedia;

/**
 * 媒体资源核心服务。
 * 负责媒体文件的上传、去重、删除等"实体"操作，不涉及商品关联。
 *
 * @author wimoor
 */
public interface IErpMediaService extends IService<ErpMedia> {

    /**
     * 单文件上传。
     *
     * @param file      上传文件
     * @param mediaType {@link ErpMedia#MEDIA_TYPE_IMAGE} 或 {@link ErpMedia#MEDIA_TYPE_VIDEO}
     * @param usageType 用途类型，详见 {@link ErpMedia} USAGE_* 常量
     * @param source    来源，详见 {@link ErpMedia} SOURCE_* 常量
     * @param userInfo  上传人
     * @return 入库后的媒体记录（去重时返回已存在的记录）
     */
    ErpMedia upload(MultipartFile file, Integer mediaType, Integer usageType, Integer source, UserInfo userInfo);

    /**
     * 批量上传 ZIP。
     *
     * @return 入库成功的媒体记录
     */
    List<ErpMedia> uploadBatch(MultipartFile zipFile, Integer usageType, Integer source, UserInfo userInfo);

    /**
     * 按 shopid+md5 检查重复。
     *
     * @return 已存在的记录，否则 null
     */
    ErpMedia checkDuplicate(String shopid, String md5);

    /**
     * 按 shopid+md5 获取记录。
     */
    ErpMedia getByMd5(String shopid, String md5);

    /**
     * 删除媒体（含活跃关联检查）。
     *
     * @param mediaId 媒体ID
     * @param force   true=级联删除全部关联；false=有关联时抛异常
     * @return 是否删除成功
     */
    boolean delete(String mediaId, boolean force, UserInfo userInfo);
}
