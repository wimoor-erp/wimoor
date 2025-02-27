package com.wimoor.amazon.product.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductSalesPlanDTO extends BasePageQuery {
  String groupid;
  String warehouseid;
}
