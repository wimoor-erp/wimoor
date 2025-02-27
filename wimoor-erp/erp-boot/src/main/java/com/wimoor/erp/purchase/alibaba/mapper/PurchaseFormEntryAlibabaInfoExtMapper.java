package com.wimoor.erp.purchase.alibaba.mapper;


import java.math.BigDecimal;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.purchase.alibaba.pojo.entity.PurchaseFormEntryAlibabaInfoExt;
@Mapper
public interface PurchaseFormEntryAlibabaInfoExtMapper extends BaseMapper<PurchaseFormEntryAlibabaInfoExt> {
 
	BigDecimal getPriceByAlibabaOrder(@Param("orderid") String orderid,@Param("acct") String acct);
}