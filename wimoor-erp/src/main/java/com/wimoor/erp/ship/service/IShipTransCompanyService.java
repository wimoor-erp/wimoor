package com.wimoor.erp.ship.service;


import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.ship.pojo.entity.ErpShipTransType;
import com.wimoor.erp.ship.pojo.entity.ShipTransChannel;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompany;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompayAPI;


public interface IShipTransCompanyService extends IService<ShipTransCompany> {
	public IPage<ShipTransCompany> findByCondition(Page<?> page,String shopid, String search,String isdelete);
	int updateChannel(ShipTransChannel shipChannel) throws ERPBizException;
	int saveChannel(ShipTransChannel tranChannel) throws ERPBizException;
	List<ShipTransChannel> selectchannelbyExample(Object example);
	void deletechannel(String id) throws ERPBizException;
	List<ShipTransChannel> selectChannelAll();
	int delDetail(Object example);
	Map<String, Object> saveDetail(String shopid,String id,UserInfo user,List<Map<String, Object>> sublist,String name,String simplename, String apicompany, String token) throws ERPBizException;
	List<Map<String,Object>> findByCompanyId(String comid);
	List<Map<String,Object>>  selectBycom(String comid,String shopid,String marketplaceid, String transtype);
	List<Map<String,Object>> selectBychannel(String company,String channel,String marketplaceid, String transtype);
	List<ShipTransCompany> findBylimit(String shopid);
	Map<String,String> deleteIds(String[] idlist);
	void setShipTransExcelBook(SXSSFWorkbook workbook, String search, String mydate, String shopid);
	List<ShipTransCompayAPI> loadApiCompany(UserInfo user);
	public List<Map<String, Object>> findListItem(Map<String, Object> map);
	public List<ShipTransCompany> getCompanyTranstypeList(Map<String, Object> maps);
	public List<ErpShipTransType> selectTransTypeList(String shopid);
	List<ShipTransChannel> shipTransChannelByShopid(String shopid);
	public List<Map<String, Object>> findDelByCompanyId(String comid);
}
