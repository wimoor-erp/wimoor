package com.wimoor.erp.stock.pojo.entity;

import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseForm;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_erp_dispatch_oversea_form")
@ApiModel(value="ErpDispatchOverseaForm对象", description="")
public class ErpDispatchOverseaForm extends  ErpBaseForm {

    private static final long serialVersionUID=1L;
    
    private BigInteger fromWarehouseid;

    private BigInteger toWarehouseid;

    private BigInteger groupid;

    private String remark;
    
    private String country;
    @TableField("arrivalTime")
    private Date arrivalTime;


}
