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
 * 预操作，此表内不存储任何记录。当预操作结束后自动删除
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_erp_warehouse_shelf_inventory_opt_pro")
@ApiModel(value="WarehouseShelfInventoryOptProVo对象", description="预操作，此表内不存储任何记录。当预操作结束后自动删除")
public class WarehouseShelfInventoryOptPro extends BaseEntity{

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "货架ID")
    private BigInteger shelfid;

    @ApiModelProperty(value = "产品ID")
    private BigInteger materialid;

    @ApiModelProperty(value = "公司ID")
    private BigInteger shopid;

    @ApiModelProperty(value = "操作数量")
    private Integer quantity;

    @ApiModelProperty(value = "操作数量对于的体积(系统生成)")
    private Float size;

    @ApiModelProperty(value = "操作前结余重量(系统生成)")
    private Integer balanceQty;

    @ApiModelProperty(value = "操作前结余体积(系统生成)")
    private Float balanceSize;

    @ApiModelProperty(value = "0：出库；1：入库;2：修正下架；3：修正上架(系统处理)")
    private Integer opt;

    @ApiModelProperty(value = "操作人(系统生成)")
    private BigInteger operator;

    @ApiModelProperty(value = "操作时间(系统生成)")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime opttime;


}
