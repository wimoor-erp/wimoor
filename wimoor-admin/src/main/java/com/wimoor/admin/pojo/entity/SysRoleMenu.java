package com.wimoor.admin.pojo.entity;

import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_sys_role_menu")
public class SysRoleMenu {
    
    private BigInteger roleId;

    private BigInteger menuId;

}
