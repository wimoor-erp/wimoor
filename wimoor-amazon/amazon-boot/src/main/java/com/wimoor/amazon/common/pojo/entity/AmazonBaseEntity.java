package com.wimoor.amazon.common.pojo.entity;

 
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Data
@EqualsAndHashCode(callSuper = true)
public class AmazonBaseEntity extends BaseEntity{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableField(value = "operator")
    private String operator;
 
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT_UPDATE,value = "opttime")
    private Date opttime;

 

 

 
 
}
