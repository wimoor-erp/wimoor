package com.wimoor.amazon.inboundV2.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName t_erp_ship_custom_data
 */
@Data
@TableName(value ="t_erp_ship_custom_data")
public class CustomData implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private String id;

    /**
     * 
     */
    private String ftype;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private String ename;

    /**
     * 
     */
    private String encode;

    /**
     * 
     */
    private String code;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}