package com.wimoor.amazon.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_amz_settlement_amount_description")
public class AmzAmountDescription extends BaseEntity{

	private static final long serialVersionUID = 8945706029696667918L;

	@TableField(value= "cname")
    private String cname;

	@TableField(value="ename")
    private String ename;

	@TableField(value="cdescription")
    private String cdescription;

	@TableField(value="edescription")
    private String edescription;
	
}
