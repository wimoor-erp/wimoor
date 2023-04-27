package com.wimoor.erp.finance.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_fin_journalaccount")
public class FinJournalAccount extends ErpBaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1614160713410091558L;

	@NotNull(message="费用类型不能为空")
	@TableField(value= "ftype")
    private String ftype;
	
	@NotNull(message="项目ID不能为空")
	@TableField(value= "projectid")
    private String projectid;

	@NotNull(message="金额不能为空")
	@TableField(value= "amount")
    private BigDecimal amount;
	
	@TableField(value= "balance")
	private BigDecimal balance;

	@TableField(value= "remark")
    private String remark;

	@TableField(value= "creator")
    private String creator;

	@TableField(value= "createtime")
    private Date createtime;
    
    @TableField(value= "shopid")
    private String shopid;

    @TableField(value= "acct")
    private String acct;
	 
}