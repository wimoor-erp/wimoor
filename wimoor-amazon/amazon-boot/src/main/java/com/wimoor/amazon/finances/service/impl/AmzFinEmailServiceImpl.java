package com.wimoor.amazon.finances.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wimoor.admin.pojo.dto.SysEmailDTO;
import com.wimoor.amazon.api.AdminClientOneFeignManager;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IAmazonGroupService;
import com.wimoor.amazon.auth.service.IAmazonSellerMarketService;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.finances.mapper.AmzFinAccountMapper;
import com.wimoor.amazon.finances.service.IAmzFinEmailService;
import com.wimoor.amazon.inventory.mapper.InventoryReportMapper;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.FileUpload;

@Service
public class AmzFinEmailServiceImpl implements IAmzFinEmailService{
	@Resource
	private InventoryReportMapper inventoryReportMapper;
	@Resource
	private IAmazonGroupService iAmazonGroupService;
	@Resource
	private IAmazonSellerMarketService iAmazonSellerMarketService;
	@Resource
	IExchangeRateHandlerService exchangeRateHandlerService;
	@Resource
	IAmazonAuthorityService amazonAuthorityService;
	@Resource
	AmzFinAccountMapper amzFinAccountMapper;
	@Resource
	ErpClientOneFeignManager erpClientOneFeignManager;
	@Resource
	AdminClientOneFeignManager adminClientOneFeignManager;
	@Resource
	FileUpload fileUpload ;
	public Map<String, Map<String,Object>> getTotalFinAcc(List<Map<String, Object>> FBAInvCostList,
			List<Map<String, Object>> allFinAccountList, String shopid) {
		Map<String,Map<String,Object>> result = new HashMap<String,Map<String,Object>>();
		String currency = "RMB";
		BigDecimal untransfer_all = new BigDecimal("0");
		BigDecimal transfer_all = new BigDecimal("0");
		BigDecimal totalprice_all = new BigDecimal("0");
		List<AmazonGroup> grouplist = iAmazonGroupService.selectByShopId(shopid);
		for(AmazonGroup amzgroup:grouplist) {
			HashMap<String, Object> param = new HashMap<String,Object>();
			param.put("name", amzgroup.getName());
			result.put(amzgroup.getId(), param);
		}
		//补上没有FBA库存货值的店铺
		for (Map<String, Object> map : allFinAccountList) {
			String groupid = map.get("groupid").toString();
			Map<String, Object> item = result.get(groupid);
			BigDecimal amount =map.get("amount") == null ? new BigDecimal("0") : (BigDecimal) map.get("amount");
			String mycurrency=map.get("currency").toString();
			BigDecimal newamount = exchangeRateHandlerService.changeCurrencyByLocal(shopid, mycurrency, currency, amount, 3);
	        if("convert".equals(map.get("type"))) {
	         	BigDecimal oldamount =item.get("transfer_total") == null ? new BigDecimal("0") : (BigDecimal) item.get("transfer_total");
	        	item.put("transfer_total", oldamount.add(newamount));
	        	transfer_all = transfer_all.add(newamount);
	        }else {
	           	BigDecimal oldamount =item.get("untransfer_total") == null ? new BigDecimal("0") : (BigDecimal) item.get("untransfer_total");
	        	item.put("untransfer_total", oldamount.add(newamount));
	          	untransfer_all = untransfer_all.add(newamount);
	        }
			String marketname = GeneralUtil.getMarketname(mycurrency);
			item.put("marketname", marketname);
			String fcurrency = GeneralUtil.formatCurrency(mycurrency);
			item.put("fcurrency", fcurrency);
		}

		for (Map<String, Object> map : FBAInvCostList) {
			String groupid = map.get("groupid").toString();
			Map<String, Object> item = result.get(groupid);
			if(item==null) {
				item=new HashMap<String,Object>();
				result.put(groupid, item);
			}
			BigDecimal amount =new BigDecimal("0");
			BigDecimal oldamount =new BigDecimal("0");
			if(map.get("totalprice")==null){
				 map.put("totalprice", new BigDecimal("0"));
			}
			if(map!=null&&map.get("totalprice")!=null) {				 
				 amount = new BigDecimal(map.get("totalprice").toString());
			}
			if(item!=null&&item.get("totalprice")!=null) {
				 oldamount = (BigDecimal) item.get("totalprice");
			}
			item.put("totalprice", oldamount.add(amount));
			totalprice_all = totalprice_all.add(amount);
		}
		Map<String, Object> item = result.get("sum");
		if(item==null) {
			item=new HashMap<String,Object>();
			result.put("sum", item);
		}
		item.put("untransfer_all", untransfer_all);
		item.put("transfer_all", transfer_all);
		item.put("totalprice_all", totalprice_all);
		return result;
	}
	
	
	public void getFinEmailContent(StringBuffer buf,String shopid,String beginDate,String endDate){
		List<Map<String, Object>> localInvCostList =erpClientOneFeignManager.findInventoryNowCostByShopId(shopid);
		List<Map<String, Object>> FBAInvCostList = inventoryReportMapper.findFBAInvCostByShopId(shopid);
		if ((localInvCostList == null || localInvCostList.size() == 0) && (FBAInvCostList == null || FBAInvCostList.size() == 0)) {
			return;
		}
		List<AmazonGroup> grouplist = iAmazonGroupService.selectByShopId(shopid);
		List<Map<String, Object>> allFinAccountList = amzFinAccountMapper.selectFinByShopid(shopid, beginDate, endDate);
		
		Map<String, Map<String,Object>> result = getTotalFinAcc(FBAInvCostList, allFinAccountList, shopid);
		//改代码
		
		buf.append("<table style='width:700px;'>");
		buf.append("<thead><tr style='height:30px;border:1px solid #eee; background:#fafafa;color:#666'><th>店铺</th><th style='text-align:right;'>已出账金额</th><th style='text-align:right;'>未出账金额</th><th style='text-align:right;'>FBA库存货值</th></tr></thead>");
		buf.append("<tbody>");
		int j=0;
		for (Entry<String, Map<String, Object>> mapentry:result.entrySet()) {
			Map<String, Object> map =mapentry.getValue();
			if(mapentry.getKey().equals("sum")) {
				continue;
			}
			String color = "#f2f2f2";
			if (j % 2 == 1) {
				color = "#fff";
			}
			if(map.get("transfer_total")==null) {
				map.put("transfer_total","0.00");
			}
			if(map.get("untransfer_total")==null) {
				map.put("untransfer_total","0.00");
			}
			if(map.get("totalprice")==null) {
				map.put("totalprice","0.00");
			}
			buf.append("<tr style='height:30px;background-color: " + color + ";'><td>" + map.get("name")
					+ "</td><td style='text-align:right;color: #00a65a !important;'>￥"
					+ GeneralUtil.formatterQuantity(new BigDecimal(map.get("transfer_total").toString()))
					+ "</td><td style='text-align:right;color: #dd4b39 !important;'>￥"
					+ GeneralUtil.formatterQuantity(new BigDecimal(map.get("untransfer_total").toString()))
					+ "</td><td style='text-align:right;'>￥"
					+ GeneralUtil.formatterQuantity(new BigDecimal(map.get("totalprice").toString())) + "</td></tr>");
		}
		buf.append("</tbody>");
		Map<String, Object> map=result.get("sum");
		buf.append("<tr style='height:30px;'><td>合计</td><td style='text-align:right;color: #00a65a !important;'>￥"
				+ GeneralUtil.formatterQuantity(new BigDecimal(map.get("transfer_all").toString()))
				+ "</td><td style='text-align:right;color: #dd4b39 !important;'>￥"
				+ GeneralUtil.formatterQuantity(new BigDecimal(map.get("untransfer_all").toString())) + "</td><td style='text-align:right;'>￥"
				+ GeneralUtil.formatterQuantity(new BigDecimal(map.get("totalprice_all").toString())) + "</td></tr>");
		buf.append("</table>");
		buf.append("<div style='margin:15px 0px'>为了您更清楚的了解到数据的来源，下面提供了各个店铺在每个站点下的汇总金额。其中汇率转换是按照wimoor系统主页展示的每日汇率计算的。</div>");
		buf.append("<table style='width:700px;'>");
		buf.append("<thead><tr style='height:30px;border:1px solid #eee; background:#fafafa;color:#666'><th>店铺</th><th>站点</th><th style='text-align:right;'>已出账金额</th><th style='text-align:right;'>FBA库存货值</th></tr></thead>");
		buf.append("<tbody>");
		j++;
		int index=0;
		String mycurrency="";
		for(AmazonGroup group:grouplist) {
			List<Map<String, Object>> marketmap = iAmazonSellerMarketService.selectByGroup(group.getId());
		    boolean isEU=false;
			for(Map<String, Object> market:marketmap) {
				BigDecimal transfer_total =new BigDecimal("0");
				BigDecimal transfer_total_new =new BigDecimal("0");
				BigDecimal totalprice=new BigDecimal("0");
				if(market.get("region").toString().equals("EU")) {
					if(isEU==false) {
						isEU=true;
					}else {
						continue;
					}
				}
				mycurrency=market.get("currency").toString();
				for (Map<String, Object> mmap : allFinAccountList) {
					String groupid = mmap.get("groupid").toString();
					if(groupid.equals(group.getId())&&mmap.get("currency").toString().equals(mycurrency)&&"convert".equals(mmap.get("type"))) {
						BigDecimal amount =mmap.get("amount") == null ? new BigDecimal("0") : (BigDecimal) mmap.get("amount");
						BigDecimal newamount = exchangeRateHandlerService.changeCurrencyByLocal(shopid, mycurrency, "RMB", amount, 3);
				        transfer_total =transfer_total.add(amount);
				        transfer_total_new = transfer_total_new.add(newamount);
					}
				}
				for (Map<String, Object> mmap:FBAInvCostList) {
					String groupid = mmap.get("groupid").toString();
					if(groupid.equals(group.getId())&&(market.get("marketplaceid").toString().equals(mmap.get("marketplaceid").toString())||mmap.get("marketplaceid").toString().equals(market.get("region").toString()))) {
						totalprice= totalprice.add(new BigDecimal(mmap.get("totalprice").toString()));
					}
				}
				String color = "#f2f2f2";
				if (index % 2 == 1) {
					color = "#fff";
				}
				String marketname = GeneralUtil.getMarketname(mycurrency);
				String fcurrency = GeneralUtil.formatCurrency(mycurrency);
 
				String str="<td style='text-align:right;background-color:#f2f2f2'>￥"+ GeneralUtil.formatterQuantity(totalprice) + "</td>";
				buf.append("<tr style='height:50px;background-color: " + color + ";'><td>" + group.getName()
						+ "</td><td>" +  marketname+ "</td><td style='text-align:right'><span style='color: #00a65a !important;'>"
						+  fcurrency+GeneralUtil.formatterQuantity(transfer_total)
						+ "</span><br><span>￥"
						+ GeneralUtil.formatterQuantity(transfer_total_new)
						+ "</span>" + "</td>"
						+ str + "</tr>");
				index++;
			}
		}

		buf.append("</tbody>");
		buf.append("<tr style='height:30px;'><td>合计</td><td> </td><td style='text-align:right;color: #00a65a !important;'>￥"
						+ GeneralUtil.formatterQuantity(new BigDecimal(map.get("transfer_all").toString())) + "</td><td style='text-align:right;'>￥"
				        + GeneralUtil.formatterQuantity(new BigDecimal(map.get("totalprice_all").toString())) + "</td></tr>");
		buf.append("</table>");
		if (localInvCostList != null && localInvCostList.size() > 0) {
			buf.append("<br>");
			buf.append("<table style='width:700px;'>");
			buf.append("<thead><tr style='height:30px;border:1px solid #eee; background:#fafafa;color:#666'><th>本地仓库</th><th style='text-align:right;'>库存货值</th></tr></thead>");
			buf.append("<tbody>");
			BigDecimal loacalprice_all = new BigDecimal("0");
			for (int i = 0; i < localInvCostList.size(); i++) {
				Map<String, Object> mapInv = localInvCostList.get(i);
				String color = "#f2f2f2";
				if (i % 2 == 1) {
					color = "#fff";
				}
				loacalprice_all = loacalprice_all.add(new BigDecimal(mapInv.get("totalprice").toString()));
				buf.append("<tr style='height:30px;background-color: " + color + ";'><td>" + mapInv.get("warehousename")
						+ "</td><td style='text-align:right;'>￥"
						+ GeneralUtil.formatterQuantity(new BigDecimal(mapInv.get("totalprice").toString()))
						+ "</td></tr>");
			}
			buf.append("</tbody>");
			buf.append("<tr style='height:30px;'><td>合计</td><td style='text-align:right;'>￥"
					+ GeneralUtil.formatterQuantity(loacalprice_all) + "</td></tr>");
			buf.append("</table>");
		}
		buf.append("</div></div>");
		buf.append("<div style='width:100%;height:1px;background:#eee'></div>");
		buf.append("<div style='padding:30px 30px'>");
		buf.append("<div>");
		buf.append("<span style='color: rgb(192, 192, 192); font-size: x-small;'> 如果您有任何关于用户体验方面的问题或建议，都可联系我们：service@wimoor.com </span>");
		buf.append("</div>");
		buf.append("<div>");
		buf.append("<span style='color: rgb(192, 192, 192); font-size: x-small;'> 如有其它疑问，请拨打wimoor官方热线 86-13554833402获得更多帮助,Copyright © 2017 深圳市万墨信息科技有限公司版权所有   </span>");
		buf.append("</div>");
		buf.append("</div>");
		buf.append("</div>");
	}
	

