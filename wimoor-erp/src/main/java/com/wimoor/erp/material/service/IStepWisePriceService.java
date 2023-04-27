package com.wimoor.erp.material.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.material.pojo.entity.StepWisePrice;

public interface IStepWisePriceService extends IService<StepWisePrice>{
	/**
	 * 删除没有绑定material的数据
	 */
	void deleteIsNull();

	List<StepWisePrice> selectByMaterial(String id);
	Map<String,Object> selectAssByMaterial(String materialid);
	void deleteByMaterial(String id);
	public  Map<String,Object> getMaterialPriceByAmount(String material,Integer planamount);
}
