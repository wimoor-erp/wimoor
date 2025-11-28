package com.wimoor.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_user_wechat_mp")
public class SysUserWechatMP {
	
	@TableField(value= "openid")
    private String openid;

	@TableField(value= "userid")
    private String userid;

	@TableField(value= "access_token")
    private String accessToken;

	@TableField(value= "refresh_token")
    private String refreshToken;
	
	@TableField(value= "ftype")
    private String ftype;

    
}