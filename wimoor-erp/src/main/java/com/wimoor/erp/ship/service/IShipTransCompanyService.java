package com.wimoor.erp.ship.service;


import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;
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
import com.wimoor.erp.ship.pojo.entity.ShipTransDetail;


public interface IShipTransCompanyService extends IService<ShipTransCompany> {
	public IPage<ShipTransCompany> findByCondition(Page<?> page,String shopid, String search,String isdelete);
	void deletechannel(String id) throws ERPBizException;
	List<ShipTransDetail> findByCompanyId(String comid);
	List<Map<String,Object>>  selectBycom(String comid,String shopid,String marketplaceid, String transtype);
	List<Map<String,Object>> selectBychannel(String company,String channel,String marketplaceid, String transtype);
	List<ShipTransCompany> findBylimit(String shopid);
	Boolean deleteIds(String[] idlist);
	void setShipTransExcelBook(SXSSFWorkbook workbook, String search, String mydate, String shopid);
	List<ShipTransCompayAPI> loadApiCompany(UserInfo user);
	public List<ShipTransDetail> findListItem(Map<String, Object> map);
	public List<ShipTransCompany> getCompanyTranstypeList(Map<String, Object> maps);
	public List<ErpShipTransType> selectTransTypeList(String shopid);
	List<ShipTransChannel> shipTransChannelByShopid(String shopid);
	public List<ShipTransDetail> findDelByCompanyId(String comid);
	public List<ShipTransCompany> findByShopid(String companyid);
	public List<ErpShipTransType> getTransTypeAll(String companyid);
	Boolean saveDetail(UserInfo user, ShipTransCompany shipcompany);
	public  void uploadTransDetailByExcel(Sheet sheet, UserInfo user);
	public ShipTransCompayAPI getCompanyApiById(String companyid);
}
