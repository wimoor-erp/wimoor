package com.wimoor.sys.tool.pojo.entity;

import java.math.BigInteger;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.admin.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_tags")
@ApiModel(value="SysTags对象", description="")
public class SysTags  extends BaseEntity{

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "标签名称")
    private String name;
    
    @ApiModelProperty(value = "标签的值")
    private String value;
    
    @ApiModelProperty(value = "分组ID")
    private String taggroupid;

    @ApiModelProperty(value = "公司ID")
    private BigInteger shopid;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "创建人")
    private BigInteger creator;

    @ApiModelProperty(value = "修改人")
    private String operator;
    
    @ApiModelProperty(value = "备注")
    private String remark;
    
    private Integer status;
    
    private Integer sort;
    
    private String color;
  
    


}
