package com.wimoor.erp.finance.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
 

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_fin_project")
public class FinanceProject  extends ErpBaseEntity{
     
	/**
	 * 
	 */
	private static final long serialVersionUID = 2448368403588276205L;

	@TableField(value="name")
    private String name;

	@TableField(value="issys")
    private Boolean issys;

	@TableField(value="shopid")
    private String shopid;

	@TableField(value="createdate")
    private Date createdate;

	@TableField(value="creator")
    private String creator;
 
}