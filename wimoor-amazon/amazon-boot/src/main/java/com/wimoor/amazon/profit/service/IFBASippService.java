package com.wimoor.amazon.profit.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.profit.pojo.entity.FBASipp;

public interface IFBASippService extends IService<FBASipp> {

	FBASipp findByProductTierIdNew(String productTierId, boolean isClothing, String country, BigDecimal outboundWeight);

}
