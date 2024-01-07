package com.wimoor.amazon.product.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazon.spapi.api.DefinitionsApi;
import com.amazon.spapi.client.ApiException;
import com.amazon.spapi.model.definitions.ProductType;
import com.amazon.spapi.model.definitions.ProductTypeDefinition;
import com.amazon.spapi.model.definitions.ProductTypeList;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.product.mapper.ProductTypeMapper;
import com.wimoor.amazon.product.pojo.entity.ProductTypes;
import com.wimoor.amazon.product.service.IProductProductTypeService;
import com.wimoor.common.mvc.BizException;

@Service
public class ProductProductTypeServiceImpl implements IProductProductTypeService {
	 
	@Autowired
	ApiBuildService apiBuildService;
	@Autowired
	IAmazonAuthorityService amazonAuthorityService;
	@Autowired
	ProductTypeMapper productTypeMapper;
	
	@Override
	public void runApi(AmazonAuthority amazonAuthority) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void captureProductType(AmazonAuthority amazonAuthority,String marketplaceid) {
		// TODO Auto-generated method stub
		 DefinitionsApi api = apiBuildService.GetDefinitionsApi(amazonAuthority);
		try {
			ProductTypeList response = api.searchDefinitionsProductTypes(Arrays.asList(marketplaceid), null);
			if(response!=null && response.getProductTypes()!=null && response.getProductTypes().size()>0) {
				Date nowdate=new Date();
				for (ProductType item:response.getProductTypes()) {
					QueryWrapper<ProductTypes> queryWrapper=new QueryWrapper<ProductTypes>();
					queryWrapper.eq("name", item.getName());
					List<ProductTypes> oldlist = productTypeMapper.selectList(queryWrapper);
					if(oldlist!=null && oldlist.size()>0) {
						continue;
					}else {
						ProductTypes entity=new ProductTypes();
						entity.setName(item.getName());
						entity.setRefreshtime(nowdate);
						productTypeMapper.insert(entity);
					}
				}
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("接口调用错误："+e.getMessage());
		}
	}
	
	@Override
	public ProductTypeDefinition captureTypeDefinition(AmazonAuthority amazonAuthority,String marketplaceid,String typename) {
		// TODO Auto-generated method stub

		DefinitionsApi api = apiBuildService.GetDefinitionsApi(amazonAuthority);
		try {
			ProductTypeDefinition response = api.getDefinitionsProductType(typename,
																			Arrays.asList(marketplaceid), 
																			amazonAuthority.getSellerid(),
																			null,  
																			null,
																			null, 
																			null);
			if(response!=null) {
				 return response;
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("接口调用错误："+e.getMessage());
		}
		return null;
	}
	 
	 
	 
}
