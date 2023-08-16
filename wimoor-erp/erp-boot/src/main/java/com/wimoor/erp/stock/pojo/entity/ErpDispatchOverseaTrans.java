package com.wimoor.erp.stock.pojo.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_dispatch_oversea_trans")
@ApiModel(value="ErpDispatchOverseaTrans对象", description="")
public class ErpDispatchOverseaTrans extends BaseEntity{

    private static final long serialVersionUID=1L;

    private BigInteger formid;

    private BigInteger company;

    private BigInteger channel;

    private BigDecimal singleprice;

    private BigDecimal transweight;

    private String wunit;

    private BigDecimal otherfee;

    private String ordernum;

    private Date opttime;

    private BigInteger operator;

    private String remark;

    @TableField("arrivalTime")
    private Date arrivalTime;

    private Date outarrtime;

    private Date inarrtime;

    private Integer wtype;

    private BigInteger transtype;


}
