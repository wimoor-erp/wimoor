package com.wimoor.amazon.adv.sb.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_adgroups_hsa")
public class AmzAdvAdgroupsHsa extends AmzAdvAdgroupsHsaKey {
	@Column(name="name")
    private String name;

	@Column(name="opttime")
    private Date opttime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getOpttime() {
        return opttime;
    }

    public void setOpttime(Date opttime) {
        this.opttime = opttime;
    }
}