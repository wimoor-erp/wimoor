package com.wimoor.erp.common.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_erp_download_report")
public class DownloadReport {
	@TableId(value= "id")
	private String id;

	@TableField(value= "userid")
    private String userid;

	@TableField(value= "shopid")
    private String shopid;

	@TableField(value= "ftype")
    private String ftype;

	@TableField(value= "log")
	private String log;

	@TableField(value= "isrun")
	private Boolean isrun;

	@TableField(value= "content")
    private byte[] content;

	@TableField(value="createtime")
	private Date createtime;
	 
}