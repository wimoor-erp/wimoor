package com.wimoor.amazon.inbound.service.impl;

 
import org.springframework.stereotype.Service;

import com.wimoor.amazon.inbound.mapper.ShipAddressToMapper;
import com.wimoor.amazon.inbound.pojo.entity.AmzShipFulfillmentCenter;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddressTo;
import com.wimoor.amazon.inbound.pojo.entity.ShipInboundShipment;
import com.wimoor.amazon.inbound.service.IAmzShipFulfillmentCenterService;
import com.wimoor.amazon.inbound.service.IShipAddressToService;

import lombok.RequiredArgsConstructor;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("shipAddressToService")
@RequiredArgsConstructor
public class ShipAddressToServiceImpl extends ServiceImpl<ShipAddressToMapper,ShipAddressTo>  implements IShipAddressToService {
    final IAmzShipFulfillmentCenterService iAmzShipFulfillmentCenterService;
    
	@Override
	public ShipAddressTo getToAddress(ShipInboundShipment ship) {
		// TODO Auto-generated method stub
		AmzShipFulfillmentCenter center=iAmzShipFulfillmentCenterService.getByCode(ship.getDestinationfulfillmentcenterid());
		if(ship.getShiptoaddressid()!=null) {
			ShipAddressTo address= this.baseMapper.selectById(ship.getShiptoaddressid());
			if(address != null&&center!=null){
				address.setArea(center.getArea());
				address.setIsfar(center.getIsfar());
			}
			return address;
		}else {
			if(center!=null) {
				ShipAddressTo address = new ShipAddressTo();
				address.setAddressline1(center.getAddressName());
				address.setCity(center.getCity());
				address.setPostalcode(center.getZip());
				address.setCountrycode(center.getCountry());
				address.setName(center.getCode());
				address.setStateorprovincecode(center.getState());
				address.setArea(center.getArea());
				address.setIsfar(center.getIsfar());
				return address;
			}else {
				return null;
			}
		}
	}
	 

}
