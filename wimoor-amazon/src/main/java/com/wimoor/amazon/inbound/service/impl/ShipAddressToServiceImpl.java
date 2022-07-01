package com.wimoor.amazon.inbound.service.impl;

 
import org.springframework.stereotype.Service;

import com.wimoor.amazon.inbound.mapper.ShipAddressToMapper;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddressTo;
import com.wimoor.amazon.inbound.service.IShipAddressToService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("shipAddressToService")
public class ShipAddressToServiceImpl extends ServiceImpl<ShipAddressToMapper,ShipAddressTo>  implements IShipAddressToService {
	 

}
