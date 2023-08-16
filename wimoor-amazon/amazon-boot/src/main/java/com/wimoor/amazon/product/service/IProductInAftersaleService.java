package com.wimoor.amazon.product.service;

import com.wimoor.amazon.product.pojo.entity.ProductInAftersale;

import java.util.List;

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
}
