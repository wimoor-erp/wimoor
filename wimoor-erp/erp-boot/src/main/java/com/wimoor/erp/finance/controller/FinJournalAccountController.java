package com.wimoor.erp.finance.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.pojo.entity.BasePageQuery;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.finance.pojo.dto.FinQueryDTO;
import com.wimoor.erp.finance.pojo.entity.FinJournalAccount;
import com.wimoor.erp.finance.service.IFinJournalAccountService;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;


@Api(tags = "财务接口")
@RestController
@SystemControllerLog( "财务记账")
@RequestMapping("/api/v1/fin/journal")
@RequiredArgsConstructor
public class FinJournalAccountController {
	final IFinJournalAccountService finJournalAccountService;
	
	@SystemControllerLog( "记一笔账")
	@PostMapping("/save")
	@Transactional
	public Result<Map<String, Object>> saveDataAction(@RequestBody FinJournalAccount faccount)  {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		// 系统当前日期
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		String operator = userinfo.getId();
		faccount.setShopid(shopid);
		faccount.setOpttime(date);
		faccount.setOperator(operator);
		Boolean result = false;
		String msg="";
		if (faccount.idIsNULL()) {
			faccount.setCreator(operator);
			faccount.setCreatetime(date);
		} 
		if(StrUtil.isEmpty(faccount.getId())){faccount.setId(null);}
		result = finJournalAccountService.saveData(faccount, userinfo);
		if(result){
			msg="操作成功!";
		}else{
			msg="操作失败!";
		}
		map.put("msg", msg);
		map.put("faccount", faccount);
		return Result.success(map);
	}
	
	@SystemControllerLog( "撤销一笔账")
	@GetMapping("/cancel")
	@Transactional
	public Result<?> cancelDataAction(String id)  {
		UserInfo userinfo = UserInfoContext.get();
		// 系统当前日期
		  finJournalAccountService.cancelData(id, userinfo);
 
		return Result.success();
	}
	
