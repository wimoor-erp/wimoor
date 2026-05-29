package com.wimoor.erp.material.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.material.pojo.entity.ErpMediaRef;

@Mapper
public interface ErpMediaRefMapper extends BaseMapper<ErpMediaRef> {

    /**
     * 按 mediaId 查询所有未删除的关联记录（用于 delete 前的活跃关联检查）。
     */
    List<ErpMediaRef> selectByMediaId(@Param("mediaId") String mediaId);

    /**
     * 当前 SKU 当前主图的 ref 记录（用于 setMain 时降级旧主图）。
     */
    ErpMediaRef selectMainByMaterial(@Param("materialId") String materialId,
                                     @Param("refType") Integer refType);
}
