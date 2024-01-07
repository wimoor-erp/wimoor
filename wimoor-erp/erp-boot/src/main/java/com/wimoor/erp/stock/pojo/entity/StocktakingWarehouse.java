package com.wimoor.erp.stock.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2023-07-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_stocktaking_warehouse")
@ApiModel(value="StocktakingWarehouse对象", description="")
public class StocktakingWarehouse implements Serializable {

    private static final long serialVersionUID=1L;

    private BigInteger stocktakingid;

    private BigInteger warehouseid;


}
