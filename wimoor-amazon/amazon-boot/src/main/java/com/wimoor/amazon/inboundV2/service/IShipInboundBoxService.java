package com.wimoor.amazon.inboundV2.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipCartDTO;
import com.wimoor.amazon.inboundV2.pojo.dto.PackingDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundBox;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundItem;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundOperation;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import com.wimoor.common.user.UserInfo;

public interface IShipInboundBoxService  extends IService<ShipInboundBox>{
	public List<ShipInboundBox> findListByPackageGroupid(String formid,String packageGroupid) ;
	public List<ShipInboundBox> findListByShipmentid(String formid,String shipmentid);
	public List<Map<String, Object>> findShipInboundBox(String shipmentid) ;
	public Map<String, Object> getBoxDetial(PackingDTO dto);
	Map<String, Object> getShipmentBoxDetial(PackingDTO dto);
	ShipInboundOperation generatePackingOptions(String formid);
	ShipInboundOperation setPackingInformation(ShipInboundPlan plan, UserInfo user, List<ShipInboundItem> itemlist);
	void savePackingInformation(ShipCartDTO dto, UserInfo user);
	Map<String,Object> listPackingOptions(PackingDTO dto);
	public Object listPackingGroupItems(PackingDTO dto);
	public ShipInboundOperation confirmPackingOption(PackingDTO dto);
	public List<Map<String, Object>> findAllBoxDim(String formid);
	public Boolean hasSubmitPackage(ShipInboundPlan inplan);

}
