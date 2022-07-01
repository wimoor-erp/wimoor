package com.wimoor.erp.finance.pojo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
 
 

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_fin_type_journalmonthly")
public class FinTypeJournalMonthly extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -399693339946649604L;

	@TableField(value="projectid")
    private String projectid;

	@TableField(value="acct")
    private String acct;

	@TableField(value="year")
    private Integer year;

	@TableField(value="month")
    private Integer month;

	@TableField(value="rec")
    private BigDecimal rec;

	@TableField(value="pay")
    private BigDecimal pay;
 
 
}