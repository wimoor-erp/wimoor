package com.wimoor.admin.pojo.dto;

import lombok.Data;

@Data
public class UserRegisterInfoDTO {
	String email ;
	String name ;
	String password ;
	String oldpassword ;
	String company ;
	String phone ;
	String code ;
	String key ;
	String salekey ;
	String invitecode ;
	String account;
	String ftype = "mobile";
}
