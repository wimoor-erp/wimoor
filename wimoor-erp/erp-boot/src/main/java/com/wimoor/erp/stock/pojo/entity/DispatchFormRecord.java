package com.wimoor.erp.stock.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_dispatch_form_record")
public class DispatchFormRecord extends BaseEntity {
	public DispatchFormRecord(){
		super();
	}
	public DispatchFormRecord(DispatchForm form){
	     this.formid=form.getId();
		 this.auditstatus=form.getAuditstatus();
		 this.opttime=new Date();
		 this.operator=form.getOperator();
	}
	@TableField(value= "formid")
	protected String formid;

	@TableField(value= "auditstatus")
	protected Integer auditstatus;

	@TableField(value= "opttime")
	private Date opttime;

	@TableField(value= "operator")
	private String operator;
  

}