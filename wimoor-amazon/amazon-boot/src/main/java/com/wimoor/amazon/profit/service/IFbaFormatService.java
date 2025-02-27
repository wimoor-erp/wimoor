package com.wimoor.amazon.profit.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.profit.pojo.entity.FBAFormat;

public interface IFbaFormatService extends IService<FBAFormat> {

	FBAFormat findByProductTierIdNew(String productTierId, boolean isClothing,String country);

	FBAFormat findByProductTierIdAndWeight(String productTierId, BigDecimal outboundWeight,String country);
	
	FBAFormat findByProductTierIdAndWeightSL(String productTierId, BigDecimal outboundWeight,String fenpeiType,String country);

	FBAFormat findByProductTierIdAndIsMedia2(String productTierId, boolean media,String country);

	FBAFormat findByProductTierAndType(String productTierId, String shipmentType,String country);

	FBAFormat findEUfbaFormat(String fenpeiType, String productTierId, String country, BigDecimal weight);

	
}
