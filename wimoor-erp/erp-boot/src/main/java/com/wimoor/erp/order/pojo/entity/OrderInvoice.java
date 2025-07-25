package com.wimoor.erp.order.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="OrderInvoice", description="订单发票")
@TableName("t_erp_order_invoice")
public class OrderInvoice extends ErpBaseEntity {
    @TableField(value = "country")
    private String country;

    @TableField(value = "shopid")
    private String shopid;

    @TableField(value = "logoUrl")
    private String logoUrl;

    @TableField(value = "image")
    private String image;

    @TableField(value = "company")
    private String company;

    @TableField(value = "company_simple")
    private String companySimple;

    @TableField(value = "province")
    private String province;

    @TableField(value = "billto")
    private String billto;

    @TableField(value = "city")
    private String city;

    @TableField(value = "address")
    private String address;

    @TableField(value = "bank")
    private String bank;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "account")
    private String account;

    @TableField(value = "abn")
    private String abn;

    @TableField(value = "rate")
    private String rate;

    @TableField(value = "creator")
    private String creator;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "createtime")
    private Date createtime;

}
