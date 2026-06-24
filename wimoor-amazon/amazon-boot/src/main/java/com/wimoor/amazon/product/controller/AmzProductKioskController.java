package com.wimoor.amazon.product.controller;


import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.product.service.IDataKioskRequestService;
import com.wimoor.common.result.Result;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p>
 * 流量报表 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-28
 */
@Api(tags = "kiosk接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report/product/kiosk")
public class AmzProductKioskController {

	final IDataKioskRequestService iDataKioskRequestService;
	final IAmazonAuthorityService amazonAuthorityService;

	@GetMapping("/createQuery")
	public Result<?> createQueryAction(String amazonAuthId,String marketplaceId)  {
		return iDataKioskRequestService.createQuery(amazonAuthId, marketplaceId);
    }

	@GetMapping("/getQueries")
	public Result<?> getQueriesAction(String amazonAuthId,String queryId)  {
		return iDataKioskRequestService.getQueries(amazonAuthId, queryId);
    }

	@GetMapping("/getQuery")
	public Result<?> getQueryAction(String amazonAuthId,String queryId)  {
		return iDataKioskRequestService.getQuery(amazonAuthId, queryId);
	}

	@GetMapping("/getDocument")
	public Result<?> getDocumentAction(String amazonAuthId,String documentId)  {
		return iDataKioskRequestService.getDocument(amazonAuthId, documentId);
	}

	@GetMapping("/refresh")
	public Result<?> refreshAction()  {
		iDataKioskRequestService.refresh();
		return Result.success("刷新成功");
	}

	@GetMapping("/requestReport")
	public Result<?> requestReportAction()  {
		List<AmazonAuthority> authlist = amazonAuthorityService.getAllAuth();
		for(AmazonAuthority amazonAuthority:authlist) {
			iDataKioskRequestService.runApi(amazonAuthority);
		}
		return Result.success("请求成功");
	}

}

