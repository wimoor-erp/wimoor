package com.wimoor.erp.stock.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.stock.pojo.entity.ChangeWhForm;

public interface IChangeWhFormService extends IService<ChangeWhForm> {

	IPage<Map<String, Object>> findByCondition(Page<?> page,Map<String, Object> map);

	Map<String, Object> findById(String id);

	Map<String, Object> saveAction(ChangeWhForm dispatchForm, List<Map<String, Object>> skumapstr, UserInfo user);

	void deleteChangeWhForm(UserInfo user, String id);

	String uploadchangeStockByExcel(Sheet sheet, UserInfo user);
	
}
