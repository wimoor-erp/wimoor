package com.wimoor.amazon.follow.pojo.entity;

import java.time.LocalTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_v2_product_info_follow_time")
@ApiModel(value="ProductInfoFollowTime对象", description="")
public class ProductInfoFollowTime extends BaseEntity{

    private static final long serialVersionUID=1L;

    private String shopid;
    
    private String name;

    private LocalTime starttime;

    private LocalTime endtime;

    private String creator;

    private String operator;

    private Date createtime;

    private Date opttime;


}
