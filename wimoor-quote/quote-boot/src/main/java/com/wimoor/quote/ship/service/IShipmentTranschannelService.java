package com.wimoor.quote.ship.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.quote.ship.pojo.dto.ShipmentSupplierTranschannelDTO;
import com.wimoor.quote.ship.pojo.entity.ShipmentTranschannel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author liufei
* @description 针对表【t_shipment_transchannel】的数据库操作Service
* @createDate 2025-07-22 18:05:03
*/
public interface IShipmentTranschannelService extends IService<ShipmentTranschannel> {

    IPage<Map<String,Object>> listPage(ShipmentSupplierTranschannelDTO dto);
}

