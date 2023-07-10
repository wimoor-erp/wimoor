package com.wimoor.amazon.adv.task.pojo;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.wimoor.amazon.adv.common.pojo.BaseObject;
@Entity
@Table(name="t_sys_timetask_log")
public class SysTimeTaskLog extends BaseObject{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8386062322965039495L;

	@Column(name = "createdate")
    private Date createdate;

    @Column(name = "job_id")
    private String jobId;

    @Column(name = "reason")
    private String reason;

    @Column(name = "state")
    private String state;

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId == null ? null : jobId.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }
}