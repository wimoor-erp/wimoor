package com.wimoor.erp.order.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_oversea_cycle")
@ApiModel(value="AmzInboundFbaCycle对象", description="")
public class ErpOverseaCycle extends BaseEntity {

    private static final long serialVersionUID=1L;
 
    private BigInteger shopid;

    private BigInteger transtype;

    private String country;

    @ApiModelProperty(value = "名称")
    private String name;

    private String number;

    @ApiModelProperty(value = "安全库存周期")
    private Integer stockingCycle;

    @ApiModelProperty(value = "发货频率")
    private Integer minCycle;

    @ApiModelProperty(value = "上架周期")
    private Integer putOnDays;

    @ApiModelProperty(value = "头程周期")
    private Integer firstLegDays;

    private Boolean isdefault;

    private String remark;

    @ApiModelProperty(value = "操作人")
    private BigInteger operator;

    @ApiModelProperty(value = "操作人名称")
    @TableField(exist=false)
    private String operatorname;
    
    @ApiModelProperty(value = "修改时间")
    private Date opttime;


}
