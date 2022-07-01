package com.wimoor.admin.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Data
public class UserVO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger id;

    private String name;

    private String avatar;

    private List<String> roles;

    private List<String> perms ;

}
