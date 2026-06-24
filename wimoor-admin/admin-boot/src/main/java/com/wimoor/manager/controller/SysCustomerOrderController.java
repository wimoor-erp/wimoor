package com.wimoor.manager.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.manager.mapper.SysCustomerOrderMapper;
import com.wimoor.manager.pojo.dto.CustomerOrderListDTO;
import com.wimoor.manager.pojo.dto.CustomerOrderQueryDTO;
import com.wimoor.manager.pojo.entity.SysCustomerOrder;
import com.wimoor.manager.service.ISysCustomerOrderService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 客户订单表 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-09-27
 */
@Api(tags = "客户订单接口")
@RestController
@RequestMapping("/api/v1/sysCustomerOrder")
@RequiredArgsConstructor
public class SysCustomerOrderController {
	final ISysCustomerOrderService iSysCustomerOrderService;
	final SysCustomerOrderMapper sysCustomerOrderMapper;
	
	@PostMapping("/list")
	public Result<IPage<CustomerOrderListDTO>> list(@RequestBody CustomerOrderQueryDTO query)  {
		UserInfo userInfo= UserInfoContext.get();
        query.setShopid(userInfo.getCompanyid());
		return Result.success(sysCustomerOrderMapper.selectOrderList(query.getPage(), query));
	}

	@PostMapping("/listAll")
	public Result<IPage<CustomerOrderListDTO>> listAll(@RequestBody CustomerOrderQueryDTO query)  {
		UserInfo userInfo= UserInfoContext.get();
		return Result.success(sysCustomerOrderMapper.selectOrderAllList(query.getPage(), query));
	}
	
	@GetMapping("/detail")
	public Result<SysCustomerOrder> detail(String id)  {
	     return Result.success(iSysCustomerOrderService.getById(id));
	}
	
	@PostMapping("/save")
	public Result<?> save(@RequestBody SysCustomerOrder order)  {
	     return Result.success(iSysCustomerOrderService.save(order));
	}
	
	@PostMapping("/update")
	public Result<?> update(@RequestBody SysCustomerOrder order)  {
	     return Result.success(iSysCustomerOrderService.updateById(order));
	}
	
	@PostMapping("/delete")
	public Result<?> delete(@RequestBody SysCustomerOrder order)  {
	     return Result.success(iSysCustomerOrderService.removeById(order.getId()));
	}
}