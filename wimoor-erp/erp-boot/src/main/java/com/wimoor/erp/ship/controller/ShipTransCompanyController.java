package com.wimoor.erp.ship.controller;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.impl.PictureServiceImpl;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.ship.pojo.dto.ShipTransCompanyListDTO;
import com.wimoor.erp.ship.pojo.dto.ShipTransDetailDTO;
import com.wimoor.erp.ship.pojo.entity.ErpShipTransType;
import com.wimoor.erp.ship.pojo.entity.ShipTransChannel;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompany;
import com.wimoor.erp.ship.pojo.entity.ShipTransCompayAPI;
import com.wimoor.erp.ship.pojo.entity.ShipTransDetail;
import com.wimoor.erp.ship.service.IErpShipTransTypeService;
import com.wimoor.erp.ship.service.IShipTransChannelService;
import com.wimoor.erp.ship.service.IShipTransCompanyService;
import com.wimoor.erp.ship.service.IShipTransCompanyZhihuiService;
import com.wimoor.erp.ship.service.IShipTransDetailService;
import com.wimoor.erp.warehouse.service.IWarehouseService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
 
@Api(tags = "物流渠道")
@RestController
@SystemControllerLog("物流渠道")
@RequestMapping("/api/v1/shipTransCompany")
@RequiredArgsConstructor
public class ShipTransCompanyController {

