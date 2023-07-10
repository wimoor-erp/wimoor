package com.wimoor.amazon.adv.common.pojo;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
 

@Entity
@Table(name="t_amz_adv_operate_log")
public class AmzAdvOperateLog {
	
	@Id
	@Column(name="id")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger id;
	
	@Column(name="profileid")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger profileid;

	@Column(name="campaignId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger campaignid;

	@Column(name="adGroupId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger adgroupid;

	@Column(name="opttime")
    private Date opttime;

	@Column(name="operator")
    private String operator;

	@Column(name="beanclasz")
    private String beanclasz;

	@Column(name="beforeobject")
    private String beforeobject;

	@Column(name="afterobject")
    private String afterobject;
	
	@Column(name="remark")
    private String remark;
	
    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigInteger getId() {
        return id;
    }

    public BigInteger getProfileid() {
		return profileid;
	}

	public void setProfileid(BigInteger profileid) {
		this.profileid = profileid;
	}

	public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getCampaignid() {
		return campaignid;
	}

	public void setCampaignid(BigInteger campaignid) {
		this.campaignid = campaignid;
	}

	public BigInteger getAdgroupid() {
		if(adgroupid == null)
			return new BigInteger("0");
		return adgroupid;
	}

	public void setAdgroupid(BigInteger adgroupid) {
		this.adgroupid = adgroupid;
	}

	public Date getOpttime() {
        return opttime;
    }

    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getBeanclasz() {
        return beanclasz;
    }

    public void setBeanclasz(String beanclasz) {
        this.beanclasz = beanclasz == null ? null : beanclasz.trim();
    }

    public String getBeforeobject() {
        return beforeobject;
    }

    public void setBeforeobject(String beforeobject) {
        this.beforeobject = beforeobject == null ? null : beforeobject.trim();
    }

    public String getAfterobject() {
        return afterobject;
    }

    public void setAfterobject(String afterobject) {
        this.afterobject = afterobject == null ? null : afterobject.trim();
    }
}