package com.wimoor.amazon.profit.pojo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
@Data
@TableName("t_individualfee")  
@ApiModel(value="IndividualFee对象", description="个人卖家费用")
public class IndividualFee {
    private Integer id;

    private String country;

    private BigDecimal peritemfee;
 
}