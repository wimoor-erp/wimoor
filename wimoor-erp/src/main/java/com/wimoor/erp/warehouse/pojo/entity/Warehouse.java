package com.wimoor.erp.warehouse.pojo.entity;



import java.math.BigInteger;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Warehouse对象", description="仓库")
@TableName("t_erp_warehouse")
public class Warehouse  extends ErpBaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -296533637594471333L;

	@ApiModelProperty(value = "仓库名称")
	@NotNull(message="仓库名称不能为空")
	@Size(max=36,message="仓库名称不能超过36个字符")
	@TableField(value= "name")
    private String name;

	@ApiModelProperty(value = "仓库类型([t_erp_warehouse_type]:self,self_test,self_usable,self_unusable)")
    @TableField(value= "ftype")
    private String ftype;

	@ApiModelProperty(value = "仓库层级")
    private String flevel;

	@ApiModelProperty(value = "仓库编码")
    private String number;

	@ApiModelProperty(value = "地址")
    @NotNull(message="地址不能为空")
	@Size(max=500,message="地址不能超过500个字符")
	@TableField(value= "address")
    private String address;

	@ApiModelProperty(value = "备注")
    @Size(max=500,message="备注不能超过500个字符")
	@TableField(value= "remark")
    private String remark;

    @ApiModelProperty(value = "公司id")
    private String shopid;
    
    @ApiModelProperty(value = "海外仓id")
    private BigInteger fbawareid;

    @ApiModelProperty(value = "店铺id")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private BigInteger groupid;
    
    @ApiModelProperty(value = "国家")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String country;
    
    @ApiModelProperty(value = "父亲id")
    private String parentid;
    
    @ApiModelProperty(value = "是否默认")
    @TableField(value= "isdefault")
    private Boolean isdefault;
 
    @ApiModelProperty(value = "备货周期")
    @NotNull(message="安全库存周期不能为空")
    @TableField(value= "stocking_cycle")
    private Integer stockingCycle;
    
    @ApiModelProperty(value = "是否禁用")
    @TableField(value="disabled")
    private Boolean disabled;
    
    @ApiModelProperty(value = "是否在盘点[当为盘点时，不能操作库存]")
    @TableField(value="isstocktaking")
    private Boolean isstocktaking;
    
    @ApiModelProperty(value = "最小补货周期")
    @NotNull(message="最小补货周期不能为空")
    @TableField(value="min_cycle")
	private Integer mincycle;
    
    @ApiModelProperty(value = "仓库排序")
    @TableField(value="findex")
    private Integer findex;
    
    @ApiModelProperty(value = "当为true时，库存可以为负数")
    @TableField(value="ishungry")
    private Boolean ishungry;
    
    @ApiModelProperty(value = "地址id")
    @TableField(value="addressid")
    private String addressid;
    
    @ApiModelProperty(value = "子仓位列表")
    @TableField(exist = false)
    private List<Warehouse> children;
 
    @ApiModelProperty(value = "ID")
    @TableField(exist = false)
    private String value;
    
    @ApiModelProperty(value = "名称")
    @TableField(exist = false)
    private String label;
    
	public Boolean getIsstocktaking() {
		if(isstocktaking==null)return false;
		return isstocktaking;
	}
 

	public Boolean getIsdefault() {
		if(isdefault==null)return false;
		return isdefault;
	}
	
	
 
	
	
 
}