package com.wimoor.amazon.adv.common.pojo;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_warningdate")
public class AmzAdvWarningDate extends AmzAdvWarningDateKey {
	 
	@Column(name="num_impressions")
    private BigDecimal numImpressions;
	
	@Column(name="num_clicks")
    private BigDecimal numClicks;
	
	@Column(name="num_CSRT")
    private BigDecimal numCSRT;
	
	@Column(name="num_ACOS")
    private BigDecimal numACOS;
	
	@Column(name="absoluteNum_impressions")
    private Integer absoluteNumImpressions;
	
	@Column(name="absoluteNum_clicks")
    private Integer absoluteNumClicks;
	
	@Column(name="absoluteNum_CSRT")
    private BigDecimal absoluteNumCSRT;
	
	@Column(name="absoluteNum_ACOS")
    private BigDecimal absoluteNumACOS;

	public BigDecimal getNumImpressions() {
		return numImpressions;
	}

	public Integer getAbsoluteNumImpressions() {
		return absoluteNumImpressions;
	}

	public void setAbsoluteNumImpressions(int absoluteNumImpressions) {
		this.absoluteNumImpressions = absoluteNumImpressions;
	}

	public Integer getAbsoluteNumClicks() {
		return absoluteNumClicks;
	}

	public void setAbsoluteNumClicks(int absoluteNumClicks) {
		this.absoluteNumClicks = absoluteNumClicks;
	}

	public BigDecimal getAbsoluteNumCSRT() {
		return absoluteNumCSRT;
	}

	public void setAbsoluteNumCSRT(BigDecimal absoluteNumCSRT) {
		this.absoluteNumCSRT = absoluteNumCSRT;
	}

	public BigDecimal getAbsoluteNumACOS() {
		return absoluteNumACOS;
	}

	public void setAbsoluteNumACOS(BigDecimal absoluteNumACOS) {
		this.absoluteNumACOS = absoluteNumACOS;
	}

	public void setNumImpressions(BigDecimal numImpressions) {
		this.numImpressions = numImpressions;
	}

	public BigDecimal getNumClicks() {
		return numClicks;
	}

	public void setNumClicks(BigDecimal numClicks) {
		this.numClicks = numClicks;
	}

	public BigDecimal getNumCSRT() {
		return numCSRT;
	}

	public void setNumCSRT(BigDecimal numCSRT) {
		this.numCSRT = numCSRT;
	}

	public BigDecimal getNumACOS() {
		return numACOS;
	}

	public void setNumACOS(BigDecimal numACOS) {
		this.numACOS = numACOS;
	}
	
}