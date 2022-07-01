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
	
	public List<AmzProductRefresh> findForDetailRefresh(String amazonauthid){
		return this.baseMapper.findForDetailRefresh(amazonauthid);
	}
}
