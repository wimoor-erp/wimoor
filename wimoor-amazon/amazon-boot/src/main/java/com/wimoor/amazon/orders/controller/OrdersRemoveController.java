package com.wimoor.amazon.orders.controller;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersRemoveDTO;
import com.wimoor.amazon.orders.pojo.vo.AmazonOrdersRemoveVo;
import com.wimoor.amazon.orders.service.IAmzOrderRemovesService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.common.user.UserLimitDataType;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 订单抓取 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Api(tags = "订单移除接口")
@RestController
@RequestMapping("/api/v0/orders/remove")
public class OrdersRemoveController{
	 
    @Resource
    IAmzOrderRemovesService iAmzOrderRemovesService;

	@ApiOperation("订单移除列表")
	@PostMapping("/removelist")
	public Result<IPage<AmazonOrdersRemoveVo>> getRemovelistAction(@RequestBody AmazonOrdersRemoveDTO condition) {
		UserInfo userinfo = UserInfoContext.get();
		if(userinfo.isLimit(UserLimitDataType.operations)) {
			condition.setOwner(userinfo.getId());
		}
		if(StrUtil.isNotBlank(condition.getSearch())&&!condition.getSearch().contains("%")) {
			condition.setSearch("%"+condition.getSearch().trim()+"%");
		}else {
			condition.setSearch(null);
		}
		IPage<AmazonOrdersRemoveVo> list = iAmzOrderRemovesService.selectRemovesList(condition);
		return Result.success(list);
	}
	  
	  	
	  	@ApiOperation("订单退货列表导出")
	  	@GetMapping("/downloadRemovelist")
	    public  void downloadlistAction(
	    		AmazonOrdersRemoveDTO condition,
	    		 HttpServletResponse response){
	  		try {
		  		SXSSFWorkbook workbook = new SXSSFWorkbook();
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=ReturnsReport.xlsx");// 设置文件名
				UserInfo userinfo = UserInfoContext.get();
				if(userinfo.isLimit(UserLimitDataType.operations)) {
					condition.setOwner(userinfo.getId());
				}
				iAmzOrderRemovesService.downloadRemoveslist(workbook,condition);
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

