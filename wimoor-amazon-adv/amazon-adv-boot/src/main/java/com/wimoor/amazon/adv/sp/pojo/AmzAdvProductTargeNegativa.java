package com.wimoor.amazon.adv.sp.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_amz_adv_product_targe_negativa")
public class AmzAdvProductTargeNegativa extends AmzAdvProductTargeNegativaKey {

	@Column(name="expressionType")
    private String expressiontype;

	@Column(name="expression")
    private String expression;
	
	@Column(name="state")
    private String state;

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

    public Date getOpttime() {
        return opttime;
    }

    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }
}