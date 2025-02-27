package com.wimoor.erp.change.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@TableName("t_erp_purchase_form_entry_change")
@ApiModel(value="PurchaseFormEntryChange对象", description="")
public class PurchaseFormEntryChange implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;
    
    private String shopid;
    
    private String number;

    private String entryid;

    private String supplierid;

    private String logistics;

    private Integer amount;

    @ApiModelProperty(value = "1:进行中，0：已完成")
    private Integer auditstatus;

    @TableField(value= "without_inv")
    private Boolean withoutInv;

    private Integer totalin;

    private String materialid;

    private String mainid;

    @TableField(value= "ass_form_id")
    private String assemblyFormId;

    @TableField(value= "dis_form_id")
    private String disassembleFormId;

    private String warehouseid;

    private String remark;

    private String operator;

    private String creator;

    private Date opttime;

    private Date createtime;


}
