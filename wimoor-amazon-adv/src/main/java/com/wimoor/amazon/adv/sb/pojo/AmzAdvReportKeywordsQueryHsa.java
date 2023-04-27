package com.wimoor.amazon.adv.sb.pojo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wimoor.amazon.adv.common.pojo.BaseObject;
import com.wimoor.amazon.adv.common.pojo.JsonBigIntergeSerializer;
 
 

@Entity
@Table(name="t_amz_adv_rpt2_hsa_keywords_query")
public class AmzAdvReportKeywordsQueryHsa extends BaseObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5101296371416472126L;
 
	@Id
	@Column(name="bydate")
    private Date bydate;
 
	@Column(name="keywordId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger keywordid;

	@Column(name="adGroupId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger adgroupid;

	@Column(name="campaignId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger campaignid;

	@Column(name="profileid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger profileid;
 
	@Column(name="impressions")
    private Integer impressions;

	@Column(name="clicks")
    private Integer clicks;

	@Column(name="cost")
    private BigDecimal cost;

	@Column(name="attributedConversions14d")
    private Integer attributedconversions14d;

	@Column(name="attributedSales14d")
    private BigDecimal attributedsales14d;
 
	
	@Column(name="queryid")
    private BigInteger queryid;
	
	@Column(name="opttime")
    private Date opttime;
	
	public Date getBydate() {
		return bydate;
	}

	public void setBydate(Date bydate) {
		this.bydate = bydate;
	}

	public BigInteger getKeywordid() {
		return keywordid;
	}

	public void setKeywordid(BigInteger keywordid) {
		this.keywordid = keywordid;
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

    public Integer getAttributedconversions14d() {
        return attributedconversions14d;
    }

    public void setAttributedconversions14d(Integer attributedconversions14d) {
        this.attributedconversions14d = attributedconversions14d;
    }

    public BigDecimal getAttributedsales14d() {
        return attributedsales14d;
    }

    public void setAttributedsales14d(BigDecimal attributedsales14d) {
        this.attributedsales14d = attributedsales14d;
    }

	public BigInteger getQueryid() {
		return queryid;
	}

	public void setQueryid(BigInteger queryid) {
		this.queryid = queryid;
	}

	public Date getOpttime() {
		return opttime;
	}

	public void setOpttime(Date opttime) {
		this.opttime = opttime;
	}
 
}