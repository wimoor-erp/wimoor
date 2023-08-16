package com.wimoor.amazon.adv.common.pojo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="t_amz_adv_portfolios")
public class AmzAdvPortfolios {
	
	@Id
	@Column(name="id")
    private BigInteger id;
	
	@Id
	@Column(name="profileid")
    private BigInteger profileid;

	@Column(name="name")
    private String name;

	@Column(name="policy")
    private String policy;
	
	@Column(name="currencyCode")
    private String currencycode;

	@Column(name="amount")
    private BigDecimal amount;

	@Column(name="state")
    private String state;

	@Column(name="inBudget")
    private Boolean inbudget;

	@Column(name="startDate")
    private Date startdate;

	@Column(name="endDate")
    private Date enddate;

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

	public String getCurrencycode() {
		return currencycode;
	}

	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}

	public BigInteger getProfileid() {
		return profileid;
	}

	public void setProfileid(BigInteger profileid) {
		this.profileid = profileid;
	}

	public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy == null ? null : policy.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Boolean getInbudget() {
        return inbudget;
    }

    public void setInbudget(Boolean inbudget) {
        this.inbudget = inbudget;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Date getOpttime() {
        return opttime;
    }

    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }
}