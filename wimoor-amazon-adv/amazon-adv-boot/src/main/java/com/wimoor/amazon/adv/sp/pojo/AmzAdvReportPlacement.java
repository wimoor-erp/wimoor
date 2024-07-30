package com.wimoor.amazon.adv.sp.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_amz_adv_rpt_placement")
public class AmzAdvReportPlacement  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7627187489980753596L;

	@Id
	@Column(name="id")
    private Integer id;

	@Column(name="name")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}