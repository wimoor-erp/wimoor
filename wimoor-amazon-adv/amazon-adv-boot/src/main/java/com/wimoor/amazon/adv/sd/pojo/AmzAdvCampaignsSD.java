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
@Table(name="t_amz_adv_campaigns_sd")
public class AmzAdvCampaignsSD {
	
	@Id
	@Column(name="campaignId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger campaignid;

	@Column(name="profileid")
    private BigInteger profileid;
	
	@Column(name="portfolioId")
    private BigInteger portfolioId;
	
	@Column(name="name")
    private String name;

	@Column(name="tactic")
    private String tactic;

	@Column(name="costType")
    private String costtype;
	
	@Column(name="budgetType")
    private String budgettype;

	@Column(name="budget")
    private BigDecimal budget;

	@Column(name="startDate")
    private Date startDate;

	@Column(name="endDate")
    private Date endDate;

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
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTactic() {
        return tactic;
    }

    public void setTactic(String tactic) {
        this.tactic = tactic == null ? null : tactic.trim();
    }

    public String getBudgettype() {
        return budgettype;
    }

    public void setBudgettype(String budgettype) {
        this.budgettype = budgettype == null ? null : budgettype.trim();
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    
    public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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


 
      
	/**
	 * @return the servingStatus
	 */
    @Transient
	public String getServingStatus() {
		return servingStatus;
	}

	/**
	 * @param servingStatus the servingStatus to set
	 */
    @Transient
	public void setServingStatus(String servingStatus) {
		this.servingStatus = servingStatus;
	}

	/**
	 * @return the creationDate
	 */
    @Transient
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
    @Transient
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the lastUpdatedDate
	 */
    @Transient
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public BigInteger getPortfolioId() {
		return portfolioId;
	}

	public void setPortfolioId(BigInteger portfolioId) {
		this.portfolioId = portfolioId;
	}

	/**
	 * @param lastUpdatedDate the lastUpdatedDate to set
	 */
    @Transient
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public String getCosttype() {
		return costtype;
	}

	public void setCosttype(String costtype) {
		this.costtype = costtype;
	}

	 

    
}