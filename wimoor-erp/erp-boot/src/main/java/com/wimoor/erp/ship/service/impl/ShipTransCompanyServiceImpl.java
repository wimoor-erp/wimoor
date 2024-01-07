package com.wimoor.erp.ship.service.impl;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.api.AmazonClientOneFeignManager;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.ship.mapper.ErpShipTransTypeMapper;
import com.wimoor.erp.ship.mapper.ShipTransCompanyMapper;
import com.wimoor.erp.ship.mapper.ShipTransCompayAPIMapper;
import com.wimoor.erp.ship.mapper.ShipTransDetailHisMapper;
import com.wimoor.erp.ship.pojo.entity.ErpShipTransType;
import com.wimoor.erp.ship.pojo.entity.ShipTransChannel;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompany;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompayAPI;
import com.wimoor.erp.ship.pojo.entity.ShipTransDetail;
import com.wimoor.erp.ship.pojo.entity.ShipTransDetailHis;
import com.wimoor.erp.ship.service.IShipTransChannelService;
import com.wimoor.erp.ship.service.IShipTransCompanyService;
import com.wimoor.erp.ship.service.IShipTransDetailService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

@Service("shipTransCompanyService")
@RequiredArgsConstructor
public class ShipTransCompanyServiceImpl extends ServiceImpl<ShipTransCompanyMapper,ShipTransCompany> implements IShipTransCompanyService {
	
	final ErpShipTransTypeMapper erpShipTransTypeMapper;
	final IShipTransChannelService shipTransChannelService;
	final IShipTransDetailService shipTransDetailService;
	final ShipTransDetailHisMapper shipTransDetailHisMapper;
	final AmazonClientOneFeignManager amazonClientOneFeign;
	final ShipTransCompayAPIMapper shipTransCompayAPIMapper;
	@Override
	public IPage<ShipTransCompany> findByCondition(Page<?> page, String shopid, String search, String isdelete) {
		// TODO Auto-generated method stub
		return this.baseMapper.findByCondition(page,shopid, search,isdelete);
	}


	@Override
	public void deletechannel(String id) throws ERPBizException {
		// TODO Auto-generated method stub
		shipTransChannelService.removeById(id);
	}
 
	public void handlerTransDetailHis(ShipTransDetailHis his,ShipTransDetail pojo,String userid) {
		if(pojo!=null) {
			his.setCbmrate(pojo.getCbmrate());
			his.setChanname(pojo.getChanname());
			his.setCompany(pojo.getCompany());
			his.setDrate(pojo.getDrate());
			his.setMarketplaceid(pojo.getMarketplaceid());
			his.setPretime(pojo.getPretime());
			his.setPrice(pojo.getPrice());
			his.setPriceunits(pojo.getPriceunits());
			his.setTranstype(pojo.getTranstype());
			his.setOpttime(new Date());
			his.setId(pojo.getId());
			his.setOperator(userid);
			his.setRemark(pojo.getRemark());
			his.setChannel(pojo.getChannel());
			his.setSubarea(pojo.getSubarea());
		}
	}
	
