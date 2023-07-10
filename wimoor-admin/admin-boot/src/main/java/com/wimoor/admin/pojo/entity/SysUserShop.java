package com.wimoor.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_shop")
public class SysUserShop extends com.wimoor.common.pojo.entity.BaseEntity {
	private static final long serialVersionUID = 8619486384248070391L;
	@TableField(value= "user_id")
	private String userId;
	@TableField(value= "shop_id")
    private String shopId;
 
}