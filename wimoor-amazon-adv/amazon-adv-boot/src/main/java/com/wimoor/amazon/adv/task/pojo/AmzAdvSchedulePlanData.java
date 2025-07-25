package com.wimoor.amazon.adv.task.pojo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_schedule_plandata")
public class AmzAdvSchedulePlanData {
	
	@Id
	@Column(name="id")
    private BigInteger id;

	@Column(name="planid")
    private BigInteger planid;

	@Column(name="profileid")
    private BigInteger profileid;

	@Column(name="campaignId")
    private BigInteger campaignid;

	@Column(name="adGroupId")
    private BigInteger adgroupid;

	@Column(name="adId")
    private BigInteger adid;

	@Column(name="keywordId")
    private BigInteger keywordid;

	@Column(name="oldbid")
    private BigDecimal oldbid;

	@Column(name="oldstatus")
    private String oldstatus;
	
	@Column(name="operator")
    private String operator;

	@Column(name="opttime")
    private Date opttime;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getPlanid() {
        return planid;
    }

    public void setPlanid(BigInteger planid) {
        this.planid = planid  ;
    }

    public BigInteger getProfileid() {
        return profileid;
    }

    public void setProfileid(BigInteger profileid) {
        this.profileid = profileid;
    }

    public BigInteger getCampaignid() {
        return campaignid;
    }

    public void setCampaignid(BigInteger campaignid) {
        this.campaignid = campaignid;
    }

    public BigInteger getAdgroupid() {
        return adgroupid;
    }

    public void setAdgroupid(BigInteger adgroupid) {
        this.adgroupid = adgroupid;
    }

    public BigInteger getAdid() {
        return adid;
    }

    public void setAdid(BigInteger adid) {
        this.adid = adid;
    }

	public BigInteger getKeywordid() {
        return keywordid;
    }

    public void setKeywordid(BigInteger keywordid) {
        this.keywordid = keywordid;
    }

    public BigDecimal getOldbid() {
        return oldbid;
    }

    public void setOldbid(BigDecimal oldbid) {
        this.oldbid = oldbid;
    }

    public String getOldstatus() {
        return oldstatus;
    }

    public void setOldstatus(String oldstatus) {
        this.oldstatus = oldstatus == null ? null : oldstatus.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getOpttime() {
        return opttime;
    }

    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }
}