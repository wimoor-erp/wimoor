package com.wimoor.amazon.transparency.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_amz_transparency_task
 */
@TableName(value ="t_amz_transparency_task")
@Data
public class TransparencyTask {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 
     */
    private String skuinfoid;

    /**
     * 
     */
    private String gtin;

    /**
     * 
     */
    private Integer count;

    /**
     * 
     */
    private String url;

    /**
     * 
     */
    private String status;

    private String jobid;

    private String location;

    /**
     * 
     */
    private Date createtime;

    /**
     * 
     */
    private String creator;
}