package com.wimoor.common.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_sys_quartz_task")  
public class QuartzTask {
	
   @TableId(value = "id" )
   @ApiModelProperty(value = "任务ID")
   private String id;
   
   @ApiModelProperty(value = "任务名称")
   @TableField(value= "name")
   private String name;
   
   @ApiModelProperty(value = "任务分组")
   @TableField(value= "fgroup")
   private String fgroup;
   
   @ApiModelProperty(value = "执行时间")
   @TableField(value= "cron")
   private String cron;
   
   @ApiModelProperty(value = "参数设置")
   @TableField(value= "parameter")
   private String parameter;
   
   @ApiModelProperty(value = "描述信息")
   @TableField(value= "description")
   private String description;
   
   @ApiModelProperty(value = "执行路径")
   @TableField(value= "path")
   private String path;
   
   @ApiModelProperty(value = "服务")
   @TableField(value= "server")
   private String server;
   
   @ApiModelProperty(value = "优先级")
   @TableField(value= "priority")
   private Integer priority;
   
   @ApiModelProperty(value = "对应类")
   @TableField(value= "bean")
   private String bean;
   
   @ApiModelProperty(value = "对应方法")
   @TableField(value= "method")
   private String method;
   
   @ApiModelProperty(value = "创建时间")
   @TableField(value= "createdate")
   private Date createdate;
}
