package com.wimoor.amazon.follow.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.follow.pojo.dto.ProductFollowListDTO;
import com.wimoor.amazon.follow.pojo.dto.ProductInfoFollowSaveDTO;
import com.wimoor.amazon.follow.pojo.entity.ProductInfoFollow;
import com.wimoor.amazon.follow.service.IProductInfoFollowService;
import com.wimoor.common.pojo.entity.BasePageQuery;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-06
 */
@Api(tags = "跟卖接口")
@RestController
@SystemControllerLog("跟卖")
@RequestMapping("/api/v1/follow/productInfoFollow")
public class ProductInfoFollowController {

	@Autowired
	IProductInfoFollowService iProductInfoFollowService;
	
	
	@PostMapping("/getProductFollowList")
	public Result<IPage<Map<String,Object>>> getListDataAction(@RequestBody ProductFollowListDTO dto) {
		UserInfo user = UserInfoContext.get();
		dto.setShopid(user.getCompanyid());
		if(StrUtil.isNotBlank(dto.getSearch())) {
			dto.setSearch("%"+dto.getSearch().trim()+"%");
		}
		IPage<Map<String,Object>> list=iProductInfoFollowService.findByCondition(dto);
		return Result.success(list);
	}
	
	//删除跟卖
	@PostMapping("/deleteProfuctFollow")
	public Result<?> deleteProfuctFollowAction(@RequestBody List<String> ids) {
		Map<String, Object> map = iProductInfoFollowService.deleteProfuctFollow(ids);
		return Result.success(map);
	}
	//删除跟卖
	@GetMapping("/refreshFulfillableProfuctFollow")
	public Result<?> refreshFulfillableProfuctFollowAction(  String id) {
		ProductInfoFollow follow = iProductInfoFollowService.getById(id);
		List<Map<String, Object>> result = iProductInfoFollowService.pushOnlineProfuctFollow(Arrays.asList(follow),"手动调用自动调整库存");
		return Result.success(result);
	}
	
	@GetMapping("/refreshFulfillable")
	public Result<?> refreshFulfillableAction( ) {
		iProductInfoFollowService.taskOnlineAsin();
		return Result.success("");
	}
	//保存跟卖
	@PostMapping("/saveProfuctFollow")
	public Result<?> saveProfuctFollowAction(@RequestBody List<ProductInfoFollowSaveDTO> dto) {
		UserInfo user = UserInfoContext.get();
		if(dto!=null&&dto.size()>0) {
			for(ProductInfoFollowSaveDTO item:dto) {
				item.setShopid(user.getCompanyid());
				item.setUserid(user.getId());
			}
		}
		List<Map<String, Object>> res = iProductInfoFollowService.saveProfuctFollow(dto);
		return Result.success(res);
	}
	private List<ProductInfoFollow>  getProductInfoFollowList(List<String> ids) {
		List<ProductInfoFollow> list=new ArrayList<ProductInfoFollow>();
		for(String id:ids) {
			ProductInfoFollow follow = iProductInfoFollowService.getById(id);
			if(follow!=null) {
				list.add(follow);
			}
		}
		return list;
	}
	//上架跟卖
	@PostMapping("/pushProfuctFollow")
	public Result<?> pushProfuctFollowAction(@RequestBody List<String> ids) {
		List<ProductInfoFollow> list=getProductInfoFollowList(ids);
		if(list!=null&&list.size()>0) {
			for(ProductInfoFollow item:list) {
				item.setStatusUpload("NEEDDISCOVERABLE");
				iProductInfoFollowService.updateById(item);
			}
		}
		return Result.success();
	}
	
	//重新调价
	@PostMapping("/priceProfuctFollow")
	public Result<?> priceProfuctFollowAction(@RequestBody List<String> ids) {
		List<ProductInfoFollow> list=getProductInfoFollowList(ids);
		if(list!=null&&list.size()>0) {
			for(ProductInfoFollow item:list) {
				item.setStatusPrice("NEEDPRICE");
				iProductInfoFollowService.updateById(item);
			}
		}
		return Result.success();
	}
	//立即跟卖
	@PostMapping("/salesProfuctFollow")
	public Result<?> salesProfuctFollowAction(@RequestBody List<String> ids) {
		List<ProductInfoFollow> list=getProductInfoFollowList(ids);
		if(list!=null&&list.size()>0) {
			for(ProductInfoFollow item:list) {
				item.setStatusUpload("NEEDONLINE");
				iProductInfoFollowService.updateById(item);
			}
		}
		return Result.success();
	}
	
	//暂停跟卖
	@PostMapping("/unsalesProfuctFollow")
	public Result<?> unsalesProfuctFollowAction(@RequestBody List<String> ids) {
		List<ProductInfoFollow> list=getProductInfoFollowList(ids);
		if(list!=null&&list.size()>0) {
			for(ProductInfoFollow item:list) {
				item.setStatusUpload("NEEDOFFLINE");
				iProductInfoFollowService.updateById(item);
			}
		} 
		return Result.success();
	}
	
	//更新跟卖
	@PostMapping("/updateProfuctFollow")
	public Result<?> updateProfuctFollowAction(@RequestBody ProductInfoFollowSaveDTO dto) {
		UserInfo user = UserInfoContext.get();
		dto.setShopid(user.getCompanyid());
		dto.setUserid(user.getId());
		Map<String, Object> res=iProductInfoFollowService.saveProfuctFollow(dto);
		return Result.success(res);
	}
	
