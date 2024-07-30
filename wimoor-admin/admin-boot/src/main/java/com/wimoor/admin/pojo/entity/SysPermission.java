package com.wimoor.admin.pojo.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigInteger;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_permission")
public class SysPermission extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4252918155993747825L;

    private String name;

    private BigInteger menuId;
    private String urlPerm;

    private String btnPerm;

    // 有权限的角色编号集合
    @TableField(exist = false)
    private List<String> roles;
    
    @TableField(exist = false)
    private String  roleids;
}
