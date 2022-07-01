package com.wimoor.erp.common.service;


import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.common.pojo.entity.MarketPriority;

public interface IMarketPriorityService extends IService<MarketPriority> {

	String saveMarketPriority(List<Map<String, Object>> mapList, String groupid);
}
