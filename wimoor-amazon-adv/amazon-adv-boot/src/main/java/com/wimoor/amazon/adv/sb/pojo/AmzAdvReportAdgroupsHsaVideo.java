package com.wimoor.amazon.adv.sb.pojo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wimoor.amazon.adv.common.pojo.JsonBigIntergeSerializer;
 

@Entity
@Table(name="t_amz_adv_rpt2_hsa_campaigns_video")
public class AmzAdvReportAdgroupsHsaVideo {
	
	@Id
	@Column(name="adGroupId")
	@JsonSerialize(using = JsonBigIntergeSerializer.class)
    private BigInteger adgroupid;
	
	@Id
	@Column(name="bydate")
    private Date bydate;

	@Column(name="opttime")
    private Date opttime;
	
	@Column(name="viewableImpressions")
	private Integer viewableImpressions;
	
	@Column(name="videoFirstQuartileViews")
	private Integer videoFirstQuartileViews;
	
	@Column(name="videoMidpointViews")
	private Integer videoMidpointViews;
	
	@Column(name="videoThirdQuartileViews")
	private Integer videoThirdQuartileViews;
	
	@Column(name="videoCompleteViews")
	private Integer videoCompleteViews;
	
	@Column(name="video5SecondViews")
	private Integer video5SecondViews;
	
	@Column(name="video5SecondViewRate")
	private BigDecimal video5SecondViewRate;
	
	@Column(name="videoUnmutes")
	private Integer videoUnmutes;
	
	@Column(name="vtr")
	private BigDecimal vtr;
	
	@Column(name="vctr")
	private BigDecimal vctr;

	public BigInteger getAdgroupid() {
		return adgroupid;
	}

	public void setAdgroupid(BigInteger adgroupid) {
		this.adgroupid = adgroupid;
	}

	public Date getBydate() {
		return bydate;
	}

	public void setBydate(Date bydate) {
		this.bydate = bydate;
	}

	public Date getOpttime() {
		return opttime;
	}

	public void setOpttime(Date opttime) {
		this.opttime = opttime;
	}

	public Integer getViewableImpressions() {
		return viewableImpressions;
	}

	public void setViewableImpressions(Integer viewableImpressions) {
		this.viewableImpressions = viewableImpressions;
	}

	public Integer getVideoFirstQuartileViews() {
		return videoFirstQuartileViews;
	}

	public void setVideoFirstQuartileViews(Integer videoFirstQuartileViews) {
		this.videoFirstQuartileViews = videoFirstQuartileViews;
	}

	public Integer getVideoMidpointViews() {
		return videoMidpointViews;
	}

	public void setVideoMidpointViews(Integer videoMidpointViews) {
		this.videoMidpointViews = videoMidpointViews;
	}

	public Integer getVideoThirdQuartileViews() {
		return videoThirdQuartileViews;
	}

	public void setVideoThirdQuartileViews(Integer videoThirdQuartileViews) {
		this.videoThirdQuartileViews = videoThirdQuartileViews;
	}

	public Integer getVideoCompleteViews() {
		return videoCompleteViews;
	}

	public void setVideoCompleteViews(Integer videoCompleteViews) {
		this.videoCompleteViews = videoCompleteViews;
	}

	public Integer getVideo5SecondViews() {
		return video5SecondViews;
	}

	public void setVideo5SecondViews(Integer video5SecondViews) {
		this.video5SecondViews = video5SecondViews;
	}

	public BigDecimal getVideo5SecondViewRate() {
		return video5SecondViewRate;
	}

	public void setVideo5SecondViewRate(BigDecimal video5SecondViewRate) {
		this.video5SecondViewRate = video5SecondViewRate;
	}

	public Integer getVideoUnmutes() {
		return videoUnmutes;
	}

	public void setVideoUnmutes(Integer videoUnmutes) {
		this.videoUnmutes = videoUnmutes;
	}

	public BigDecimal getVtr() {
		return vtr;
	}

	public void setVtr(BigDecimal vtr) {
		this.vtr = vtr;
	}

	public BigDecimal getVctr() {
		return vctr;
	}

	public void setVctr(BigDecimal vctr) {
		this.vctr = vctr;
	}
	
  public boolean iszero() {
		  if(viewableImpressions!=null&&viewableImpressions!=0)return false; 
		  if(videoFirstQuartileViews!=null&&videoFirstQuartileViews!=0)return false; 	
		  if(videoMidpointViews!=null&&videoMidpointViews!=0)return false; 	
		  if(videoThirdQuartileViews!=null&&videoThirdQuartileViews!=0)return false; 	
		  if(videoCompleteViews!=null&&videoCompleteViews!=0)return false; 	
		  if(video5SecondViews!=null&&video5SecondViews!=0)return false; 	
		  if(videoUnmutes!=null&&videoUnmutes!=0)return false; 	
		  if(video5SecondViewRate!=null&&video5SecondViewRate.floatValue()>0.001)return false; 	
		  if(vtr!=null&&vtr.floatValue()>0.001)return false; 	
		  if(vctr!=null&&vctr.floatValue()>0.001)return false; 	
		  return true;
	  }
	 
}