package com.wimoor.amazon.product.service.impl;

import com.wimoor.amazon.product.pojo.entity.ProductInAftersale;
import com.wimoor.amazon.product.mapper.ProductInAftersaleMapper;
import com.wimoor.amazon.product.service.IProductInAftersaleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-11-28
 */
@Service
public class ProductInAftersaleServiceImpl extends ServiceImpl<ProductInAftersaleMapper, ProductInAftersale> implements IProductInAftersaleService {

	@Override
	public void insertBatch(List<ProductInAftersale> salesafter) {
		// TODO Auto-generated method stub
		if(salesafter!=null&&salesafter.size()>0) {
			this.baseMapper.insertBatch(salesafter);
		}
	}

 
}
