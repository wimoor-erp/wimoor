package com.wimoor.amazon.adv.report.service;

import java.math.BigDecimal;
import java.util.Map;

public interface IAmzAdvSumProductAdsService {
  public Map<String,Object> getSumProduct(Map<String,Object> param);
  public Map<String, Object> getDaysSumProduct(Map<String,Object> param);
  public BigDecimal orderSummaryAll(Map<String,Object> param);
  public Map<String,Object> orderDaysSummaryAll(Map<String,Object> param);
  public Map<String, Object> getMonthSumProduct(Map<String,Object> param); 
}
