package com.wimoor.amazon.transparency.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_amz_transparency_tcode
 */
@TableName(value ="t_amz_transparency_tcode")
@Data
public class TransparencyTcode {

    /**
     *
     */
    @TableId
    private String tcode;

    private String taskid;

    private String sku;

    private String gtin;

    @TableField(value = "type_of_codes")
    private String typeOfCodes;

    private String operator;
    private String jobid;
    private Date usetime;
    /**
     * 
     */
    private Date createtime;
}