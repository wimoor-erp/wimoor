package com.wimoor.feishu.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_sys_feishu_auth")
@ApiModel(value="Auth对象", description="")
public class Auth implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId
    private String appId;

    private String appSecret;

    private String encryptKey;

    private String verificationToken;


}
