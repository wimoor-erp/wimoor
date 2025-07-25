package com.wimoor.erp.purchase.alibaba.pojo.entity;

  import com.alibaba.logistics.param.AlibabaTradeGetLogisticsInfosBuyerViewResult;
   import com.alibaba.ocean.rawsdk.client.APIId;
  import com.alibaba.ocean.rawsdk.common.AbstractAPIRequest;
  
  public class AlibabaTradeGetLogisticsInfosBuyerViewParam extends AbstractAPIRequest<AlibabaTradeGetLogisticsInfosBuyerViewResult>
  {
  private String logisticsId;
  private String orderId;
  private String webSite;

    public AlibabaTradeGetLogisticsInfosBuyerViewParam()
    {
      this.oceanApiId = new APIId("com.alibaba.logistics", "alibaba.trade.getLogisticsInfos.buyerView", 1);
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