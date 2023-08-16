package com.wimoor.amazon.inbound.service.impl;


import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inbound.mapper.ShipAddressMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inbound.service.IShipAddressService;
import com.wimoor.common.GeneralUtil;

@Service("shipAddressService")
public class ShipAddressServiceImpl extends  ServiceImpl<ShipAddressMapper,ShipAddress>  implements IShipAddressService {

	public IPage<ShipAddress> findByCondition(Page<?> page,String shopid,String groupid,String disable,String search) {
		if(GeneralUtil.isEmpty(search))search=null;
		else search=search.trim()+"%";
		return this.baseMapper.findByCondition(page,shopid,groupid,disable,search);
	}

	public List<ShipAddress> getDefaultFromAdress(String shopid) {
		QueryWrapper<ShipAddress> queryWrapper = new QueryWrapper<ShipAddress>();
		queryWrapper.eq("shopid",shopid);
		queryWrapper.eq("isfrom",true);
		queryWrapper.eq("isdefault",true);
		queryWrapper.orderByDesc("opttime");
		List<ShipAddress> list = this.baseMapper.selectList(queryWrapper);
		return list;
	}

	 
}
