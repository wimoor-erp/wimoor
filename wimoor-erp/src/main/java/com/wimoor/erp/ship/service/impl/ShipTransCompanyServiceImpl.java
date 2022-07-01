package com.wimoor.erp.ship.service.impl;


import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.ship.mapper.ShipTransCompanyMapper;
import com.wimoor.erp.ship.pojo.entity.ErpShipTransType;
import com.wimoor.erp.ship.pojo.entity.ShipTransChannel;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompany;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompayAPI;
import com.wimoor.erp.ship.service.IShipTransCompanyService;

import lombok.RequiredArgsConstructor;

@Service("shipTransCompanyService")
@RequiredArgsConstructor
public class ShipTransCompanyServiceImpl extends ServiceImpl<ShipTransCompanyMapper,ShipTransCompany> implements IShipTransCompanyService {@Override
	public IPage<ShipTransCompany> findByCondition(Page<?> page, String shopid, String search, String isdelete) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateChannel(ShipTransChannel shipChannel) throws ERPBizException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int saveChannel(ShipTransChannel tranChannel) throws ERPBizException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ShipTransChannel> selectchannelbyExample(Object example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletechannel(String id) throws ERPBizException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ShipTransChannel> selectChannelAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delDetail(Object example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, Object> saveDetail(String shopid, String id, UserInfo user, List<Map<String, Object>> sublist,
			String name, String simplename, String apicompany, String token) throws ERPBizException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> findByCompanyId(String comid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> selectBycom(String comid, String shopid, String marketplaceid, String transtype) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> selectBychannel(String company, String channel, String marketplaceid,
			String transtype) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ShipTransCompany> findBylimit(String shopid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> deleteIds(String[] idlist) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setShipTransExcelBook(SXSSFWorkbook workbook, String search, String mydate, String shopid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ShipTransCompayAPI> loadApiCompany(UserInfo user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> findListItem(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ShipTransCompany> getCompanyTranstypeList(Map<String, Object> maps) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ErpShipTransType> selectTransTypeList(String shopid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ShipTransChannel> shipTransChannelByShopid(String shopid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> findDelByCompanyId(String comid) {
		// TODO Auto-generated method stub
		return null;
	}
 
	 

}
