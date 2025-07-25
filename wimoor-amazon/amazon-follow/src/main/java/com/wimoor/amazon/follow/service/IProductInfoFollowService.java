package com.wimoor.amazon.follow.service;

import com.wimoor.amazon.follow.pojo.dto.ProductFollowListDTO;
import com.wimoor.amazon.follow.pojo.dto.ProductInfoFollowSaveDTO;
import com.wimoor.amazon.follow.pojo.entity.ProductInfoFollow;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-06
 */
public interface IProductInfoFollowService extends IService<ProductInfoFollow> {

	IPage<Map<String, Object>> findByCondition(ProductFollowListDTO dto);

	List<Map<String, Object>> saveProfuctFollow(List<ProductInfoFollowSaveDTO> dto);
	Map<String, Object> saveProfuctFollow(ProductInfoFollowSaveDTO dto);

	Map<String,Object> deleteProfuctFollow(List<String> ids);
	
	void downloadDetailExport(SXSSFWorkbook workbook, Map<String, Object> parameter);

	Map<String, Object> uploadShipListByExcel(Sheet sheet, String shopid, String authid, String marketplaceid,String timeid);
	
	List<Map<String, Object>> findRecordList(String pid,String opttype);

	 
	public IPage<Map<String, Object>> findWarningList(Page<?> page,String shopid,String authid,String marketplaceid); 
	void ignoreWarningList(List<String> ids);

	Integer findWarningNunmber(String companyid);

	void changeProfuctFollowTime(List<String> ids);

	void changeProfuctFollow(String ftype, List<String> ids);

	void changeAllCycle(String shopid, String cycle);
	
    void pushOnlineProfuctFollow(List<ProductInfoFollow> list);
    public List<Map<String, Object>> pushOnlineProfuctFollow(List<ProductInfoFollow> list,String remark) ;
    public void syncProductInfo(String shopid,String userid);

	Map<String, Object> pushProfuctFollow(List<ProductInfoFollow> ids, String ftype, String remark);
	
	public void taskOrderAsin();
	public void taskOnlineAsin();
}
