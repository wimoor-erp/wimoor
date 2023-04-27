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
 * 用户数据权限，放在用户信息中，登录后将在所有模块生效
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_user_datalimit")
@ApiModel(value="SysUserDatalimit对象", description="用户数据权限，放在用户信息中，登录后将在所有模块生效")
public class SysUserDatalimit implements Serializable {

    private static final long serialVersionUID=1L;

    private BigInteger userid;

    @ApiModelProperty(value = "数据授权类型")
    private String datatype;

    @ApiModelProperty(value = "true表示需要限制，false表示不需要限制")
    private Boolean islimit;


}
