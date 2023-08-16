package com.wimoor.erp.material.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.material.pojo.dto.MaterialConsumableDTO;
import com.wimoor.erp.material.pojo.entity.MaterialConsumable;
import com.wimoor.erp.material.pojo.vo.MaterialConsumableVO;
import com.wimoor.erp.ship.pojo.dto.ConsumableOutFormDTO;

public interface IMaterialConsumableService extends IService<MaterialConsumable> {
	List<MaterialConsumable> selectByMainmid(String materialid);
	public List<MaterialConsumableVO> selectConsumableByMainmid(String id,String warehouseid ,String shopid) ;
	public 	List<Map<String, Object>> selectConsumableByMainSKU(String shopid,String warehouseid,List<Map<String,Object>> skulist);
	public List<Map<String, Object>> findConsumableDetailList(Map<String,Object> maps);
	int deleteByMainmid(String materialid);
	List<MaterialConsumableVO> selectConsumableByMainmid(String materialid,String shopid);
	List<MaterialConsumableVO> selectConsumableBySubmid(String materialid,String warehouseid,String shopid);

	int saveInventoryConsumable(UserInfo user, ConsumableOutFormDTO dto);

	List<Map<String, Object>> findConsumableHistory(String shopid, String shipmentid);

	List<Map<String, Object>> findConsumableDetailByShipment(String shopid, String shipmentid);
	List<Map<String, Object>> findConsumableDetailBySkuList(UserInfo user, MaterialConsumableDTO dto);
}
