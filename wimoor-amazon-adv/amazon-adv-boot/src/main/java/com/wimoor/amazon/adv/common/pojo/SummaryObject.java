package com.wimoor.amazon.adv.common.pojo;

import java.math.BigDecimal;

public class SummaryObject{
	private String asin;
	
	private Integer clicks;

    private Integer impressions;

    private Double ctr;

    private String currency;

    private BigDecimal spend;

    private BigDecimal cpc;
	
    private BigDecimal totalsales;
	
	private Integer orders;
 
    private Double acos;
	
    private Double roas;
	
    private Double spc;
	
	private Integer units;
	
	private Integer count;
	
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getAsin() {
			return asin;
		}

		public void setAsin(String asin) {
			this.asin = asin;
		}
		
	public Integer getClicks() {
		if(clicks==null)return 0;
		return clicks;
	}

	public void setClicks(Integer clicks) {
		this.clicks = clicks;
	}

	public Integer getImpressions() {
		if(impressions==null)return 0;
		return impressions;
	}

	public void setImpressions(Integer impressions) {
		this.impressions = impressions;
	}

	public Double getCtr() {
		if(ctr==null)return 0.0;
		return ctr;
	}

	public void setCtr(Double ctr) {
		this.ctr = ctr;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getSpend() {
		if(spend==null)return new BigDecimal("0");
		return spend;
	}

	public void setSpend(BigDecimal spend) {
		this.spend = spend;
	}

	public BigDecimal getCpc() {
		if(cpc==null)return new BigDecimal("0");
		return cpc;
	}

	public void setCpc(BigDecimal cpc) {
		this.cpc = cpc;
	}

	public BigDecimal getTotalsales() {
		if(totalsales==null)return new BigDecimal("0");
		return totalsales;
	}

	public void setTotalsales(BigDecimal totalsales) {
		this.totalsales = totalsales;
	}

	public Integer getOrders() {
		if(orders==null)return 0;
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public Double getAcos() {
		if(acos==null)return 0.0;
		return acos;
	}

	public void setAcos(Double acos) {
		this.acos = acos;
	}

	public Double getRoas() {
		if(roas==null)return 0.0;
		return roas;
	}

	public void setRoas(Double roas) {
		this.roas = roas;
	}

	public Double getSpc() {
		if(spc==null)return 0.0;
		return spc;
	}

	public void setSpc(Double spc) {
		this.spc = spc;
	}

	public Integer getUnits() {
		if(units==null)return 0;
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}
	
	
}