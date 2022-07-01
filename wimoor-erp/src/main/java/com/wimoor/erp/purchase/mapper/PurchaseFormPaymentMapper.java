package com.wimoor.erp.purchase.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPayment;
@Mapper
public interface PurchaseFormPaymentMapper  extends BaseMapper<PurchaseFormPayment> {
 
	  List<Map<String,Object>> paymentReport(Map<String, Object> param);
 
}