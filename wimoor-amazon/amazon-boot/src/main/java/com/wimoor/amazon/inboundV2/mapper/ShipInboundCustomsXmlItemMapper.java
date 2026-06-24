package com.wimoor.amazon.inboundV2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipCustomXmlDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundCustomsXmlItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
* @author liufei
* @description 针对表【t_erp_ship_v2_inboundshipment_customs_xml_item】的数据库操作Mapper
* @createDate 2025-12-03 13:35:14
* @Entity com.wimoor.amazon.inboundV2.pojo.entity.InboundshipmentCustomsXmlItem
*/
@Mapper
public interface ShipInboundCustomsXmlItemMapper extends BaseMapper<ShipInboundCustomsXmlItem> {
    /**
     * @description: 分页查询自定义 Customs XML 项列表
     * @param page 分页信息
     * @param dto 查询参数
     * @return 分页结果
     */
    IPage<Map<String, Object>> findList(Page<Object> page, ShipCustomXmlDTO dto);
}




