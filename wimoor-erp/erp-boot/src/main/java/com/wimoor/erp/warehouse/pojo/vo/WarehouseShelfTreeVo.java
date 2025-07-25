package com.wimoor.erp.warehouse.pojo.vo;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import com.wimoor.erp.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 仓库货柜
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WarehouseShelfVo对象", description="仓库货柜树形")
public class WarehouseShelfTreeVo  extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8526330045933801869L;

	@ApiModelProperty(value = "仓位ID")
    private BigInteger addressid;

    @ApiModelProperty(value = "货柜名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String number;

    @ApiModelProperty(value = "容量")
    private Float capacity;

    @ApiModelProperty(value = "长度")
    private Float length;

    @ApiModelProperty(value = "宽度")
    private Float width;

    @ApiModelProperty(value = "高度")
    private Float height;

    @ApiModelProperty(value = "父货柜ID")
    private BigInteger parentid;

    @ApiModelProperty(value = "排序即（柜子所在位置）")
    private Integer sort;

    @ApiModelProperty(value = "所有付货柜编码如：A01!033!F01")
    private String treepath;

    @ApiModelProperty(value = "名称")
    private String numbername;
    
    @ApiModelProperty(value = "公司ID")
    private BigInteger shopid;

    @ApiModelProperty(value = "是否报警")
    private Boolean iswarn;

    @ApiModelProperty(value = "是否逻辑删除")
    private Boolean isdelete;

    @ApiModelProperty(value = "是否冻结")
    private Boolean isfrozen;

    @ApiModelProperty(value = "操作人")
    private BigInteger operator;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime opttime;

    @ApiModelProperty(value = "创建人")
    private BigInteger creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime creattime;

    @ApiModelProperty(value = "是否可以点击")
    private Boolean isclick;

    @ApiModelProperty(value = "孩子")
    private List<WarehouseShelfTreeVo> children;
}
