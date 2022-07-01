package com.wimoor.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

 
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_menu")
public class SysMenu extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6460782889198446595L;

    private String parentId;

    private String name;

    private String icon;

    /**
     * 路由路径
     */
    private String path;

    private String component;

    private Integer sort;

    private Integer visible;

    private String redirect;
    
    private String oldid;

    @TableField(exist = false)
    private List<String> roles;
    @TableField(exist = false)
    private List<String> permissions;

}
