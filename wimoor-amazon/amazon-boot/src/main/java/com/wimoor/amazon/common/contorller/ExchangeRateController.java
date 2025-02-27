package com.wimoor.amazon.common.contorller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.common.pojo.entity.ExchangeRateCustomer;
import com.wimoor.amazon.common.service.IExchangeRateService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "汇率接口")
@RestController
@RequestMapping("/api/v1/exchangeRate")
@RequiredArgsConstructor
public class ExchangeRateController {
final IExchangeRateService iExchangeRateService;

@GetMapping("/getMyCurrencyRate")
public Result<?> getMyCurrencyRate(String byday) {
	UserInfo user = UserInfoContext.get();
	return Result.success(iExchangeRateService.getMyCurrencyRate(user.getCompanyid(),byday)) ;
}

@SystemControllerLog( "保存币种")
@PostMapping("/saveMyCurrencyRate")
public  Result<?> saveMyCurrencyRate(@RequestBody ExchangeRateCustomer rate) {
	UserInfo user = UserInfoContext.get();
	iExchangeRateService.saveMyCurrencyRate(user, rate);
	return  Result.success();
}


@GetMapping("/downFinRate")
public void downDataExcelByRateAction(String byday, HttpServletResponse response, Model model)  {
	// 创建新的Excel工作薄
	SXSSFWorkbook workbook = new SXSSFWorkbook();
	// 将数据写入Excel
	UserInfo user = UserInfoContext.get();
	List<Map<String, Object>> list = iExchangeRateService.getMyCurrencyRate(user.getCompanyid(),byday);
	iExchangeRateService.setExcelBook(workbook, list);
	try {
		response.setContentType("application/force-download");// 设置强制下载不打开
		response.addHeader("Content-Disposition", "attachment;fileName=CommodityRevenueFinRate" + System.currentTimeMillis() + ".xlsx");// 设置文件名
		ServletOutputStream fOut = response.getOutputStream();
		workbook.write(fOut);
		workbook.close();
		fOut.flush();
		fOut.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
}

}
