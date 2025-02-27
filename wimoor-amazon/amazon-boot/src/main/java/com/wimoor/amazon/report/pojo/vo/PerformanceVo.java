package com.wimoor.amazon.report.pojo.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
@Data
public class PerformanceVo {
	BigDecimal orderDefectRate=new BigDecimal("0");
	BigDecimal orderDefectRateTargetValue=new BigDecimal("0");
	Integer policyTargetValue=  Integer.parseInt("0");
	Integer policyDefectsCount=Integer.parseInt("0");
	BigDecimal validTrackingRate=new BigDecimal("0");
	BigDecimal validTrackingRateTargetValue=new BigDecimal("0");
	BigDecimal lateShipmentRate=new BigDecimal("0");
	BigDecimal lateShipmentRateTargetValue=new BigDecimal("0");
	BigDecimal cancelRate=new BigDecimal("0");
	BigDecimal cancelRateTargetValue=new BigDecimal("0");
	BigDecimal invoiceDefectRate=new BigDecimal("0");
	BigDecimal invoiceDefectRateTargetValue=new BigDecimal("0");
	Date refreshTime;
}
