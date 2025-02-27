package com.wimoor.amazon.finances.service;

import com.wimoor.amazon.finances.pojo.entity.AmzFinUserItemData;
import com.wimoor.common.user.UserInfo;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 客户导入的SKU财务项费用-应用于商品营收其他费用项目导入 服务类
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-27
 */
public interface IAmzFinUserItemDataService extends IService<AmzFinUserItemData> {
	public IPage<Map<String, Object>> getFinDataList(Page<?> page,UserInfo user, Map<String, Object> params) ;
	public void saveFinItemData(List<AmzFinUserItemData> amzFinUserItemDataList) ;
	public int saveFinItemData(AmzFinUserItemData amzFinUserItemData) ;
	public int deleteFinItemData(String id);
	public List<Map<String, Object>> getFinDataLastWeek(List<Map<String, Object>> list) ;
	public List<Map<String, Object>> getFinDataForSku(Map<String, Object> param);
	public List<Map<String, Object>> getFinDataList(UserInfo user, Map<String, Object> params);
	public void setExcelBookByOtherFee(SXSSFWorkbook workbook, List<Map<String, Object>> list);
	public void setExcelBookByOtherFeeMonth(SXSSFWorkbook workbook, List<Map<String, Object>> list,String title) ;
	public List<Map<String, Object>> loadFile(UserInfo user, InputStream inputStream);
}
