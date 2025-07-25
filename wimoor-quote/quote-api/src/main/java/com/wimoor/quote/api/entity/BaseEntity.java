package com.wimoor.quote.api.entity;

 

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.wimoor.quote.api.util.UUIDUtil;
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
		if(id==null) {
			id= UUIDUtil.getUUIDshort();
		}
		return id;
	}

	public boolean isNullId(){
		return id==null;
	}
	public void setId(String id) {
		this.id = id;
	}
   
   
}