	@PostMapping("/list")
	public Result<IPage<Map<String, Object>>> getListData(@RequestBody FinQueryDTO dto) {
		UserInfo userinfo = UserInfoContext.get();
		Map<String, Object> param = new HashMap<String, Object>();
		String shopid = userinfo.getCompanyid();
		String search = dto.getSearch();
		
		if (StrUtil.isEmpty(search)) {
			search = null;
		} else {
			search ="%"+ search.trim() + "%";
		}
		param.put("shopid", shopid);
		param.put("search", search);
		String project=dto.getProject();
		if(StrUtil.isNotEmpty(project)){
			param.put("projectid", project);
		}else{
			param.put("projectid", null);
		}
		String acc=dto.getAcc();
		if(StrUtil.isNotEmpty(acc)){
			param.put("acc", acc);
		}else{
			param.put("acc", null);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = dto.getFromDate();
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -30);
			fromDate = sdf.format(cal.getTime());
			param.put("fromDate", fromDate);
		}
		String toDate = dto.getToDate();
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("endDate", toDate.trim());
		} else {
			toDate = sdf.format(new Date());
			param.put("endDate", toDate);
		}
		IPage<Map<String, Object>> list = finJournalAccountService.findByCondition(dto.getPage(),param);
		return Result.success(list);
	}

	
 
	@PostMapping(value = "outinsum")
	public Result<Map<String,Object>> outInSumAction(@RequestBody FinQueryDTO dto)  {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		Map<String, Object> param = new HashMap<String, Object>();
		String acc=dto.getAcc();
		if(StrUtil.isNotEmpty(acc)){
			param.put("acc", acc);
		}else{
			param.put("acc", null);
		}
		param.put("shopid", shopid);
		param.put("search", null);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date nowdate=new Date();
		String dateStr = GeneralUtil.formatDate(nowdate, sdf);
		String fromDate = dateStr.substring(0, dateStr.length()-2)+"01";
		String toDate = dateStr+" 23:59:59";
		param.put("fromDate", fromDate);
		param.put("endDate", toDate);
		Map<String,Object> result = finJournalAccountService.findSumByCondition(param);
		if(result!=null){
		result.put("insum",GeneralUtil.formatterQuantity(((BigDecimal)result.get("insum"))));
		result.put("outsum",GeneralUtil.formatterQuantity(((BigDecimal)result.get("outsum"))));
		}
		return Result.success(result);
	}
	
	@PostMapping(value = "paymentsum")
	public Result<List<Map<String,Object>>> paymentSumAction(@RequestBody FinQueryDTO dto) {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		String sumType = dto.getSumType();
		Map<String, Object> param = new HashMap<String, Object>();
		String search = dto.getSearch();
		if (StrUtil.isEmpty(search)) {
			search = null;
		} else {
		   	search ="%"+ search.trim() + "%";
		}
		param.put("shopid", shopid);
		param.put("search", search);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate = dto.getFromDate();
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -30);
			fromDate = sdf.format(cal.getTime());
			param.put("fromDate", fromDate);
		}
		String toDate = dto.getToDate();
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("endDate", toDate.trim());
		} else {
			toDate = sdf.format(new Date());
			param.put("endDate", toDate);
		}
		param.put("sumType", sumType);

 
		List<Map<String,Object>> list = finJournalAccountService.paymentSumByCondition(param);
		return Result.success(list);
	}
	
	 
	@PostMapping(value = "findDetialByCondition")
	public Result<List<Map<String,Object>>> findDetialByConditionAction(@RequestBody FinQueryDTO dto)  {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		Map<String, Object> param = new HashMap<String, Object>();
		String search =dto.getSearch();
		if (StrUtil.isEmpty(search)) {
			search = null;
		} else {
			search ="%"+ search.trim() + "%";
		}
		param.put("shopid", shopid);
		param.put("search", search);
		String project=dto.getProject();
		if(StrUtil.isNotEmpty(project)){
			param.put("projectid", project);
		}else{
			param.put("projectid", null);
		}
		String acc=dto.getAcc();
		if(StrUtil.isNotEmpty(acc)){
			param.put("acc", acc);
		}else{
			param.put("acc", null);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String createdate = dto.getCreatedate();
		if (StrUtil.isNotEmpty(createdate)) {
			param.put("createtime", createdate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -30);
			createdate = sdf.format(cal.getTime());
			param.put("createtime", createdate);
		}
		List<Map<String,Object>> list = finJournalAccountService.findDetialByCondition(param);
		return Result.success(list);
	}
	
 
	@PostMapping(value = "summaryTable")
	public Result<IPage<Map<String,Object>>> summaryTableAction(@RequestBody BasePageQuery query)  {
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		List<Map<String,Object>> list = finJournalAccountService.paymentSum(shopid);
		return Result.success(query.getListPage(list));
	}
	
	@PostMapping(value = "/downExcelDate")
	public void downExcelDateAction(@RequestBody FinQueryDTO dto, HttpServletResponse response){
		// 创建新的Excel工作薄
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		// 将数据写入Excel
		Map<String,Object> param = new HashMap<String, Object>();
		UserInfo userinfo = UserInfoContext.get();
		String shopid = userinfo.getCompanyid();
		String search =dto.getSearch();
		if (StrUtil.isEmpty(search)) {
			search = null;
		} else {
			search = search.trim() + "%";
		}
		param.put("shopid", shopid);
		param.put("search", search);
		String project=dto.getProject();
		if(StrUtil.isNotEmpty(project)){
			param.put("projectid", project);
		}else{
			param.put("projectid", null);
		}
		String acc=dto.getAcc();
		if(StrUtil.isNotEmpty(acc)){
			param.put("acc", acc);
		}else{
			param.put("acc", null);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fromDate =dto.getFromDate();
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -30);
			fromDate = sdf.format(cal.getTime());
			param.put("fromDate", fromDate);
		}
		String toDate = dto.getToDate();
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("endDate", toDate.trim());
		} else {
			toDate = sdf.format(new Date());
			param.put("endDate", toDate);
		}
		finJournalAccountService.setExcelBook(workbook, param);
		
		try {
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=fAccount"+System.currentTimeMillis()+".xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

 
	@GetMapping("/getPieData")
	public Result<List<Map<String,Object>>>  findPieDataAction(String acc,String year,String month){
		UserInfo userinfo = UserInfoContext.get();
		if(StrUtil.isNotEmpty(year)){
			if(StrUtil.isEmpty(month)){
				month=null;
			}
			if(StrUtil.isEmpty(acc)){
				acc=null;
			}
			List<Map<String,Object>> list=finJournalAccountService.findAllAcountByType(userinfo,acc,year,month);
			return Result.success(list);
		}else{
			return Result.failed();
		}
		
	}
	
	@GetMapping("/getLineData")
	public  Result<Map<Integer,Map<String,Object>>> findLineDataAction(String acc,String year ,String month){
		UserInfo userinfo = UserInfoContext.get();
		if(StrUtil.isNotEmpty(year) ){
			if(StrUtil.isEmpty(month)){
				month=null;
			}
			if(StrUtil.isEmpty(acc)){
				acc=null;
			}
			Map<Integer,Map<String,Object>> list=finJournalAccountService.findLineDataByYear(userinfo,acc,year,month);
			return Result.success(list);
		}else{
			return Result.failed();
		}
	}
	
}
 