	@Override
	public Boolean saveDetail(  UserInfo user, ShipTransCompany shipcompany)  {
		// TODO Auto-generated method stub
			//更新
				shipcompany.setOperator(user.getId());
				shipcompany.setOpttime(new Date());
				shipcompany.setShopid(user.getCompanyid());
				QueryWrapper<ShipTransCompany> query = new QueryWrapper<ShipTransCompany>();
				query.eq("name", shipcompany.getName());
				query.eq("shopid", user.getCompanyid());
				List<ShipTransCompany> newshipList = this.baseMapper.selectList(query);
				if(newshipList != null && newshipList.size() > 0) {
					if(newshipList.get(0).getId().equals(shipcompany.getId())) {
						shipcompany.setId(newshipList.get(0).getId());
						shipcompany.setOperator(user.getId());
						shipcompany.setOpttime(new Date());
						this.baseMapper.updateById(shipcompany);
					}else {
						throw new BizException("该物流公司已经存在！");
					}
				}else {
					ShipTransCompany oldcompany = this.baseMapper.selectById(shipcompany.getId());
					if(oldcompany!=null) {
						this.baseMapper.updateById(shipcompany);
					}else {
						this.baseMapper.insert(shipcompany);
					}
				}
	 
		if(shipcompany.getDetaillist()!=null&&shipcompany.getDetaillist().size()>0){
		//先删除当前ID下的数据
			QueryWrapper<ShipTransDetail> queryDetail=new QueryWrapper<ShipTransDetail>();
			queryDetail.eq("company",shipcompany.getId());
			List<ShipTransDetail> oldlist = shipTransDetailService.list(queryDetail);
			Set<String> set=new HashSet<String>();
			for(ShipTransDetail old:oldlist){
				set.add(old.getId());
			}
			for (int i = 0; i < shipcompany.getDetaillist().size(); i++) {
				   ShipTransDetail detail= shipcompany.getDetaillist().get(i);
				   detail.setOperator(user.getId());
				   detail.setOpttime(new Date());
				   detail.setDisabled(Boolean.FALSE);
				   detail.setCompany(shipcompany.getId());
				   if(StrUtil.isEmpty(detail.getChannel())) {
					   detail.setChannel(null);
				   }
				   if(StrUtil.isEmpty(detail.getTranstype())) {
					   detail.setTranstype(null);
				   }
				   if(set.remove(detail.getId())){
					   shipTransDetailService.updateById(detail);
				   }else{
					    shipTransDetailService.save(detail);
				   }
				   ShipTransDetailHis his=new ShipTransDetailHis();
				   handlerTransDetailHis(his,detail,user.getId());
				   shipTransDetailHisMapper.insert(his);
				}
			 for (String key : set) {
				 ShipTransDetail oldShipDetail = shipTransDetailService.getById(key);
				 if(oldShipDetail!=null){
						oldShipDetail.setDisabled(Boolean.TRUE);
						shipTransDetailService.updateById(oldShipDetail);
						ShipTransDetailHis his=new ShipTransDetailHis();
						handlerTransDetailHis(his,oldShipDetail,user.getId());
						shipTransDetailHisMapper.insert(his);
					} 
			    }
		     }
			 
		return true;
	}

	@Override
	public List<ShipTransDetail> findByCompanyId(String comid) {
		// TODO Auto-generated method stub
		  List<ShipTransDetail> result = this.baseMapper.findByCompanyId(comid);
		return result;
	}


