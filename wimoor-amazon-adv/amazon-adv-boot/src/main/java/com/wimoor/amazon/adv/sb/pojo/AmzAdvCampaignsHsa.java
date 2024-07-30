package com.wimoor.amazon.adv.sb.pojo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

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
	private String budgetType;

	@Column(name = "budget")
	private BigDecimal budget;

	@Column(name = "startDate")
	private Date startDate;

	@Column(name = "endDate")
	private Date endDate;

	@Column(name = "state")
	private String state;

	@Column(name="bidding")
    private String bidding;
	
	@Column(name="brandEntityId")
    private String brandEntityId;
	
	@Column(name="goal")
    private String goal;
	
	@Column(name="productLocation")
    private String productLocation;
	
	@Column(name="tags")
    private String tags;
	
	@Column(name="costType")
    private String costType;
	
	@Column(name="smartDefault")
    private String smartDefault;
	
	@Column(name="opttime")
	private Date opttime;
	
    @Transient
	private String servingStatus;
    
    @Transient
	private String placement;

	@Transient
	private Date creationDate;
    
    @Transient
	private Date lastUpdatedDate;
	
    @Transient
    public String getServingStatus() {
		return servingStatus;
	}

    @Transient
	public void setServingStatus(String servingStatus) {
		this.servingStatus = servingStatus;
	}

    @Transient
	public Date getCreationDate() {
		return creationDate;
	}

    @Transient
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

    @Transient
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

    @Transient
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

    @Transient
    public String getPlacement() {
		return placement;
	}

    @Transient
	public void setPlacement(String placement) {
		this.placement = placement;
	}
    
    
	public BigInteger getCampaignid() {
		return campaignid;
	}

	public void setCampaignid(BigInteger campaignid) {
		this.campaignid = campaignid;
	}

	public BigInteger getProfileid() {
		return profileid;
	}

	public void setProfileid(BigInteger profileid) {
		this.profileid = profileid;
	}

	public BigInteger getPortfolioid() {
		return portfolioid;
	}

	public void setPortfolioid(BigInteger portfolioid) {
		this.portfolioid = portfolioid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBudgetType() {
		return budgetType;
	}

	public void setBudgetType(String budgetType) {
		this.budgetType = budgetType;
	}

	public BigDecimal getBudget() {
		return budget;
	}

	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
 

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBidding() {
		return bidding;
	}

	public void setBidding(String bidding) {
		this.bidding = bidding;
	}

	public String getBrandEntityId() {
		return brandEntityId;
	}

	public void setBrandEntityId(String brandEntityId) {
		this.brandEntityId = brandEntityId;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public String getProductLocation() {
		return productLocation;
	}

	public void setProductLocation(String productLocation) {
		this.productLocation = productLocation;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getSmartDefault() {
		return smartDefault;
	}

	public void setSmartDefault(String smartDefault) {
		this.smartDefault = smartDefault;
	}

	public Date getOpttime() {
		return opttime;
	}

	public void setOpttime(Date opttime) {
		this.opttime = opttime;
	}
 
	
	
 
}