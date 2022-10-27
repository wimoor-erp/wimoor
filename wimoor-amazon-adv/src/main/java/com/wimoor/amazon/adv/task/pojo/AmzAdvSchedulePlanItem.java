package com.wimoor.amazon.adv.task.pojo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_schedule_planitem")
public class AmzAdvSchedulePlanItem {
	
	@Id
	@Column(name="taskId")
    private String taskid;
	
	@Id
	@Column(name="planId")
    private BigInteger planid;

	@Column(name="type")
    private String type;
	
	@Column(name="bid")
    private BigDecimal bid;
	
	@Column(name="status")
    private String status;

	@Column(name="startTime")
    private Date starttime;

	@Column(name="weekdays")
    private String weekdays;

	public BigDecimal getBid() {
		return bid;
	}

	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public BigInteger getPlanid() {
		return planid;
	}

	public void setPlanid(BigInteger planid) {
		this.planid = planid;
	}

	public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public String getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(String weekdays) {
        this.weekdays = weekdays;
    }
}