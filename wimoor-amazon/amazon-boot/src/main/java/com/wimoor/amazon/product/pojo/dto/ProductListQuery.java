package com.wimoor.amazon.product.pojo.dto;

import java.util.List;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ProductListQueryDTO对象", description="获取商品列表")
public class ProductListQuery extends BasePageQuery{
	@ApiModelProperty(value = "查询内容", example = "TS001")
	String search ;
	@ApiModelProperty(value = "查询内容类型", example = "SKU,ASIN,Name,Remark")
	String searchtype ;
	@ApiModelProperty(value = "站点ID", example = "")
	String marketplace ;
	@ApiModelProperty(value = "是否隐藏", example = "true,false")
	String disable ;
	@ApiModelProperty(value = "转化率", example = "")
	String changeRate ;
	@ApiModelProperty(value = "颜色", example = "")
	String color;
	@ApiModelProperty(value = "是否FBA发后", example = "")
	String isfba ;
	@ApiModelProperty(value = "店铺ID", example = "")
	String groupid ;
	@ApiModelProperty(value = "产品负责人", example = "")
	String ownerid ;
	@ApiModelProperty(value = "是否变体", example = "")
	String isparent;
	@ApiModelProperty(value = "父ASIN", example = "")
	String parentasin;
	@ApiModelProperty(value = "名称", example = "")
	String name;
	@ApiModelProperty(value = "备注", example = "")
	String remark;
	@ApiModelProperty(value = "分类", example = "")
	String category;
	@ApiModelProperty(value = "存在差评", example = "")
	String isbadreview;
	@ApiModelProperty(value = "销售状态", example = "")
	String salestatus;
	@ApiModelProperty(value = "SKU", example = "")
	String sku;
	@ApiModelProperty(value = "动态查询条件", example = "")
	String paralist;
	@ApiModelProperty(value = "标签查询条件", example = "123,456,122")
	List<String> taglist;
}
