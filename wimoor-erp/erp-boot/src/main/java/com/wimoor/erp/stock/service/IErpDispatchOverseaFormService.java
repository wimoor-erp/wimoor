package com.wimoor.erp.stock.service;

import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaForm;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
public interface IErpDispatchOverseaFormService extends IService<ErpDispatchOverseaForm> {

	int submitAction(UserInfo user, String ids, String status);

	Map<String, Object> saveAction(UserInfo user, ErpDispatchOverseaForm dispatchForm, String skumapstr);

	Map<String, Object> findById(String id);

	String uploadDispatchStockByExcel(Sheet sheet, UserInfo user);

	IPage<Map<String, Object>> findByCondition(Page<Object> page, Map<String, Object> map);
	List<Map<String, Object>> getShipArrivalTimeRecord(String shopid, String country,String sku, String groupid);
}
