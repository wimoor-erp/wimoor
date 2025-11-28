package com.wimoor.amazon.finances.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.finances.pojo.entity.AmzFinUserItem;
import com.wimoor.amazon.finances.service.IAmzFinUserItemService;
import com.wimoor.amazon.finances.service.impl.AmzFinSettlementFormulaServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.pojo.entity.BasePageQuery;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Component;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-10-27
 */
@Api(tags = "其它费用接口")
@SystemControllerLog("其它费用接口")
@RestController
@Component("aAmzFinUserItemController")
@RequiredArgsConstructor
@RequestMapping("/api/v1/amzFinUserItem")
public class AmzFinUserItemController {
     final IAmzFinUserItemService  amzFinUserItemService;
	    
	    @GetMapping("/findFinItemByUser")
	   	public  Result<List<AmzFinUserItem>> findFinItemListAction(){
	    	UserInfo user = UserInfoContext.get();
	   		return Result.success(amzFinUserItemService.getFinItemList(user.getCompanyid()));
	   	}
	    
	    
		@PostMapping("/getFinList")
		public Result<?> getFinListAction(@RequestBody BasePageQuery dto) {
		  	UserInfo user = UserInfoContext.get();
			return Result.success(amzFinUserItemService.findFinListByShopid( dto.getPage(),user.getCompanyid()));
		}
	    
		
		@SystemControllerLog("删除费用")
		@GetMapping("/deleteFinItem")
		public Result<?> deleteFinItemAction(String id) {
			AmzFinUserItem item = amzFinUserItemService.getById(id);
			item.setDisable(true);
			amzFinUserItemService.updateById(item);
			return Result.success();
		}

	 
		@GetMapping("/getFinListNoPage")
		public Result<?> getFinListNoPageAction() {
			UserInfo user = UserInfoContext.get();
			Map<String, String> map = AmzFinSettlementFormulaServiceImpl.findSysFinMap();
			List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
			for (Entry<String, String> entry : map.entrySet()) {
				HashMap<String, Object> mymap = new HashMap<String, Object>();
				mymap.put("name", entry.getValue());
				list.add(mymap);
			}
			list.addAll(amzFinUserItemService.findFinListByShopidNoPage(user.getCompanyid()));
			return Result.success(list);
		}
		
		@GetMapping("/getFinListWithoutPage")
		public Result<?> getFinListWithoutPageAction() {
			UserInfo user = UserInfoContext.get();
			return Result.success(amzFinUserItemService.findFinListByShopid(user.getCompanyid()));
		}

		

		@SystemControllerLog( "保存费用项目名称")
		@PostMapping("/saveFinItemName")
		public Result<?> saveFinItemNameAction(@RequestBody AmzFinUserItem finitem) {
			UserInfo user = UserInfoContext.get();
			String name =finitem.getName();
			String id =finitem.getId();
			LambdaQueryWrapper<AmzFinUserItem> query = new  LambdaQueryWrapper<AmzFinUserItem>();
			Map<String, String> baseTitle = AmzFinSettlementFormulaServiceImpl.findSysFinMap();
			Map<String, String> result = new HashMap<String, String>();
			if (StrUtil.isEmpty(name)) {
				throw new BizException("名字不能为空");
			} else {
				if (name.length() > 10) {
					throw new BizException("长度不能大于10个字符");
				}
				if (baseTitle.containsValue(name)) {
					throw new BizException("系統名字不能使用");
				}
			}
			query.eq(AmzFinUserItem::getShopid, user.getCompanyid());
			query.eq(AmzFinUserItem::getDisable, false);
			query.eq(AmzFinUserItem::getName, name);
			List<AmzFinUserItem> list = amzFinUserItemService.list(query);
			if (list.size() > 0) {
				if (StrUtil.isNotEmpty(name)) {
					for (AmzFinUserItem item : list) {
						if (!item.getId().equals(id)) {
							throw new BizException("不能添加相同财务项");
						}
					}
				} else {
					throw new BizException("不能添加相同财务项");
				}
			}
			if (StrUtil.isEmpty(id)) {
				AmzFinUserItem entity = new AmzFinUserItem();
				entity.setCreatedate(new Date());
				entity.setCreator(user.getId());
				entity.setIsused(false);
				entity.setDisable(false);
				entity.setShopid(user.getCompanyid());
				entity.setName(name);
				Boolean temp = amzFinUserItemService.save(entity);
				result.put("id", entity.getId());
				return Result.success(temp);
				 
			} else {
				AmzFinUserItem item = amzFinUserItemService.getById(id);
				item.setName(name);
				Boolean temp = amzFinUserItemService.updateById(item);
				return Result.success(temp);
			}
		}
	    
}

