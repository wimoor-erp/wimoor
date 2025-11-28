package com.wimoor.erp.finance.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_fin_journaldaily")
public class FinJournalDaily extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8450070238115205963L;

	@TableField(value="acct")
    private String acct;

	@TableField(value="byday")
    private Date byday;

	@TableField(value="rec")
    private BigDecimal rec;

	@TableField(value="pay")
    private BigDecimal pay;

	@TableField(value="balance")
    private BigDecimal balance;

 
}