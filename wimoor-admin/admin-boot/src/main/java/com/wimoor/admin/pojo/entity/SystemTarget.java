package com.wimoor.admin.pojo.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
 
@Data
@Accessors(chain = true)
@TableName("t_sys_target")
public class SystemTarget {
 
	@TableId(value = "id" )
    private String id;
	
    @TableField(value= "name")
    private String name;

    @TableField(value= "description")
    private String description;
	
}
