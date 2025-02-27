package com.wimoor.amazon.inbound.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inbound.mapper.ShipInboundBoxMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundBox;
import com.wimoor.amazon.inbound.service.IShipInboundBoxService;

@Service("shipInboundBoxService")
public class ShipInboundBoxServiceImpl extends  ServiceImpl<ShipInboundBoxMapper,ShipInboundBox> implements IShipInboundBoxService {
 
public List<ShipInboundBox> findListByShipmentId(String shipmentid) {
	QueryWrapper<ShipInboundBox> queryWrapper = new QueryWrapper<ShipInboundBox>();
	queryWrapper.eq("shipmentid", shipmentid);
	queryWrapper.orderByAsc("boxnum");
	return this.baseMapper.selectList(queryWrapper);
}

public List<Map<String, Object>> findShipInboundBox(String shipmentid) {
	return this.baseMapper.findShipInboundBox(shipmentid);
}
}
