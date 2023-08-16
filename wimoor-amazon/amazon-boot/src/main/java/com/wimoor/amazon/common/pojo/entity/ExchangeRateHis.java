package com.wimoor.amazon.common.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
 
@Data
@TableName("t_exchangerate_his")  
@ApiModel(value="ExchangeRateHis对象", description="ExchangeRateHis")
public class ExchangeRateHis  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1621359215899556425L;

 
    @TableId(value= "id",type=IdType.AUTO)
    private int id;

    @TableField(value= "name")
    private String name;

    @TableField(value= "price")
    private BigDecimal price;

    @TableField(value= "symbol")
    private String symbol;

    @TableField(value= "type")
    private String type;
    
    @TableField(value= "byday")
    private Date byday;
    
    @TableField(value= "utctime")
    private Date utctime;
    
}