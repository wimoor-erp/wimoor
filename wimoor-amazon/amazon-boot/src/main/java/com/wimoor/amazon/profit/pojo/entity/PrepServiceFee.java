package com.wimoor.amazon.profit.pojo.entity;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;



@Data
@TableName("t_prepservicefee")  
@ApiModel(value="PrepServiceFee对象", description="产品PrepServiceFee费用")
public class PrepServiceFee {
	
	@TableId
    private Integer id;

    private String category;

    private Boolean isstandard;

    private BigDecimal prepservicefee;

    private String country;
 
}