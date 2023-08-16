package com.wimoor.amazon.adv.common.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_remark")
public class AmzAdvRemark extends AmzAdvRemarkKey {
	
	@Column(name="remark")
    private String remark;

	@Column(name="opttime")
    private Date opttime;

	@Column(name="operator")
    private String operator;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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
}