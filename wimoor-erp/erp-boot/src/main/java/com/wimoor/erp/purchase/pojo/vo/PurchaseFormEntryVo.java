package com.wimoor.erp.purchase.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;
import com.wimoor.erp.purchase.pojo.entity.PurchaseFormPayment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_purchase_form_entry")
public class PurchaseFormEntryVo extends ErpBaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6151297059089122963L;

	@TableField(value= "formid")
    private String formid;

    @TableField(value= "materialid")
    private String materialid;

    @TableField(value= "amount")
    private Integer amount;

    @NotNull(message="单价不能为空")
    @TableField(value= "itemprice")
    private BigDecimal itemprice;

    @TableField(value= "image")
    private String image;

    @TableField(value= "name")
    private String name;

    @TableField(value= "sku")
    private String sku;
	
    
}