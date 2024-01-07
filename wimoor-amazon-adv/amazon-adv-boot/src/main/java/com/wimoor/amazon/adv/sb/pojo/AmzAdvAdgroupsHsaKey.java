package com.wimoor.amazon.adv.sb.pojo;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Id;

public class AmzAdvAdgroupsHsaKey {
	@Id
	@Column(name="adGroupId")
    private BigInteger adgroupid;

	@Column(name="campaignId")
    private BigInteger campaignid;

	@Column(name="profileid")
    private BigInteger profileid;

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

}