package com.wimoor.amazon.finances.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_fin_user_item")
@ApiModel(value="AmzFinUserItem对象", description="")
public class AmzFinUserItem  extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7117494323907939591L;

	@TableField(value="shopid")
    private String shopid;

	@TableField(value="name")
    private String name;

	@TableField(value="creator")
    private String creator;

	@TableField(value="disable")
    private Boolean disable;

	@TableField(value="isused")
    private Boolean isused;

	@TableField(value="createdate")
    private Date createdate;
 
}