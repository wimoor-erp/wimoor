package com.wimoor.erp.purchase.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_preprocessing_form")
public class PreprocessForm extends BaseEntity {

        @TableField(value="shopid")
        private String shopid;

        @TableField(value="warehouseid")
        private String warehouseid;

        @TableField(value="number")
        private String number;

        @TableField(value="ftype")
        private Integer ftype;

        @TableField(value="is_check_inv_time")
        private Date isCheckInvTime;

        @TableField(value="is_out_consumable_time")
        private Date isOutConsumableTime;

        @TableField(value="is_dispatch_time")
        private Date isDispatchTime;

        @TableField(value="is_down_time")
        private Date isDownTime;

        @TableField(value="is_assembly_time")
        private Date isAssemblyTime;

        @TableField(value="createtime")
        private Date createtime;

        @TableField(value="creator")
        private String creator;

        @TableField(value="isrun")
        private Boolean isrun;

        @TableField(value="opttime")
        private Date opttime;

        @TableField(value="operator")
        private String operator;

        @TableField(value="remark")
        private String remark;

}
