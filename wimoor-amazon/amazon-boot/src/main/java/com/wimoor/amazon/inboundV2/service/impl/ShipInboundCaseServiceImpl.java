package com.wimoor.amazon.inboundV2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundCaseV2Mapper;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundCase;
import com.wimoor.amazon.inboundV2.service.IShipInboundCaseService;

@Service("shipInboundCaseV2Service")
public class ShipInboundCaseServiceImpl extends  ServiceImpl<ShipInboundCaseV2Mapper,ShipInboundCase> implements IShipInboundCaseService {
 
	
	public List<ShipInboundCase> findByBox(String boxid, Integer boxnum) {
		QueryWrapper<ShipInboundCase> queryWrapper = new QueryWrapper<ShipInboundCase>();
		// TODO Auto-generated method stub
		queryWrapper.eq("boxid", boxid);
		queryWrapper.eq("numberofcase", boxnum);
		return this.baseMapper.selectList(queryWrapper);
	}

	@Override
	public List<ShipInboundCase> findShipInboundCaseByBoxid(String boxid) {
		// TODO Auto-generated method stub
		QueryWrapper<ShipInboundCase> queryWrapper = new QueryWrapper<ShipInboundCase>();
		// TODO Auto-generated method stub
		queryWrapper.eq("boxid", boxid);
		return this.baseMapper.selectList(queryWrapper);
	}

}
