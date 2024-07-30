package com.wimoor.amazon.adv.sb.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_amz_adv_keywords_negativa_hsa")
public class AmzAdvKeywordsNegativaHsa extends AmzAdvKeywordsNegativaHsaKey {
	
	@Column(name="campaignType")
    private String campaigntype;
	
	@Column(name="keywordText")
    private String keywordtext;

	@Column(name="matchType")
    private String matchtype;

	@Column(name="state")
    private String state;

	@Column(name="opttime")
    private Date opttime;
	
	@Transient
	private Date creationDate;
	
	@Transient
	private Date lastUpdatedDate;
	
	@Transient
	private String servingStatus;

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
	public String getServingStatus() {
		return servingStatus;
	}

	@Transient
	public void setServingStatus(String servingStatus) {
		this.servingStatus = servingStatus;
	}

    public String getCampaigntype() {
		return campaigntype;
	}

	public void setCampaigntype(String campaigntype) {
		this.campaigntype = campaigntype;
	}

	public String getKeywordtext() {
        return keywordtext;
    }

    public void setKeywordtext(String keywordtext) {
        this.keywordtext = keywordtext == null ? null : keywordtext.trim();
    }

    public String getMatchtype() {
        return matchtype;
    }

    public void setMatchtype(String matchtype) {
        this.matchtype = matchtype == null ? null : matchtype.trim();
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