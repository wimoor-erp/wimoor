package com.wimoor.erp.assembly.service;

import java.util.List;
import java.util.Map;


import com.alibaba.fastjson.JSONArray;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.assembly.pojo.dto.AssemblyFormListDTO;
import com.wimoor.erp.assembly.pojo.entity.AssemblyForm;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.purchase.pojo.dto.PurchaseSaveDTO;
import com.wimoor.erp.ship.pojo.dto.ShipInboundShipmentDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IAssemblyFormService extends IService<AssemblyForm> {
	
	Map<String, Object> findById(String id);
	
	Map<String, Object> saveAssemblyForm(AssemblyForm form, UserInfo user) throws BizException;
	
	Map<String,Object> selectSubASList(String warehouseid,String materialid,String shopid);
	
	int deleteAssemblyForm(String id, UserInfo user );
	
	Map<String, Object> saveMutilForm(List<AssemblyForm> formlist, UserInfo user, PurchaseSaveDTO dto ) throws BizException;
	
	AssemblyForm getLastOneFormByMaterial(Object id);
	
	Map<String, Object> updateAssemblyEvent(String formid,int amount_handle, UserInfo user) throws BizException;
	
	int findSumNeed(String shopid);
	
	List<AssemblyForm> getLastFormByMaterial(Object id, int i);
	
	Map<String, Object> updateToStopOperateEvent(String formid, UserInfo user);
	
	Map<String, Object> getLastAssRecord(String shopid, String warehouseid);
	
	List<Map<String, Object>> getAssemblySumReport(Map<String, Object> param);
	
	Integer  readyInMainInventory(AssemblyForm  form, int needin, UserInfo user) throws BizException;
	
	Map<String, Object> updateAssemblyAmount(String formid, String value, UserInfo user);
	
	String assemblyCompareToSku(String sku,String shopid,JSONArray array);
	
	Map<String, Object> getCountNum(Map<String, Object> param);

	void createAssemblyFormByShipment(UserInfo user, String warehouseid, Material material, ShipInboundShipmentDTO shipment,
			int needassembly);

	void cancelByShipment(UserInfo user, ShipInboundShipmentDTO shipment);

	Integer findhasAssemblyFromShipment(String shipmentid);

	public Map<String, Object> resetAssForm(UserInfo user,String id);

	IPage<Map<String, Object>> findByCondition(AssemblyFormListDTO dto);
	
	public void refreshInbound(String shopid,String warehouseid,String materialid) ;

	List<AssemblyForm> getLastFormsByMaterials(List<String> ids);
}
