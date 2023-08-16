package com.wimoor.amazon.profit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.profit.pojo.entity.OutboundWeightFormat;

public interface IOutBoundWeightFormatService extends IService<OutboundWeightFormat> {

	OutboundWeightFormat findByProductTierId(String productTierId, boolean media);


}
