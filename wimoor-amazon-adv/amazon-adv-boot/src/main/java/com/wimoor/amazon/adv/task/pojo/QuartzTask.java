package com.wimoor.amazon.adv.task.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;

 
@Entity
@Table(name="t_sys_quartz_task")
public class QuartzTask {
   @Id
   @ApiModelProperty(value = "任务ID")
   @Column(name="id")
   private String id;
   
   @Column(name="name")
   @ApiModelProperty(value = "任务名称")
   private String name;
   
   @Column(name="fgroup")
   @ApiModelProperty(value = "任务分组")
   private String fgroup;
   
   @Column(name="cron")
   @ApiModelProperty(value = "执行时间")
   private String cron;
   
   @Column(name="parameter")
   @ApiModelProperty(value = "参数设置")
   private String parameter;
   
   @Column(name="description")
   @ApiModelProperty(value = "描述信息")
   private String description;
   
   @Column(name="path")
   @ApiModelProperty(value = "执行路径")
   private String path;
   
   @Column(name="server")
   @ApiModelProperty(value = "服务")
   private String server;
   
   @Column(name="bean")
   @ApiModelProperty(value = "对应类")
   private String bean;
   
   @Column(name="method")
   @ApiModelProperty(value = "对应方法")
   private String method;
   
   @Column(name="createdate")
   @ApiModelProperty(value = "创建时间")
   private Date createdate;
   
   @Column(name="isdelete")
   @ApiModelProperty(value = "是否删除")
   private Boolean isdelete;
   
public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getFgroup() {
	return fgroup;
}

public void setFgroup(String fgroup) {
	this.fgroup = fgroup;
}

public String getCron() {
	return cron;
}

public void setCron(String cron) {
	this.cron = cron;
}

public String getParameter() {
	return parameter;
}

public void setParameter(String parameter) {
	this.parameter = parameter;
}

public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

public String getPath() {
	return path;
}

public void setPath(String path) {
	this.path = path;
}

public String getServer() {
	return server;
}

public void setServer(String server) {
	this.server = server;
}

public String getBean() {
	return bean;
}

public void setBean(String bean) {
	this.bean = bean;
}

public String getMethod() {
	return method;
}

public void setMethod(String method) {
	this.method = method;
}

public Date getCreatedate() {
	return createdate;
}

public void setCreatedate(Date createdate) {
	this.createdate = createdate;
}

public Boolean getIsdelete() {
	return isdelete;
}

public void setIsdelete(Boolean isdelete) {
	this.isdelete = isdelete;
}
   
   
}
