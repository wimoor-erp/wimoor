package com.wimoor.amazon.profit.pojo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
 
@Data
@TableName("t_fixed_closingfee")  
@ApiModel(value="FixedClosingFee对象", description="产品FixedClosingFee费用")
public class FixedClosingFee {
	@TableId(value= "id")
    private Integer id;

    private String format;

    private String category;

    private BigDecimal fee;

    private String country;

    private Integer sort;
 
}