	@Transactional
	public void uploadTransDetailByExcel(Sheet sheet, UserInfo user)   {
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row skuRow = sheet.getRow(i);
			Cell cell = skuRow.getCell(0);
			cell.setCellType(CellType.STRING);
			String companyname = cell.getStringCellValue();
			if(StrUtil.isEmpty(companyname)) {
				continue;
			}
			QueryWrapper<ShipTransCompany> query=new QueryWrapper<ShipTransCompany>();
			query.eq("shopid",user.getCompanyid());
			query.eq("name",companyname);
			Cell cell1 = skuRow.getCell(1);
			cell1.setCellType(CellType.STRING);
			String marketplaceName = cell1.getStringCellValue();
			ShipTransCompany company=null;
			List<ShipTransCompany> comlist = this.baseMapper.selectList(query);
			if(comlist!=null&&comlist.size()>0) {
				company=comlist.get(0);
			}else {
				  company=new ShipTransCompany();
				  company.setShopid(user.getCompanyid());
				  company.setIsdelete(false);
				  company.setName(companyname);
				  company.setOperator(user.getId());
				  company.setOpttime(new Date());
				  this.baseMapper.insert(company);
			}
			Result<?> result=amazonClientOneFeign.getMarketplaceAction(marketplaceName);
			String marketplaceid=null;
			if(Result.isSuccess(result)&&result.getData()!=null) {
				Map<String, Object> marketmap  = BeanUtil.beanToMap(result.getData());
				marketplaceid=marketmap.get("marketplaceid").toString();
			}
			
			String simplename =null;
			if(marketplaceid!=null  ) {
				String comid=company.getId();
				ShipTransDetail pojo=new ShipTransDetail();
				pojo.setCompany(comid);
				pojo.setDisabled(Boolean.FALSE);
				pojo.setMarketplaceid(marketplaceid);
				String  subarea= null;
				Cell cell2 = skuRow.getCell(2);
				if(cell2!=null) {
					cell2.setCellType(CellType.STRING);
					subarea = cell2.getStringCellValue();
				}
				pojo.setSubarea(subarea);
			 
				
				String transtype = null;
				Cell cell3 = skuRow.getCell(3);
				if(cell3!=null) {
					cell3.setCellType(CellType.STRING);
					transtype = cell3.getStringCellValue();
				}
				transtype=handlerTransType(user,transtype);
				pojo.setTranstype(transtype);
				
				Cell cell4 = skuRow.getCell(4);
				cell4.setCellType(CellType.STRING);
				String channame = cell4.getStringCellValue();
				pojo.setChanname(channame);
				
				Cell cell5 = skuRow.getCell(5);
				cell5.setCellType(CellType.STRING);
				String pretime = cell5.getStringCellValue();
				pojo.setPretime(Integer.parseInt(pretime));
				
				String priceunits="kg";
				Cell cell6 = skuRow.getCell(6);
				if(cell5!=null) {
					cell6.setCellType(CellType.STRING);
					priceunits = cell6.getStringCellValue();
				}
				if("kg".equalsIgnoreCase(priceunits)) {
					priceunits="weight";
				}else if("m3".equalsIgnoreCase(priceunits)||"cbm".equalsIgnoreCase(priceunits)){
					priceunits="volume";
				}else {
					priceunits=null;
				}
				pojo.setPriceunits(priceunits);
				Cell cell7 = skuRow.getCell(7);
				if(cell7!=null) {
				  cell7.setCellType(CellType.STRING);
					if(cell7.getStringCellValue()!=null) {
				      String price = cell7.getStringCellValue();
				      pojo.setPrice(new BigDecimal(price));
					}
				}
				Cell cell8 = skuRow.getCell(8);
				if(cell8!=null) {
					cell8.setCellType(CellType.STRING);
					if(cell8.getStringCellValue()!=null) {
					String drate = cell8.getStringCellValue();
						if(StrUtil.isNotBlank(drate)) {
							pojo.setDrate(Integer.parseInt(drate));
						}
					}
				}
				Cell cell9 = skuRow.getCell(9);
				String cbm =null;
				if(cell9!=null) {
					cell9.setCellType(CellType.STRING);
					cbm = cell9.getStringCellValue();
				}
				if(cbm!=null&&StrUtil.isNotBlank(cbm)) {
					pojo.setCbmrate(Integer.parseInt(cbm));
				}else{
					pojo.setCbmrate(null);
				}
				
				Cell cell10 = skuRow.getCell(10);
				cell10.setCellType(CellType.STRING);
				simplename = cell10.getStringCellValue();
				Cell cell11 = skuRow.getCell(11);
				if(cell11!=null) {
					cell11.setCellType(CellType.STRING);
					String channeltype = cell11.getStringCellValue();
					channeltype=handlerChannelType(user,channeltype);
					pojo.setChannel(channeltype);
				}
				
				Cell cell12 = skuRow.getCell(12);
				if(cell12!=null) {
					cell12.setCellType(CellType.STRING);
					String remark = cell12.getStringCellValue();
					pojo.setRemark(remark);
				}
				pojo.setOpttime(new Date());
				pojo.setOperator(user.getId());
				//判断是新增还是添加
				QueryWrapper<ShipTransDetail> queryDetail=new QueryWrapper<ShipTransDetail>();
				queryDetail.eq("company",comid);
				queryDetail.eq("marketplaceid",marketplaceid);
				queryDetail.eq("channame",channame);
				if(pojo.getSubarea()!=null) {
					queryDetail.eq("subarea",pojo.getSubarea());
				}
				if(pojo.getTranstype()!=null) {
					queryDetail.eq("transtype",pojo.getTranstype());
				}
				if(pojo.getChannel()!=null) {
					queryDetail.eq("channel",pojo.getChannel());
				}
				List<ShipTransDetail> tlist = shipTransDetailService.list(queryDetail);
				if(tlist!=null && tlist.size()>0) {
					//更新
					ShipTransDetail oldpojo = tlist.get(0);
					if(oldpojo!=null && oldpojo.getId()!=null) {
						oldpojo.setCbmrate(pojo.getCbmrate());
						oldpojo.setChanname(pojo.getChanname());
						oldpojo.setCompany(comid);
						oldpojo.setDrate(pojo.getDrate());
						oldpojo.setOperator(user.getId());
						oldpojo.setOpttime(new Date());
						oldpojo.setPretime(pojo.getPretime());
						oldpojo.setPrice(pojo.getPrice());
						oldpojo.setPriceunits(pojo.getPriceunits());
						oldpojo.setTranstype(pojo.getTranstype());
						if(StrUtil.isNotBlank(pojo.getRemark())) {
							oldpojo.setRemark(pojo.getRemark());
						}
						Boolean res = shipTransDetailService.updateById(oldpojo);
						if(res) {
							ShipTransDetailHis pojo2=new ShipTransDetailHis();
							handlerTransDetailHis(pojo2,oldpojo,user.getId());
							shipTransDetailHisMapper.insert(pojo2);
						}
					}
				}else {
					//插入
					Boolean res = shipTransDetailService.save(pojo);
					if(res) {
						ShipTransDetailHis pojo2=new ShipTransDetailHis();
						handlerTransDetailHis(pojo2,pojo,user.getId());
						shipTransDetailHisMapper.insert(pojo2);
					}
				}
				if(StrUtil.isNotBlank(simplename)) {
					if(!simplename.equals(company.getSimplename())||company.getIsdelete()==true) {
						company.setSimplename(simplename);
						company.setIsdelete(false);
						this.baseMapper.updateById(company);
					}
				}else if(company.getIsdelete()==true) {
					company.setIsdelete(false);
					this.baseMapper.updateById(company);
				}
			}else {
				continue;
			}
		}
	 
