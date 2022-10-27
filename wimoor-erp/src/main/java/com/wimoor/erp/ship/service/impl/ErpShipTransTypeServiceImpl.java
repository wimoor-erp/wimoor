package com.wimoor.erp.ship.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.ship.mapper.ErpShipTransTypeMapper;
import com.wimoor.erp.ship.pojo.entity.ErpShipTransType;
import com.wimoor.erp.ship.service.IErpShipTransTypeService;
@Service("erpShipTransTypeService")
public class ErpShipTransTypeServiceImpl extends  ServiceImpl<ErpShipTransTypeMapper,ErpShipTransType> implements IErpShipTransTypeService {

}
