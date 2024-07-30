package com.wimoor.amazon.adv.common.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wimoor.common.GeneralUtil;
 

@Entity
@Table(name="t_amz_adv_profile")
public class AmzAdvProfile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8375170556412379091L;

	@Id
	@Column(name="id")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger id;

	@Column(name="countryCode")
    private String countrycode;

	@Column(name="currencyCode")
    private String currencycode;

	@Column(name="marketplaceId")
    private String marketplaceid;

	@Column(name="timezone")
    private String timezone;
	
	@Column(name="type")
    private String type;
	
	@Column(name="sellerId")
    private String sellerid;
	
	@Column(name="dailyBudget")
	private BigDecimal dailyBudget;

	@Column(name="errorDay")
	private Date errorDay;
	
	@Column(name="opttime")
	private Date opttime;
	
	@Column(name="errorLog")
	private String errorLog;
	
	public BigDecimal getDailyBudget() {
		return dailyBudget;
	}

	public void setDailyBudget(BigDecimal dailyBudget) {
		this.dailyBudget = dailyBudget;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name="advauthId")
    private String advauthid;
	
	@Transient
	AmzAdvAuth advauth;
	@Transient
	public void setAdvAuth(AmzAdvAuth advauth) {
		this.advauth=advauth;
	}
	@Transient
	public AmzAdvAuth getAdvAuth() {
		return this.advauth;
	}
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id ;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode == null ? null : countrycode.trim();
    }

    public String getCurrencycode() {
        return currencycode;
    }

    public void setCurrencycode(String currencycode) {
        this.currencycode = currencycode == null ? null : currencycode.trim();
    }

    public String getMarketplaceid() {
        return marketplaceid;
    }

    public void setMarketplaceid(String marketplaceid) {
        this.marketplaceid = marketplaceid == null ? null : marketplaceid.trim();
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid == null ? null : sellerid.trim();
    }

	public String getAdvauthid() {
		return advauthid;
	}

	public void setAdvauthid(String advauthid) {
		this.advauthid = advauthid;
	}

	public Date getErrorDay() {
		return errorDay;
	}

	public void setErrorDay(Date errorDay) {
		this.errorDay = errorDay;
	}

	public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

	public Date getOpttime() {
		return opttime;
	}

	public void setOpttime(Date opttime) {
		this.opttime = opttime;
	}

	public boolean isError() {
		// TODO Auto-generated method stub
		if(errorDay!=null&&GeneralUtil.distanceOfHour(errorDay,new Date())<1)return true;
		return false;
	}
    
    
}