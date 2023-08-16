package com.wimoor.erp.common.pojo.entity;

 

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
 

@Data
public class BaseEntity implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@TableId(value = "id" )
	@ApiModelProperty(value = "ID")
    String id;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public boolean idIsNULL() {
	   return StrUtil.isBlank(id);
	} 
   
}
