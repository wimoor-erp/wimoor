package com.wimoor.common;

public interface SecurityConstants {

    /**
     * 认证请求头key
     */
    String AUTHORIZATION_KEY = "Authorization";

    /**
     * JWT令牌前缀
     */
    String JWT_PREFIX = "Bearer ";


    /**
     * Basic认证前缀
     */
    String BASIC_PREFIX = "Basic ";

    /**
     * JWT载体key
     */
    String JWT_PAYLOAD_KEY = "payload";

    /**
     * JWT ID 唯一标识
     */
    String JWT_JTI = "jti";

    /**
     * JWT ID 唯一标识
     */
    String JWT_EXP = "exp";

    /**
     * 黑名单token前缀
     */
    String TOKEN_BLACKLIST_PREFIX = "auth:token:blacklist:";

    String JWT_TOKEN_HEADER = "token";
    
    String JWT_TOKEN_PREFIX="tokenjwt:";
    
    String TOKEN_WHITELIST_PREFIX="auth:token:whitelist:";
    
    String USER_ID_KEY = "userId";

    String USER_NAME_KEY = "username";

    String CLIENT_ID_KEY = "client_id";

    /**
     * JWT存储权限前缀
     */
    String AUTHORITY_PREFIX = "ROLE_";

    /**
     * JWT存储权限属性
     */
    String JWT_AUTHORITIES_KEY = "authorities";

    String GRANT_TYPE_KEY = "grant_type";

    String REFRESH_TOKEN_KEY = "refresh_token";

    String APP_API_PATTERN = "/*/app-api/**";

    /**
     * 认证方式
     */
    String AUTHENTICATION_METHOD = "authenticationMethod";

    /**
     * 验证码key前缀
     */
    String VALIDATE_CODE_PREFIX = "VALIDATE_CODE:";

    /**
     * 短信验证码key前缀
     */
    String SMS_CODE_PREFIX = "SMS_CODE:";

    /**
     * 接口文档 Knife4j 测试客户端ID
     */
    String TEST_CLIENT_ID = "client";
    public static final String HEADER_USER_INFO = "X-USERINFO";

}
