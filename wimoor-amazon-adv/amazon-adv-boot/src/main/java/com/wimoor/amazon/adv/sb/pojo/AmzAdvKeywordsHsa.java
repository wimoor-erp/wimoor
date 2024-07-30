package com.wimoor.amazon.adv.sb.pojo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_amz_adv_keywords_hsa")
public class AmzAdvKeywordsHsa extends AmzAdvKeywordsHsaKey {
	@Column(name="keywordText")
    private String keywordtext;

	@Column(name="nativeLanguageKeyword")
    private String nativeLanguageKeyword;
	
	@Column(name="matchType")
    private String matchtype;

	
	@Column(name="bid")
    private BigDecimal bid;

	@Column(name="state")
    private String state;

	@Column(name="opttime")
    private Date opttime;

    public String getKeywordtext() {
        return keywordtext;
    }
    
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

	public void setKeywordtext(String keywordtext) {
        this.keywordtext = keywordtext == null ? null : keywordtext.trim();
    }

    public String getMatchtype() {
        return matchtype;
    }

    public void setMatchtype(String matchtype) {
        this.matchtype = matchtype == null ? null : matchtype.trim();
    }

    public BigDecimal getBid() {
		return bid;
	}

	public void setBid(BigDecimal bid) {
		this.bid = bid;
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

	public String getNativeLanguageKeyword() {
		return nativeLanguageKeyword;
	}

	public void setNativeLanguageKeyword(String nativeLanguageKeyword) {
		this.nativeLanguageKeyword = nativeLanguageKeyword;
	}
    
    
}