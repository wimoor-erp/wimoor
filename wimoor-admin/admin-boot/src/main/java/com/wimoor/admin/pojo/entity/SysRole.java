package com.wimoor.admin.pojo.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_role")
public class SysRole implements  Serializable  {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2032630341374103301L;

	@TableId(value= "id")
    private BigInteger id;

	@TableField(value= "name")
    private String name;

    @ApiModelProperty("角色类型")
    @TableField(value= "type")
    private String type;

    @TableField(value= "issystem")
    private Boolean issystem;

    private BigInteger shopid;

    @TableField(exist = false)
    private List<BigInteger> menuIds;

    @TableField(exist = false)
    private List<BigInteger> permissionIds;

}
