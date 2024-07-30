package com.wimoor.amazon.adv.report.pojo;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="t_amz_adv_snapshot")
public class AmzAdvSnapshot {
	
	@Id
	@Column(name="snapshotId")
    private String snapshotid;

	@Column(name="profileid")
    private BigInteger profileid;

	@Column(name="region")
    private String region;

    public String getSnapshotid() {
        return snapshotid;
    }

    public void setSnapshotid(String snapshotid) {
        this.snapshotid = snapshotid == null ? null : snapshotid.trim();
    }

    public BigInteger getProfileid() {
        return profileid;
    }

    public void setProfileid(BigInteger profileid) {
        this.profileid = profileid  ;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }
    
	@Column(name="status")
    private String status;

	@Column(name="location")
    private String location;
	
	@Column(name="recordtype")
    private String recordtype;
	
	@Column(name="campaignType")
    private String campaigntype;
	
	@Column(name="fileSize")
    private Integer filesize;
	
	@Column(name="requesttime")
    private Date requesttime;
	
	@Column(name="opttime")
    private Date opttime;
	
	@Column(name="treat_number")
    private Integer treatnumber;
	
	@Column(name="treat_status")
    private String treatstatus;
	
	@Column(name="log")
    private String log;
	
	public String getTreatstatus() {
		return treatstatus;
	}

	public void setTreatstatus(String treatstatus) {
		this.treatstatus = treatstatus;
	}

	public Date getRequesttime() {
		return requesttime;
	}

	public void setRequesttime(Date requesttime) {
		this.requesttime = requesttime;
	}

	public Integer getTreatnumber() {
		return treatnumber == null ? 0 : treatnumber;
	}

	public void setTreatnumber(Integer treatnumber) {
		this.treatnumber = treatnumber;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public Integer getFilesize() {
		return filesize;
	}

	public void setFilesize(Integer filesize) {
		this.filesize = filesize;
	}

	public String getCampaigntype() {
		return campaigntype;
	}

	public void setCampaigntype(String campaigntype) {
		this.campaigntype = campaigntype;
	}
	
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRecordtype() {
        return recordtype;
    }

    public void setRecordtype(String recordtype) {
        this.recordtype = recordtype == null ? null : recordtype.trim();
    }

    public Date getOpttime() {
        return opttime;
    }

    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
    
}