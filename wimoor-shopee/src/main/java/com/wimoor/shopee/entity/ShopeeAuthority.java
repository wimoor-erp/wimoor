package com.wimoor.shopee.entity;

import java.math.BigInteger;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
 

@TableName("t_shopee_authority")
@Data
public class ShopeeAuthority {
	@TableId(type = IdType.AUTO)
	private Integer id;
	private BigInteger shopid;
	private BigInteger groupid;
    private String token;
    private String refresh_token;
    private Date validtime;
}
