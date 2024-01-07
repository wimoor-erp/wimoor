package com.wimoor.common.user;


 
public class UserInfoContext {

    public static final String HEADER_USER_INFO = "X-USERINFO";

    private static ThreadLocal<UserInfo> threadLocal = new ThreadLocal<>();

    public static UserInfo get() {
        return threadLocal.get();
    }

    public static void set(UserInfo userInfo) {
        threadLocal.set(userInfo);
    }

}