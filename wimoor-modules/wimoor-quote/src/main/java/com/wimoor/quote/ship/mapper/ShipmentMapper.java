package com.wimoor.quote.ship.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.quote.api.dto.QuoteDTO;
import com.wimoor.quote.ship.pojo.entity.Shipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface  ShipmentMapper extends BaseMapper<Shipment> {
    IPage<Shipment> findByCondition(Page page, @Param("param") QuoteDTO dto);
}
