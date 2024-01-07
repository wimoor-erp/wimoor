package com.wimoor.amazon.inbound.service.impl;

import java.math.BigInteger;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inbound.mapper.ShipInboundShipmentRecordMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipmentRecord;
import com.wimoor.amazon.inbound.service.IShipInboundShipmentRecordService;


@Service("shipInboundShipmentRecordService")
public class ShipInboundShipmentRecordServiceImpl  extends ServiceImpl<ShipInboundShipmentRecordMapper,ShipInboundShipmentRecord> implements IShipInboundShipmentRecordService {

	public void saveRecord(ShipInboundShipment shipInboundShipment) {
		// TODO Auto-generated method stub
		if(shipInboundShipment != null) {
			ShipInboundShipmentRecord shipInboundShipmentRecord = new ShipInboundShipmentRecord();
			shipInboundShipmentRecord.setShipmentid(shipInboundShipment.getShipmentid());
			shipInboundShipmentRecord.setStatus(shipInboundShipment.getStatus());
			shipInboundShipmentRecord.setOpttime(new Date());
			shipInboundShipmentRecord.setOperator(new BigInteger(shipInboundShipment.getOperator()));
			this.save(shipInboundShipmentRecord);
		}
	}

}
