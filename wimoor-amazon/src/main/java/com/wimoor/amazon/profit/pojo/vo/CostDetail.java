package com.wimoor.amazon.profit.pojo.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

 

import io.swagger.annotations.ApiModel;
import lombok.Data;
@Data
@ApiModel(value="CostDetail对象", description="产品费用")
public class CostDetail implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2453383952019217889L;

	private Integer id;

    private String country;
    
    private String currency;
    
    private String productTier;
    
    private String productTierId;
    
    private BigDecimal outboundWeight = new BigDecimal("0");
    
    private BigDecimal purchase = new BigDecimal("0");

    private BigDecimal shipment = new BigDecimal("0");

    private BigDecimal referralFee = new BigDecimal("0");

    private BigDecimal VCFee = new BigDecimal("0");

    private BigDecimal FBA = new BigDecimal("0");
    
    //private BigDecimal DST = new BigDecimal("0");

    private BigDecimal storageFee = new BigDecimal("0");

    private BigDecimal manualProcessingFee = new BigDecimal("0");
    
    private BigDecimal labelServiceFee = new BigDecimal("0");
    
    private BigDecimal prepServiceFee = new BigDecimal("0");
    
    private BigDecimal inPlaceFee = new BigDecimal("0");

    private BigDecimal tax = new BigDecimal("0");
    
    private BigDecimal taxRate = new BigDecimal("0");
    
    private BigDecimal currencyTransportRate = new BigDecimal("0");

    private BigDecimal currencyTransportFee = new BigDecimal("0");

    private BigDecimal marketing = new BigDecimal("0");
    
    private BigDecimal marketingRate = new BigDecimal("0");

    private BigDecimal promotionFee = new BigDecimal("0");
    
    private BigDecimal promotionRate = new BigDecimal("0");
    
    private BigDecimal vatRate = new BigDecimal("0");
    
    private BigDecimal vatFee = new BigDecimal("0");
    
    private BigDecimal others = new BigDecimal("0");//其他费用
    
    private BigDecimal otherRate = new BigDecimal("0");//其它每单销售固定成本比率

    private BigDecimal othersFee = new BigDecimal("0");
    
    private BigDecimal rateFee = new BigDecimal("0");//所有比率之和
    
    private BigDecimal closingFee = new BigDecimal("0");
    
    private BigDecimal fbaTax = new BigDecimal("0");
    
    private BigDecimal fbaTaxFee = new BigDecimal("0");
    
    private BigDecimal gstrate = new BigDecimal("0");
    
    private BigDecimal import_GST = new BigDecimal("0");
    
    private BigDecimal selling_GSTRate = new BigDecimal("0");
    
    private BigDecimal selling_GST = new BigDecimal("0");
    
    private BigDecimal corporateInRate = new BigDecimal("0");
    
    private BigDecimal corporateInFee = new BigDecimal("0");
    
    private BigDecimal totalCost;//所有成本项之和
    
    private BigDecimal sellingPrice;
    
    private BigDecimal profit;//利润
    
    private String margin;//利润率
    
    private List<Map<String, String>> resultList;//按固定利率估算售价和利润
    
    public CostDetail() {
		
	}
    
    public CostDetail(Map<String,String> map) {
		this.setCountry(map.get("country"));
		this.setCurrency(map.get("currency"));
		this.setProductTier(map.get("productTier"));
		this.setProductTierId(map.get("productTierId"));
		String outboundWeight = map.get("outboundWeight");
		this.setOutboundWeight(new BigDecimal(outboundWeight));
		this.setPurchase(new BigDecimal(map.get("purchase")));
		this.setShipment(new BigDecimal(map.get("shipment")));
		this.setReferralFee(new BigDecimal(map.get("referralFee")));
		String vcfee = map.get("vcfee");///
		this.setVCFee(new BigDecimal(vcfee));
		this.setFBA(new BigDecimal(map.get("fba")));///
		this.setStorageFee(new BigDecimal(map.get("storageFee")));
		this.setPrepServiceFee(new BigDecimal(map.get("prepServiceFee")));
		this.setManualProcessingFee(new BigDecimal(map.get("manualProcessingFee")));
		this.setLabelServiceFee(new BigDecimal(map.get("labelServiceFee")));
		this.setInPlaceFee(new BigDecimal(map.get("inPlaceFee")));
		this.setTax(new BigDecimal(map.get("tax")));
		this.setTaxRate(new BigDecimal(map.get("taxRate")));
		this.setCurrencyTransportFee(new BigDecimal(map.get("currencyTransportFee")));
		this.setCurrencyTransportRate(new BigDecimal(map.get("currencyTransportRate")));
		this.setMarketing(new BigDecimal(map.get("marketing")));
		this.setMarketingRate(new BigDecimal(map.get("marketingRate")));
		this.setPromotionFee(new BigDecimal(map.get("promotionFee")));
		this.setPromotionRate(new BigDecimal(map.get("promotionRate")));
		this.setVatRate(new BigDecimal(map.get("vatRate")));
		this.setVatFee(new BigDecimal(map.get("vatFee")));
		this.setOthers(new BigDecimal(map.get("others")));
		this.setOtherRate(new BigDecimal(map.get("otherRate")));
		this.setOthersFee(new BigDecimal(map.get("othersFee")));
		this.setRateFee(new BigDecimal(map.get("rateFee")));
		this.setClosingFee(new BigDecimal(map.get("closingFee")));
		this.setFbaTax(new BigDecimal(map.get("fbaTax")));
		this.setFbaTaxFee(new BigDecimal(map.get("fbaTaxFee")));
		this.setGstrate(new BigDecimal(map.get("gstrate")));
		this.setImport_GST(new BigDecimal(map.get("import_GST")));
		this.setSelling_GSTRate(new BigDecimal(map.get("selling_GSTRate")));
		this.setSelling_GST(new BigDecimal(map.get("selling_GST")));
		this.setCorporateInRate(new BigDecimal(map.get("corporateInRate")));
		this.setCorporateInFee(new BigDecimal(map.get("corporateInFee")));
		if (map.get("totalCost")!=null && !map.get("totalCost").equals("null")) {
			this.setTotalCost(new BigDecimal(map.get("totalCost")));
		}
   	}

    
	public String getProductTierId() {
		return productTierId;
	}

	public void setProductTierId(String productTierId) {
		this.productTierId = productTierId;
	}

	public BigDecimal getCorporateInRate() {
		return corporateInRate;
	}

	public void setCorporateInRate(BigDecimal corporateInRate) {
		this.corporateInRate = corporateInRate;
	}

	public BigDecimal getCorporateInFee() {
		return corporateInFee;
	}

	public void setCorporateInFee(BigDecimal corporateInFee) {
		this.corporateInFee = corporateInFee;
	}


	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public BigDecimal getMarketingRate() {
		return marketingRate;
	}

	public void setMarketingRate(BigDecimal marketingRate) {
		this.marketingRate = marketingRate;
	}

	public BigDecimal getPromotionRate() {
		return promotionRate;
	}

	public void setPromotionRate(BigDecimal promotionRate) {
		this.promotionRate = promotionRate;
	}

	public BigDecimal getVatRate() {
		return vatRate;
	}

	public void setVatRate(BigDecimal vatRate) {
		this.vatRate = vatRate;
	}
	
	public BigDecimal getVatFee() {
		return vatFee;
	}

	public void setVatFee(BigDecimal vatFee) {
		this.vatFee = vatFee;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getRateFee() {
		return rateFee;
	}

	public void setRateFee(BigDecimal rateFee) {
		this.rateFee = rateFee;
	}

	public BigDecimal getOtherRate() {
		return otherRate;
	}

	public void setOtherRate(BigDecimal otherRate) {
		this.otherRate = otherRate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getProductTier() {
		return productTier;
	}

	public void setProductTier(String productTier) {
		this.productTier = productTier;
	}

	public BigDecimal getOutboundWeight() {
		return outboundWeight;
	}

	public void setOutboundWeight(BigDecimal outboundWeight) {
		this.outboundWeight = outboundWeight;
	}

	public BigDecimal getPurchase() {
		return purchase;
	}

	public void setPurchase(BigDecimal purchase) {
		this.purchase = purchase;
	}

	public BigDecimal getShipment() {
		return shipment;
	}

	public void setShipment(BigDecimal shipment) {
		this.shipment = shipment;
	}

	public BigDecimal getReferralFee() {
		return referralFee;
	}

	public void setReferralFee(BigDecimal referralFee) {
		this.referralFee = referralFee;
	}

	public BigDecimal getVCFee() {
		return VCFee;
	}

	public void setVCFee(BigDecimal vCFee) {
		VCFee = vCFee;
	}

	public BigDecimal getFBA() {
		return FBA;
	}

	public void setFBA(BigDecimal fBA) {
		this.FBA = fBA;
	}
	

	public BigDecimal getStorageFee() {
		return storageFee;
	}

	public void setStorageFee(BigDecimal storageFee) {
		this.storageFee = storageFee;
	}

	public BigDecimal getPrepServiceFee() {
		return prepServiceFee;
	}

	public void setPrepServiceFee(BigDecimal prepServiceFee) {
		this.prepServiceFee = prepServiceFee;
	}

	public BigDecimal getManualProcessingFee() {
		return manualProcessingFee;
	}

	public void setManualProcessingFee(BigDecimal manualProcessingFee) {
		this.manualProcessingFee = manualProcessingFee;
	}

	public BigDecimal getLabelServiceFee() {
		return labelServiceFee;
	}

	public void setLabelServiceFee(BigDecimal labelServiceFee) {
		this.labelServiceFee = labelServiceFee;
	}

	public BigDecimal getInPlaceFee() {
		return inPlaceFee;
	}

	public void setInPlaceFee(BigDecimal inPlaceFee) {
		this.inPlaceFee = inPlaceFee;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public BigDecimal getCurrencyTransportFee() {
		return currencyTransportFee;
	}

	public void setCurrencyTransportFee(BigDecimal currencyTransportFee) {
		this.currencyTransportFee = currencyTransportFee;
	}

	public BigDecimal getCurrencyTransportRate() {
		return currencyTransportRate;
	}

	public void setCurrencyTransportRate(BigDecimal currencyTransportRate) {
		this.currencyTransportRate = currencyTransportRate;
	}

	public BigDecimal getMarketing() {
		return marketing;
	}

	public void setMarketing(BigDecimal marketing) {
		this.marketing = marketing;
	}

	public BigDecimal getPromotionFee() {
		return promotionFee;
	}

	public void setPromotionFee(BigDecimal promotionFee) {
		this.promotionFee = promotionFee;
	}

	public BigDecimal getOthers() {
		return others;
	}

	public void setOthers(BigDecimal others) {
		this.others = others;
	}

	public BigDecimal getOthersFee() {
		return othersFee;
	}

	public void setOthersFee(BigDecimal othersFee) {
		this.othersFee = othersFee;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public BigDecimal getProfit() {
		if(profit==null)return new BigDecimal("0");
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public String getMargin() {
		return margin;
	}

	public void setMargin(String margin) {
		this.margin = margin;
	}

	public List<Map<String, String>> getResultList() {
		return resultList;
	}

	public void setResultList(List<Map<String, String>> resultList) {
		this.resultList = resultList;
	}

	public BigDecimal getClosingFee() {
		return closingFee;
	}

	public void setClosingFee(BigDecimal closingFee) {
		this.closingFee = closingFee;
	}

	public BigDecimal getFbaTax() {
		return fbaTax;
	}

	public void setFbaTax(BigDecimal fbaTax) {
		this.fbaTax = fbaTax;
	}

	public BigDecimal getFbaTaxFee() {
		return fbaTaxFee;
	}

	public void setFbaTaxFee(BigDecimal fbaTaxFee) {
		this.fbaTaxFee = fbaTaxFee;
	}

	public BigDecimal getGstrate() {
		return gstrate;
	}

	public void setGstrate(BigDecimal gstrate) {
		this.gstrate = gstrate;
	}

	public BigDecimal getImport_GST() {
		return import_GST;
	}

	public void setImport_GST(BigDecimal import_GST) {
		this.import_GST = import_GST;
	}

	public BigDecimal getSelling_GSTRate() {
		return selling_GSTRate;
	}

	public void setSelling_GSTRate(BigDecimal selling_GSTRate) {
		this.selling_GSTRate = selling_GSTRate;
	}

	public BigDecimal getSelling_GST() {
		return selling_GST;
	}

	public void setSelling_GST(BigDecimal selling_GST) {
		this.selling_GST = selling_GST;
	}

	public void setPerItemFee(BigDecimal perItemFee) {
		// TODO Auto-generated method stub
		
	}
 

}