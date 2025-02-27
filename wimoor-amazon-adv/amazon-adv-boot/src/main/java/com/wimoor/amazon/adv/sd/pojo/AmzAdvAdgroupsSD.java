package com.wimoor.amazon.adv.sd.pojo;

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
@Table(name="t_amz_adv_adgroups_sd")
public class AmzAdvAdgroupsSD {
	
	@Id
	@Column(name="adGroupId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger adgroupid;

	@Column(name="campaignId")
    private BigInteger campaignid;

	@Column(name="profileid")
    private BigInteger profileid;

	@Column(name="name")
    private String name;

	@Column(name="state")
    private String state;
	
	@Column(name="bidOptimization")
    private String bidOptimization;
	
	@Column(name="creativeType")
    private String creativeType;
	
	@Column(name="defaultBid")
    private BigDecimal defaultbid;

	@Column(name="opttime")
    private Date opttime;

	@Transient
	private String servingStatus;
	    
    @Transient
	private Date creationDate;
    
    @Transient
	private Date lastUpdatedDate;
    
    @Transient
    private String camname;
    
    public BigInteger getAdgroupid() {
        return adgroupid;
    }

    public void setAdgroupid(BigInteger adgroupid) {
        this.adgroupid = adgroupid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
    
    public String getCamname() {
        return camname;
    }

    public void setCamname(String camname) {
        this.camname = camname == null ? null : camname.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public BigDecimal getDefaultbid() {
        return defaultbid;
    }

    public void setDefaultbid(BigDecimal defaultbid) {
        this.defaultbid = defaultbid;
    }

    public Date getOpttime() {
        return opttime;
    }

    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }

	public String getBidOptimization() {
		return bidOptimization;
	}

	public void setBidOptimization(String bidOptimization) {
		this.bidOptimization = bidOptimization;
	}

	public String getCreativeType() {
		return creativeType;
	}

	public void setCreativeType(String creativeType) {
		this.creativeType = creativeType;
	}

	/**
	 * @return the servingStatus
	 */
	public String getServingStatus() {
		return servingStatus;
	}

	/**
	 * @param servingStatus the servingStatus to set
	 */
	public void setServingStatus(String servingStatus) {
		this.servingStatus = servingStatus;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the lastUpdatedDate
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	/**
	 * @param lastUpdatedDate the lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
    
}