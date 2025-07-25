package com.wimoor.amazon.profit.pojo.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("t_manual_processing_fee")  
@ApiModel(value="ManualProcessingFee对象", description="")
public class ManualProcessingFee {
    private Integer id;

    private String month;

    private BigDecimal manualprocessingfee;

    private String country;
 
}