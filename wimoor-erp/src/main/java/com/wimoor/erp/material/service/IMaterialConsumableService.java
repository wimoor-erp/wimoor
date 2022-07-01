package com.wimoor.erp.material.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.material.pojo.entity.MaterialConsumable;

public interface IMaterialConsumableService extends IService<MaterialConsumable> {
	List<MaterialConsumable> selectByMainmid(String materialid);

	int deleteByMainmid(String materialid);
}
