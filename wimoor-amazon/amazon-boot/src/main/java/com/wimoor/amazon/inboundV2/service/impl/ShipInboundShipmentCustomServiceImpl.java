package com.wimoor.amazon.inboundV2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundShipmentCustomMapper;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundShipmentCustom;
import com.wimoor.amazon.inboundV2.service.IShipInboundShipmentCustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("shipInboundShipmentCustomService")
@RequiredArgsConstructor
public class ShipInboundShipmentCustomServiceImpl extends ServiceImpl<ShipInboundShipmentCustomMapper, ShipInboundShipmentCustom> implements IShipInboundShipmentCustomService {


}
