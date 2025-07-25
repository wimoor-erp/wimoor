package com.wimoor.quote.ship.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.quote.ship.pojo.dto.ShipmentSupplierTranschannelDTO;
import com.wimoor.quote.ship.pojo.entity.ShipmentSupplierTranschannel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author liufei
* @description 针对表【t_shipment_supplier_transchannel】的数据库操作Mapper
* @createDate 2025-07-22 18:06:30
* @Entity com.wimoor.quote.ship.pojo.entity.ShipmentSupplierTranschannel
*/
@Mapper
public interface ShipmentSupplierTranschannelMapper extends BaseMapper<ShipmentSupplierTranschannel> {

    IPage<Map<String, Object>> listPage(Page<Object> page, @Param("param")ShipmentSupplierTranschannelDTO dto);
}




