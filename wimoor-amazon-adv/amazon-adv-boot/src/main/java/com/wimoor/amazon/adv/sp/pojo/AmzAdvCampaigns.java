package com.wimoor.amazon.adv.sp.pojo;

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
    private String campaigntype;

	@Column(name="dailyBudget")
    private BigDecimal dailybudget;

	@Column(name="targetingType")
    private String targetingtype;
	
	@Column(name="premiumBidAdjustment")
    private String premiumbidadjustment;
	
	@Column(name="bidding")
    private String bidding;

	@Column(name="state")
    private String state;

	@Column(name="startDate")
    private Date startdate;
	
	@Column(name="endDate")
    private Date enddate;

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
    
	public BigInteger getPortfolioid() {
		return portfolioid;
	}

	public void setPortfolioid(BigInteger portfolioid) {
		this.portfolioid = portfolioid;
	}

	public String getBidding() {
		return bidding;
	}

	public void setBidding(String bidding) {
		this.bidding = bidding;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
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

    public String getCampaigntype() {
        return campaigntype;
    }

    public void setCampaigntype(String campaigntype) {
        this.campaigntype = campaigntype == null ? null : campaigntype.trim();
    }

    public BigDecimal getDailybudget() {
        return dailybudget;
    }

    public void setDailybudget(BigDecimal dailybudget) {
        this.dailybudget = dailybudget;
    }

    public String getTargetingtype() {
        return targetingtype;
    }

    public void setTargetingtype(String targetingtype) {
        this.targetingtype = targetingtype == null ? null : targetingtype.trim();
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

	public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
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
		if("sponsoredBrands".equals(campaigntype)) {
			return "hsa";
		}else {
			return "sp";
		}
    }

	@Override
	public String toString() {
		return "AmzAdvCampaigns [campaignid=" + campaignid + ", name=" + name + ", campaigntype=" + campaigntype
				+ ", dailybudget=" + dailybudget + ", targetingtype=" + targetingtype + ", state=" + state
				+ ", startdate=" + startdate + ", enddate=" + enddate
				+ ", profileid=" + profileid + ", opttime=" + opttime + "]";
	}


}