package com.wimoor.amazon.adv.sd.pojo;

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
@Table(name="t_amz_adv_productads_sd")
public class AmzAdvProductadsSD {
	
	@Id
	@Column(name="adId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger adid;

	@Column(name="adGroupId")
    private BigInteger adgroupid;

	@Column(name="campaignId")
    private BigInteger campaignid;

	@Column(name="profileid")
    private BigInteger profileid;

	@Column(name="sku")
    private String sku;

	@Column(name="asin")
    private String asin;

	@Column(name="state")
    private String state;

	@Column(name="opttime")
    private Date opttime;

	@Column(name="adName")
	String adName;
	
	@Column(name="landingPageType")
	String landingPageType;
	
	@Column(name="landingPageURL")
	String landingPageURL;
			
	@Transient
	private String servingStatus;
	    
    @Transient
	private Date creationDate;
    
    @Transient
	private Date lastUpdatedDate;
    
    
   
	public String getAdName() {
		return adName;
	}

	public void setAdName(String adName) {
		this.adName = adName;
	}

	public String getLandingPageType() {
		return landingPageType;
	}

	public void setLandingPageType(String landingPageType) {
		this.landingPageType = landingPageType;
	}

	public String getLandingPageURL() {
		return landingPageURL;
	}

	public void setLandingPageURL(String landingPageURL) {
		this.landingPageURL = landingPageURL;
	}

	public String getServingStatus() {
		return servingStatus;
	}

	public void setServingStatus(String servingStatus) {
		this.servingStatus = servingStatus;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public BigInteger getAdid() {
        return adid;
    }

    public void setAdid(BigInteger adid) {
        this.adid = adid;
    }

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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin == null ? null : asin.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Date getOpttime() {
        return opttime;
    }

    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }
}