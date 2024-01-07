package com.wimoor.erp.warehouse.pojo.vo;

 
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WarehouseShelfInventorySummaryVo对象", description="库位库存汇总")
public class WarehouseShelfInventorySummaryVo {
    @ApiModelProperty(value = "SKU个数")
    private Long skunum;
    
    @ApiModelProperty(value = "仓库名称")
    private String warehousename;
    
    @ApiModelProperty(value = "SKU异常数量")
    private Integer expnumber;
    
    @ApiModelProperty(value = "容量")
    private Float size;

    @ApiModelProperty(value = "库存数量")
    private Long quantity;
    
    @ApiModelProperty(value = "操作人")
    private String operator;
    
    @ApiModelProperty(value = "操作时间")
    private Date opttime;
    
    @ApiModelProperty(value = "创建人")
    private String creator;
    
    @ApiModelProperty(value = "创建时间")
    private Date creattime;
}
