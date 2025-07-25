package com.wimoor.amazon.adv.sp.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_amz_adv_productads")
public class AmzAdvProductads extends AmzAdvProductadsKey {

	@Column(name="sku")
    private String sku;
	
	@Column(name="asin")
    private String asin;

	@Column(name="state")
    private String state;
	
	@Column(name="customText")
	private String customText;

	@Column(name="opttime")
    private Date opttime;

	@Transient
	private String servingStatus;
	    
    @Transient
	private Date creationDate;
    
    @Transient
	private Date lastUpdatedDate;
	
    @Transient
    public String getServingStatus() {
		return servingStatus;
	}

    @Transient
	public void setServingStatus(String servingStatus) {
		this.servingStatus = servingStatus;
	}

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
	
    public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
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

	public String getCustomText() {
		return customText;
	}

	public void setCustomText(String customText) {
		this.customText = customText;
	}
    
}