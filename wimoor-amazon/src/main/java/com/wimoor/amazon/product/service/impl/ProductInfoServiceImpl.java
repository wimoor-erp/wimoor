package com.wimoor.amazon.product.service.impl;

import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品信息 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements IProductInfoService {

	@Override
	public List<ProductInfo> selectBySku(String sku, String marketplaceid, String amazonAuthId) {
		// TODO Auto-generated method stub
		QueryWrapper<ProductInfo> query=new QueryWrapper<ProductInfo>();
		query.eq("sku", sku);
		query.eq("marketplaceid", marketplaceid);
		query.eq("amazonAuthId", amazonAuthId);
		List<ProductInfo> list = this.baseMapper.selectList(query);
		if(list!=null&&list.size()>0) {
			return list;
		}else {
			return null;
		}
	}

	@Override
	public ProductInfo productOnlyone(String amazonAuthId, String sku, String marketplaceid) {
		// TODO Auto-generated method stub
		List<ProductInfo> list = selectBySku(sku,marketplaceid,amazonAuthId);
		if(list!=null&&list.size()>0) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
	@Override
	public List<ProductInfo> selectByAsin(String amazonAuthId, String asin, String marketplaceid) {
		// TODO Auto-generated method stub
		QueryWrapper<ProductInfo> query=new QueryWrapper<ProductInfo>();
		query.eq("asin", asin);
		query.eq("marketplaceid", marketplaceid);
		query.eq("amazonAuthId", amazonAuthId);
		List<ProductInfo> list = this.baseMapper.selectList(query);
		if(list!=null&&list.size()>0) {
			return list;
		}else {
			return null;
		}
	 
	}
	
	
	public List<Map<String, Object>> findShopSku(String shopid, String sku) {
		return this.baseMapper.findShopSku(shopid, sku);
	}

}
