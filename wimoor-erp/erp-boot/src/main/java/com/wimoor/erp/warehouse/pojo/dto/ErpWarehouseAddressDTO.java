package com.wimoor.erp.warehouse.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ErpWarehouseAddressDTO对象", description="查询仓库地址")
public class ErpWarehouseAddressDTO  extends BasePageQuery{
    String name ;
    String status;
}
