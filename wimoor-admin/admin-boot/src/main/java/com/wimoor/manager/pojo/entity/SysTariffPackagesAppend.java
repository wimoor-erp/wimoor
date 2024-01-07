package com.wimoor.manager.pojo.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("t_sys_tariff_packages_append")
@ApiModel(value="SysTariffPackagesAppend对象", description="")
public class SysTariffPackagesAppend implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String ftype;

    private String name;

    private Integer units;

    private BigDecimal price;

    private BigDecimal discount;


}
