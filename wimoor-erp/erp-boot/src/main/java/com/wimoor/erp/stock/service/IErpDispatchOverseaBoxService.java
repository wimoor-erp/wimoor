package com.wimoor.erp.stock.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.stock.pojo.dto.PackingDTO;
import com.wimoor.erp.stock.pojo.dto.ShipCartDTO;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaBox;

import java.util.List;
import java.util.Map;

public interface IErpDispatchOverseaBoxService  extends IService<ErpDispatchOverseaBox>{
	public List<ErpDispatchOverseaBox> findListByPackageGroupid(String formid ) ;
	public List<ErpDispatchOverseaBox> findListByShipmentid(String formid);
	public List<Map<String, Object>> findShipInboundBox(String formid) ;
	public Map<String, Object> getBoxDetial(PackingDTO dto);
	void savePackingInformation(ShipCartDTO dto, UserInfo user);
	public List<Map<String, Object>> findAllBoxDim(String formid);

}
