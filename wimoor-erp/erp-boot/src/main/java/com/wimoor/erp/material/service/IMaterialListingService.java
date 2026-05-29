package com.wimoor.erp.material.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.material.pojo.entity.MaterialListing;

public interface IMaterialListingService extends IService<MaterialListing> {

    /**
     * 查询某产品所有语言的Listing信息（概要，不含description）
     */
    List<MaterialListing> listByMaterialId(String materialid);

    /**
     * 查询某产品指定语言的完整Listing信息（含description）
     */
    MaterialListing getByMaterialIdAndLang(String materialid, String lang);

    /**
     * 保存或更新Listing信息（UPSERT）
     */
    MaterialListing saveListing(MaterialListing listing);

    /**
     * 按ID删除Listing记录
     */
    boolean deleteListing(String id);
}
