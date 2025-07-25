package com.wimoor.erp.warehouse.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.util.List;
import java.io.Serializable;
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
 * @since 2022-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_warehouse_address")
@ApiModel(value="ErpWarehouseAddress对象", description="")
public class ErpWarehouseAddress implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private BigInteger id;

    private BigInteger shopid;

    @ApiModelProperty(value = "地址名称")
    private String name;
    
    @ApiModelProperty(value = "地址编码")
    private String number;
    
    @ApiModelProperty(value = "地址街道详情")
    private String detail;

    @ApiModelProperty(value = "邮编")
    private String postcode;

    @ApiModelProperty(value = "业主电话")
    private String phone;

    @ApiModelProperty(value = "业主（房东）")
    private String landlord;

    @ApiModelProperty(value = "到期时间")
    private LocalDateTime lostEffectDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否失效（是否删除）")
    private Boolean disabled;

    @ApiModelProperty(value = "修改人")
    private BigInteger operator;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime opttime;

    @ApiModelProperty(value = "创建人")
    private BigInteger creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime creattime;

    @TableField(exist=false)
    @ApiModelProperty(value = "绑定此地址得仓库")
    List<Warehouse> boundWarehouseList;
}
