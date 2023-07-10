package com.wimoor.amazon.inbound.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inbound.mapper.ShipInboundCaseMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundCase;
import com.wimoor.amazon.inbound.service.IShipInboundCaseService;

@Service("shipInboundCaseService")
public class ShipInboundCaseServiceImpl extends  ServiceImpl<ShipInboundCaseMapper,ShipInboundCase> implements IShipInboundCaseService {
 
	
	public List<ShipInboundCase> findByShipmentBox(String shipmentid, Integer boxnum) {
		QueryWrapper<ShipInboundCase> queryWrapper = new QueryWrapper<ShipInboundCase>();
		// TODO Auto-generated method stub
		queryWrapper.eq("shipmentid", shipmentid);
		queryWrapper.eq("numberofcase", boxnum);
		return this.baseMapper.selectList(queryWrapper);
	}

}
