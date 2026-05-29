package com.wimoor.erp.material.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.material.pojo.entity.ErpMedia;

@Mapper
public interface ErpMediaMapper extends BaseMapper<ErpMedia> {

    /**
     * 按 shopid + md5 精确查询（去重）
     */
    ErpMedia selectByMd5(@Param("shopid") String shopid, @Param("md5") String md5);

    /**
     * 按 material_id 联表查询其全部关联媒体（包含 ref 信息）。
     * @param materialId 商品ID
     * @param refType    NULL = 不限制；否则只取该类型
     */
    List<ErpMediaWithRef> selectByMaterialId(@Param("materialId") String materialId,
                                             @Param("refType") Integer refType);

    /**
     * 媒体 + 关联视图。
     */
    class ErpMediaWithRef extends ErpMedia {
        private String refId;
        private String materialId;
        private Integer refType;
        private Integer picClass;
        private Integer sortOrder;
        private Integer isMain;
        private String platform;
        private String marketplaceId;
        private String slotPosition;

        public String getRefId() { return refId; }
        public void setRefId(String refId) { this.refId = refId; }
        public String getMaterialId() { return materialId; }
        public void setMaterialId(String materialId) { this.materialId = materialId; }
        public Integer getRefType() { return refType; }
        public void setRefType(Integer refType) { this.refType = refType; }
        public Integer getPicClass() { return picClass; }
        public void setPicClass(Integer picClass) { this.picClass = picClass; }
        public Integer getSortOrder() { return sortOrder; }
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
        public Integer getIsMain() { return isMain; }
        public void setIsMain(Integer isMain) { this.isMain = isMain; }
        public String getPlatform() { return platform; }
        public void setPlatform(String platform) { this.platform = platform; }
        public String getMarketplaceId() { return marketplaceId; }
        public void setMarketplaceId(String marketplaceId) { this.marketplaceId = marketplaceId; }
        public String getSlotPosition() { return slotPosition; }
        public void setSlotPosition(String slotPosition) { this.slotPosition = slotPosition; }
    }
}
