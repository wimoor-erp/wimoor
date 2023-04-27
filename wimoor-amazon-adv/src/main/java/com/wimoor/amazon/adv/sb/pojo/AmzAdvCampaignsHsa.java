package com.wimoor.amazon.adv.sb.pojo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wimoor.amazon.adv.common.pojo.JsonBigIntergeSerializer;
 

@Entity
@Table(name = "t_amz_adv_campaigns_hsa")
public class AmzAdvCampaignsHsa {

	@Id
	@Column(name = "campaignId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
	private BigInteger campaignid;

	@Column(name = "profileid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
	private BigInteger profileid;
	
	@Column(name = "portfolioid")
	private BigInteger portfolioid;

	@Column(name = "name")
	private String name;

	@Column(name = "budgetType")
	private String budgettype;

	@Column(name = "budget")
	private BigDecimal budget;

	@Column(name = "startDate")
	private Date startdate;

	@Column(name = "endDate")
	private Date enddate;

	@Column(name = "servingStatus")
	private String servingstatus;

	@Column(name = "state")
	private String state;

	@Column(name = "spendingPolicy")
	private String spendingpolicy;

	@Column(name = "bidOptimization")
	private String bidoptimization;
	
	@Column(name = "bidAdjustments")
	private String bidAdjustments;
	
	@Column(name = "bidMultiplier")
	private BigDecimal bidMultiplier;

	@Column(name = "opttime")
	private Date opttime;
	
	@Column(name = "brandEntityId")
	private String brandEntityId;
	
	@Column(name = "landingPage")
	private String landingPage;
	
	@Column(name = "adFormat")
	private String adFormat;
	
	@Column(name = "creative")
	private String creative;
	
	@Column(name = "tags")
	private String tags;
	
	
	@Transient
	private String brandName;
	@Transient
	private String brandLogoAssetID;
	@Transient
	private String headline;
	@Transient
	private Boolean shouldOptimizeAsins;
	@Transient
	private List<String> asins;
	@Transient
	private List<String> allAsins;
	@Transient
	private String brandLogoUrl;
	@Transient
	private String url;
	@Transient
	private String pageType;
	
	public List<String> getAllAsins() {
		return allAsins;
	}

	public void setAllAsins(List<String> allAsins) {
		this.allAsins = allAsins;
	}

	public String getBrandEntityId() {
		return brandEntityId;
	}

	public void setBrandEntityId(String brandEntityId) {
		this.brandEntityId = brandEntityId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandLogoAssetID() {
		return brandLogoAssetID;
	}

	public void setBrandLogoAssetID(String brandLogoAssetID) {
		this.brandLogoAssetID = brandLogoAssetID;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public Boolean getShouldOptimizeAsins() {
		return shouldOptimizeAsins;
	}

	public void setShouldOptimizeAsins(Boolean shouldOptimizeAsins) {
		this.shouldOptimizeAsins = shouldOptimizeAsins;
	}

	public List<String> getAsins() {
		return asins;
	}

	public void setAsins(List<String> asins) {
		this.asins = asins;
	}

	public String getBrandLogoUrl() {
		return brandLogoUrl;
	}

	public void setBrandLogoUrl(String brandLogoUrl) {
		this.brandLogoUrl = brandLogoUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public BigDecimal getBidMultiplier() {
		return bidMultiplier;
	}

	public void setBidMultiplier(BigDecimal bidMultiplier) {
		this.bidMultiplier = bidMultiplier;
	}

	public BigInteger getPortfolioid() {
		return portfolioid;
	}

	public void setPortfolioid(BigInteger portfolioid) {
		this.portfolioid = portfolioid;
	}

	public BigInteger getCampaignid() {
		return campaignid;
	}

	public void setCampaignid(BigInteger campaignid) {
		this.campaignid = campaignid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getBudgettype() {
		return budgettype;
	}

	public void setBudgettype(String budgettype) {
		this.budgettype = budgettype == null ? null : budgettype.trim();
	}

	public BigDecimal getBudget() {
		return budget;
	}

	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public String getServingstatus() {
		return servingstatus;
	}

	public void setServingstatus(String servingstatus) {
		this.servingstatus = servingstatus == null ? null : servingstatus.trim();
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state == null ? null : state.trim();
	}

	public String getSpendingpolicy() {
		return spendingpolicy;
	}

	public void setSpendingpolicy(String spendingpolicy) {
		this.spendingpolicy = spendingpolicy == null ? null : spendingpolicy.trim();
	}

	public String getBidoptimization() {
		return bidoptimization;
	}

	public void setBidoptimization(String bidoptimization) {
		this.bidoptimization = bidoptimization == null ? null : bidoptimization.trim();
	}

	public BigInteger getProfileid() {
		return profileid;
	}

	public void setProfileid(BigInteger profileid) {
		this.profileid = profileid;
	}

	public Date getOpttime() {
		return opttime;
	}

	public void setOpttime(Date opttime) {
		this.opttime = opttime;
	}

	
	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}

	public String getAdFormat() {
		return adFormat;
	}

	public void setAdFormat(String adFormat) {
		this.adFormat = adFormat;
	}

	public String getCreative() {
		return creative;
	}

	public void setCreative(String creative) {
		this.creative = creative;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getBidAdjustments() {
		return bidAdjustments;
	}

	public void setBidAdjustments(String bidAdjustments) {
		this.bidAdjustments = bidAdjustments;
	}

	@Override
	public String toString() {
		return "AmzAdvCampaignsHsa [campaignid=" + campaignid + ", name=" + name + ", budgettype=" + budgettype
				+ ", budget=" + budget + ", startdate=" + startdate + ", enddate=" + enddate + ", servingstatus="
				+ servingstatus + ", state=" + state + ", spendingpolicy=" + spendingpolicy + ", bidoptimization="
				+ bidoptimization + ", profileid=" + profileid + ", opttime=" + opttime + "]";
	}
}