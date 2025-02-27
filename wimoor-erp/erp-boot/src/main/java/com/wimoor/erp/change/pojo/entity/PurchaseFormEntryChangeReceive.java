package com.wimoor.erp.change.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2024-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_purchase_form_entry_change_receive")
@ApiModel(value="PurchaseFormEntryChangeReceive对象", description="")
public class PurchaseFormEntryChangeReceive implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    private String entrychangeid;

    private Integer amount;

    private String remark;

    private String operator;
    
    private Boolean disable;
    
    private Date opttime;


}
