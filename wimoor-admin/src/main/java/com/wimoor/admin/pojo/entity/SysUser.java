package com.wimoor.admin.pojo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
 
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;


@Data
@TableName("t_user")  
public class SysUser   implements Serializable{

	private static final long serialVersionUID = 6335284616701585713L;
 
	
	@TableField(exist = false)
    private Map<String,Object> userinfo;
 
    @TableId(value= "id")
    private String id;
    
    @TableField(value= "account")
    private String account;
    
    @TableField(value= "password")
    private String password;

    @TableField(value= "salt")
    private String salt;
    
    @TableField(value= "leader_id")
    private String leaderId;
    
    @TableField(value= "createDate")
    private Date createDate;
    
    @TableField(value= "losingEffect")
    private Date losingeffect;
    
    @TableField(value= "logicDelete")
    private Boolean logicDelete;
    
    @TableField(value= "disable")
    private Boolean disable;
    
    @TableField(value= "member")
    private int member;

	@TableField(value= "passwordkey")
    private String passwordkey;
	
	@TableField(value="lastlogintime")
	private Date lastlogintime;
	
	@TableField(value="lastloginip")
	private String lastloginip;
	
	@TableField(value="lastloginsession")
	private String lastloginsession;
	
	@TableField(value="ftype")
	private String ftype;
	
	@TableField(value= "isActive")
	private Boolean isActive;
	
	@TableField(value="hasEmail")
	private Boolean hasEmail;
 
	@TableField(value="deptid")
	private String deptid;
	
	@TableField(value="opttime")
	Date opttime;
	
	
    @TableField(exist = false)
    String shopid;
 
    @TableField(exist = false)
    Map<String,Object> contextmap = new HashMap<String,Object>();
    
 
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId == null ? null : leaderId.trim();
    }

	public String getCredentialsSalt() {
		 return account + salt;
	}
 
	public Boolean getHasEmail() {
		if(hasEmail==null)return false;
		return hasEmail;
	}
 

}