package com.wimoor.amazon.profit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.profit.pojo.entity.InventoryStorageFee;

public interface IInventoryStorageFeeService extends IService<InventoryStorageFee> {

	InventoryStorageFee getPriceByCountry(String country, String month, boolean isStandard);


}
