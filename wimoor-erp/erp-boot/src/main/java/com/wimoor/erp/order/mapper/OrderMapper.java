package com.wimoor.erp.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.order.pojo.entity.Order;
import com.wimoor.erp.purchase.alibaba.pojo.entity.ErpPurchaseAlibabaContact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wimoor team
 * @since 2023-03-16
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
   IPage<Map<String,Object>> findByCondition(Page<Map<String,Object>> page,@Param("param") Map<String,String> param);
   IPage<Map<String,Object>> findOrderByCondition(Page<Map<String,Object>> page,@Param("param") Map<String,String> param);
   List<Map<String,Object>>  findOrderByCondition(@Param("param") Map<String,String> param);
   IPage<Map<String, Object>> findMaterialByCondition(Page<?> page,@Param("param")Map<String, String> map);
   List<Map<String, Object>> findMaterialByCondition(@Param("param")Map<String, String> map);
   void summary();
    List<Map<String, Object>> listByDate(@Param("param")Map<String, Object> param);
}


