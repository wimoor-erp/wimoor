package com.wimoor.amazon.follow.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.AmazonClientOneFeignManager;
import com.wimoor.amazon.follow.mapper.ProductInfoFollowMapper;
import com.wimoor.amazon.follow.mapper.ProductInfoFollowRecordMapper;
import com.wimoor.amazon.follow.mapper.ProductInfoFollowTimeMapper;
import com.wimoor.amazon.follow.pojo.dto.ProductFollowListDTO;
import com.wimoor.amazon.follow.pojo.dto.ProductInfoFollowSaveDTO;
import com.wimoor.amazon.follow.pojo.entity.ProductInfoFollow;
import com.wimoor.amazon.follow.pojo.entity.ProductInfoFollowRecord;
import com.wimoor.amazon.follow.service.IProductInfoFollowService;
import com.wimoor.amazon.product.pojo.dto.ProductListingItemDTO;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.service.ISerialNumService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2023-06-06
 */
@Service
public class ProductInfoFollowServiceImpl extends ServiceImpl<ProductInfoFollowMapper, ProductInfoFollow> implements IProductInfoFollowService {
	 
	@Autowired
	AmazonClientOneFeignManager amazonClientOneFeignManager;
	@Autowired
	ISerialNumService serialNumService;
	@Autowired
	ProductInfoFollowRecordMapper productInfoFollowRecordMapper;
	@Autowired
	ProductInfoFollowTimeMapper productInfoFollowTimeMapper;
	@Resource
	ThreadPoolTaskExecutor threadPoolTaskExecutor;
	private void setItemOffers(Map<String, Object> item ,List<Map<String, Object>> offerlist) {
		String sellerid=item.get("sellerid").toString();
		Map<String, Object> offerlowest=null;
		Map<String, Object> myself=null;
		for (Map<String, Object> offeritem:offerlist) {
			String offsellerid=offeritem.get("sellerid").toString();
			if(offsellerid.equals(sellerid)) {
				offeritem.put("isself", true);
				myself=offeritem;
			}else {
				offeritem.put("isself", false);
			}
			BigDecimal listing_price_amount=offeritem.get("listing_price_amount")!=null?new BigDecimal(offeritem.get("listing_price_amount").toString()):new BigDecimal("0");
			BigDecimal shiping_amount=offeritem.get("shiping_amount")!=null?new BigDecimal(offeritem.get("shiping_amount").toString()):new BigDecimal("0");
			offeritem.put("listing_price_amount", listing_price_amount);
			offeritem.put("shiping_amount", shiping_amount);
			if(offeritem.get("is_buy_box_winner")!=null && (Boolean)offeritem.get("is_buy_box_winner")==true
			 && offeritem.get("losttime")==null) {
				item.put("winoffer",offeritem);
			}
			offerlowest=minOffer(offerlowest,offeritem);
		}
		if(offerlowest!=null) {
			item.put("minoffer",offerlowest);
		}
		if(myself!=null) {
			item.put("myself",myself);
			BigDecimal listing_price_amount=myself.get("listing_price_amount")!=null?new BigDecimal(myself.get("listing_price_amount").toString()):new BigDecimal("0");
			BigDecimal shiping_amount=myself.get("shiping_amount")!=null?new BigDecimal(myself.get("shiping_amount").toString()):new BigDecimal("0");
			item.put("buyprice",listing_price_amount);
			item.put("buyshipprice",shiping_amount);
		}
		item.put("offers",offerlist);
	}
	@Override
	public IPage<Map<String, Object>> findByCondition(ProductFollowListDTO dto) {
		IPage<Map<String, Object>> list = this.baseMapper.findByCondition(dto.getPage(), dto);
		if(list.getTotal()>0) {
			for (int i = 0; i < list.getRecords().size(); i++) {
				Map<String, Object> item = list.getRecords().get(i);
				String asin=item.get("asin").toString();
				String marketplaceid=item.get("marketplaceid").toString();
				List<Map<String, Object>> offerlist=this.baseMapper.findFollowOfferlist(asin,marketplaceid);
				if(offerlist!=null && offerlist.size()>0) {
					 setItemOffers(item , offerlist) ;
				}else {
					try {
						new Thread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								ProductListingItemDTO followdto=new ProductListingItemDTO();
								followdto.setPid(item.get("pid").toString());
								amazonClientOneFeignManager.recordFollowListAction(followdto);
							}}).start();
					}catch(Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		return list;
	}

	private Map<String, Object> minOffer(Map<String, Object> offerlowest, Map<String, Object> item) {
		if(offerlowest==null)return item;
		BigDecimal lowestprice=offerlowest.get("listing_price_amount")!=null?new BigDecimal(offerlowest.get("listing_price_amount").toString()):new BigDecimal("0");
		BigDecimal lowestshipprice=offerlowest.get("shiping_amount")!=null?new BigDecimal(offerlowest.get("shiping_amount").toString()):new BigDecimal("0");
		if(item==null)return offerlowest;
		BigDecimal itemprice=item.get("listing_price_amount")!=null?new BigDecimal(item.get("listing_price_amount").toString()):new BigDecimal("0");
		BigDecimal itemshipprice=item.get("shiping_amount")!=null?new BigDecimal(item.get("shiping_amount").toString()):new BigDecimal("0");
		lowestprice=lowestprice.add(lowestshipprice);
		itemprice=itemprice.add(itemshipprice);
		if(lowestprice.compareTo(itemprice)>0) {
			return item;
		}else {
			return offerlowest;
		}
	}
	@Override
	public List<Map<String, Object>> saveProfuctFollow(List<ProductInfoFollowSaveDTO> dto) {
		List<Map<String,Object>> result=new LinkedList<Map<String,Object>>();
		for(ProductInfoFollowSaveDTO item:dto) {
			result.add(this.saveProfuctFollow(item));
		}
		return result;
	}
	
	public Map<String, Object> saveProfuctFollow(ProductInfoFollowSaveDTO dto) {
		List<String> marketplaceids=new ArrayList<String>();
		marketplaceids.add(dto.getMarketplaceid());
		ProductListingItemDTO savedto=new ProductListingItemDTO();
		//判断是否去重
		if("1".equals(dto.getIsrepeat())) {
			List<Map<String, Object>> list=this.baseMapper.findProductInfo(dto.getShopid(),dto.getAsin(),dto.getMarketplaceid());
			if(list!=null && list.size()>0) {
				throw new BizException("该站点已存在ASIN:"+dto.getAsin()+",不能重复添加！");
			}
		}
		if(StrUtil.isEmpty(dto.getSku())) {
			try {
				String sku = serialNumService.readSerialNumber(dto.getShopid(), "FOL");
				savedto.setSku(sku);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			savedto.setSku(dto.getSku());
		}
		savedto.setAsin(dto.getAsin());
		savedto.setPrice(dto.getStartprice());
		savedto.setFilfullable(dto.getFulfillable());
		savedto.setLeaddays(dto.getCycle());
		savedto.setMarketplaceids(marketplaceids);
		savedto.setAmazonauthid(dto.getAmazonauthid());
		savedto.setLowestprice(dto.getOverprice());
		savedto.setMaxOrderQuantity(dto.getMaxorderqty());
		savedto.setMerchantShippingGroup(dto.getTemplateid());
		savedto.setImage(dto.getImage());
		Map<String, Object> map = amazonClientOneFeignManager.saveAsinAction(savedto);
		//保存ProductInfoFollow  map是info
		String pid=map.get("id").toString();
		ProductInfoFollow oldfollow = this.baseMapper.selectById(pid);
		if(oldfollow!=null) {
			ProductInfoFollow follow=oldfollow;
			if(dto.getTimeid()!=null) {
				Calendar c=Calendar.getInstance();
				c.add(Calendar.YEAR, -10);
				follow.setTimeid(dto.getTimeid());
				follow.setExecuteStart(c.getTime());
				follow.setExecuteEnd(c.getTime());
			}
			if(dto.getOverprice()!=null) {
				follow.setAssumeprice(new BigDecimal(dto.getOverprice()));
			}
			if(dto.getLowestquantity()!=null) {
				follow.setLowestQuantity(Integer.parseInt(dto.getLowestquantity()));
			}
			if(StrUtil.isNotBlank(dto.getMaxorderqty())) {
				follow.setMaxOrderQuantity(Integer.parseInt(dto.getMaxorderqty().toString()));
			}
			if(dto.getCycle()!=null) {
				follow.setDeliveryCycle(Integer.parseInt(dto.getCycle()));
			}
			if(dto.getFulfillable()!=null) {
				follow.setQuantity(Integer.parseInt(dto.getFulfillable()));
			}
			if(dto.getStartprice()!=null) {
				follow.setPrice(new BigDecimal(dto.getStartprice()));
			}
			if(StrUtil.isNotBlank(dto.getTemplateid())) {
				follow.setTemplateid(dto.getTemplateid());
			}
			follow.setRemark(dto.getRemark());
			follow.setOperator(dto.getUserid());
			follow.setOpttime(new Date());
			follow.setImage(dto.getImage());
			this.baseMapper.updateById(follow);
			saveRecord(follow);
		}else {
			ProductInfoFollow follow=new ProductInfoFollow();
			follow.setTimeid(dto.getTimeid());
			if(StrUtil.isNotBlank(dto.getOverprice())) {
				follow.setAssumeprice(new BigDecimal(dto.getOverprice()));
			}
			if(StrUtil.isNotBlank(dto.getMaxorderqty())) {
				follow.setMaxOrderQuantity(Integer.parseInt(dto.getMaxorderqty().toString()));
			}
			follow.setCreator(dto.getUserid());
			if(StrUtil.isNotBlank(dto.getCycle())) {
			   follow.setDeliveryCycle(Integer.parseInt(dto.getCycle()));
			}
			if(StrUtil.isNotBlank(dto.getTemplateid())) {
				follow.setTemplateid(dto.getTemplateid());
			}
			if(dto.getLowestquantity()!=null) {
				follow.setLowestQuantity(Integer.parseInt(dto.getLowestquantity()));
			}
			follow.setOrdersSum(0);
			//info的数据
			follow.setStatusUpload("NOUPLOAD");
			if(StrUtil.isNotBlank(dto.getFulfillable())) {
			    follow.setQuantity(Integer.parseInt(dto.getFulfillable()));
			}
			if(map.get("name")!=null ) {
				follow.setName(map.get("name").toString());
			}
			if(map.get("brand")!=null ) {
				follow.setBrand(map.get("brand").toString());
			}
			if(map.get("binding")!=null ) {
				follow.setImage(map.get("binding").toString());
			}
			follow.setPid(map.get("id").toString());
			follow.setPrice(new BigDecimal(map.get("price").toString()));
			follow.setOldprice(new BigDecimal(map.get("price").toString()));
			follow.setRemark(dto.getRemark());
			Calendar c=Calendar.getInstance();
			c.add(Calendar.YEAR, -10);
			follow.setExecuteStart(c.getTime());
			follow.setExecuteEnd(c.getTime());
			follow.setOperator(dto.getUserid());
			follow.setOpttime(new Date());
			follow.setOperator(dto.getUserid());
			follow.setCreatetime(new Date());
			follow.setImage(dto.getImage());
			this.baseMapper.insert(follow);
			saveRecord(follow);
		}
		
		return map;
	}

	@Override
	public Map<String,Object> deleteProfuctFollow(List<String> ids) {
		Map<String,Object> map=new HashMap<String, Object>();
		Integer count=0;
		if(ids!=null && ids.size()>1) {
			for (int i = 0; i < ids.size(); i++) {
				String pid=ids.get(i);
				ProductInfoFollow follow = this.baseMapper.selectById(pid);
				follow.setStatusUpload("NEEDDELETE");
				follow.setOpttime(new Date());
				this.baseMapper.updateById(follow);
				this.baseMapper.invalidProduct(follow.getPid());
				count++;
			}
		}else {
			List<ProductInfoFollow> followlist=new ArrayList<ProductInfoFollow>();
			if(ids!=null&&ids.size()==1) {
				followlist.add(this.baseMapper.selectById(ids.get(0)));
			}
			this.deleteProfuctFollowList(followlist);
		}
		return map;
	}
	
	public void deleteProfuctFollowList(List<ProductInfoFollow> followlist) {
		if(followlist!=null && followlist.size()>0) {
			ProductListingItemDTO deldto=new ProductListingItemDTO();
			for (int i = 0; i < followlist.size(); i++) {
				ProductInfoFollow follow =followlist.get(i);
				deldto.setPid(follow.getPid());
				try {			
					this.baseMapper.invalidProduct(follow.getPid());
					amazonClientOneFeignManager.deleteSkuAction(deldto);
					follow.setStatusUpload("ISDELETE");
					follow.setOpttime(new Date());
					this.baseMapper.deleteById(follow.getPid());
				} catch (Exception e) {
					follow.setStatusUpload("DELETEFAIL");
					follow.setOpttime(new Date());
					this.baseMapper.updateById(follow);
				}
				saveRecord(follow);
			}
		}
		 
	}
	
	@Override
	public synchronized Map<String, Object> pushProfuctFollow(List<ProductInfoFollow> ids,String ftype,String remark) {
		Map<String,Object> map=new HashMap<String, Object>();
		Integer count=0;
		if(ids!=null && ids.size()>0) {
			ProductListingItemDTO savedto=new ProductListingItemDTO();
			for (int i = 0; i < ids.size(); i++) {
				ProductInfoFollow follow =	this.baseMapper.selectById(ids.get(i).getPid());
				String pid=follow.getPid();
				follow.setOpttime(new Date());
				savedto.setPid(pid);
				if(follow.getStatusUpload().contains("DELETE")) {
					 continue;
				 }
				 if(follow.getStatusUpload().equals("NEEDONLINE")) {
					 remark="批量在线";
				 }
				 if(follow.getStatusUpload().equals("NEEDOFFLINE")) {
					 remark="批量下线";
				 }	 
				 if(follow.getStatusPrice()!=null) {
					 if(follow.getStatusPrice().equals("NEEDPRICE")) {
					      remark="批量改价";
					 }
					 if(follow.getStatusPrice().equals("NEEDDISCOVERABLE")) {
						 remark="批量发布";
					 }
				 }
				
				if("unsales".equals(ftype)||"upload".equals(ftype)) {
					savedto.setFilfullable("0");
				}else  {
					savedto.setFilfullable(follow.getQuantity().toString());
				}
				if("upload".equals(ftype)||"price".equals(ftype)) {
					savedto.setPrice(follow.getPrice().toString());
				}
				if(follow!=null&&follow.getDeliveryCycle()!=null) {
					savedto.setLeaddays(follow.getDeliveryCycle().toString());
				}
				
				if(follow!=null&&follow.getAssumeprice()!=null) {
					savedto.setLowestprice(follow.getAssumeprice().toString());
				}
				if(StrUtil.isNotBlank(follow.getTemplateid())) {
					savedto.setMerchantShippingGroup(follow.getTemplateid());
				}
				try {
					amazonClientOneFeignManager.pushAsinAction(savedto);
					follow =	this.baseMapper.selectById(pid);
					follow.setOpttime(new Date());
					savedto.setPid(pid);
					if(follow.getStatusUpload().contains("DELETE")) {
						 continue;
					 }
					if("unsales".equals(ftype)) {
						follow.setStatusUpload("ISOFFLINE");
						follow.setExecuteEnd(new Date());
					}
					if("upload".equals(ftype)) {
						follow.setStatusUpload("ISOFFLINE");
					}
					if("sales".equals(ftype)) {
						follow.setStatusUpload("ISONLINE");
						follow.setExecuteStart(new Date());
					}
					if("price".equals(ftype)) {
						follow.setStatusPrice("PRICECHANGE");
						follow.setExecuteStart(new Date());
					}
					follow.setErrormsg(null);
					this.baseMapper.updateById(follow);
					count++;
				} catch (Exception e) {
					amazonClientOneFeignManager.pushAsinAction(savedto);
					follow =	this.baseMapper.selectById(pid);
					follow.setOpttime(new Date());
					savedto.setPid(pid);
					if(follow.getStatusUpload().contains("DELETE")) {
						 continue;
					 }
					if("unsales".equals(ftype)) {
						follow.setStatusUpload("OFFLINEFAIL");
					}
					if("upload".equals(ftype)) {
						follow.setStatusUpload("UPLOADFAIL");
					}
					if("sales".equals(ftype)) {
						follow.setStatusUpload("ONLINEFAIL");
					}
					if("price".equals(ftype)) {
						follow.setStatusPrice("PRICEFAIL");
					}
					if(e!=null&&e.getMessage()!=null) {
						follow.setErrormsg(e.getMessage());
					}
					this.baseMapper.updateById(follow);
				}
			   
				follow.setRemark(remark);
				saveRecord(follow);
			}
		}
		map.put("total", ids.size());
		map.put("success", count);
		map.put("fail", ids.size()-count);
		return map;
		

	}
	
	//保存日志记录
	public void saveRecord(ProductInfoFollow follow) {
		ProductInfoFollowRecord his=new ProductInfoFollowRecord();
		BeanUtil.copyProperties(follow, his);
		productInfoFollowRecordMapper.insert(his);
	}
	 //开始推送ASIN
	 @Scheduled(cron = "0/10 * * * * ?")
	 public void taskStartPushAsin() {
		//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Calendar cend=Calendar.getInstance();
		String etime=sdf.format(cend.getTime());
		
		Calendar nowDate=Calendar.getInstance();
		nowDate.add(Calendar.MINUTE, -10);
		String stime=sdf.format(nowDate.getTime());
		String daytime = GeneralUtil.fmtDate(nowDate.getTime()) ;
		//启动需要在线的跟卖
		try {
			List<ProductInfoFollow> startfollowlist = this.baseMapper.selectstartTimeList(stime,etime,daytime);
			if(startfollowlist!=null && startfollowlist.size()>0) {
				 execut2(startfollowlist,"sales","定时自动上线销售");
			} 
		}catch(Exception e) {
			e.printStackTrace();
		}
		
        try {
        	//停用需要离线的跟卖
    		List<ProductInfoFollow> endfollowlist = this.baseMapper.selectendTimeList(stime,etime,daytime);
    		if(endfollowlist!=null && endfollowlist.size()>0) {
    			 execut2(endfollowlist,"unsales","定时自动下线");
    		} 
		}catch(Exception e) {
			e.printStackTrace();
		}
		
        try {
        	List<ProductInfoFollow> deletefollowlist = this.baseMapper.selectDeleteList();
    		if(deletefollowlist!=null && deletefollowlist.size()>0) {
    			 execut2(deletefollowlist,"delete","批量删除产品");
    		} 
		}catch(Exception e) {
			e.printStackTrace();
		}
		
         try {
        	 List<ProductInfoFollow> pricefollowlist = this.baseMapper.selectPriceList();
     		if(pricefollowlist!=null && pricefollowlist.size()>0) {
     			 execut2(pricefollowlist,"price","批量调价");
     		} 
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
	 }
	 
	 @Scheduled(cron = "0 0/15 * * * ?")
	 public void taskOnlineAsin() {
		//启动需要在线的跟卖
		List<ProductInfoFollow> startfollowlist = this.baseMapper.needInvRefresh();
		if(startfollowlist!=null && startfollowlist.size()>0) {
			 execut(startfollowlist,"online","自动调整库存");
		} 
	 }
	 
	 @Scheduled(cron = "0 0 0/1 * * ?")
	 public void taskOrderAsin() {
		LambdaQueryWrapper<ProductInfoFollow> queryWrapper=new LambdaQueryWrapper<ProductInfoFollow>();
		queryWrapper.in(ProductInfoFollow::getStatusUpload, "ISONLINE","ISOFFLINE");
		List<ProductInfoFollow> startfollowlist = this.baseMapper.selectList(queryWrapper);
		if(startfollowlist!=null && startfollowlist.size()>0) {
			 execut(startfollowlist,"order","订单统计");
		} 
		
	 }
	 
	 @Scheduled(cron = "0 0/30 * * * ?")
	 public void taskChangePrice() {
		LambdaQueryWrapper<ProductInfoFollow> queryWrapper=new LambdaQueryWrapper<ProductInfoFollow>();
		queryWrapper.in(ProductInfoFollow::getStatusUpload, "ISONLINE","ISOFFLINE");
		List<ProductInfoFollow> startfollowlist = this.baseMapper.selectList(queryWrapper);
		if(startfollowlist!=null && startfollowlist.size()>0) {
			 execut(startfollowlist,"changeprice","调整刚刚上架产品价格");
		} 
		
	 }
	 
	 void execut2(List<ProductInfoFollow> followlist ,String ftype,String remark) {
		 
		         for(ProductInfoFollow item:followlist) {
		        	 item.setOpttime(new Date());
					 this.updateById(item);
		        	 threadPoolTaskExecutor.execute(new Runnable() {
							@Override
							public void run() {
								try {
									if(ftype.equals("online")) {
										pushOnlineProfuctFollow(Arrays.asList(item));
									}else if(ftype.equals("order")) {
										orderSummaryProfuctFollow(Arrays.asList(item),ftype);
									}else if(ftype.equals("delete")) {
										deleteProfuctFollowList(Arrays.asList(item));
									}else if(ftype.equals("changeprice")){
										changeProductPrice(Arrays.asList(item),ftype);
									}else {
										pushProfuctFollow(Arrays.asList(item),ftype,remark);
									}
								}catch(Exception e) {
									e.printStackTrace();
								}
								
							}
				    	});
		         }
				 
		 
	 }
	 
	 void execut(List<ProductInfoFollow> followlist ,String ftype,String remark) {
		 Map<String,LinkedList<ProductInfoFollow>> authmap=new HashMap<String, LinkedList<ProductInfoFollow>>(); 
		    //按授权分组
			for (ProductInfoFollow item:followlist) {
				String authid=item.getAmazonauthid();
				//一个authid一个run
				item.setOpttime(new Date());
				this.updateById(item);
				if(authmap.containsKey(authid)) {
					LinkedList<ProductInfoFollow> list =authmap.get(authid);
					list.add(item);
					authmap.put(authid,list);
				}else {
					LinkedList<ProductInfoFollow> list = new LinkedList<ProductInfoFollow>();
					list.add(item);
					authmap.put(authid,list);
				}
			}
			//按分组执行线程
			for (Entry<String, LinkedList<ProductInfoFollow>> entry:authmap.entrySet()) {
			     LinkedList<ProductInfoFollow> value = entry.getValue() ;
				 threadPoolTaskExecutor.execute(executStart(value,ftype,remark));
			}
	 }
	 
    Runnable executStart(List<ProductInfoFollow> list, String ftype,String remark){
    	return new Runnable() {
			@Override
			public void run() {
				if(ftype.equals("online")) {
					pushOnlineProfuctFollow(list);
				}else if(ftype.equals("order")) {
					orderSummaryProfuctFollow(list,ftype);
				}else if(ftype.equals("delete")) {
					deleteProfuctFollowList(list);
				}else if(ftype.equals("changeprice")){
					changeProductPrice(list,ftype);
				}else {
					pushProfuctFollow(list,ftype,remark);
				}
			}
    	};
    }
    
    public void pushOnlineProfuctFollow(List<ProductInfoFollow> list) {
    	pushOnlineProfuctFollow(list,"自动调整库存");
    }
    
    public List<Map<String, Object>> pushOnlineProfuctFollow(List<ProductInfoFollow> list,String remark) {
    	ArrayList<ProductInfoFollow> needpush = new ArrayList<ProductInfoFollow>();
    	List<Map<String, Object>> resultlist=new LinkedList<Map<String,Object>>();
    	int count=0;
    	for(ProductInfoFollow follow:list) {
    		ProductListingItemDTO dto=new ProductListingItemDTO();
        	dto.setPid(follow.getPid());
        	count++;
        	follow.setRefreshtime(new Date());
    		this.updateById(follow);
        	if(count>10) {
        		return null;
        	}
        	Map<String, Object> item = null;
        	try {
        		item = amazonClientOneFeignManager.refreshInfoBySKUAction(dto);
        		resultlist.add(item);
        	}catch(Exception e) {
        		if(e.getMessage().contains("not found in marketplace")) {
        			needpush.add(follow);
        		}
        		if(e.getMessage()!=null&&(e.getMessage().contains("Unauthorized")
        				||e.getMessage().contains("Error getting LWA Access Token")
        				||e.getMessage().contains("Access to requested resource is denied"))) {
        			e.printStackTrace();
        			break;
        		}
        		continue;
        		
        	}
    		
        	if(item!=null&&item.get("fulfillmentAvailability")!=null) {
        		@SuppressWarnings("unchecked")
				List<Map<String,Object>> qty= (List<Map<String,Object>>)item.get("fulfillmentAvailability");
        		if(qty!=null&&qty.size()>0) {
        			for(Map<String,Object> fitem:qty) {
        				if(fitem.get("fulfillmentChannelCode")!=null&&fitem.get("fulfillmentChannelCode").toString().toUpperCase().contains("AMAZON")) {
            				continue;
            			}
        				if(follow.getLowestQuantity()==null) {
        					follow.setLowestQuantity(1);
        				}
            			if(fitem.get("quantity")==null||Integer.parseInt(fitem.get("quantity").toString())<follow.getLowestQuantity()) {
            				needpush.add(follow);
            			}
        			}
        		}
        	}
    	}
    	if(needpush.size()>0) {
    			this.pushProfuctFollow(needpush, "sales",remark);
    	}
    	return resultlist;
    }
    
    void changeProductPrice(List<ProductInfoFollow> list, String ftype) {
    	 for(ProductInfoFollow follow:list) {
    		// List<Map<String, Object>> offerlist=this.baseMapper.findFollowOffers(follow.getPid());
    		// if(offerlist==null||offerlist.size()==0) {
    			 ProductListingItemDTO followdto=new ProductListingItemDTO();
    	 	     followdto.setPid(follow.getPid());
    	 	     amazonClientOneFeignManager.recordFollowListAction(followdto);
    		// }else {
    			    //for(Map<String, Object> item:offerlist) {
    				//  if(item.get("sellerid").equals(item.get("psellerid"))&&(Boolean)item.get("is_buy_box_winner")) {
    				//	   ProductListingItemDTO followdto=new ProductListingItemDTO();
    	    	 	//     followdto.setPid(follow.getPid());
    	    	 	//     amazonClientOneFeignManager.recordFollowListAction(followdto);
    				// }
    			// }
    		// }
    	 }
    }
    
    void orderSummaryProfuctFollow(List<ProductInfoFollow> list, String ftype) {
    	for(ProductInfoFollow follow:list) {
    	    Map<String,Object> result=this.baseMapper.findOrderSummary(follow.getPid());
    	    if(result!=null&&result.get("qty")!=null) {
    	    	follow.setOrdersSum(Integer.parseInt(result.get("qty").toString()));
    	    	if(result.get("purchase_date")!=null) {
    	    		follow.setLastOrderTime(GeneralUtil.getDate(result.get("purchase_date")));
    	    	}
    	    	this.baseMapper.updateById(follow);
    	    }
    	   
    	}
    }
	@Override
	public void downloadDetailExport(SXSSFWorkbook workbook, Map<String, Object> parameter) {
		ProductFollowListDTO dto=new ProductFollowListDTO();
		dto.setAmazonauthid(parameter.get("amazonauthid").toString());
		dto.setMarketplaceid(parameter.get("marketplaceid").toString());
		dto.setShopid(parameter.get("shopid").toString());
		List<Map<String, Object>> list = this.baseMapper.findByCondition(dto);
		//操作Excel
		Map<String, Object> titlemap = new LinkedHashMap<String, Object>();
		titlemap.put("asin", "ASIN");
		titlemap.put("sku", "SKU");
		titlemap.put("price", "价格");
		titlemap.put("oldprice", "初始价格");
		titlemap.put("assumeprice", "承受价格");
		titlemap.put("remark", "备注");
		titlemap.put("lastOrderTime", "最近出单");
		titlemap.put("ordersSum", "累计出单");
		titlemap.put("name", "名称");
		if (list.size() > 0 && list != null) {
			Sheet sheet = workbook.createSheet("sheet1");
			Row trow0 = sheet.createRow(0);
			Cell cell0 = trow0.createCell(0);
			cell0.setCellValue(list.get(0).get("gname").toString()+list.get(0).get("marketname").toString());
			// 在索引0的位置创建行（最顶端的行）
			Row trow = sheet.createRow(1);
			Cell cell = null;
			Object[] titlearray = titlemap.keySet().toArray();
			for (int i = 0; i < titlearray.length; i++) {
				cell = trow.createCell(i); // 在索引0的位置创建单元格(左上端)
				Object value = titlemap.get(titlearray[i].toString());
				cell.setCellValue(value.toString());
			}
			for (int i = 0; i < list.size(); i++) {
				Row row = sheet.createRow(i + 2);
				Map<String, Object> map = list.get(i);
				for (int j = 0; j < titlearray.length; j++) {
					cell = row.createCell(j); // 在索引0的位置创建单元格(左上端)
					Object value = map.get(titlearray[j].toString());
					if (value != null) {
						cell.setCellValue(value.toString());
					}
					
					
				}
			}
		} else {
			Sheet sheet = workbook.createSheet("sheet1");
			Row trow0 = sheet.createRow(0);
			Cell cell0 = trow0.createCell(0);
			cell0.setCellValue("暂无匹配记录");
		} 
	}
    private String getValue(Cell cell) {
    	String value=null;
    	if(cell!=null) {
    		cell.setCellType(CellType.STRING);
    		value = cell.getStringCellValue();
		}
    	return value;
    }
	@Override
	public Map<String, Object> uploadShipListByExcel(Sheet sheet, String shopid, String authid, String marketplaceid,String timeid) {
		Map<String, Object> map=new HashMap<String, Object>();
		List<ProductInfoFollowSaveDTO> dto=new ArrayList<ProductInfoFollowSaveDTO>();
		for(int i = 1; i <= sheet.getLastRowNum(); i++){
			Row crow = sheet.getRow(i);
			Cell cell = crow.getCell(0);
			String asin = getValue(cell);
			Cell cell2 = crow.getCell(1);
			String price =getValue(cell2);
			Cell cell3 = crow.getCell(2);
			String qty = getValue(cell3);
			if(StrUtil.isEmpty(asin) || StrUtil.isEmpty(price) || StrUtil.isEmpty(qty)) {
				continue;
			}
			Cell cell4 = crow.getCell(3);
			String sku =getValue(cell4);
			if (StrUtil.isEmpty(sku)) { 
				try {
					sku= serialNumService.readSerialNumber(shopid, "FOL");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Cell cell5 = crow.getCell(4);
			String remark =getValue(cell5);
			
			Cell cell6 = crow.getCell(5);
			String overprice =getValue(cell6);
			
			Cell cell7 = crow.getCell(6);
			String cycle =getValue(cell7);
			 
			Cell cell8 = crow.getCell(7);
			String maxorders =getValue(cell8);
			
			ProductInfoFollowSaveDTO item=new ProductInfoFollowSaveDTO();
			item.setAmazonauthid(authid);
			item.setAsin(asin);
			item.setSku(sku);
			item.setStartprice(price);
			item.setFulfillable(qty);
			item.setCycle(cycle);
			item.setMarketplaceid(marketplaceid);
			item.setOverprice(overprice);
			item.setMaxorderqty(maxorders);
			item.setRemark(remark);
			item.setTimeid(timeid);
			item.setIsrepeat("1");
			item.setShopid(shopid);
			dto.add(item);
		}
		this.saveProfuctFollow(dto);
		return map;
	}

	@Override
	public List<Map<String, Object>> findRecordList(String pid,String opttype) {
		return this.baseMapper.findRecordList(pid,opttype);
	}
	
	@Override
	public IPage<Map<String, Object>> findWarningList(Page<?> page,String shopid,String authid,String marketplaceid) {
		return this.baseMapper.findWarningList(page,shopid, authid, marketplaceid);
	}

	@Override
	public void ignoreWarningList(List<String> ids) {
           for(String id:ids) {
        	   ProductInfoFollow follow = this.baseMapper.selectById(id);
        	   Map<String,Object> info =this.baseMapper.findInfo(id);
        	   String name=info.get("name")!=null?info.get("name").toString():null;
        	   String brand=info.get("brand")!=null?info.get("brand").toString():null;
        	   String url=info.get("url")!=null?info.get("url").toString():null;
        	   follow.setName(name);
        	   follow.setBrand(brand);
        	   follow.setImage(url);
        	   this.baseMapper.updateById(follow);
           }
	}

	@Override
	public Integer findWarningNunmber(String companyid) {
		// TODO Auto-generated method stub
		return this.baseMapper.findWarningNunmber(companyid);
	}

	@Override
	public void changeProfuctFollowTime(List<String> ids) {
		String timeid=ids.get(0);
		for (int i = 1; i < ids.size(); i++) {
			String pid = ids.get(i);
			ProductInfoFollow follow = this.baseMapper.selectById(pid);
			follow.setTimeid(timeid);
			Calendar c=Calendar.getInstance();
			c.add(Calendar.YEAR, -10);
			follow.setExecuteStart(c.getTime());
			follow.setExecuteEnd(c.getTime());
			this.baseMapper.updateById(follow);
		}
	}
	@Override
	public void changeProfuctFollow(String ftype, List<String> ids) {
		//拿出第一行要修改的值
		String value=ids.get(0);
		if(StrUtil.isEmpty(value)) {
			throw new BizException("请输入正确的值！");
		}
		for (int i = 1; i < ids.size(); i++) {
			String pid = ids.get(i);
			ProductInfoFollow follow = this.baseMapper.selectById(pid);
			if("price".equals(ftype)) {
				follow.setPrice(new BigDecimal(value));
			}
			if("lowprice".equals(ftype)) {
				follow.setAssumeprice(new BigDecimal(value));
			}
			if("rateprice".equals(ftype)) {
				if(follow.getPrice()!=null) {
					follow.setPrice(new BigDecimal(value).multiply(follow.getPrice()).setScale(2, RoundingMode.HALF_UP));
				}
			}
			if("ratelowprice".equals(ftype)) {
				if(follow.getPrice()!=null) {
					follow.setAssumeprice(new BigDecimal(value).multiply(follow.getAssumeprice()).setScale(2, RoundingMode.HALF_UP));
				}
			}
			if("cycle".equals(ftype)) {
				follow.setDeliveryCycle(Integer.parseInt(value));
			}
			if("qty".equals(ftype)) {
				follow.setQuantity(Integer.parseInt(value));
			}
			if("lowestqty".equals(ftype)) {
				follow.setLowestQuantity(Integer.parseInt(value));
			}
			this.baseMapper.updateById(follow);
		}
	}
	
	@Override
	public void changeAllCycle(String shopid, String cycle) {
		if(StrUtil.isEmpty(cycle)) {
			throw new BizException("请输入正确的值！");
		}
		ProductFollowListDTO param=new ProductFollowListDTO();
		param.setShopid(shopid);
		List<Map<String, Object>> list = this.baseMapper.findByCondition(param);
		if(list!=null && list.size()>0) {
			for (Map<String, Object> item:list) {
				ProductInfoFollow follow = this.baseMapper.selectById(item.get("pid").toString());
				if(follow!=null) {
					follow.setDeliveryCycle(Integer.parseInt(cycle));
					this.baseMapper.updateById(follow);
				}
			}
		}
	}
	public void syncProductInfo(String shopid,String userid) {
		List<ProductInfoFollow> list = this.baseMapper.syncProductInfo(shopid, userid);
		for(ProductInfoFollow item:list) {
			ProductInfoFollow old = this.baseMapper.selectById(item.getPid());
			if(old==null) {
				this.baseMapper.insert(item);
			}
		}
		
	}
     
}
