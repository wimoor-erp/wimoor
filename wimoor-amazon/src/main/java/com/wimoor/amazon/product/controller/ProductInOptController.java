package com.wimoor.amazon.product.controller;


import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wimoor.amazon.product.pojo.entity.ProductInOpt;
import com.wimoor.amazon.product.pojo.entity.ProductPrice;
import com.wimoor.amazon.product.service.IProductInOptService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 产品信息 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
@RestController
@RequiredArgsConstructor
@Component("productInOptController")
@RequestMapping("/api/v1/report/product/productInOpt")
public class ProductInOptController {
	final IProductInOptService iProductInOptService;
    @GetMapping("/refreshAllProductAdv")
    public Result<String> refreshAllProductAdvAction() {
    	new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				iProductInOptService.refreshAllProductAdv();
			}
    	}).start();
        return Result.success("true");
    }
    
    @GetMapping("/updateOptMsku")
    public Result<String> updateOptMskuAction(String pid,String msku) {
    	UserInfo user = UserInfoContext.get();
    	ProductInOpt opt = iProductInOptService.getById(pid);
    	if(opt!=null) {
    		if(StrUtil.isNotEmpty(msku)) {
    			opt.setMsku(msku);
    			opt.setOperator(new BigInteger(user.getId()));
    			LocalDateTime lastupdate=LocalDateTime.now();
				opt.setLastupdate(lastupdate);
				iProductInOptService.updateById(opt);
				return Result.success("isok");
    		}
    	}else {
    		//做新增
    		if(StrUtil.isNotEmpty(msku)) {
    			opt=new ProductInOpt();
    			opt.setPid(new BigInteger(pid));
    			opt.setMsku(msku);
    			opt.setDisable(false);
    			opt.setOperator(new BigInteger(user.getId()));
    			LocalDateTime lastupdate=LocalDateTime.now();
				opt.setLastupdate(lastupdate);
				iProductInOptService.save(opt);
				return Result.success("isok");
    		}
    	}
    	return Result.success("fail");
        
    }
    
    @GetMapping("/updateOptProfitId")
	public Result<Map<String,Object>> updateOptProfitIdAction(String profitid,String pid) {
		Map<String,Object> maps=new HashMap<String, Object>();
		ProductInOpt productOpt = iProductInOptService.getById(pid);
		Boolean result=false;
		if(productOpt!=null) {
			if(StrUtil.isNotEmpty(profitid)) {
				productOpt.setProfitid(new BigInteger(profitid));
			}else {
				productOpt.setProfitid(null);
			}
			productOpt.setLastupdate(LocalDateTime.now());
			result=iProductInOptService.updateById(productOpt);
		}
		if(result) {
			maps.put("isok", "true");
		}else {
			maps.put("isok", "false");
		}
		return Result.success(maps);
	}
    
    @GetMapping("/getProRemarkHis")
	public Result<List<Map<String,Object>>> getProRemarkHisAction(String  pid,String ftype) {
		return Result.success(iProductInOptService.getProRemarkHis(pid, ftype));
	}
    
    @GetMapping("/getOptStatusById")
   	public Result<Integer> getOptStatusByIdAction(String  pid) {
    	ProductInOpt opt = iProductInOptService.getById(pid);
    	if(opt!=null) {
    		return Result.success(opt.getStatus());
    	}else {
    		return Result.success(null);
    	}
   	}
    
    @GetMapping("/updateOptStatus")
   	public Result<String> getOptStatusByIdAction(String pid,String status) {
    	UserInfo user = UserInfoContext.get();
    	ProductInOpt opt = iProductInOptService.getById(pid);
    	if(opt!=null) {
    		if("delete".equals(status)) {
    			opt.setStatus(null);
    		}else {
    			opt.setStatus(Integer.parseInt(status));
    		}
    		opt.setLastupdate(LocalDateTime.now());
    		opt.setOperator(new BigInteger(user.getId()));
    		iProductInOptService.updateById(opt);
    		return Result.success("ok");
    	}else {
    		opt=new ProductInOpt();
    		opt.setPid(new BigInteger(pid));
    		if("delete".equals(status)) {
    			opt.setStatus(null);
    		}else {
    			opt.setStatus(Integer.parseInt(status));
    		}
    		opt.setLastupdate(LocalDateTime.now());
    		opt.setOperator(new BigInteger(user.getId()));
    		iProductInOptService.save(opt);
    		return Result.success("ok");
    	}
   	}
    
    @GetMapping("/findPriceById")
   	public  Result<List<ProductPrice>> findPriceByIdAction(String  pid) {
   		return Result.success(iProductInOptService.findPrice(pid));
   	}
    
    @GetMapping("/saveProductTags")
    public Result<List<Map<String,Object>>> saveProductTagsAction(String pid,String ids) {
    	UserInfo user = UserInfoContext.get();
    	List<Map<String, Object>> result = iProductInOptService.saveTagsByPid(pid, ids, user.getId());
    	return Result.success(result);
    }
    
    @GetMapping("/findProductTags")
    public Result<String> findProductTagsAction(String pid) {
    	String strs=iProductInOptService.findProductTagsByPid(pid);
    	return Result.success(strs);
    }
    
    
   
    
}

