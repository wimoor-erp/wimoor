package com.wimoor.erp.ship.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_erp_ship_transtype_day")
public class ErpShipTransTypeDay {
	@TableField(value= "shopid")
	private String shopid;

	@TableField(value= "transtypeid")
    private String transtypeid;

	@TableField(value= "day")
    private Integer day;
	
	@TableField(value= "operator")
    private String operator;

	@TableField(value= "opttime")
    private Date opttime;
}