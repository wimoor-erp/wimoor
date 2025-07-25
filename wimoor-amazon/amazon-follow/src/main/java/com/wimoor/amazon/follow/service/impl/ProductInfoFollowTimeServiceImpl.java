package com.wimoor.amazon.follow.service.impl;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.follow.mapper.ProductInfoFollowTimeMapper;
import com.wimoor.amazon.follow.pojo.entity.ProductInfoFollow;
import com.wimoor.amazon.follow.pojo.entity.ProductInfoFollowTime;
import com.wimoor.amazon.follow.service.IProductInfoFollowService;
import com.wimoor.amazon.follow.service.IProductInfoFollowTimeService;
import com.wimoor.common.mvc.BizException;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-07
 */
@Service
public class ProductInfoFollowTimeServiceImpl extends ServiceImpl<ProductInfoFollowTimeMapper, ProductInfoFollowTime> implements IProductInfoFollowTimeService {
    @Autowired
	IProductInfoFollowService iProductInfoFollowService;
	@Override
	public List<ProductInfoFollowTime> findByCondition(String shopid) {
		return this.lambdaQuery().eq(ProductInfoFollowTime::getShopid, shopid).list();
	}
	public static boolean distance10Minutes(LocalTime localTime, LocalTime localTime2) {
		if(localTime==null||localTime2==null)return false;
		long second = Duration.between(localTime, localTime2).getSeconds();
		second=second>0?second:second*-1;
		return second/60>10 ? false:true;
	}
	public Integer addItem( ProductInfoFollowTime item) {
		     if( distance10Minutes(item.getStarttime(),item.getEndtime())){
		    	 throw new BizException("开始时间与结束时间必须相隔十分钟");
		     }
			 ProductInfoFollowTime old = this.lambdaQuery().eq(ProductInfoFollowTime::getShopid, item.getShopid()).eq(ProductInfoFollowTime::getName, item.getName()).one();
			 if(old!=null) {
				 throw new BizException("名称不能重复");
			 }
			 return this.baseMapper.insert(item);
		 }
	
   public Integer updateItem( ProductInfoFollowTime item) {
		   if( distance10Minutes(item.getStarttime(),item.getEndtime())){
		    	 throw new BizException("开始时间与结束时间必须相隔十分钟");
		     }
			 ProductInfoFollowTime old = this.lambdaQuery().eq(ProductInfoFollowTime::getShopid, item.getShopid()).eq(ProductInfoFollowTime::getName, item.getName()).one();
			 if(old!=null&&!old.getId().equals(item.getId())) {
				 throw new BizException("名称不能重复");
			 }
			 return this.baseMapper.updateById(item);
	}
   
   public Integer deleteItem(String id) {
		 ProductInfoFollowTime old = this.baseMapper.selectById(id);
		 if(old==null) {
			 throw new BizException("未找到要删除的记录");
		 }
		 LambdaQueryWrapper<ProductInfoFollow> query = new LambdaQueryWrapper<ProductInfoFollow>();
		 query.eq(ProductInfoFollow::getTimeid, id);
		 List<ProductInfoFollow> list = iProductInfoFollowService.list(query);
		 if(list!=null&&list.size()>0) {
			 throw new BizException("已经被使用，无法删除");
		 }
		 return this.baseMapper.deleteById(id);
   }

}