	@Resource
	IShipTransCompanyService shipTransCompanyService;
	@Resource
	IShipTransChannelService shipTransChannelService;
	final IShipTransDetailService shipTransDetailService;
	final IErpShipTransTypeService erpShipTransTypeService;
	final IShipTransCompanyZhihuiService shipTransCompanyZhihuiService;
	final IWarehouseService  iWarehouseService;
	@Resource
	IPictureService pictureService;
	@Resource
	FileUpload fileUpload;
	@GetMapping(value = "/downloadShipTransList")
	public void downloadShipTransListAction(String search,String mydate, HttpServletResponse response, Model model)  {
		// 创建新的Excel工作薄
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		// 将数据写入Excel
		 UserInfo user=UserInfoContext.get();
		String shopid =user.getCompanyid();
		if(StrUtil.isEmpty(search)) {
			search=null;
		} else {
			search=search.trim()+"%";
		}
		if(StrUtil.isNotEmpty(mydate)) {
			mydate=mydate.trim()+" 23:59:59";
		}else {
			mydate=null;
		}
		shipTransCompanyService.setShipTransExcelBook(workbook, search, mydate,shopid);
		try {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=shipTransCompany" + System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping(value = "/list")
	public Result<IPage<ShipTransCompany>> getListData(@ApiParam("物流查询")@RequestBody ShipTransCompanyListDTO dto)  {
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		String search = dto.getSearch();
		String isdelete = dto.getIsdelete();
		if(StrUtil.isEmpty(search)) {
			search=null;
		} else {
			search=search.trim()+"%";
		}
		if(StrUtil.isEmpty(isdelete)) {
			isdelete="0";
		} 
		if(isdelete.equals("true")) {
			isdelete="1";
		}else {
		    isdelete="0";
		}
		return Result.success(shipTransCompanyService.findByCondition(dto.getPage(),shopid, search,isdelete));
	}

	@GetMapping("/getminList")
	public Result<List<ShipTransCompany>> getminList() {
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		return Result.success(shipTransCompanyService.findBylimit(shopid));
	}
	
	@GetMapping("/loadApiCompany")
	public Result<List<ShipTransCompayAPI>> loadApiCompanyAction() {
		UserInfo user=UserInfoContext.get();
		return Result.success(shipTransCompanyService.loadApiCompany(user));
	}
	
	@SystemControllerLog( "保存")
	@PostMapping("/saveData")
	public Result<String> saveDataAction(@ApiParam("物流公司")@RequestBody ShipTransCompany stCompany) {
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		QueryWrapper<ShipTransCompany> query=new QueryWrapper<ShipTransCompany>();
		query.eq("shopid", shopid);
		query.eq("name", stCompany.getName());
		List<ShipTransCompany> list = shipTransCompanyService.list(query);
		if (list.size() > 0) {
		  throw	new BizException( "添加失败！当前公司名称已存在！");
		}
		Date opttime = new Date();
		stCompany.setOperator(user.getId());
		stCompany.setOpttime(opttime);
		stCompany.setShopid(shopid);
		boolean result = shipTransCompanyService.save(stCompany);
	    return Result.judge(result);
	}

	@SystemControllerLog( "更新")
	@PostMapping("/updateData")
	public Result<String> updateDataAction(@ApiParam("物流公司")@RequestBody ShipTransCompany stCompany)   {
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		String operator = user.getId();
		Date opttime = new Date();
		stCompany.setOperator(operator);
		stCompany.setOpttime(opttime);
		stCompany.setShopid(shopid);
		boolean result = shipTransCompanyService.updateById(stCompany);
		return Result.judge(result);
	}

	@GetMapping(value = "channelList")
	public Result<List<ShipTransChannel>> getChannelAction()  {
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		return Result.success(shipTransCompanyService.shipTransChannelByShopid(shopid));
	}

	@SystemControllerLog("更新渠道")
	@PostMapping("/updateChannel")
	public Result<String> updateChannelAction(@ApiParam("运输方式")@RequestBody	ShipTransChannel tranChannel)  {
		UserInfo user=UserInfoContext.get();
		String shopid =user.getCompanyid();
		String operator = user.getId();
		Date opttime = new Date();
		tranChannel.setOperator(operator);
		tranChannel.setOpttime(opttime);
		tranChannel.setShopid(shopid);
		Boolean result = shipTransChannelService.updateById(tranChannel);
		return Result.judge(result);
	}

	@SystemControllerLog( "保存渠道")
	@PostMapping("/saveChannel")
	public Result<String> saveChannelAction(@ApiParam("渠运输方式")@RequestBody List<ShipTransChannel> shipchannels) {
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		String operator = user.getId();
		Set<String> useable=new HashSet<String>();
		QueryWrapper<ShipTransChannel> query=new QueryWrapper<ShipTransChannel>();
		query.eq("shopid", shopid);
		List<ShipTransChannel> oldlist = shipTransChannelService.list(query);
		for(ShipTransChannel tranChannel:shipchannels) {
			Date opttime = new Date();
			tranChannel.setOperator(operator);
			tranChannel.setOpttime(opttime);
			tranChannel.setShopid(shopid);
			if(tranChannel.idIsNULL()) {
				  shipTransChannelService.save(tranChannel);
			} else {	
				shipTransChannelService.updateById(tranChannel);
				useable.add(tranChannel.getId());
			}
		}
		for( ShipTransChannel oldone:oldlist) {
			if(!useable.contains(oldone.getId())) {
				if(isUsedInTransDetail(oldone)) {
					throw new BizException("["+oldone.getName()+"]已被使用无法删除");
				}
				shipTransChannelService.removeById(oldone.getId());
			}
			
		}
		return Result.success();
	}
	
	private boolean isUsedInTransDetail(ShipTransChannel type) {
		QueryWrapper<ShipTransDetail> query =new QueryWrapper<ShipTransDetail>();
		query.eq("channel", type.getId());
		query.eq("disabled", false);
		return shipTransDetailService.count(query)>0;
	}
	
	@SystemControllerLog( "删除渠道")
	@GetMapping(value = "deleteChannel")
	public Result<String> deleteChannelAction(String ids)  {
		String[] idlist = ids.split(",");
		for (int i = 0; i < idlist.length; i++) {
			String id = idlist[i];
			if (StrUtil.isNotEmpty(id)) {
				shipTransCompanyService.deletechannel(id);
			}
		}
		return Result.success();
	}

 
	@GetMapping(value = "findDetail")
	public Result<List<ShipTransChannel>> findDetailAction()  {
		List<ShipTransChannel> channelList = shipTransChannelService.list();
		return Result.success(channelList);
	}

	@SystemControllerLog("保存渠道")
	@PostMapping(value = "saveDetail")
	public Result<String> saveDetailAction(@ApiParam("物流公司与渠道")@RequestBody ShipTransCompany shipcompany)  {
		UserInfo user=UserInfoContext.get();
		shipTransCompanyService.saveDetail( user,shipcompany);
		return Result.success(shipcompany.getId());
	}

	@GetMapping(value = "showDetail")
	public Result<ShipTransCompany> showDetailAction(String id) {
		  List<ShipTransDetail> itemList = shipTransCompanyService.findByCompanyId(id);
		  ShipTransCompany compa = shipTransCompanyService.getById(id);
		  if(compa.getLocation()!=null) {
			  Picture picture = pictureService.getById(compa.getLocation());
			  compa.setUploadpath(fileUpload.getPictureImage(picture.getLocation()));
		  }else {
			  compa.setUploadpath(null);
		  }
		  compa.setDetaillist(itemList);
		return Result.success(compa);
	}
	
	@GetMapping(value = "showDelDetail")
	public Result<List<ShipTransDetail>> showDelDetailAction(String id) {
		  List<ShipTransDetail> itemList = shipTransCompanyService.findDelByCompanyId(id);
		return Result.success(itemList);
	}
	
 
	
	@SystemControllerLog("批量删除")
	@GetMapping(value = "deleteInfo")
	public Result<String> deleteInfoAction(String ids) {
		String[] idlist = ids.split(",");
		Boolean result= shipTransCompanyService.deleteIds(idlist);
		return Result.judge(result);
	}
     
	

	@GetMapping(value = "/getTransType")
	public Result<List<ErpShipTransType>> getTransTypeAction(){
		UserInfo user=UserInfoContext.get();
		String shopid =user.getCompanyid();
		QueryWrapper<ErpShipTransType> query=new QueryWrapper<ErpShipTransType>();
		query.eq("shopid", shopid);
		query.eq("disable", false);
		List<ErpShipTransType> list =erpShipTransTypeService.list(query);
		return Result.success(list) ;
	}
	
	@GetMapping(value = "/getTransTypeAll")
	public Result<List<ErpShipTransType>> getTransTypeAllAction(){
		UserInfo user=UserInfoContext.get();
		String shopid =user.getCompanyid();
		QueryWrapper<ErpShipTransType> query=new QueryWrapper<ErpShipTransType>();
		query.eq("shopid", shopid);
		query.eq("disable", false);
		query.or().isNull("shopid");
		List<ErpShipTransType> list =erpShipTransTypeService.list(query);
		return Result.success(list) ;
	}
	
	@SystemControllerLog( "删除类型")
	@PostMapping(value = "/delTransType")
	public Result<String> delTransTypeAction(String typeid){
		boolean result = erpShipTransTypeService.removeById(typeid);
		return Result.judge(result);
	}
	
	@SystemControllerLog( "保存类型")
	@PostMapping(value = "/saveTransType")
	@Transactional
	public Result<String> saveTransTypeAction(@ApiParam("渠道类型")@RequestBody List<ErpShipTransType> tranlist){
		UserInfo user=UserInfoContext.get();
		String shopid =user.getCompanyid();
		QueryWrapper<ErpShipTransType> queryAll=new QueryWrapper<ErpShipTransType>();
		queryAll.eq("shopid", shopid);
		List<ErpShipTransType> oldlist = erpShipTransTypeService.list(queryAll);
		Map<String,ErpShipTransType> oldMap=new HashMap<String,ErpShipTransType>();
		for(ErpShipTransType item:oldlist) {
			oldMap.put(item.getName(), item);
		}
		Set<String> nameset=new HashSet<String>();
        for(ErpShipTransType item:tranlist) {
            ErpShipTransType oldone = oldMap.get(item.getName());
            if(!nameset.contains(item.getName())) {
            	nameset.add(item.getName());
            }else if(oldone==null||oldone.getId().equals(item.getId())) {
            	throw new BizException("运输方式名称不能重复");
            }
			if(oldone!=null) {
				oldMap.remove(item.getName());
				if(oldone.getDisable()) {
					oldone.setDisable(false);
					erpShipTransTypeService.updateById(oldone);
				}
				continue;
			}else {
				if(StrUtil.isBlank(item.getName())) {
	            	throw new BizException("运输方式名称不能为空");
	            }
				item.setId(iWarehouseService.getUUID());
				item.setShopid(shopid);
				item.setOperator(user.getId());
				item.setOpttime(new Date());
				erpShipTransTypeService.save(item);
			}
        }
        
        for( Entry<String, ErpShipTransType> entry:oldMap.entrySet()) {
        	ErpShipTransType oldone=entry.getValue();
        	if(isUsedInTransDetail(oldone)) {
        		throw new BizException("["+oldone.getName()+"]已被使用无法删除");
        	}
        	oldone.setDisable(true);
        	erpShipTransTypeService.updateById(oldone);
        }
		return Result.success();
	}
	
	private boolean isUsedInTransDetail(ErpShipTransType type) {
		return shipTransDetailService.usedTransType(  type.getShopid(),type.getId() )>0;
	}

	@SystemControllerLog( "更新")
	@PostMapping(value = "/uploadTransDetail",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<Boolean> uploadExcelAction(@RequestParam("file")MultipartFile file)  {
	       UserInfo user=UserInfoContext.get();
			if (file != null) {
				try {
					InputStream inputStream = file.getInputStream();
					Workbook workbook = WorkbookFactory.create(inputStream);
					Sheet sheet = workbook.getSheetAt(0);
					 shipTransCompanyService.uploadTransDetailByExcel(sheet, user);
					workbook.close();
					return Result.success();
				} catch (IOException e) {
					e.printStackTrace();
					return Result.failed();
				} catch (EncryptedDocumentException e) {
					e.printStackTrace();
				} catch (InvalidFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		return Result.failed();
	}
	
	@SystemControllerLog( "更新")
	@PostMapping(value = "/uploadPrice",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<ShipTransCompany> uploadPriceAction(@ApiParam("物流公司ID")@RequestParam String comid,@RequestParam("file")MultipartFile file)  {
	       UserInfo user=UserInfoContext.get();
			if (file != null) {
				try {
					InputStream inputStream = file.getInputStream();
					ShipTransCompany com = shipTransCompanyService.getById(comid);
					String filePath = PictureServiceImpl.transFilePath + user.getCompanyid();
					String filename = file.getOriginalFilename();
					if(filename!=null) {
						int len = filename.lastIndexOf(".");
						String filenames = filename.substring(0, len);
						String imgtype=filename.substring(len, filename.length());
						filename=filenames+System.currentTimeMillis()+imgtype;
						filename= System.currentTimeMillis()+imgtype;
					}else {
						filename= System.currentTimeMillis()+".xlsx";
					}
					String location=null;
					if(com.getLocation()!=null) {
						location=com.getLocation().toString();
					}
					Picture picture = pictureService.uploadPicture(inputStream, null, filePath, filename,location );
					com.setLocation(new BigInteger(picture.getId()));
					com.setUploadpath(picture.getLocation());
					shipTransCompanyService.updateById(com);
					com.setUploadpath(fileUpload.getPictureImage(picture.getLocation()));
					return Result.success(com);
				} catch (IOException e) {
					e.printStackTrace();
					return Result.failed();
				} catch (EncryptedDocumentException e) {
					e.printStackTrace();
				}  catch (Exception e) {
					e.printStackTrace();
				}
			}
		return Result.failed();
	}
	
	@ApiOperation(value = "获取物流公司")
	@GetMapping(value = "/getCompanyList")
	public Result<List<ShipTransCompany>> getCompanyListAction() {
		UserInfo user=UserInfoContext.get();
		QueryWrapper<ShipTransCompany> query=new QueryWrapper<ShipTransCompany>();
		query.eq("shopid",user.getCompanyid());
		query.eq("isdelete",false);
		List<ShipTransCompany> list = shipTransCompanyService.list(query);
		for(ShipTransCompany item:list) {
			if(item.getLocation()!=null) {
				     Picture picture = pictureService.getById(item.getLocation());
					 item.setUploadpath(fileUpload.getPictureImage(picture.getLocation()));
			}
		
		}
		return Result.success(list);
	}
	   
    
    @ApiOperation(value = "获取物流渠道列表")
    @GetMapping("/getChannels")
    public Result<List<ShipTransChannel>> getChannelsAction() {
    	UserInfo user=UserInfoContext.get();
    	List<ShipTransChannel> list = shipTransCompanyService.shipTransChannelByShopid(user.getCompanyid());
    	return Result.success(list);
    }
    
    @ApiOperation(value = "获取物流商列表")
    @GetMapping("/getTranlist")
    public Result<List<ShipTransCompany>> getTransListAction() {
    	UserInfo user=UserInfoContext.get();
    	List<ShipTransCompany> listCompany = shipTransCompanyService.findByShopid(user.getCompanyid());
    	return Result.success(listCompany);
    }
    
 // 级联操作渠道(选了公司带出渠道)
    @ApiOperation(value = "根据物流商获取渠道列表")
    @GetMapping("/getChannel")
 	public Result<List<Map<String, Object>>> channelChangeAction(
 			@ApiParam("物流公司ID")@RequestParam String company,
			@ApiParam("marketplaceid")@RequestParam String marketplaceid,
			@ApiParam("物流商类型")@RequestParam String transtype
 			){
 		UserInfo user=UserInfoContext.get();
 		if(StrUtil.isEmpty(marketplaceid)) {
 			marketplaceid=null;
 		}
 		if(StrUtil.isEmpty(transtype)) {
 			transtype=null;
 		}
 		List<Map<String, Object>> list = shipTransCompanyService.selectBycom(company, user.getCompanyid(), marketplaceid,transtype);
 		return Result.success(list);
 	}

 	// 级联操作渠道(选了渠道带出其它信息)
    @ApiOperation(value = "根据物流商获取渠道详细列表")
    @GetMapping("/getCompanyDetail")
 	public Result<List<Map<String, Object>>> getcomDetailAction(
 			@ApiParam("物流公司ID")@RequestParam String company,
			@ApiParam("渠道ID")@RequestParam String channel,
			@ApiParam("物流商类型")@RequestParam String transtype,
			@ApiParam("marketplaceid")@RequestParam String marketplaceid
 			) {
 		List<Map<String, Object>> list = shipTransCompanyService.selectBychannel(company, channel, marketplaceid,transtype);
 		if (list != null && list.size() > 0 && list.get(0) != null) {
 			for (int i = 0; i < list.size(); i++) {
 				String needday = "0";
 				Date nowdate = new Date();
 				if (list.get(i) != null && list.get(i).get("needday") != null) {
 					needday = list.get(i).get("needday").toString();
 					Calendar calendar = Calendar.getInstance();
 					calendar.setTime(nowdate);
 					calendar.add(Calendar.DAY_OF_MONTH, Integer.parseInt(needday));
 					nowdate = calendar.getTime();
 				}
 				list.get(i).put("days", nowdate);
 			}
 		}
 		return Result.success(list);
 	}
    
	@ApiOperation(value = "根据店铺站点查看物流渠道")
	@GetMapping(value = "/getCompanyTranstypeList")
	public Result<List<ShipTransCompany>> getCompanyTranstypeListAction(String transtype,String marketplaceid) {
		UserInfo user=UserInfoContext.get();
		Map<String,Object> maps=new HashMap<String,Object>(); 
		maps.put("shopid", user.getCompanyid());
		maps.put("transtype",transtype);
		maps.put("marketplaceid",marketplaceid);
		List<ShipTransCompany> list=shipTransCompanyService.getCompanyTranstypeList(maps);
	 
		for(ShipTransCompany item:list) {
			if(item.getLocation()!=null) {
				     Picture picture = pictureService.getById(item.getLocation());
					 item.setUploadpath(fileUpload.getPictureImage(picture.getLocation()));
			}
		
		}
		return Result.success(list);
	}
	
	@ApiOperation(value = "查看物流渠道")
	@PostMapping(value = "/listitem")
	public Result<IPage<ShipTransDetail>> listitemAction(@ApiParam("物流渠道")@RequestBody ShipTransDetailDTO dto) {
		UserInfo user=UserInfoContext.get();
		String marketplaceid=dto.getMarketplaceid();
		String transtype=dto.getTranstype();
		String priceunits=dto.getPriceunits();
		String company=dto.getCompany();
		String search=dto.getSearch();
		Map<String,Object> map=new HashMap<String,Object>();
		if(StrUtil.isEmpty(marketplaceid)) {
			marketplaceid=null;
		}
		if(StrUtil.isEmpty(transtype)) {
			transtype=null;
		}
		if(StrUtil.isEmpty(priceunits)) {
			priceunits=null;
		}
		if(StrUtil.isEmpty(company)) {
			company=null;
		}
		if(StrUtil.isEmpty(search)) {
			search=null;
		}else {
			search="%"+search+"%";
		}
		map.put("marketplaceid", marketplaceid);
		map.put("transtype", transtype);
		map.put("priceunits", priceunits);
		map.put("company", company);
		map.put("shopid", user.getCompanyid());
		map.put("search", search);
		List<ShipTransDetail> list=shipTransCompanyService.findListItem(map);
		IPage<ShipTransDetail> page = dto.getListPage(list);
		return Result.success(page);
	}
	
	
	@GetMapping(value = "getTransCompanyAPI")
	Result<ShipTransCompayAPI> shipTransDetialAction(String companyid )   {
		ShipTransCompayAPI companyapi = shipTransCompanyService.getCompanyApiById(companyid);
		return Result.success(companyapi);
	}
	
	@GetMapping(value = "shipTransDetial")
	Result<JSONObject> shipTransDetialAction(String companyid , String shipmentid,String ordernum)   {
		UserInfo user=UserInfoContext.get();
		return Result.success(shipTransCompanyZhihuiService.shipTransDetial(user,companyid,shipmentid,ordernum));
	}
	@GetMapping(value = "shipTransDetialShipment")
	Result<JSONObject> shipTransDetialShipmentAction(String companyid, String shipmentid)  {
		UserInfo user=UserInfoContext.get();
		return Result.success(shipTransCompanyZhihuiService.shipTransDetialShipment(user,companyid,shipmentid));
	}
	
	
}
