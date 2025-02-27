package com.wimoor.amazon.inventory.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 用于存储欧洲各个国家的库存
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-08
 */
@Data
@TableName("t_amz_rpt_inventory_country")
@ApiModel(value="AmzInventoryCountryReport对象", description="用于存储欧洲各个国家的库存")
public class AmzInventoryCountryReport  implements Serializable {

    private static final long serialVersionUID=1L;

	@TableId(value = "id" )
	@ApiModelProperty(value = "ID")
    String id;
	
    @ApiModelProperty(value = "SKU")
    private String sku;

    @ApiModelProperty(value = "FBA仓库标示码")
    private String fnsku;

    @ApiModelProperty(value = "商品标示")
    private String asin;

    @ApiModelProperty(value = "产品新旧类型")
    private String fcondition;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "库存数量")
    private Integer quantity;

    @ApiModelProperty(value = "授权ID")
    private BigInteger authid;
    
    @ApiModelProperty(value = "刷新时间")
    private Date refreshtime;


}
