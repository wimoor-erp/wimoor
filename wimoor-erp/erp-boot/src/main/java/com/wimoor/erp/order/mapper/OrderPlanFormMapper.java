package com.wimoor.erp.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.order.pojo.entity.OrderPlanForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderPlanFormMapper extends BaseMapper<OrderPlanForm> {
    IPage<Map<String, Object>> findPlanForm(Page<Object> page, @Param("param")Map<String, String> param);
    List<Map<String, Object>> findPlanFormEntry(@Param("param")Map<String, Object> param);
    List<Map<String, Object>> findPlanForm(@Param("param")Map<String, Object> param);
}
