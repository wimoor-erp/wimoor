package com.wimoor.amazon.profit.pojo.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("t_variable_closing_fee")  
@ApiModel(value="VariableClosingFee对象", description="物流方式确定的可变结费用")
public class VariableClosingFee {
	
	@TableId(value= "id")
    private Integer id;

    private String country;

    private Integer typeid;

    private String ismedia;

    private String logisticsid;

    private String name;

    private String format;
 
}