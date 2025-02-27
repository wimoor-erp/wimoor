package com.wimoor.amazon.profit.pojo.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class InPlaceFeeDTO {
	String sku;
	String inplace;
	String length;
	String width;
	String height;
	String weight;
	BigDecimal amount;
	BigDecimal amountCny;
}
