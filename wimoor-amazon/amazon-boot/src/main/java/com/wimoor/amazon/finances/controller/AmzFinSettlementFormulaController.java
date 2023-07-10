package com.wimoor.amazon.finances.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wimoor.amazon.finances.service.IAmzFinSettlementFormulaService;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@Api(tags = "SKU利润公式")
@SystemControllerLog("SKU利润公式")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/fin/settlementFormula")
public class AmzFinSettlementFormulaController {
final IAmzFinSettlementFormulaService iAmzFinSettlementFormulaService;
@GetMapping("/loadformula")
public Result<?> loadformulaAction() {
	UserInfo user = UserInfoContext.get();
	return Result.success(iAmzFinSettlementFormulaService.getAmzFinSettlementFormulaConvert(user.getCompanyid())) ;
}

@SystemControllerLog( "保存公式")
@GetMapping("/formulaSave")
public  Result<?> formulaSaveAction(String formuladata,String pricetype) {
	UserInfo user = UserInfoContext.get();
	return  Result.success(iAmzFinSettlementFormulaService.saveformulaData(user, formuladata,pricetype));
}

@GetMapping("/getformulaTitle")
public Result<?> getformulaTitleAction() {
	UserInfo user = UserInfoContext.get();
	return Result.success(iAmzFinSettlementFormulaService.getformulaTitle(user.getCompanyid()));
}

}
