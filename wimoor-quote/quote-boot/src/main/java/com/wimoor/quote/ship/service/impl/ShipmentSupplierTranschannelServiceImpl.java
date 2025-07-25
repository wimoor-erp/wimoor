package com.wimoor.quote.ship.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.quote.ship.pojo.dto.ShipmentSupplierTranschannelDTO;
import com.wimoor.quote.ship.pojo.entity.ShipmentSupplierTranschannel;
import com.wimoor.quote.ship.service.IShipmentSupplierTranschannelService;
import com.wimoor.quote.ship.mapper.ShipmentSupplierTranschannelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
* @author liufei
* @description 针对表【t_shipment_supplier_transchannel】的数据库操作Service实现
* @createDate 2025-07-22 18:06:30
*/
@Service
@RequiredArgsConstructor
public class ShipmentSupplierTranschannelServiceImpl extends ServiceImpl<ShipmentSupplierTranschannelMapper, ShipmentSupplierTranschannel>
    implements IShipmentSupplierTranschannelService {

    @Override
    public IPage<Map<String, Object>> listPage(ShipmentSupplierTranschannelDTO dto) {
        return this.baseMapper.listPage(dto.getPage(),dto);
    }
}