		return ;
	}
	

	
	private String handlerChannelType(UserInfo user, String channeltype) {
		// TODO Auto-generated method stub
		if(StrUtil.isEmpty(channeltype)||"".equals(channeltype)) {
			return null;
		}else {
	          List<ShipTransChannel> oldone = shipTransChannelService.selectByshopid(user.getCompanyid(), channeltype);
		    if(oldone!=null&&oldone.size()>0) {
		    	return oldone.get(0).getId();
		    }else {
		    	ShipTransChannel record=new ShipTransChannel();
		    	record.setName(channeltype);
		    	record.setShopid(user.getCompanyid());
		    	record.setOperator(user.getId());
		    	record.setOpttime(new Date());
		    	shipTransChannelService.save(record);
				return record.getId();
		    }
		}
	}
	
	public String handlerTransType(UserInfo user,String typenname) {
		if(StrUtil.isEmpty(typenname)||"".equals(typenname)) {
			return null;
		}else {
		    List<ErpShipTransType> oldone = erpShipTransTypeMapper.selectTransTypeList(user.getCompanyid(), typenname);
		    if(oldone!=null&&oldone.size()>0) {
		    	return oldone.get(0).getId();
		    }else {
		    	ErpShipTransType record=new ErpShipTransType();
		    	record.setName(typenname);
		    	record.setShopid(user.getCompanyid());
		    	record.setOperator(user.getId());
		    	record.setOpttime(new Date());
				erpShipTransTypeMapper.insert(record);
				return record.getId();
		    }
		}
	}
	
	
	@Override
	public List<Map<String, Object>> selectBycom(String comid, String shopid, String marketplaceid, String transtype) {
		return this.baseMapper.selectBycom(comid, shopid, marketplaceid, transtype);
	}

	@Override
	public List<Map<String, Object>> selectBychannel(String company, String channel, String marketplaceid,
			String transtype) {
		return this.baseMapper.selectBychannel(company, channel, marketplaceid, transtype);
	}

	@Override
	public List<ShipTransCompany> findBylimit(String shopid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteIds(String[] idlist) {
		// TODO Auto-generated method stub
        int i=0;
		for(String id:idlist) {
			if(StrUtil.isNotEmpty(id)) {
				ShipTransCompany oldone = this.baseMapper.selectById(id);
				oldone.setIsdelete(true);
				i+=this.baseMapper.updateById(oldone);
			}
		}
		if(i==idlist.length) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public void setShipTransExcelBook(SXSSFWorkbook workbook, String search,String mydate, String shopid) {
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("name", "物流公司");
		titlemap.put("marketname", "区域");
		titlemap.put("subarea", "分区");
		titlemap.put("transtype", "运输方式");
		titlemap.put("channame", "渠道名称");
		titlemap.put("pretime", "渠道时效（天）");
		titlemap.put("priceunits", "计价单位(kg/m3)");
		titlemap.put("price", "价格（元）");
		titlemap.put("drate", "材积基数(cbcm)");
		titlemap.put("cbmrate", "材积基数(cbm)");
		titlemap.put("simplename", "公司简称");
		titlemap.put("channeltype", "渠道类型");
		titlemap.put("remark", "备注");
		titlemap.put("operator", "操作人");
		titlemap.put("opttime", "操作时间");
		List<Map<String,Object>> list = this.baseMapper.findAllByCondition(shopid,  mydate, search);
		Sheet sheet = workbook.createSheet("sheet1");
		// 在索引0的位置创建行（最顶端的行）
		Row trow = sheet.createRow(0);
		Object[] titlearray = titlemap.keySet().toArray();
		for (int i = 0; i < titlearray.length; i++) {
			Cell cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
			Object value = titlemap.get(titlearray[i].toString());
			cell.setCellValue(value.toString());
		}
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 1);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					Cell cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					String key = titlearray[j].toString();
					Object value = map.get(key);
					if(key.equals("opttime")){
						if(value!=null && value.toString().endsWith(".0")){
							value = value.toString().substring(0,value.toString().length()-2);
						}
					}
					if(key.equals("priceunits")) {
						if("weight".equals(value)) {
							value="kg";
						}
						if("volume".equals(value)) {
							value="m3";
						}
					}
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}
		}
	
}

	@Override
	public List<ShipTransCompayAPI> loadApiCompany(UserInfo user) {
		// TODO Auto-generated method stub
		List<ShipTransCompayAPI> apilist = shipTransCompayAPIMapper.selectList(null);
		return apilist;
	}

	@Override
	public List<ShipTransDetail> findListItem(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.baseMapper.findListItem(map);
	}

	@Override
	public List<ShipTransCompany> getCompanyTranstypeList(Map<String, Object> maps) {
		return this.baseMapper.getCompanyTranstypeList(maps);
	}

	@Override
	public List<ErpShipTransType> selectTransTypeList(String shopid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ShipTransChannel> shipTransChannelByShopid(String shopid) {
		return shipTransChannelService.selectByshopid(shopid,null);
	}

	@Override
	public List<ShipTransDetail> findDelByCompanyId(String comid) {
		// TODO Auto-generated method stub
		return  this.baseMapper.findDisabledByCompanyId(comid);
	}

	@Override
	public List<ShipTransCompany> findByShopid(String companyid) {
		QueryWrapper<ShipTransCompany> queryWrapper=new QueryWrapper<ShipTransCompany>();
		queryWrapper.eq("shopid", companyid);
		queryWrapper.eq("isdelete", false);
		return this.baseMapper.selectList(queryWrapper);
	}

	@Override
	public List<ErpShipTransType> getTransTypeAll(String shopid) {
		QueryWrapper<ErpShipTransType> queryWrapper=new QueryWrapper<ErpShipTransType>();
		queryWrapper.eq("shopid", shopid);
		queryWrapper.or().isNull("shopid");
		return erpShipTransTypeMapper.selectList(queryWrapper);
	}


	@Override
	public ShipTransCompayAPI getCompanyApiById(String companyid) {
		// TODO Auto-generated method stub
		ShipTransCompany com = this.baseMapper.selectById(companyid);
	    if(com!=null&&com.getApi()!=null&&com.getAccessToken()!=null) {
	    	return shipTransCompayAPIMapper.selectById(com.getApi());
	    }else {
	    	return null;
	    }
	}
 
	 

}
