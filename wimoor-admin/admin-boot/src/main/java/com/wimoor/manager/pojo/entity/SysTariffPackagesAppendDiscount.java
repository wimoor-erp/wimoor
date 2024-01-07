package com.wimoor.manager.pojo.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_tariff_packages_append_discount")
@ApiModel(value="SysTariffPackagesAppendDiscount对象", description="")
public class SysTariffPackagesAppendDiscount implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer appendid;

    private Integer packages;

    private Integer month;

    private BigDecimal discount;


}
