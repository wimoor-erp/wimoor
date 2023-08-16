package com.wimoor.amazon.adv.common.pojo;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
 

public class AmzAdvRemindKey {
	@Id
	@Column(name="profileid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger profileid;

	@Id
	@Column(name="campaignid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger campaignid;

	@Id
	@Column(name="adgroupid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger adgroupid;

	@Id
	@Column(name="keywordid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger keywordid;

	@Id
	@Column(name="adid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger adid;
	
	@Id
	@Column(name="targetid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger targetid;
	
    public BigInteger getTargetid() {
		return targetid;
	}

	public void setTargetid(BigInteger targetid) {
		this.targetid = targetid;
	}

	public BigInteger getProfileid() {
        return profileid;
    }

    public void setProfileid(BigInteger profileid) {
        this.profileid = profileid;
    }

 
    public BigInteger getCampaignid() {
		return campaignid;
	}

	public void setCampaignid(BigInteger campaignid) {
		this.campaignid = campaignid;
	}

	public BigInteger getAdgroupid() {
        return adgroupid;
    }

    public void setAdgroupid(BigInteger adgroupid) {
        this.adgroupid = adgroupid;
    }

    public BigInteger getKeywordid() {
        return keywordid;
    }

    public void setKeywordid(BigInteger keywordid) {
        this.keywordid = keywordid;
    }

    public BigInteger getAdid() {
        return adid;
    }

    public void setAdid(BigInteger adid) {
        this.adid = adid;
    }
}