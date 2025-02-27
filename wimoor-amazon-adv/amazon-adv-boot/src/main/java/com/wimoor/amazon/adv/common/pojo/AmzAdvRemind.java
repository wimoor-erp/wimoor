package com.wimoor.amazon.adv.common.pojo;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_remind")
public class AmzAdvRemind extends AmzAdvRemindKey {
	@Column(name="cycle")
    private Integer cycle;

	@Column(name="quota")
    private String quota;

	@Column(name="fcondition")
    private String condition;

	@Column(name="subtrahend")
    private String subtrahend;

	@Column(name="amount")
    private BigDecimal amount;

	@Column(name="iswarn")
    private Boolean iswarn;
	
	@Column(name="createdate")
	private Date createdate;
	
	@Column(name="opttime")
	private Date opttime;

	@Column(name="creator")
	private String creator;
	
	@Column(name="operator")
	private String operator;
	
	@Column(name="recordtype")
	private String recordtype;
	
	
    public String getRecordtype() {
		return recordtype;
	}

	public void setRecordtype(String recordtype) {
		this.recordtype = recordtype;
	}

	public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota == null ? null : quota.trim();
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition == null ? null : condition.trim();
    }

    public String getSubtrahend() {
        return subtrahend;
    }

    public void setSubtrahend(String subtrahend) {
        this.subtrahend = subtrahend == null ? null : subtrahend.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getIswarn() {
        return iswarn;
    }

    public void setIswarn(Boolean iswarn) {
        this.iswarn = iswarn;
    }

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getOpttime() {
		return opttime;
	}

	public void setOpttime(Date opttime) {
		this.opttime = opttime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
    
}