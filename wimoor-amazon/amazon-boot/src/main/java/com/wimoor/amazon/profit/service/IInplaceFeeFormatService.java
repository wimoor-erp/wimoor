package com.wimoor.amazon.profit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.profit.pojo.entity.InplaceFeeFormat;

public interface IInplaceFeeFormatService extends IService<InplaceFeeFormat>{

	InplaceFeeFormat findByInvplaceFeeId(String invplaceFee, boolean isStandard, String country);

	InplaceFeeFormat findByProductTierId(String productTierId);

}
