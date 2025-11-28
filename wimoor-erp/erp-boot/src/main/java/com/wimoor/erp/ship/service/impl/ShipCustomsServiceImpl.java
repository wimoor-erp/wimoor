package com.wimoor.erp.ship.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.material.mapper.MaterialCustomsMapper;
import com.wimoor.erp.material.pojo.entity.MaterialCustoms;
import com.wimoor.erp.ship.service.IShipCustomsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service("shipCustomsService")
@RequiredArgsConstructor
public class ShipCustomsServiceImpl extends ServiceImpl<MaterialCustomsMapper,MaterialCustoms> implements IShipCustomsService {



}
