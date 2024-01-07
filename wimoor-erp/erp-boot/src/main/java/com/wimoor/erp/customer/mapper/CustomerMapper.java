package com.wimoor.erp.customer.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.customer.pojo.entity.Customer;
@Mapper
public interface CustomerMapper  extends BaseMapper<Customer> {
	List<Map<String,Object>> findByCondition(@Param("search")String search,@Param("shopid")String shopid);
	IPage<Map<String,Object>> findByCondition(Page<?>  page,@Param("search")String search,@Param("shopid")String shopid);
	List<Customer> findByShopId(@Param("shopid")String shopid,@Param("name") String name);
	Customer findByName(@Param("shopid")String shopid,@Param("name") String name);
}
