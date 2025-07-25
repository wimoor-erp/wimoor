package com.wimoor.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

 
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@TableName("t_sys_menu")
public class SysMenu  implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -6460782889198446595L;
	@TableId(type=IdType.AUTO)
	private String id;
	
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

    @TableField(exist = false)
    private List<String> roles;
    @TableField(exist = false)
    private List<String> permissions;
    

    @TableField(fill = FieldFill.INSERT)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;
    

}
