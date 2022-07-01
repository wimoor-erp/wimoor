package com.wimoor.amazon.orders.pojo.entity;

import java.io.Serializable;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_order_invoice_vat")
@ApiModel(value="AmzOrdersInvoiceVat对象", description="订单发票信息vat表")
public class AmzOrdersInvoiceVat extends BaseEntity implements Serializable{
	
	

	private static final long serialVersionUID = 5720011796399905660L;

	@TableField(value = "groupid")
    private String groupid;

	@TableField(value = "country")
    private String country;

	@TableField(value = "vat_num")
    private String vatNum;

	@TableField(value = "vat_rate")
    private Float vatRate;
	
}
