package com.wimoor.erp.warehouse.pojo.vo;

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
 * 仓库货柜
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_erp_warehouse_shelf")
@ApiModel(value="WarehouseShelfVo对象", description="仓库货柜")
public class WarehouseShelfVo  extends BaseEntity{

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "仓库ID")
    private BigInteger addressid;
    
    @ApiModelProperty(value = "仓库名称")
    private String warehousename;

    @ApiModelProperty(value = "货柜名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String number;

    @ApiModelProperty(value = "容量(cm³)")
    private Float capacity;

    @ApiModelProperty(value = "容量使用比例")
    private Float usesize;
    
    @ApiModelProperty(value = "长度(cm)")
    private Float length;

    @ApiModelProperty(value = "宽度(cm)")
    private Float width;

    @ApiModelProperty(value = "高度(cm)")
    private Float height;

    @ApiModelProperty(value = "父货柜ID")
    private BigInteger parentid;

    @ApiModelProperty(value = "排序即（柜子所在位置）")
    private Integer sort;
    
    @ApiModelProperty(value = "SKU异常数量")
    private Integer expnumber;

    @ApiModelProperty(value = "所有付货柜编码如：A01!033!F01")
    private String treepath;

    @ApiModelProperty(value = "公司ID")
    private BigInteger shopid;

    @ApiModelProperty(value = "是否报警")
    private Boolean iswarn;

    @ApiModelProperty(value = "是否逻辑删除")
    private Boolean isdelete;

    @ApiModelProperty(value = "是否冻结")
    private Boolean isfrozen;

    @ApiModelProperty(value = "操作人ID")
    private BigInteger operator;
    
    @ApiModelProperty(value = "操作人名称")
    private String operatorname;

    @ApiModelProperty(value = "创建人ID")
    private BigInteger creator;
    
    @ApiModelProperty(value = "创建人名称")
    private String creatorname;
    
    @ApiModelProperty(value = "操作时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime opttime;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creattime;

    @ApiModelProperty(value = "当前库位中的汇总情况")
    WarehouseShelfInventorySummaryVo summary;
}
