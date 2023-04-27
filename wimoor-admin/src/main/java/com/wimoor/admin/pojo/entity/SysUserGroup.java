package com.wimoor.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigInteger;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户客户对店铺的权限绑定
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_user_group")
@ApiModel(value="SysUserGroup对象", description="用户客户对店铺的权限绑定")
public class SysUserGroup implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "用户ID")
    private BigInteger userid;

    @ApiModelProperty(value = "店铺ID")
    private BigInteger groupid;


}
