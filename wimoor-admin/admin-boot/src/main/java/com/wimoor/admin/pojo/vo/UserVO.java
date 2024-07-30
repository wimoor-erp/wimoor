package com.wimoor.admin.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.wimoor.common.mvc.Dict;

@Data
public class UserVO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigInteger id;

    private String name;

    private String account;

    private Date createDate;

    private Date losingEffect ;
    
    @Dict(dictCode = "user_status")
    private Integer status;

    private String image;
    private String deptname;
    private List<String> roles;
    private List<String> perms ;
    private List<String> groups ;
}
