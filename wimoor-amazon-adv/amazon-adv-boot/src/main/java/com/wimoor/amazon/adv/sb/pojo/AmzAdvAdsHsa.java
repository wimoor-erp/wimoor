package com.wimoor.amazon.adv.sb.pojo;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_amz_adv_ads_hsa")
public class AmzAdvAdsHsa  {
	
	@Id
	@Column(name="adId")
    private BigInteger adid;
	
	@Column(name="adGroupId")
    private BigInteger adgroupid;

	@Column(name="campaignId")
    private BigInteger campaignid;

	@Column(name="profileid")
    private BigInteger profileid;


	@Column(name="name")
    private String name;

	@Column(name="opttime")
    private Date opttime;
	
	@Column(name="creative")
    private String creative;
	
	
	@Column(name="landingPage")
    private String landingPage;
	
	
	@Column(name="state")
    private String state;
	
	@Transient
	private String camname;
	
	
	public String getCreative() {
		return creative;
	}

	public void setCreative(String creative) {
		this.creative = creative;
	}

	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
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

	
	
    public String getCamname() {
		return camname;
	}

	public void setCamname(String camname) {
		this.camname = camname;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getOpttime() {
        return opttime;
    }

    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
    
    
}