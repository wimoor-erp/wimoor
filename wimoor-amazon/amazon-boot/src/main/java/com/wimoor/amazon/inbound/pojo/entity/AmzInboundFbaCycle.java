package com.wimoor.amazon.inbound.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import java.util.Date;
import java.math.BigInteger;

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
 * @since 2022-11-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_amz_inbound_fba_cycle")
@ApiModel(value="AmzInboundFbaCycle对象", description="")
public class AmzInboundFbaCycle extends BaseEntity {

    private static final long serialVersionUID=1L;
 
    private BigInteger shopid;

    private BigInteger transtype;

    private String marketplaceid;

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
