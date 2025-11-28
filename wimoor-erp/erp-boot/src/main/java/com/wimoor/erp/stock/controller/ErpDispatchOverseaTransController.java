package com.wimoor.erp.stock.controller;


import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.GeneralUtil;
import com.wimoor.erp.stock.pojo.dto.OverseaReportDTO;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.stock.pojo.entity.ErpDispatchOverseaTrans;
import com.wimoor.erp.stock.service.IErpDispatchOverseaTransService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
@Api(tags = "海外仓调库单接口")
@RestController
@RequestMapping("/api/v1/inventory/dispatch/overseaTrans")
@RequiredArgsConstructor
public class ErpDispatchOverseaTransController {
	@Resource
	IErpDispatchOverseaTransService iErpDispatchOverseaTransService;
	    
    @Transactional
	@GetMapping("/getInfo")
	public Result<Map<String,Object>> getInfoAction(String id){
		return  Result.success(iErpDispatchOverseaTransService.getInfo(id));
	}
    
    @Transactional
  	@PostMapping("/saveInfo")
  	public Result<Boolean> saveInfoAction(@RequestBody ErpDispatchOverseaTrans trans){
    	UserInfo user = UserInfoContext.get();
    	ErpDispatchOverseaTrans one=iErpDispatchOverseaTransService.lambdaQuery().eq(ErpDispatchOverseaTrans::getFormid,trans.getFormid()).one();
    	if(one==null) {
    		trans.setOperator(new BigInteger(user.getId()));
    		trans.setOpttime(new Date());
    		return  Result.success(iErpDispatchOverseaTransService.save(trans));
    	}else {
    		trans.setId(one.getId());
    		trans.setOperator(new BigInteger(user.getId()));
    		trans.setOpttime(new Date());
    		return  Result.success(iErpDispatchOverseaTransService.updateById(trans));
    	}

	}


