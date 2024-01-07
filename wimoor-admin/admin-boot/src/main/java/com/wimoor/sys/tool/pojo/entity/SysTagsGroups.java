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
@TableName("t_sys_tags_groups")
@ApiModel(value="SysTagsGroups对象", description="")
public class SysTagsGroups   extends BaseEntity{

    private static final long serialVersionUID=1L;

    private String name;

    private String shopId;

    private String description;

    private BigInteger creator;

    private String opterator;

    private Integer status;
    
    @ApiModelProperty(value = "备注")
    private  String  remark;
    
    private Integer sort;
    
    


}
