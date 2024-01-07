package com.wimoor.amazon.adv.sd.pojo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wimoor.amazon.adv.common.pojo.JsonBigIntergeSerializer;
 

@Entity
@Table(name="t_amz_adv_product_targe_sd")
public class AmzAdvProductTargeSD {
	@Id
	@Column(name="targetId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger targetid;

	@Column(name="adGroupId")
    private BigInteger adgroupid;

	@Column(name="profileid")
    private BigInteger profileid;

	@Column(name="expressionType")
    private String expressiontype;

	@Column(name="expression")
    private String expression;

	@Column(name="state")
    private String state;

	@Column(name="bid")
    private BigDecimal bid;

	@Column(name="opttime")
    private Date opttime;

	@Transient
	private String servingStatus;
	    
    @Transient
	private Date creationDate;
    
    @Transient
	private Date lastUpdatedDate;
    
    
    public String getServingStatus() {
		return servingStatus;
	}

	public void setServingStatus(String servingStatus) {
		this.servingStatus = servingStatus;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public BigInteger getTargetid() {
        return targetid;
    }

    public void setTargetid(BigInteger targetid) {
        this.targetid = targetid;
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

    public String getExpressiontype() {
        return expressiontype;
    }

    public void setExpressiontype(String expressiontype) {
        this.expressiontype = expressiontype == null ? null : expressiontype.trim();
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression == null ? null : expression.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public Date getOpttime() {
        return opttime;
    }

    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }
}