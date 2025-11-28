package com.wimoor.admin.pojo.dto;

import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ApiModel(value="SysUserRoleDTO对象", description="用户角色关系")
@TableName("t_user_role")
public class SysUserRoleDTO {
	
	@ApiModelProperty(value = "用户ID")
	@TableField(value= "user_id")
    private BigInteger userId;

	@ApiModelProperty(value = "角色ID")
	@TableField(value= "role_id")
    private BigInteger roleId;

}