package com.wimoor.erp.purchase.alibaba.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_erp_purchase_form_entry_logistics")
public class PurchaseFormEntryLogistics {
	 
    @TableId(value= "entryid")
    private String entryid;
    
    @TableField(value= "logisticsId")
    private String logisticsid;
    
    @TableField(value= "refreshtime")
    private Date refreshtime;
 
}