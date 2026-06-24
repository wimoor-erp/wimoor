package com.wimoor.amazon.inboundV2.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipCustomXmlDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundCustomsXml;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import feign.Param;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

/**
* @author liufei
* @description 针对表【t_erp_ship_v2_inboundshipment_customs_xml】的数据库操作Mapper
* @createDate 2025-12-03 13:35:14
* @Entity com.wimoor.amazon.inboundV2.pojo.entity.InboundshipmentCustomsXml
*/
@Mapper
public interface ShipInboundCustomsXmlMapper extends BaseMapper<ShipInboundCustomsXml> {
    String getNextSequenceNumber(ShipInboundCustomsXml xmlInfo);

    IPage<Map<String, Object>> findList(Page<?> page,@Param("dto") ShipCustomXmlDTO dto);
    List<Map<String, Object>> findList(@Param("dto") ShipCustomXmlDTO dto);
    Map<String, Object> findSummary(ShipCustomXmlDTO dto);
}




