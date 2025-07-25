package com.wimoor.amazon.product.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_product_in_review
 */
@TableName(value ="t_product_in_review")
@Data
public class ProductInReview {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 
     */
    private String asin;

    /**
     * 
     */
    private String marketplaceid;

    /**
     * 
     */
    private Double starofrate;

    /**
     * 
     */
    private Integer reviewnum;

    /**
     * 
     */
    private Double starofrate1;

    /**
     * 
     */
    private Double starofrate2;

    /**
     * 
     */
    private Double starofrate3;

    /**
     * 
     */
    private Double starofrate4;

    /**
     * 
     */
    private Double starofrate5;

    /**
     * 
     */
    private Boolean negative;

    /**
     * 
     */
    private String positivereview;

    /**
     * 
     */
    private String criticalreview;

    /**
     * 
     */
    private Date refreshtime;

    /**
     * 
     */
    private Integer refreshnum;
}