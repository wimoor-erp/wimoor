package com.wimoor.erp.stock.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
 
 
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_changewh_form")
public class ChangeWhForm extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8159926706106868999L;

	@TableField(value= "number")
    private String number;

	@TableField(value= "warehouseid")
    private String warehouseid;

	@TableField(value= "remark")
    private String remark;

	@TableField(value= "shopid")
    private String shopid;

	@TableField(value= "creator")
    private String creator;

	@TableField(value= "createdate")
    private Date createdate;

	@TableField(value= "operator")
    private String operator;

	@TableField(value= "opttime")
    private Date opttime;

	@TableField(value= "auditstatus")
    private Integer auditstatus;


     
}