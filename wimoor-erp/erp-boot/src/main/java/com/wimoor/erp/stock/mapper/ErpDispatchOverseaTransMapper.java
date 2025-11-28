package com.wimoor.erp.stock.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaTrans;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
@Mapper
public interface ErpDispatchOverseaTransMapper extends BaseMapper<ErpDispatchOverseaTrans> {

    Map<String, Object> getInfo(@Param("id") String id);
    IPage<Map<String, Object>> transFeeSharedDetail(Page<Object> page, @Param("param")Map<String, Object> param);
    List<Map<String, Object>> transFeeSharedDetail(@Param("param")Map<String, Object> param);

    List<Map<String, Object>> transFeeShared(@Param("param") Map<String, Object> params);
    IPage<Map<String, Object>> transFeeShared(Page<?> page,@Param("param") Map<String, Object> param);

    IPage<Map<String, Object>> transFeeSharedWeek(Page<Object> page, @Param("param")Map<String, Object> param);
}
