package com.wimoor.amazon.profit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.profit.pojo.entity.FBALabelingFee;

public interface IFbaLabelingFeeService extends IService<FBALabelingFee> {

	FBALabelingFee getPriceByProductTierId(boolean isStandard, String country);


}
