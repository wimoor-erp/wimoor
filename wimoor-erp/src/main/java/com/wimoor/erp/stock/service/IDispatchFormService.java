package com.wimoor.erp.stock.service;

import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.stock.pojo.entity.DispatchForm;

public interface IDispatchFormService extends IService<DispatchForm> {
	IPage<Map<String,Object>> findByCondition(Page<?> page,Map<String,Object> map);

	Map<String, Object> findById(String id);

	Map<String, Object> saveAction(UserInfo user, DispatchForm dispatchForm, String skumapstr)throws BizException;

	String uploadDispatchStockByExcel(Sheet sheet, UserInfo user) throws Exception;
	
	public int submitAction( UserInfo user,String ids,String status ) throws BizException ;
 
}
