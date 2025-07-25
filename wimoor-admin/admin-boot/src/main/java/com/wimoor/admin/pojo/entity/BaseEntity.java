package com.wimoor.admin.pojo.entity;

 

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wimoor.admin.util.UUIDUtil;

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

    @TableField(fill = FieldFill.INSERT)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;
    
	public String getId() {
		if(id==null) {
			id=UUIDUtil.getUUIDshort();
		}
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
   
   
}
