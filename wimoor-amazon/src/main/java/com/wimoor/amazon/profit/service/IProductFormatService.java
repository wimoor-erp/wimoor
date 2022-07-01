package com.wimoor.amazon.profit.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.profit.pojo.entity.ProductFormat;
 
public interface IProductFormatService extends IService<ProductFormat> {

	List<Map<String, Object>> findTierFormatByCountry(String country);

	List<Map<String, Object>> findSmlProductTierByCountry(String country);

}
