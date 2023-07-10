package com.wimoor.amazon.adv.sd.pojo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wimoor.amazon.adv.common.pojo.JsonBigIntergeSerializer;
 

@Entity
@Table(name="t_amz_adv_rpt2_sd_campaigns_t00001")
public class AmzAdvReportCampaignsT0001SD{
	@Id
	@Column(name="campaignId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger campaignid;

	@Id
	@Column(name="bydate")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
	private Date bydate;

	@Id
	@Column(name="profileid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
	private BigInteger profileid;

	@Column(name="impressions")
    private Integer impressions;

	@Column(name="clicks")
    private Integer clicks;

	@Column(name="cost")
    private BigDecimal cost;

	@Column(name="currency")
    private String currency;

	@Column(name="attributedDPV14d")
    private Integer attributeddpv14d;

	@Column(name="attributedUnitsSold14d")
    private Integer attributedunitssold14d;

	@Column(name="attributedSales14d")
    private Integer attributedsales14d;
    
	  public BigInteger getCampaignid() {
	        return campaignid;
	    }

	    public void setCampaignid(BigInteger campaignid) {
	        this.campaignid = campaignid;
	    }

	    public Date getBydate() {
	        return bydate;
	    }

	    public void setBydate(Date bydate) {
	        this.bydate = bydate;
	    }

	    public BigInteger getProfileid() {
	        return profileid;
	    }

	    public void setProfileid(BigInteger profileid) {
	        this.profileid = profileid;
	    }

    public Integer getImpressions() {
        return impressions;
    }

    public void setImpressions(Integer impressions) {
        this.impressions = impressions;
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public Integer getAttributeddpv14d() {
        return attributeddpv14d;
    }

    public void setAttributeddpv14d(Integer attributeddpv14d) {
        this.attributeddpv14d = attributeddpv14d;
    }

    public Integer getAttributedunitssold14d() {
        return attributedunitssold14d;
    }

    public void setAttributedunitssold14d(Integer attributedunitssold14d) {
        this.attributedunitssold14d = attributedunitssold14d;
    }

    public Integer getAttributedsales14d() {
        return attributedsales14d;
    }

    public void setAttributedsales14d(Integer attributedsales14d) {
        this.attributedsales14d = attributedsales14d;
    }
}