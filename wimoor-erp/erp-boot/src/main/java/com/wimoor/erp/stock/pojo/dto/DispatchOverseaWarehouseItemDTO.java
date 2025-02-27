package com.wimoor.erp.stock.pojo.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="DispatchWarehouseItemDTO对象", description="调库单保存/提交DTO")
public class DispatchOverseaWarehouseItemDTO extends DispatchWarehouseItemDTO{
	String groupid;
	String country;
}
