package com.wimoor.amazon.adv.task.pojo;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_schedule_plan")
public class AmzAdvSchedulePlan {
	
	@Id
	@Column(name="id")
    private BigInteger id;
	
	@Column(name="taskName")
    private String taskname;

	@Column(name="status")
    private String status;

	@Column(name="conditionsExecute")
    private String conditionsexecute;

	@Column(name="executeStatus")
    private String executestatus;

	@Column(name="startDate")
    private Date startdate;

	@Column(name="operator")
    private String operator;

	@Column(name="opttime")
    private Date opttime;

	@Column(name="creator")
    private String creator;

	@Column(name="createtime")
    private Date createtime;
	
	@Column(name="remark")
    private String remark;

    public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname == null ? null : taskname.trim();
    }

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getConditionsexecute() {
        return conditionsexecute;
    }

    public void setConditionsexecute(String conditionsexecute) {
        this.conditionsexecute = conditionsexecute == null ? null : conditionsexecute.trim();
    }

    public String getExecutestatus() {
        return executestatus;
    }

    public void setExecutestatus(String executestatus) {
        this.executestatus = executestatus == null ? null : executestatus.trim();
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}