package com.wimoor.amazon.adv.sb.pojo;

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
@Table(name="t_amz_adv_rpt2_hsa_product_targets")
public class AmzAdvReportProductTargetsHsa   {
	
	@Id
	@Column(name="bydate")
    private Date bydate;

	@Id
	@Column(name="targetId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger targetid;

	@Id
	@Column(name="campaignId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger campaignid;

	@Id
	@Column(name="profileid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger profileid;

	@Id
	@Column(name="adGroupId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger adgroupid;
	
	@Column(name="impressions")
    private Integer impressions;

	@Column(name="clicks")
    private Integer clicks;

	@Column(name="cost")
    private BigDecimal cost;
	
	@Column(name="opttime")
    private Date opttime;
	
    public Date getBydate() {
        return bydate;
    }

    public void setBydate(Date bydate) {
        this.bydate = bydate;
    }

	public BigInteger getTargetid() {
		return targetid;
	}

	public void setTargetid(BigInteger targetid) {
		this.targetid = targetid;
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

	public BigInteger getAdgroupid() {
		return adgroupid;
	}

	public void setAdgroupid(BigInteger adgroupid) {
		this.adgroupid = adgroupid;
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