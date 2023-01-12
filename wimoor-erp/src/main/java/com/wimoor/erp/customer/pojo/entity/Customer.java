package com.wimoor.erp.customer.pojo.entity;
 
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_customer")
public class Customer extends ErpBaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6904556776271887804L;

	@NotNull(message="名称不能为空")
	@Size(max=50,message="名称的长度不能超过50个字符")
    @TableField(value= "name")
    private String name;

	@NotNull(message="编码不能为空")
	@Size(max=40,message="编码的长度不能超过40个字符")
    @TableField(value= "number")
    private String number;

    @TableField(value= "fullname")
    private String fullname;

    @TableField(value= "ftype")
    private String ftype;

    @NotNull(message="联系人不能为空")
    @Size(max=50,message="联系人的长度不能超过50个字符且不小于2个字符")
    @TableField(value= "contacts")
    private String contacts;
    
    @Size(max=11,message="请正确输入手机号码或电话的格式")
    @TableField(value= "phone_num")
    private String phone_num;

    @Size(max=2000,message="其它联系信息的长度不能超过2000个字符")
    @TableField(value= "contact_info")
    private String contact_info;

    @Size(max=500,message="地址的长度不能超过500个字符")
    @TableField(value= "address")
    private String address;

    @TableField(value= "shopid")
    private String shopid;


  
 
}