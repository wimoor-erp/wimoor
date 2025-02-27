package com.wimoor.erp.common.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.erp.common.mapper.SummaryDataMapper;
import com.wimoor.erp.common.pojo.entity.SummaryData;
import com.wimoor.erp.common.service.ISummaryDataService;


@Service("summaryDataService")
public class SummaryDataServiceImpl extends ServiceImpl<SummaryDataMapper,SummaryData> implements ISummaryDataService {
		 
 
 
		public void refreshProNum() {
			  this.baseMapper.refreshProNum();
		}

		public List<SummaryData> findMainReport(String shopid, String ftype) {
			return this.baseMapper.findMainReport(shopid, ftype);
		}
		
		public void summary(Set<String> shopset){
			for(String shopid:shopset){
				summarySalesOrders(shopid);
			}
		}
		public void refreshProRate(){
			this.baseMapper.refreshProRate();
		}
		
		public void summaryProduct(){
			this.baseMapper.refreshProNum();
		}
		
		public Integer summarySalesOrders(String shopid){
			QueryWrapper<SummaryData> queryWrapper = new QueryWrapper<SummaryData>();
			queryWrapper.eq("shopid", shopid);
			queryWrapper.eq("ftype","main-2-sales");
			this.baseMapper.delete(queryWrapper);
			queryWrapper.eq("ftype","main-2-orders");
			this.baseMapper.delete(queryWrapper);
			Calendar cale = Calendar.getInstance();  
		             cale.add(Calendar.MONTH, 0);  
		             cale.set(Calendar.DAY_OF_MONTH, 1);  
			  Map<String, Object> result = this.baseMapper.monthSalesOrders(shopid, cale.getTime());
			  Integer sales=0;
			  
			  if(result!=null&&result.get("quantity")!=null) {
				  sales=Integer.parseInt(result.get("quantity").toString());
			  }
			SummaryData sumtype=new SummaryData();
			sumtype.setFtype("main-2-sales");
			sumtype.setShopid(shopid);
			sumtype.setUpdatetime(new Date());
			sumtype.setValue(new BigDecimal(sales.toString()));
			this.baseMapper.insert(sumtype);
			  Integer orders=0;
			 if(result!=null&&result.get("ordernumber")!=null) {
				 orders=Integer.parseInt(result.get("ordernumber").toString());
			  }
			sumtype=new SummaryData();
			sumtype.setFtype("main-2-orders");
			sumtype.setShopid(shopid);
			sumtype.setUpdatetime(new Date());
			sumtype.setValue(new BigDecimal(orders.toString()));
			this.baseMapper.insert(sumtype);
			return sales;
		}
		
 
		
}
