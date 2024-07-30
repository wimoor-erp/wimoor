package com.wimoor.amazon.adv.report.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_report_request")
public class AmzAdvRequest extends AmzAdvRequestKey {
	
	@Column(name="recordType")
    private String recordtype;
	
	@Column(name="campaignType")
    private String campaigntype;
	
	@Column(name="segment")
    private String segment;
	
	@Column(name="reportType")
	private Integer reportType;
	
	@Column(name="creativeType")
    private String creativeType;

	@Column(name="status")
	private String status;
	
	@Column(name="statusDetails")
    private String statusdetails;
	
	@Column(name="opttime")
	private Date opttime;
	
	@Column(name="location")
	private String location;
	
	@Column(name="fileSize")
	private Integer filesize;
    
	@Column(name="startDate")
	private Date startDate;
	
	@Column(name="endDate")
	private Date endDate;
	
	@Column(name="requesttime")
	private Date requesttime;
	
	@Column(name="treat_number")
	private Integer treatNumber;
	
	@Column(name="treat_status")
	private String treatStatus;
	
	@Column(name="log")
	private String log;
	
	@Column(name="isrun")
	private boolean isrun;

	public Date getRequesttime() {
		return requesttime;
	}

	public void setRequesttime(Date requesttime) {
		this.requesttime = requesttime;
	}

	public Integer getTreatNumber() {
		return treatNumber==null?0:treatNumber;
	}

	public void setTreatNumber(Integer treatNumber) {
		this.treatNumber = treatNumber;
	}

	public String getTreatStatus() {
		return treatStatus;
	}

	public void setTreatStatus(String treatStatus) {
		this.treatStatus = treatStatus;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}
	
	public String getCampaigntype() {
		return campaigntype;
	}

	public void setCampaigntype(String campaigntype) {
		this.campaigntype = campaigntype;
	}
	public String getRecordtype() {
        return recordtype;
    }

    public void setRecordtype(String recordtype) {
        this.recordtype = recordtype == null ? null : recordtype.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getStatusdetails() {
        return statusdetails;
    }

    public void setStatusdetails(String statusdetails) {
        this.statusdetails = statusdetails == null ? null : statusdetails.trim();
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

	public Integer getFilesize() {
		return filesize;
	}

	public void setFilesize(Integer filesize) {
		this.filesize = filesize;
	}
 
	public boolean isIsrun() {
		return isrun;
	}

	public void setIsrun(boolean isrun) {
		this.isrun = isrun;
	}

	public String getCreativeType() {
		return creativeType;
	}

	public void setCreativeType(String creativeType) {
		this.creativeType = creativeType;
	}

	public Integer getReportType() {
		return reportType;
	}

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
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
    
}