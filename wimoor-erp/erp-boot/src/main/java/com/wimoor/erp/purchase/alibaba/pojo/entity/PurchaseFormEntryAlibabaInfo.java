package com.wimoor.erp.purchase.alibaba.pojo.entity;

import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_purchase_form_entry_alibabainfo")
public class PurchaseFormEntryAlibabaInfo {
 
    @TableId(value= "entryid")
    private String entryid;

    @TableField(value= "alibaba_auth")
    private BigInteger alibabaAuth;

    @TableField(value= "alibaba_orderid")
    private BigInteger alibabaOrderid;

    @TableField(value= "logistics_info")
    private String logisticsInfo;

    @TableField(value= "order_info")
    private String orderInfo;

    @TableField(value= "logistics_trace_info")
    private String logisticsTraceInfo;
    
    
}