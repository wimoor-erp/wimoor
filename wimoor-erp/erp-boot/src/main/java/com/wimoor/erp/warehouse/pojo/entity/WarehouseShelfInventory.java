package com.wimoor.erp.warehouse.pojo.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 货架产品库存
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_erp_warehouse_shelf_inventory")
@ApiModel(value="WarehouseShelfInventoryVo对象", description="货架产品库存")
public class WarehouseShelfInventory extends BaseEntity{

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "货柜ID")
    private BigInteger shelfid;

    @ApiModelProperty(value = "产品ID")
    private BigInteger materialid;

    @ApiModelProperty(value = "公司ID")
    private BigInteger shopid;

    @ApiModelProperty(value = "仓库ID")
    private BigInteger warehouseid;
    
    @ApiModelProperty(value = "当前数量")
    private Integer quantity;

    @ApiModelProperty(value = "当前体积")
    private Float size;

    @ApiModelProperty(value = "操作人")
    private BigInteger operator;

    @ApiModelProperty(value = "操作时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime opttime;


}
