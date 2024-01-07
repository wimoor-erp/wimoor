package com.wimoor.erp.purchase.alibaba.pojo.entity;

import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_purchase_form_entry_alibabainfo_ext")
public class PurchaseFormEntryAlibabaInfoExt {
 
    @TableId(value= "entryid")
    private String entryid;

    @TableField(value= "alibaba_auth")
    private BigInteger alibabaAuth;

    @TableField(value= "alibaba_orderid")
    private BigInteger alibabaOrderid;

    @TableField(value= "order_status")
    private String orderStatus;

    @TableField(value= "logistics_status")
    private Boolean logisticsStatus;
    
    @TableField(value= "logistics_trace_status")
    private Boolean logisticsTraceStatus;
    
    @TableField(value= "order_refresh_time")
    private Date orderRefreshTime;
    
    @TableField(value= "logistics_refresh_time")
    private Date logisticsRefreshTime;

    @TableField(value= "logistics_trace_refresh_time")
    private Date logisticsTraceRefreshTime;
    
    @TableField(value= "signinfo")
    private String signinfo;

    @TableField(value= "paydate")
    private Date paydate;
    
    @TableField(value= "countdate")
    private Date countdate;
}