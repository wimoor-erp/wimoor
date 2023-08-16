package com.wimoor.erp.stock.service;

import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaFormEntry;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
public interface IErpDispatchOverseaFormEntryService extends IService<ErpDispatchOverseaFormEntry> {

	List<Map<String, Object>> findFormDetailByFormid(String id, String string, String string2, String shopid);

	List<Map<String, Object>> selectByFormid(String id);

	List<ErpDispatchOverseaFormEntry> findByFormid(String id);

	void deleteByFormid(String id);

}