	public void sendWeekEmailDetailTask() {
		Calendar c=Calendar.getInstance();
		int j = c.get(Calendar.DAY_OF_WEEK);
		if(j!=Calendar.MONDAY) {
			return;
		}
		sendWeekEmailDetail() ;
	}

	public void sendWeekEmailDetail() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<String> list = amazonAuthorityService.getAllBossEmalShop();
				for(String shopid:list) {
					try {
						sendWeekEmailDetail(shopid);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}}).start();
	}
	
	public void sendMonthEmailDetailTask() {
		Calendar c=Calendar.getInstance();
		int j = c.get(Calendar.DAY_OF_MONTH);
		if(j!=1) {
			return;
		}
		sendMonthEmailDetail();
	
	}
	public void sendMonthEmailDetail() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<String> list = amazonAuthorityService.getAllBossEmalShop();
				for(String shopid:list) {
					try {
						sendMonthEmailDetail(shopid);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
			}}).start();
		
		
	}
	public void sendWeekEmailDetail(String shopid) {
		String photoPath = fileUpload.photoServer;
	    SysEmailDTO dto=new SysEmailDTO();
	    dto.setToCompany(shopid);
	    dto.setSubject("Wimoor每周财务汇总");
		StringBuffer buf = new StringBuffer();
			buf.append("<includetail>");
			buf.append("<div style='font:Verdana normal 14px;color:#000;'>");
			buf.append("<div style='position:relative;'>");
			buf.append("<div style='background: rgb(239, 239, 239); padding: 40px 0px;'>");
			buf.append("<div width='800px'>");
			buf.append("<div style='width:800px;max-device-width: 800px;margin:0px auto;background:#fff;border-top-radius:4px;'>");
			buf.append("<div style='line-height: 0;'><img src='" + photoPath
					+ "/temp/header.png' modifysize='71%' diffpixels='15px'></div>");
			buf.append("<div style='padding: 50px;'>");
			buf.append("<img src='" + photoPath + "/temp/logo.png'>");
			buf.append("<div style='text-align: left;font-size:12px;color:#666;margin-top:8px;'>");
			buf.append("&nbsp;万&nbsp; &nbsp;墨&nbsp; &nbsp;信&nbsp; &nbsp;息&nbsp; &nbsp;科&nbsp; &nbsp;技");
			buf.append("<span style='color: rgb(0, 0, 0); font-size: 14px;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span>");
			buf.append("</div>");
			buf.append("<div style='text-align: left;font-size:14px;color:#666;margin-top:30px;'>");
//			buf.append("<div style='margin-bottom:15px'>很抱歉，之前发送的财务汇总邮件出现了错误数据，不便之处敬请谅解。这是最新的财务汇总，请查收。</div>");
			buf.append("<h2 style='margin:0px'>财务一览</h2>");
			buf.append("<div style='color: #999;font-size:12px;'>时间："
//					+ "2020年02月01日-2020年02月29日"
					+ GeneralUtil.formatDate(GeneralUtil.getSevenDayBefore(new Date()), "yyyy年MM月dd日") + "-"
					+ GeneralUtil.formatDate(GeneralUtil.getYesterday(new Date()), "yyyy年MM月dd日") + "</div>");
			buf.append("<br>");
			
			String beginDate = GeneralUtil.formatDate(GeneralUtil.getSevenDayBefore(new Date()), "yyyy-MM-dd");
			String endDate = GeneralUtil.formatDate(new Date(), "yyyy-MM-dd");
			getFinEmailContent(buf, shopid, beginDate, endDate);
			
			buf.append("<div style='text-align: center;line-height: 0;'><img src='" + photoPath + "/temp/footer.png'><br><br></div> ");
			buf.append("</div> </div> </div> </div>");
			buf.append("<!--<![endif]-->");
			buf.append("</includetail>");
			dto.setContent(buf.toString());
			this.adminClientOneFeignManager.sendBoss(dto);
		
	}


	
	public void sendMonthEmailDetail(String shopid) {
		String photoPath = fileUpload.photoServer;
	    SysEmailDTO dto=new SysEmailDTO();
        dto.setToCompany(shopid);
	    dto.setSubject("Wimoor每月财务汇总");
		StringBuffer buf = new StringBuffer();
		buf.append("<includetail>");
		buf.append("<div style='font:Verdana normal 14px;color:#000;'>");
		buf.append("<div style='position:relative;'>");
		buf.append("<div style='background: rgb(239, 239, 239); padding: 40px 0px;'>");
		buf.append("<div width='800px'>");
		buf.append("<div style='width:800px;max-device-width: 800px;margin:0px auto;background:#fff;border-top-radius:4px;'>");
		buf.append("<div style='line-height: 0;'><img src='" + photoPath + "/temp/header.png' modifysize='71%' diffpixels='15px'></div>");
		buf.append("<div style='padding: 50px;'>");
		buf.append("<img src='" + photoPath + "/temp/logo.png'>");
		buf.append("<div style='text-align: left;font-size:12px;color:#666;margin-top:8px;'>");
		buf.append("&nbsp;万&nbsp; &nbsp;墨&nbsp; &nbsp;信&nbsp; &nbsp;息&nbsp; &nbsp;科&nbsp; &nbsp;技");
		buf.append("<span style='color: rgb(0, 0, 0); font-size: 14px;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;</span>");
		buf.append("</div>");
		buf.append("<div style='text-align: left;font-size:14px;color:#666;margin-top:30px;'>");
		buf.append("<h2 style='margin:0px'>财务一览</h2>");
		buf.append("<div style='color: #999;font-size:12px;'>时间："
				+ GeneralUtil.formatDate(GeneralUtil.getFirstdayOfMonth(GeneralUtil.getLastMonthday(new Date())), "yyyy年MM月dd日")
				+ "-" + GeneralUtil.formatDate(GeneralUtil.getLastdayOfMonth(GeneralUtil.getLastMonthday(new Date())), "yyyy年MM月dd日") + "</div>");
		buf.append("<br>");
		
		String beginDate = GeneralUtil.formatDate(GeneralUtil.getFirstdayOfMonth(GeneralUtil.getLastMonthday(new Date())), "yyyy-MM-dd");
		String endDate = GeneralUtil.formatDate(GeneralUtil.getFirstdayOfMonth(new Date()), "yyyy-MM-dd");
		getFinEmailContent(buf, shopid, beginDate, endDate);
		buf.append("<div style='text-align: center;line-height: 0;'><img src='" + photoPath + "/temp/footer.png'><br><br></div> ");
		buf.append("</div> </div> </div> </div>");
		buf.append("<!--<![endif]-->");
		buf.append("</includetail>");
		dto.setContent(buf.toString());
		this.adminClientOneFeignManager.sendBoss(dto);
		
	}

}
