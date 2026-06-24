package com.wimoor.feishu.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 飞书授权信息
 * </p>
 *
 * @author wimoor team
 * @since 2023-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_feishu_auth")
@ApiModel(value="Auth对象", description="飞书授权信息")
public class Auth implements Serializable {

    private static final long serialVersionUID=1L;
    
    @TableId
    @ApiModelProperty(value = "应用ID")
    private String appId;

    @ApiModelProperty(value = "应用密钥")
    private String appSecret;

    @ApiModelProperty(value = "加密密钥")
    private String encryptKey;

    @ApiModelProperty(value = "验证令牌")
    private String verificationToken;

    @ApiModelProperty(value = "店铺ID")
    private String shopid;

    @ApiModelProperty(value = "操作时间")
    private Date opttime;

    @ApiModelProperty(value = "操作人")
    private String operator;


}
