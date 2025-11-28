package com.wimoor.erp.stock.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 
 * @TableName t_erp_outwh_form_consumable
 */
@Data
@TableName(value ="t_erp_outwh_form_consumable")
public class OutwhFormConsumable extends BaseEntity {
    /**
     * 
     */
    /**
     * 
     */
    private String submid;

    private String formid;

    /**
     * 
     */
    private String mainmid;

    /**
     * 
     */
    private BigDecimal units;

    /**
     * 
     */
    private Integer shipqty;

    /**
     * 
     */
    private Integer subout;

    /**
     * 
     */

}