	@PostMapping(value = "/getShipFeeDetailReport")
	public Result<?> getShipmentFeeDetailReportAction(@RequestBody OverseaReportDTO dto)  {
		Map<String, Object> param = new HashMap<String, Object>();
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		param.put("shopid", shopid);
		String towarehouseid =dto.getTowarehouseid() ;
		if (StrUtil.isEmpty(towarehouseid)) {
			towarehouseid = null;
		}
		param.put("towarehouseid", towarehouseid);
		String warehouseid =dto.getWarehouseid();
		if (StrUtil.isEmpty(warehouseid)) {
			warehouseid = null;
		}
		param.put("warehouseid", warehouseid);
		String search =dto.getSearch();
		if (StrUtil.isNotEmpty(search)) {
			param.put("search", search.trim() + "%");
		} else {
			param.put("search", null);
		}
		String fromDate =dto.getFromdate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
		}
		String toDate =dto.getEnddate();
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("endDate", toDate.trim());
		} else {
			toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("endDate", toDate);
		}
		String company=dto.getCompany();
		String channel=dto.getChannel();
		if (StrUtil.isNotEmpty(company)) {
			param.put("company", company);
		} else {
			param.put("company", null);
		}
		if (StrUtil.isNotEmpty(channel)) {
			param.put("channel", channel);
		} else {
			param.put("channel", null);
		}
		param.put("ftype", dto.getFtype());
		IPage<Map<String, Object>> pagelist = iErpDispatchOverseaTransService.getShipFeeDetailReport(dto.getPage(),param);
		return Result.success(pagelist);
	}

	@PostMapping(value = "/downShipFeeDetailReportExcel")
	public void downShipFeeDetailReportExcelAction(@RequestBody OverseaReportDTO dto, HttpServletResponse response)   {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=Shipmenttemplate.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			UserInfo user=UserInfoContext.get();
			String shopid = user.getCompanyid();
			// 将数据写入Excel
			Map<String, Object> params = new HashMap<String, Object>();
			String towarehouseid =dto.getTowarehouseid();
			if (StrUtil.isEmpty(towarehouseid)) {
				towarehouseid = null;
			}
			params.put("towarehouseid", towarehouseid);
			String warehouseid =dto.getWarehouseid();
			if (StrUtil.isEmpty(warehouseid)) {
				warehouseid = null;
			}
			params.put("warehouseid", warehouseid);
			String search =dto.getSearch();
			if (StrUtil.isNotEmpty(search)) {
				params.put("search", search.trim() + "%");
			} else {
				params.put("search", null);
			}
			String fromDate =dto.getFromdate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StrUtil.isNotEmpty(fromDate)) {
				params.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -7);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				params.put("fromDate", fromDate);
			}
			String toDate =dto.getEnddate();
			if (StrUtil.isNotEmpty(toDate)) {
				params.put("endDate", toDate.trim());
			} else {
				toDate = GeneralUtil.formatDate(new Date(), sdf);
				params.put("endDate", toDate);
			}
			String isShip =dto.getIsShip();
			params.put("shopid", shopid);
			params.put("isShip", isShip);
			params.put("ftype", dto.getFtype());
			iErpDispatchOverseaTransService.setShipFeeDetailReport(workbook, params);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@PostMapping(value = "/getShipSkuFeeReport")
	public Result<?> getShipSkuFeeReportAction(@RequestBody OverseaReportDTO dto){
		Map<String, Object> param = new HashMap<String, Object>();
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		param.put("shopid", shopid);
		String towarehouseid = dto.getTowarehouseid();
		if (StrUtil.isEmpty(towarehouseid)) {
			towarehouseid = null;
		}
		param.put("towarehouseid", towarehouseid);
		String warehouseid = dto.getWarehouseid();
		if (StrUtil.isEmpty(warehouseid)) {
			warehouseid = null;
		}
		param.put("warehouseid", warehouseid);
		String search = dto.getSearch();
		if (StrUtil.isNotEmpty(search)) {
			param.put("search", search.trim() + "%");
		} else {
			param.put("search", null);
		}

		String fromDate =dto.getFromdate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
		}
		String toDate =dto.getEnddate() ;
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("endDate", toDate.trim());
		} else {
			toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("endDate", toDate);
		}
		String company=dto.getCompany();
		String channel=dto.getChannel();
		if (StrUtil.isNotEmpty(company)) {
			param.put("company", company);
		} else {
			param.put("company", null);
		}
		if (StrUtil.isNotEmpty(channel)) {
			param.put("channel", channel);
		} else {
			param.put("channel", null);
		}

		IPage<Map<String, Object>> pagelist = iErpDispatchOverseaTransService.transSKUFeeShared(dto.getPage(),param);
		return Result.success(pagelist);
	}


	@PostMapping(value = "/downShipFeeReportExcel")
	public void downShipFeeReportExcelAction(@RequestBody OverseaReportDTO dto, HttpServletResponse response) {
		try {
			// 创建新的Excel工作薄
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			response.setContentType("application/force-download");// 设置强制下载不打开
			response.addHeader("Content-Disposition", "attachment;fileName=Shipmenttemplate.xlsx");// 设置文件名
			ServletOutputStream fOut = response.getOutputStream();
			UserInfo user=UserInfoContext.get();
			String shopid = user.getCompanyid();
			// 将数据写入Excel
			Map<String, Object> params = new HashMap<String, Object>();
			String towarehouseid =dto.getTowarehouseid();
			if (StrUtil.isEmpty(towarehouseid)) {
				towarehouseid = null;
			}
			params.put("towarehouseid", towarehouseid);
			String warehouseid =dto.getWarehouseid();
			if (StrUtil.isEmpty(warehouseid)) {
				warehouseid = null;
			}
			params.put("warehouseid", warehouseid);
			String search =dto.getSearch();
			if (StrUtil.isNotEmpty(search)) {
				params.put("search", search.trim() + "%");
			} else {
				params.put("search", null);
			}
			String fromDate =dto.getFromdate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if (StrUtil.isNotEmpty(fromDate)) {
				params.put("fromDate", fromDate.trim());
			} else {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -7);
				fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
				params.put("fromDate", fromDate);
			}
			String toDate =dto.getEnddate();
			if (StrUtil.isNotEmpty(toDate)) {
				params.put("endDate", toDate.trim());
			} else {
				toDate = GeneralUtil.formatDate(new Date(), sdf);
				params.put("endDate", toDate);
			}
			String isShip =dto.getIsShip();
			params.put("shopid", shopid);
			params.put("isShip", isShip);
			iErpDispatchOverseaTransService.setShipFeeReport(workbook, params);
			workbook.write(fOut);
			workbook.close();
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@PostMapping(value = "/getShipFeeReport")
	public Result<IPage<Map<String, Object>>> getShipFeeReportAction(@RequestBody OverseaReportDTO dto) {
		Map<String, Object> param = new HashMap<String, Object>();
		UserInfo user=UserInfoContext.get();
		String shopid = user.getCompanyid();
		param.put("shopid", shopid);
		String towarehouseid =dto.getTowarehouseid();
		if (StrUtil.isEmpty(towarehouseid)) {
			towarehouseid = null;
		}
		param.put("towarehouseid", towarehouseid);
		String warehouseid = dto.getWarehouseid();
		if (StrUtil.isEmpty(warehouseid)) {
			warehouseid = null;
		}
		param.put("warehouseid", warehouseid);
		String search = dto.getSearch();
		if (StrUtil.isNotEmpty(search)) {
			param.put("search", search.trim() + "%");
		} else {
			param.put("search", null);
		}
		String fromDate = dto.getFromdate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (StrUtil.isNotEmpty(fromDate)) {
			param.put("fromDate", fromDate.trim());
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -7);
			fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
			param.put("fromDate", fromDate);
		}
		String toDate = dto.getEnddate();
		if (StrUtil.isNotEmpty(toDate)) {
			param.put("endDate", toDate.trim());
		} else {
			toDate = GeneralUtil.formatDate(new Date(), sdf);
			param.put("endDate", toDate);
		}
		String company=dto.getCompany();
		String channel=dto.getChannel();
		if (StrUtil.isNotEmpty(company)) {
			param.put("company", company);
		} else {
			param.put("company", null);
		}
		if (StrUtil.isNotEmpty(channel)) {
			param.put("channel", channel);
		} else {
			param.put("channel", null);
		}
		IPage<Map<String, Object>> pagelist = iErpDispatchOverseaTransService.getShipFeeReport(dto.getPage(),param);
		return Result.success(pagelist);
	}
}

