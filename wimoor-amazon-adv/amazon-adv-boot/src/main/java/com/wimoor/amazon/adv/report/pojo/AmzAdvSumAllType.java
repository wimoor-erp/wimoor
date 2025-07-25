package com.wimoor.amazon.adv.report.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name="t_amz_adv_sumalltype")
public class AmzAdvSumAllType extends AmzAdvSumAllTypeKey {
	@Column(name="quantity")
    private Integer quantity;

	@Column(name="opttime")
    private Date opttime;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getOpttime() {
        return opttime;
    }

    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }
}