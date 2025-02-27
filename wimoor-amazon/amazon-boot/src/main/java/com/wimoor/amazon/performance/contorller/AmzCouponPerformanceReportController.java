package com.wimoor.amazon.performance.contorller;

import com.wimoor.amazon.performance.pojo.dto.PerformanceReportListDTO;
import com.wimoor.amazon.performance.service.IAmzCouponPerformanceReportService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Coupon接口")
@RestController
@RequestMapping("/api/v0/rpt/performance/coupon")
@RequiredArgsConstructor
public class AmzCouponPerformanceReportController {
    final IAmzCouponPerformanceReportService iAmzCouponPerformanceReportService;
    @ApiOperation(value = "刷新接受信息的Destination")
    @PostMapping("/list")
    public Result<?> listAction(@RequestBody PerformanceReportListDTO dto ){
        UserInfo userInfo = UserInfoContext.get();
        dto.setShopid(userInfo.getCompanyid());
        return Result.success(iAmzCouponPerformanceReportService.findByCondition(dto));
    }
}