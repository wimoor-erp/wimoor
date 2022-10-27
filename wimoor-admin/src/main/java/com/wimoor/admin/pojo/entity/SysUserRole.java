package com.wimoor.admin.pojo.entity;

import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_role")
public class SysUserRole extends com.wimoor.common.pojo.entity.BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7907120392687249943L;

	@TableField(value= "user_id")
    private BigInteger userId;

	@TableField(value= "role_id")
    private BigInteger roleId;

}
