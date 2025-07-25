package com.wimoor.amazon.adv.sb.pojo;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Id;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wimoor.amazon.adv.common.pojo.JsonBigIntergeSerializer;
 

public class AmzAdvKeywordsHsaKey {
	@Column(name="adGroupId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger adgroupid;

	@Column(name="campaignId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger campaignid;

	@Id
	@Column(name="keywordId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger keywordid;
	
	@Column(name="campaignType")
    private String campaigntype;
	
	@Column(name="profileid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger profileid;

	public BigInteger getProfileid() {
		return profileid;
	}

	public void setProfileid(BigInteger profileid) {
		this.profileid = profileid;
	}

	public String getCampaigntype() {
		return campaigntype;
	}

	public void setCampaigntype(String campaigntype) {
		this.campaigntype = campaigntype;
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

	public BigInteger getKeywordid() {
		return keywordid;
	}

	public void setKeywordid(BigInteger keywordid) {
		this.keywordid = keywordid;
	}

}