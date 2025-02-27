package com.wimoor.auth.client.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

public class EasyTypeToken extends UsernamePasswordToken{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2564928913725078138L;
	
	private LoginType type;//表示当前登录的类型
	
	 public EasyTypeToken() {
	        super();
	    }
	    public EasyTypeToken(String username, String password, LoginType type, boolean rememberMe,  String host) {
	        super(username, password, rememberMe,  host);
	        this.type = type;
	    }
	    /**免密登录*/
	    public EasyTypeToken(String username) {
	        super(username, "", false, null);
	        this.type = LoginType.NOPASSWD;
	    }
	    /**账号密码登录*/
	    public EasyTypeToken(String username, String password) {
	        super(username, password, false, null);
	        this.type = LoginType.PASSWORD;
	    }
		public LoginType getType() {
			return type;
		}
		public void setType(LoginType type) {
			this.type = type;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
	     
	 
}
