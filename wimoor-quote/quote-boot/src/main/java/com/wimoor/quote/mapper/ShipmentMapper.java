package com.wimoor.quote.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.quote.api.dto.QuoteDTO;
import com.wimoor.quote.api.entity.Shipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface  ShipmentMapper extends BaseMapper<Shipment> {
    IPage<Shipment> findByCondition(Page page, @Param("param") QuoteDTO dto);
}
