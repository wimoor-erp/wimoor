package com.wimoor.amazon.product.service;

import com.wimoor.amazon.product.pojo.dto.ProductInAftersaleDTO;
import com.wimoor.amazon.product.pojo.entity.ProductInAftersale;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-11-28
 */
public interface IProductInAftersaleService extends IService<ProductInAftersale> {

	void insertBatch(List<ProductInAftersale> salesafter);
	
	List<Map<String,Object>> getSummary(String shopid,String groupid);

	IPage<Map<String, Object>> findList(Page<Object> page, ProductInAftersaleDTO dto);
}
