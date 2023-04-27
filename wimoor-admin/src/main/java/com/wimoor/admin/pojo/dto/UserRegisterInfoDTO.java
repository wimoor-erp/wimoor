package com.wimoor.admin.pojo.dto;

import lombok.Data;

@Data
public class UserRegisterInfoDTO {
	String email ;
	String name ;
	String password ;
	String company ;
	String phone ;
	String smscode ;
	String salekey ;
	String invitecode ;
	String account;
	String ftype = "mobile";
}
