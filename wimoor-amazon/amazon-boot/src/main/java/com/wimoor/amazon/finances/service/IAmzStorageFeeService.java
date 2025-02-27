package com.wimoor.amazon.finances.service;

import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.finances.pojo.entity.FBALongTermStorageFeeReport;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-22
 */

public interface IAmzStorageFeeService extends IService<FBALongTermStorageFeeReport>{

	Page<Map<String, Object>> findByCondition(IPage<?> page,Map<String, Object> map);
	
	Page<Map<String, Object>> findStorageFeeList(IPage<?> page,Map<String, Object> map);

	void getDownloadList(SXSSFWorkbook workbook, Map<String, Object> parameter);

	void getDownloadLongList(SXSSFWorkbook workbook, Map<String, Object> parameter);
}
