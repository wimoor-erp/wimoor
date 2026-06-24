package com.wimoor.amazon.inboundV2.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.inboundV2.mapper.ShipInboundCustomsXmlItemMapper;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipCustomXmlDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundCustomsXmlItem;
import com.wimoor.amazon.inboundV2.service.IShipInboundCustomsXmlItemService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
* @author liufei
* @description 针对表【t_erp_ship_v2_inboundshipment_customs_xml_item】的数据库操作Service实现
* @createDate 2025-12-03 13:35:14
*/
@Service
public class ShipInboundCustomsXmlItemServiceImpl extends ServiceImpl<ShipInboundCustomsXmlItemMapper, ShipInboundCustomsXmlItem>
    implements IShipInboundCustomsXmlItemService {


    @Override
    public IPage<Map<String, Object>> findList(ShipCustomXmlDTO dto) {
        return this.baseMapper.findList(dto.getPage(),dto);
    }
}




