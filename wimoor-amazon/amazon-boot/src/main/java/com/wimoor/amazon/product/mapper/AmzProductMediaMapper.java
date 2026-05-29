package com.wimoor.amazon.product.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.amazon.product.pojo.entity.AmzProductMedia;

@Mapper
public interface AmzProductMediaMapper extends BaseMapper<AmzProductMedia> {

    /**
     * 查询指定 SKU 在指定授权+站点下的全部媒体（按 sort_order 升序）。
     */
    List<AmzProductMedia> selectBySku(@Param("authorityId") String authorityId,
                                      @Param("marketplaceId") String marketplaceId,
                                      @Param("sku") String sku);

    /**
     * 删除指定 SKU 已有缓存（refresh 前清理）。
     */
    int deleteBySku(@Param("authorityId") String authorityId,
                    @Param("marketplaceId") String marketplaceId,
                    @Param("sku") String sku);
}
