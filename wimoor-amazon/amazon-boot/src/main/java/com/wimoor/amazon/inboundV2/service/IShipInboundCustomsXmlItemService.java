package com.wimoor.amazon.inboundV2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipCustomXmlDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundCustomsXmlItem;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Map;

/**
* @author liufei
* @description 针对表【t_erp_ship_v2_inboundshipment_customs_xml】的数据库操作Service
* @createDate 2025-12-03 13:35:14
*/
public interface IShipInboundCustomsXmlItemService extends IService<ShipInboundCustomsXmlItem> {

    IPage<Map<String,Object>> findList(ShipCustomXmlDTO dto);

}
