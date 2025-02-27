package com.wimoor.amazon.inboundV2.service.impl;

import java.math.BigInteger;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentRecordV2Mapper;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentRecord;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentRecordV2Service;


@Service("shipInboundV2ShipmentRecordService")
public class ShipInboundShipmentRecordV2ServiceImpl  extends ServiceImpl<ShipInboundShipmentRecordV2Mapper,ShipInboundShipmentRecord> implements IShipInboundShipmentRecordV2Service {

	public void saveRecord(ShipInboundShipment shipInboundShipment) {
		// TODO Auto-generated method stub
		if(shipInboundShipment != null) {
			ShipInboundShipmentRecord shipInboundShipmentRecord = new ShipInboundShipmentRecord();
			shipInboundShipmentRecord.setFormid(shipInboundShipment.getFormid());
			shipInboundShipmentRecord.setShipmentid(shipInboundShipment.getShipmentConfirmationId());
			shipInboundShipmentRecord.setStatus(shipInboundShipment.getStatus());
			shipInboundShipmentRecord.setOpttime(new Date());
			shipInboundShipmentRecord.setOperator(new BigInteger(shipInboundShipment.getOperator()));
			this.save(shipInboundShipmentRecord);
		}
	}

	public void saveRecord(ShipInboundPlan plan) {
		// TODO Auto-generated method stub
		if(plan != null) {
			ShipInboundShipmentRecord shipInboundShipmentRecord = new ShipInboundShipmentRecord();
			shipInboundShipmentRecord.setFormid(plan.getId());
			shipInboundShipmentRecord.setShipmentid("#");
			shipInboundShipmentRecord.setStatus(plan.getAuditstatus());
			shipInboundShipmentRecord.setOpttime(new Date());
			shipInboundShipmentRecord.setOperator(new BigInteger(plan.getOperator()));
			this.save(shipInboundShipmentRecord);
		}
	}
	
}
