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
@TableName("t_exchangerate")  
@ApiModel(value="ExchangeInfo对象", description="ExchangeInfo")
public class ExchangeRate  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1621359215899556425L;

    @TableId(value= "id",type=IdType.AUTO)
    private Integer id;

    @TableField(value= "name")
    private String name;

    @TableField(value= "price")
    private BigDecimal price;

    @TableField(value= "symbol")
    private String symbol;

    @TableField(value= "type")
    private String type;
    
    @TableField(value= "utctime")
    private Date utctime;

    @TableField(value= "volume")
    private Integer volume;
 
}