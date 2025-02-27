package com.wimoor.amazon.adv.common.dao;

import com.wimoor.amazon.adv.common.pojo.AmzAdvertInvoices;
import com.wimoor.amazon.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AmzAdvertInvoicesMapper extends BaseMapper<AmzAdvertInvoices> {
    List<Map<String, Object>> getInvoicesSummary(Map<String, Object> param);
}
