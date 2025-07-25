package com.wimoor.amazon.adv.common.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

import com.wimoor.amazon.adv.utils.UUIDUtil;


public class BaseObject  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6237251660442372146L;
	
	@Id
    @Column(name = "id")
	protected String id;
 
	public String getId() {
		if (null == id) {
			id=UUIDUtil.getUUIDshort();
		}
			//id = UUID.randomUUID().toString();

		return id;
	}

	public Boolean HasId() {
		return id != null ? true : false;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}
}
