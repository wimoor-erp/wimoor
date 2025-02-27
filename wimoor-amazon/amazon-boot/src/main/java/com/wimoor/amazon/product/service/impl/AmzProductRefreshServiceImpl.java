package com.wimoor.amazon.product.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.product.mapper.AmzProductRefreshMapper;
import com.wimoor.amazon.product.pojo.entity.AmzProductRefresh;
import com.wimoor.amazon.product.service.IAmzProductRefreshService;

import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-17
 */
@Service
@RequiredArgsConstructor
public class AmzProductRefreshServiceImpl extends ServiceImpl<AmzProductRefreshMapper, AmzProductRefresh> implements IAmzProductRefreshService {

	@Override 
	public void insert() {
		// TODO Auto-generated method stub
		this.baseMapper.insertDefault();
	}
	
	public AmzProductRefresh findForDetailRefresh(String amazonauthid){
		return this.baseMapper.findForDetailRefresh(amazonauthid);
	}

	public List<AmzProductRefresh> findForCatalogRefresh(String amazonauthid){
		return this.baseMapper.findForCatalogRefresh(amazonauthid);
	}
	
	@Override
	public List<AmzProductRefresh> findForPriceRefresh(String amazonauthid) {
		// TODO Auto-generated method stub
		return this.baseMapper.findForPriceRefresh(amazonauthid);
	}
}
