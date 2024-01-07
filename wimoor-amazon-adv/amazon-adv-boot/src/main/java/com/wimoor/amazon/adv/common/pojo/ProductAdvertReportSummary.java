package com.wimoor.amazon.adv.common.pojo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="t_amz_advert_report_summary")
public class ProductAdvertReportSummary  extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1395966675821615234L;

	@Column(name="asin")
    private String asin;

	@Column(name="clicks")
    private Integer clicks;

	@Column(name="impressions")
    private Integer impressions;

	@Column(name="ctr")
    private Double ctr;

	@Column(name="currency")
    private String currency;

	@Column(name="spend")
    private BigDecimal spend;

	@Column(name="cpc")
    private BigDecimal cpc;

	@Column(name="acos")
    private Double acos;

	@Column(name="roas")
    private Double roas;

	@Column(name="orders")
    private Integer orders;

	@Column(name="units")
    private Integer units;

	@Column(name="spc")
    private Double spc;

	@Column(name="totalsales")
    private BigDecimal totalsales;

	@Column(name="sellerid")
    private String sellerid;

 
	@Column(name="marketplaceid")
    private String marketplaceid;
 
	@Column(name="sku")
    private String sku;
	
	@Column(name="ctype")
    private String ctype;
 
	@Column(name="bydate")
    private Date bydate;

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid == null ? null : sellerid.trim();
    }

    public String getMarketplaceid() {
        return marketplaceid;
    }

    public void setMarketplaceid(String marketplaceid) {
        this.marketplaceid = marketplaceid == null ? null : marketplaceid.trim();
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public Date getBydate() {
        return bydate;
    }

    public void setBydate(Date bydate) {
        this.bydate = bydate;
    }
    
    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin == null ? null : asin.trim();
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    public Integer getImpressions() {
        return impressions;
    }

    public void setImpressions(Integer impressions) {
        this.impressions = impressions;
    }

    public Double getCtr() {
        return ctr;
    }

    public void setCtr(Double ctr) {
        this.ctr = ctr;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public BigDecimal getSpend() {
        return spend;
    }

    public void setSpend(BigDecimal spend) {
        this.spend = spend;
    }

    public BigDecimal getCpc() {
        return cpc;
    }

    public void setCpc(BigDecimal cpc) {
        this.cpc = cpc;
    }

    public Double getAcos() {
        return acos;
    }

    public void setAcos(Double acos) {
        this.acos = acos;
    }

    public Double getRoas() {
        return roas;
    }

    public void setRoas(Double roas) {
        this.roas = roas;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public Double getSpc() {
        return spc;
    }

    public void setSpc(Double spc) {
        this.spc = spc;
    }

    public BigDecimal getTotalsales() {
        return totalsales;
    }

    public void setTotalsales(BigDecimal totalsales) {
        this.totalsales = totalsales;
    }

	public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}
    
    
}