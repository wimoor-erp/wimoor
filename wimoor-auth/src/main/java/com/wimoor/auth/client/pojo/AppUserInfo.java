package com.wimoor.auth.client.pojo;

import lombok.Data;

@Data
public class AppUserInfo {
   String appType;
   String openId;
   String employeeId;
   String userId;
   String sessionKey;
   String tenantKey;
   String accessToken;
   Integer expiresIn;
   String refreshToken;
   String unionid;
}
