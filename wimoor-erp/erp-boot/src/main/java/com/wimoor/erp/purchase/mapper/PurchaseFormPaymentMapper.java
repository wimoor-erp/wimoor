package com.wimoor.erp.purchase.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPayment;
@Mapper
public interface PurchaseFormPaymentMapper  extends BaseMapper<PurchaseFormPayment> {
 
	  List<Map<String,Object>> paymentReport(@Param("param")Map<String, Object> param);
	  IPage<Map<String,Object>> paymentReport(Page<?> page,@Param("param") Map<String, Object> param);
	  Map<String,Object> paymentReportSummary(Map<String, Object> param);
}