	//下载
	@GetMapping("downloadDetailExport")
	public void getInvDayDetailExport(String authid,String marketplaceid, HttpServletResponse response)  {
		Map<String, Object> parameter = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String shopid=userinfo.getCompanyid();
		parameter.put("shopid", shopid);
		parameter.put("amazonauthid", authid);
		parameter.put("marketplaceid", marketplaceid);
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=ProductFollow"+System.currentTimeMillis() + ".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			// 将数据写入Excel
			iProductInfoFollowService.downloadDetailExport(workbook, parameter);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping(value = "/taskOrderAsin")
	public Result<?> taskOrderAsin(){
		iProductInfoFollowService.taskOrderAsin();
		return Result.success();
	}
	
	@GetMapping(value = "/downExcelTemp")
	public void downExcelTempAction(HttpServletResponse response) {
		Workbook workbook = null;
		ServletOutputStream fOut = null;
		try {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=amazonfollowtemplate.xlsx");// 设置文件名
			fOut = response.getOutputStream();
			InputStream is = new ClassPathResource("template/amazonfollowtemplate.xlsx").getInputStream();
			// 创建新的Excel工作薄
			workbook = WorkbookFactory.create(is);
			workbook.write(fOut);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(fOut != null) {
					fOut.flush();
					fOut.close();
				}
				if(workbook != null) {
					workbook.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@ApiOperation(value = "上传follow")
	@PostMapping(value="/uploadExcel",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result<?> uploadExcelAction(String authid,String marketplaceid,String timeid,
			@RequestParam("file")MultipartFile file){
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		if (file != null) {
			try {
				InputStream inputStream = file.getInputStream();
				Workbook workbook = WorkbookFactory.create(inputStream);
				Sheet sheet = workbook.getSheetAt(0);
				return Result.success(iProductInfoFollowService.uploadShipListByExcel(sheet, shopid,authid,marketplaceid,timeid));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (EncryptedDocumentException e) {
				e.printStackTrace();
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			}
		}
		return Result.success();
	}
	
	
	//更新跟卖
	@GetMapping("/findRecordList")
	public Result<?> findRecordListAction(String pid,String opttype) {
		List<Map<String, Object>> res = iProductInfoFollowService.findRecordList(pid, opttype);
		return Result.success(res);
	}
	
	//警告列表
	@GetMapping("/findWarningList")
	public Result<?> findWarningListAction(String currentpage,String pagesize,String authid,String marketplaceid) {
		UserInfo user=UserInfoContext.get();
		BasePageQuery query = new BasePageQuery();
		query.setCurrentpage(Integer.parseInt(currentpage));
		query.setPagesize(Integer.parseInt(pagesize));
		IPage<Map<String, Object>> res = iProductInfoFollowService.findWarningList(query.getPage(),user.getCompanyid(),authid,marketplaceid);
		return Result.success(res);
	}
	
	//警告列表
	@GetMapping("/findWarningListNum")
	public Result<?> findWarningListNumAction() {
		UserInfo user=UserInfoContext.get();
		Integer res = iProductInfoFollowService.findWarningNunmber(user.getCompanyid());
		return Result.success(res);
	}
	
	//清除警告列表
	@PostMapping("/ignoreWarningList")
	public Result<?> ignoreWarningListAction(@RequestBody List<String> ids) {
		iProductInfoFollowService.ignoreWarningList(ids);
		return Result.success();
	}
	//警告列表
		@GetMapping("/ignoreWarningListAll")
		public Result<?> ignoreWarningListAllAction(String authid,String marketplaceid) {
			UserInfo user=UserInfoContext.get();
			BasePageQuery query = new BasePageQuery();
			query.setCurrentpage(1);
			query.setPagesize(10000000);
			IPage<Map<String, Object>> res = iProductInfoFollowService.findWarningList(query.getPage(),user.getCompanyid(),authid,marketplaceid);
			if(res!=null&&res.getRecords()!=null&&res.getRecords().size()>0) {
				ArrayList<String> ids = new ArrayList<String>();
				for(Map<String, Object> item:res.getRecords()) {
					ids.add(item.get("pid").toString()); 
				}
				iProductInfoFollowService.ignoreWarningList(ids);
			}
			return Result.success();
		}
	
	//清除警告列表
	@PostMapping("/changeProfuctFollowTime")
	public Result<?> changeProfuctFollowTimeAction(@RequestBody List<String> ids) {
		iProductInfoFollowService.changeProfuctFollowTime(ids);
		return Result.success();
	}
	
	@PostMapping("/changeFollow/{ftype}")
	public Result<?> changeProfuctFollowTimeAction(@PathVariable String ftype,@RequestBody List<String> ids) {
		iProductInfoFollowService.changeProfuctFollow(ftype,ids);
		return Result.success();
	}
	
	//全局修改发货天数
	@GetMapping("/changeAllCycle")
	public Result<?> changeAllCycleAction(String cycle) {
		UserInfo user=UserInfoContext.get();
		iProductInfoFollowService.changeAllCycle(user.getCompanyid(),cycle);
		return Result.success();
	}
	
	
	@GetMapping("/syncProductInfo")
	public Result<?> syncProductInfoAction() {
		UserInfo user=UserInfoContext.get();
		iProductInfoFollowService.syncProductInfo(user.getCompanyid(),user.getId());
		return Result.success();
	}

	
}

