package com.wimoor.quote.ship.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.quote.ship.pojo.dto.ShipmentSupplierTranschannelDTO;
import com.wimoor.quote.ship.pojo.entity.ShipmentTranschannel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author liufei
* @description 针对表【t_shipment_transchannel】的数据库操作Mapper
* @createDate 2025-07-22 18:05:03
* @Entity com.wimoor.quote.ship.pojo.entity.ShipmentTranschannel
*/
@Mapper
public interface ShipmentTranschannelMapper extends BaseMapper<ShipmentTranschannel> {
    IPage<Map<String, Object>> listPage(Page<?> page, @Param("param")ShipmentSupplierTranschannelDTO dto);
}




