package com.wimoor.amazon.product.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2022-07-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_product_rank")
@ApiModel(value="ProductRank对象", description="")
public class ProductRank extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    private Date  byday;

    @TableField("categoryId")
    private String categoryid;

    @TableField("`rank`")
    private Integer rank;

    private String productId;
    private String title;
    private String link;

    @TableField("isMain")
    private Boolean ismain;

    @TableField("isNewest")
    private Boolean isnewest;

}
