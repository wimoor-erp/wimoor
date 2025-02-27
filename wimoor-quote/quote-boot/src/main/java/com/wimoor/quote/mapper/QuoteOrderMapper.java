package com.wimoor.quote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.quote.api.dto.QuoteDTO;
import com.wimoor.quote.api.entity.QuoteOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface QuoteOrderMapper extends BaseMapper<QuoteOrder> {

    IPage<QuoteOrder> findByCondition(Page<?> page, @Param("param")QuoteDTO dto);
    IPage<QuoteOrder> findSupplierByCondition(Page<Object> page, @Param("param")Map<String,Object> param);
}
