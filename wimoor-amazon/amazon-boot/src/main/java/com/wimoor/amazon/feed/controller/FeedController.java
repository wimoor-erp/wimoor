package com.wimoor.amazon.feed.controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.feed.pojo.entity.AmazonFeedStatus;
import com.wimoor.amazon.feed.pojo.entity.Submitfeed;
import com.wimoor.amazon.feed.service.ISubmitfeedService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;

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
@Api(tags = "Feed接口")
@RestController
@Component("feedController")
@RequestMapping("/api/v0/feed")
public class FeedController{
	 
	@Autowired
	ISubmitfeedService iSubmitfeedService;
	@Autowired
	IAmazonAuthorityService iAmazonAuthorityService;
	
	   @ApiOperation(value = "更新未出账账期")
	   @GetMapping("/process")
	   public Result<?> processAction() {
	       iAmazonAuthorityService.executTask(iSubmitfeedService);
	       return Result.judge(true);
	   }
	   
		@ApiOperation(value = "获取queue提交信息")
		@GetMapping(value = "submitfeedInfo")
		public Result<AmazonFeedStatus> submitfeedInfoAction(String queueid) {
			if (StrUtil.isEmpty(queueid)) {
				return null;
			}
			Submitfeed submitfeed = iSubmitfeedService.GetFeedSubmissionRequest(queueid);
			if (submitfeed == null || StrUtil.isEmpty(submitfeed.getFeedProcessingStatus())) {
				return null;
			}
			return Result.success(iSubmitfeedService.getFeedStatus(submitfeed.getFeedProcessingStatus()));
		}
 
		 @ApiOperation(value = "下载feedfile文件")
		 @GetMapping("/downloadFeedFile")
		 public void downloadFeedFileAction(@RequestParam String queueid,@RequestParam String filename, HttpServletResponse response) {
				try {
					// 创建新的Excel工作薄
					response.setContentType("application/force-download");// 设置强制下载不打开
					response.addHeader("Content-Disposition", "attachment;fileName="+filename);// 设置文件名
					ServletOutputStream fOut = response.getOutputStream();
					// 将数据写入Excel
					if(StrUtil.isBlankIfStr(queueid)) {
						throw new BizException("参数feed不能为空");
					}
					iSubmitfeedService.downloadFeedFile(fOut,queueid);
					fOut.flush();
					fOut.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
}

