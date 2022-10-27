package com.wimoor.amazon.adv.sd.pojo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wimoor.amazon.adv.common.pojo.JsonBigIntergeSerializer;


@Entity
@Table(name="t_amz_adv_rpt2_sd_product_targets")
public class AmzAdvReportProductTargetsSD {
	@Id
	@Column(name="bydate")
	private Date bydate;

	@Id
	@Column(name="targetId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
	private BigInteger targetid;

	@Column(name="campaignId")
    private BigInteger campaignid;

	@Column(name="adGroupId")
    private BigInteger adgroupid;
	
	@Column(name="profileid")
    private BigInteger profileid;

	@Column(name="impressions")
    private Integer impressions;

	@Column(name="clicks")
    private Integer clicks;

	@Column(name="cost")
    private BigDecimal cost;
 
	@Column(name="opttime")
	private Date opttime;
	
    /**
	 * @return the bydate
	 */
	public Date getBydate() {
		return bydate;
	}

	/**
	 * @param bydate the bydate to set
	 */
	public void setBydate(Date bydate) {
		this.bydate = bydate;
	}

	/**
	 * @return the targetid
	 */
	public BigInteger getTargetid() {
		return targetid;
	}

	/**
	 * @param targetid the targetid to set
	 */
	public void setTargetid(BigInteger targetid) {
		this.targetid = targetid;
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

    public BigInteger getProfileid() {
        return profileid;
    }

    public void setProfileid(BigInteger profileid) {
        this.profileid = profileid;
    }

    public Integer getImpressions() {
        return impressions;
    }

    public void setImpressions(Integer impressions) {
        this.impressions = impressions;
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

	public Date getOpttime() {
		return opttime;
	}

	public void setOpttime(Date opttime) {
		this.opttime = opttime;
	}
 
}