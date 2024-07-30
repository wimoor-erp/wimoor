package com.wimoor.amazon.product.pojo.dto;


import java.util.List;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ProductInAftersaleDTO对象", description="发货计划詳情")
public class ProductInAftersaleDTO extends BasePageQuery{
String groupid;
String ftype;
String shopid;
String search;
List<String> fieldlist;
}
