package com.wimoor.amazon.adv.sp.pojo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wimoor.amazon.adv.common.pojo.JsonBigIntergeSerializer;
 
@Entity
@Table(name="t_amz_adv_campaigns")
public class AmzAdvCampaigns {
	@Id
	@Column(name="campaignId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger campaignid;

	@Column(name="profileid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger profileid;
	
	@Column(name = "portfolioid")
	private BigInteger portfolioid;
	
	@Column(name="name")
    private String name;

	@Column(name="campaignType")
    private String campaignType;

	@Column(name="dailyBudget")
    private BigDecimal dailybudget;

	@Column(name="targetingType")
    private String targetingType;
	
	@Column(name="premiumBidAdjustment")
    private String premiumbidadjustment;
	
	@Column(name="bidding")
    private String bidding;

	@Column(name="state")
    private String state;

	@Column(name="startDate")
    private Date startDate;
	
	@Column(name="endDate")
    private Date endDate;

	@Column(name="opttime")
    private Date opttime;
	
	@Transient
    private String budget;
	
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
    
	public BigInteger getPortfolioid() {
		return portfolioid;
	}

	public void setPortfolioid(BigInteger portfolioid) {
		this.portfolioid = portfolioid;
	}
	
	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget,JSONObject budgetjson) {
		this.budget = budget;
		if(budgetjson!=null) {
			this.dailybudget=budgetjson.getBigDecimal("budget");
		}
	}
	
	public String getBidding() {
		return bidding;
	}

	public void setBidding(String bidding) {
		this.bidding = bidding;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date enddate) {
		this.endDate = enddate;
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

    public String getCampaignType() {
        return campaignType;
    }

    public void setCampaignType(String campaignType) {
        this.campaignType = campaignType == null ? null : campaignType.trim();
    }

    public BigDecimal getDailybudget() {
        return dailybudget;
    }

    public void setDailybudget(BigDecimal dailybudget) {
        this.dailybudget = dailybudget;
    }

    public String getTargetingType() {
        return targetingType;
    }

    public void setTargetingType(String targetingtype) {
        this.targetingType = targetingtype == null ? null : targetingtype.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getPremiumbidadjustment() {
		return premiumbidadjustment;
	}

	public void setPremiumbidadjustment(String premiumbidadjustment) {
		this.premiumbidadjustment = premiumbidadjustment;
	}

	public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startdate) {
        this.startDate = startdate;
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

    @Transient
    public String getCompaignTypePrefix() {
		if("sponsoredBrands".equals(campaignType)) {
			return "hsa";
		}else {
			return "sp";
		}
    }

	@Override
	public String toString() {
		return "AmzAdvCampaigns [campaignid=" + campaignid + ", name=" + name + ", campaigntype=" + campaignType
				+ ", dailybudget=" + dailybudget + ", targetingtype=" + targetingType + ", state=" + state
				+ ", startdate=" + startDate + ", enddate=" + endDate
				+ ", profileid=" + profileid + ", opttime=" + opttime + "]";
	}


}