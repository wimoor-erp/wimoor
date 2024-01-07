package com.wimoor.erp.stock.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import java.math.BigInteger;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-07-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_stocktaking_item_shelf")
@ApiModel(value="ErpStocktakingItemShelf对象", description="")
public class StockTakingItemShelf extends BaseEntity{

    private static final long serialVersionUID=1L;

    private BigInteger stocktakingid;

    private BigInteger materialid;

    private BigInteger shelfid;
    
    private BigInteger warehouseid;

    private Integer amount;

    private Integer overamount;

    private Integer lossamount;


}
