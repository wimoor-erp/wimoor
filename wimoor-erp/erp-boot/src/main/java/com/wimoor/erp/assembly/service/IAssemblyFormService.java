package com.wimoor.erp.assembly.service;

import java.util.List;
import java.util.Map;

import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmenSummarytVo;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.assembly.pojo.dto.AssemblyFormListDTO;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.purchase.pojo.dto.PurchaseSaveDTO;


public interface IAssemblyFormService extends IService<AssemblyForm> {
	
	Map<String, Object> findById(String id);
	
	Map<String, Object> saveAssemblyForm(AssemblyForm form, UserInfo user) throws BizException;
	
	Map<String,Object> selectSubASList(String warehouseid,String materialid,String shopid);
	
	int deleteAssemblyForm(String id, UserInfo user );
	
	Map<String, Object> saveMutilForm(List<AssemblyForm> formlist, UserInfo user, PurchaseSaveDTO dto ) throws BizException;
	
	Map<String, Object> getLastOneFormByMaterial(String id);
	
	Map<String, Object> updateAssemblyEvent(String formid,int amount_handle, UserInfo user) throws BizException;
	
	int findSumNeed(String shopid);
	
	List<Map<String, Object>> getLastThreeFormByMaterial(String id);
	
	Map<String, Object> updateToStopOperateEvent(String formid, UserInfo user);
	
	Map<String, Object> getLastAssRecord(String shopid, String warehouseid);
	
	List<Map<String, Object>> getAssemblySumReport(Map<String, Object> param);
	
	Integer  readyInMainInventory(AssemblyForm  form, int needin, UserInfo user) throws BizException;
	
	Map<String, Object> updateAssemblyAmount(String formid, String value, UserInfo user);
	
	String assemblyCompareToSku(String sku,String shopid,JSONArray array);
	
	Map<String, Object> getCountNum(Map<String, Object> param);

	void createAssemblyFormByShipment(UserInfo user, String warehouseid, Material material, String number,
			int needassembly);

	void cancelByForm(UserInfo user, String shipmentid,Map<String,Integer> skuship);
	
	Integer findhasAssemblyFromShipment(String shopid,String shipmentid);

	public Map<String, Object> resetAssForm(UserInfo user,String id);

	IPage<Map<String, Object>> findByCondition(AssemblyFormListDTO dto);
	
	public Integer refreshInbound(String shopid,String warehouseid,String materialid) ;

	List<AssemblyForm> getLastFormsByMaterials(List<String> ids);

	Workbook downloadDetailList(Workbook workbook, AssemblyFormListDTO dto, UserInfo userinfo);

	Workbook downloadFormList(Workbook workbook, AssemblyFormListDTO dto, UserInfo userinfo);

	Integer assemblyFormNeedInv(Map<String, Object> param);


    String uploadAssemblyStockByExcel(Sheet sheet, UserInfo user);

	ShipInboundShipmenSummarytVo assemblyQuotainfos(List<String> formids);
	ShipInboundShipmenSummarytVo assemblyQuotainfos(String formid);
    List<Map<String, Object>> getItemByFormids(List<String> formids, String shopid);

	List<Map<String, Object>> getSkuListByRunid(String runid, String shopid);
	public void doneAssemblyForm(UserInfo user, AssemblyForm item,String number,String remark);
}
