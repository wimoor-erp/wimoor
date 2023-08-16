package com.wimoor.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_dept")  
public class SysDept extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private String name;

    private String parentId;

    private String treePath;

    private Integer sort;

    private Integer status;

    private Integer deleted;

}
