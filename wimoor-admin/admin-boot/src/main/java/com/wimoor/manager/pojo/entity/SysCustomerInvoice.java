package com.wimoor.manager.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

/**
 * <p>
 * 客户发票表
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_customer_invoice")
@ApiModel(value="SysCustomerInvoice对象", description="客户发票表")
public class SysCustomerInvoice extends BaseEntity implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "公司ID")
    private BigInteger shopid;

    @ApiModelProperty(value = "订单ID")
    private BigInteger orderid;

    @ApiModelProperty(value = "公司名称")
    private String company;

    @ApiModelProperty(value = "发票号")
    private String invoice;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "银行")
    private String bank;

    @ApiModelProperty(value = "卡号")
    private String cardNo;

    @ApiModelProperty(value = "发票类型")
    private String ftype;

    @ApiModelProperty(value = "发票存储地址")
    @TableField(exist = false)
    private String location;

    private String invoiceFile;

    @ApiModelProperty(value = "邮寄地址")
    private String sendAddress;

    @ApiModelProperty(value = "邮寄电话")
    private String sendPhone;

    @ApiModelProperty(value = "邮寄姓名")
    private String sendName;

    @ApiModelProperty(value = "是否已发送")
    private Boolean isSend;

    @ApiModelProperty(value = "发票类型")
    private String ivctype;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime opttime;
}