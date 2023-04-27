package com.wimoor.erp.purchase.alibaba.pojo.entity;
 
import com.alibaba.logistics.param.AlibabaTradeGetLogisticsTraceInfoBuyerViewResult;
import com.alibaba.ocean.rawsdk.client.APIId;
import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;

public class AlibabaTradeGetLogisticsTraceInfoBuyerViewParam extends AbstractAPIRequest<AlibabaTradeGetLogisticsTraceInfoBuyerViewResult>
{
  private String logisticsId;
  private String orderId;
  private String webSite;

  public AlibabaTradeGetLogisticsTraceInfoBuyerViewParam()
  {
    this.oceanApiId = new APIId("com.alibaba.logistics", "alibaba.trade.getLogisticsTraceInfo.buyerView", 1);
  }

  public String getLogisticsId()
  {
    return this.logisticsId;
  }

  public void setLogisticsId(String logisticsId)
  {
    this.logisticsId = logisticsId;
  }

  public String getOrderId()
  {
    return this.orderId;
  }

  public void setOrderId(String orderId)
  {
    this.orderId = orderId;
  }

  public String getWebSite()
  {
    return this.webSite;
  }

  public void setWebSite(String webSite)
  {
    this.webSite = webSite;
  }
}