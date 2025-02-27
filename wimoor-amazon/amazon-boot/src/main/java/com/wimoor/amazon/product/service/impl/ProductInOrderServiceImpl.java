package com.wimoor.amazon.product.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.orders.pojo.entity.OrdersSummary;
import com.wimoor.amazon.product.mapper.ProductInOptMapper;
import com.wimoor.amazon.product.mapper.ProductInOrderMapper;
import com.wimoor.amazon.product.mapper.ProductRankMapper;
import com.wimoor.amazon.product.pojo.entity.AmzProductPageviews;
import com.wimoor.amazon.product.pojo.entity.ProductInOrder;
import com.wimoor.amazon.product.pojo.entity.ProductRank;
import com.wimoor.amazon.product.service.IAmzProductPageviewsService;
import com.wimoor.amazon.product.service.IProductInOrderService;
import com.wimoor.amazon.summary.service.IOrdersSumService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 产品信息的订单销售数据 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
@Service
@RequiredArgsConstructor
public class ProductInOrderServiceImpl extends ServiceImpl<ProductInOrderMapper, ProductInOrder> implements IProductInOrderService {
	
	final ProductRankMapper productRankMapper;
	final IAmzProductPageviewsService iAmzProductPageviewsService;
	final IAmazonAuthorityService iAmazonAuthorityService;
	final IOrdersSumService iOrdersSumService;
	final ProductInOptMapper productInOptMapper;
	public Map<String, Object> selectDetialById(String pid,String shopid){
		return this.baseMapper.selectDetialById(pid, shopid);
	}

	@Override
	public List<Map<String, Object>> getChartData(Map<String, Integer> typesMap, Map<String, Object> parameter,UserInfo user) {
		String sku = parameter.get("sku") == null ? null : (String) parameter.get("sku");
		String ftype = parameter.get("ftype") == null ? null : (String) parameter.get("ftype");
		String marketplace = parameter.get("marketplace") == null ? null : (String) parameter.get("marketplace");
		String beginDate = parameter.get("beginDate") == null ? null : (String) parameter.get("beginDate");
		String endDate = parameter.get("endDate") == null ? null : (String) parameter.get("endDate");
		String amazonAuthId = parameter.get("amazonAuthId") == null ? null : parameter.get("amazonAuthId").toString();
		// 销量汇总
		List<AmzProductPageviews> sessionlist = null;
		List<ProductRank> rnklist = null;
		List<Map<String, Object>> advlist = null;
		List<OrdersSummary> orderlist = null;
		List<Map<String, Object>> ftypeList = null;
		List<Map<String, Object>> rankList = null;
		if (typesMap.containsKey("rnk")) {// sales rank
			rnklist = this.productRank(sku, marketplace, beginDate, endDate, amazonAuthId, user, ftype);
		}
		if (typesMap.containsKey("rnks")) {// sales rank 提取多条分类排名
			ftypeList = this.getCountRankBySku(sku, marketplace, amazonAuthId, user);
			if (ftypeList != null && ftypeList.size() > 0) {
				rankList = this.getRankBySku(sku, marketplace, beginDate, endDate, amazonAuthId, user);
				for (int i = 0; i < ftypeList.size(); i++) {
					typesMap.put(ftypeList.get(i).get("name").toString(), i + 1);
				}
			} else {
				rankList = new ArrayList<Map<String, Object>>();
				typesMap.put("销售排名", 1);
			}
		}
		if (typesMap.containsKey("uns") || typesMap.containsKey("ods") 
				|| typesMap.containsKey("pts") || typesMap.containsKey("aups")) {
			// total order item 订单量 // units orders 销售数量, 商品总销售额, 广告销量占比
			orderlist = iOrdersSumService.orderSummaryBySkuDate(sku, marketplace, beginDate, endDate, amazonAuthId, user, ftype);
		}
		if (typesMap.containsKey("cks")// clicks 点击量
				|| typesMap.containsKey("imp") // impressions 广告展示量
				|| typesMap.containsKey("ctr") // 广告点击率ctr
				|| typesMap.containsKey("spd") // adv spend 广告费用
				|| typesMap.containsKey("cpc") // CPC
				|| typesMap.containsKey("cr") // total order/click 转化率
				|| typesMap.containsKey("acos") // total order/click 转化率
				|| typesMap.containsKey("tos") // total sales广告销售额
				|| typesMap.containsKey("acoas") 
				|| typesMap.containsKey("aus") // 广告销量
				|| typesMap.containsKey("aups")) {// 广告销量占比
			advlist = this.advInfo(sku, marketplace, beginDate, endDate, amazonAuthId, user, ftype);
			if (typesMap.containsKey("acoas") && orderlist == null) {
				orderlist = iOrdersSumService.orderSummaryBySkuDate(sku, marketplace, beginDate, endDate, amazonAuthId, user, ftype);
			}
		}
		if (typesMap.containsKey("ses")// session 页面访问量
				|| typesMap.containsKey("pgv") // pageview 页面浏览量,
				|| typesMap.containsKey("bbp") || typesMap.containsKey("osp")) {// Unit_Session_Percentage
			sessionlist = this.sessionPage(sku, marketplace, beginDate, endDate, amazonAuthId, user, ftype);
		}

		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (Entry<String, Integer> typeentry : typesMap.entrySet()) {
			ftype = typeentry.getKey();
			Map<String, Object> map = new HashMap<String, Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
			Map<String, Object> tempmap = new HashMap<String, Object>();
			if ("ses".equals(ftype)) {// session 页面访问量
				for (int i = 0; i < sessionlist.size(); i++) {
					AmzProductPageviews item = sessionlist.get(i);
					tempmap.put(sdf.format(GeneralUtil.getDate(item.getByday())), item.getSessions());
				}
				map.put("name", "访问人数");
			} else if ("pgv".equals(ftype)) {// pageview 页面浏览量
				for (int i = 0; i < sessionlist.size(); i++) {
					AmzProductPageviews item = sessionlist.get(i);
					tempmap.put(sdf.format(GeneralUtil.getDate(item.getByday())), item.getPageViews());
				}
				map.put("name", "页面浏览数量");
			} else if ("bbp".equals(ftype)) {// BuyBox percentage 购物车比例
				for (int i = 0; i < sessionlist.size(); i++) {
					AmzProductPageviews item = sessionlist.get(i);
					tempmap.put(sdf.format(GeneralUtil.getDate(item.getByday())), item.getBuyBoxPercentage());
				}
				map.put("name", "购物车比例");
			} else if ("osp".equals(ftype)) {// Unit_Session_Percentage 销售转化率
				for (int i = 0; i < sessionlist.size(); i++) {
					AmzProductPageviews item = sessionlist.get(i);
					tempmap.put(sdf.format(GeneralUtil.getDate(item.getByday())), item.getUnitSessionPercentage());
				}
				map.put("name", "销售转化率");
			} else if ("uns".equals(ftype)) {// units orders 销售数量
				for (int i = 0; i < orderlist.size(); i++) {
					OrdersSummary item = orderlist.get(i);
					tempmap.put(sdf.format(item.getPurchaseDate()), item.getQuantity());
				}
				map.put("name", "销量");
			} else if ("pts".equals(ftype)) {// 商品总销售额
				for (int i = 0; i < orderlist.size(); i++) {
					OrdersSummary item = orderlist.get(i);
					tempmap.put(sdf.format(item.getPurchaseDate()), item.getOrderprice());
				}
				map.put("name", "销售额");
			} else if ("ods".equals(ftype)) {// total order item 订单量
				for (int i = 0; i < orderlist.size(); i++) {
					OrdersSummary item = orderlist.get(i);
					tempmap.put(sdf.format(item.getPurchaseDate()), item.getOrdersum());
				}
				map.put("name", "销售订单");
			} else if (ftype.equals("rnk")) {
				for (int i = 0; i < rnklist.size(); i++) {
					ProductRank item = rnklist.get(i);
					tempmap.put(sdf.format(item.getByday()), item.getRank());
				}
				map.put("name", "销量排名");
			} else if (typesMap.containsKey("rnks")) {
				if (ftype.equals("rnks")) {
					continue;
				}
				for (Map<String, Object> rankMap : rankList) {
					String rankName = rankMap.get("name").toString();
					if (ftype.equals(rankName)) {
						tempmap.put(sdf.format(rankMap.get("byday")), rankMap.get("rank"));
					}
					continue;
				}
				map.put("name", ftype);
			} else {
				if ("acoas".equals(ftype)) {
					long diff = GeneralUtil.getDatez(endDate).getTime() - GeneralUtil.getDatez(beginDate).getTime();// 这样得到的差值是微秒级别
					long daysize = diff / (1000 * 60 * 60 * 24);
					Calendar c = Calendar.getInstance();
					c.setTime(GeneralUtil.getDatez(beginDate));
					Map<String, Object> costmap = new HashMap<String, Object>();
					Map<String, Object> salesmap = new HashMap<String, Object>();
					for (int i = 0; i < advlist.size(); i++) {
						Map<String, Object> item = advlist.get(i);
						costmap.put(sdf.format(item.get("bydate")), item.get("spd"));
					}
					for (int i = 0; i < orderlist.size(); i++) {
						OrdersSummary item = orderlist.get(i);
						salesmap.put(sdf.format(item.getPurchaseDate()), item.getOrderprice());
					}
					for (int i = 1; i <= daysize; i++, c.add(Calendar.DATE, 1)) {
						String tempkey = sdf.format(c.getTime());
						BigDecimal sales = new BigDecimal("0");
						Object obj = salesmap.get(tempkey);
						if (obj != null) {
							sales = new BigDecimal(obj.toString());
						}
						BigDecimal cost = new BigDecimal("0");
						Object obj2 = costmap.get(tempkey);
						if (obj2 != null) {
							cost = new BigDecimal(obj2.toString());
						}
						BigDecimal acoas = new BigDecimal("0");
						if (sales.compareTo(new BigDecimal("0")) != 0) {
							acoas = cost.multiply(new BigDecimal("100")).divide(sales, 2, RoundingMode.HALF_DOWN);
						}
						tempmap.put(tempkey, acoas);
					}
				} else if ("aups".equals(ftype)) {//广告销量占比=广告订单销量/总销量
					long diff = GeneralUtil.getDatez(endDate).getTime() - GeneralUtil.getDatez(beginDate).getTime();// 这样得到的差值是微秒级别
					long daysize = diff / (1000 * 60 * 60 * 24);
					Calendar c = Calendar.getInstance();
					c.setTime(GeneralUtil.getDatez(beginDate));
					Map<String, Object> adUnitsmap = new HashMap<String, Object>();
					Map<String, Object> totalUnitsmap = new HashMap<String, Object>();
					for (int i = 0; i < advlist.size(); i++) {
						Map<String, Object> item = advlist.get(i);
						adUnitsmap.put(sdf.format(item.get("bydate")), item.get("aus"));
					}
					for (int i = 0; i < orderlist.size(); i++) {
						OrdersSummary item = orderlist.get(i);
						totalUnitsmap.put(sdf.format(item.getPurchaseDate()), item.getQuantity());
					}
					for (int i = 1; i <= daysize; i++, c.add(Calendar.DATE, 1)) {
						String tempkey = sdf.format(c.getTime());
						BigDecimal totalUnits = new BigDecimal("0");
						Object obj = totalUnitsmap.get(tempkey);
						if (obj != null) {
							totalUnits = new BigDecimal(obj.toString());
						}
						BigDecimal adUnits = new BigDecimal("0");
						Object obj2 = adUnitsmap.get(tempkey);
						if (obj2 != null) {
							adUnits = new BigDecimal(obj2.toString());
						}
						BigDecimal aups = new BigDecimal("0");
						if (totalUnits.compareTo(new BigDecimal("0")) != 0) {
							aups = adUnits.multiply(new BigDecimal("100")).divide(totalUnits, 2, RoundingMode.HALF_EVEN);
						}
						tempmap.put(tempkey, aups);
					}
					map.put("name", "广告销量占比");
				} else if(advlist!=null) {
					for (int i = 0; i < advlist.size(); i++) {
						Map<String, Object> item = advlist.get(i);
						tempmap.put(sdf.format(item.get("bydate")), item.get(ftype));
					}
				}
				if (ftype.equals("cks")) {
					map.put("name", "广告点击量");
				} else if (ftype.equals("imp")) {
					map.put("name", "广告展示量");
				} else if (ftype.equals("ctr")) {
					map.put("name", "广告点击率");
				} else if (ftype.equals("spd")) {
					map.put("name", "广告花费");
				} else if (ftype.equals("cpc")) {
					map.put("name", "广告点击花费");
				} else if (ftype.equals("acos")) {
					map.put("name", "Acos");
				} else if (ftype.equals("acoas")) {
					map.put("name", "AcoAs");
				} else if (ftype.equals("cr")) {
					map.put("name", "广告转化率");
				} else if (ftype.equals("tos")) {
					map.put("name", "广告销售额");
				} else if (ftype.equals("aus")) {
					map.put("name", "广告销量");
				}
			}
			long diff = GeneralUtil.getDatez(endDate).getTime() - GeneralUtil.getDatez(beginDate).getTime();// 这样得到的差值是微秒级别
			long daysize = diff / (1000 * 60 * 60 * 24);
			Calendar c = Calendar.getInstance();
			c.setTime(GeneralUtil.getDatez(beginDate));
			BigDecimal summary = new BigDecimal("0");
			List<String> listLabel = new ArrayList<String>();
			List<String> listData = new ArrayList<String>();
			for (int i = 1; i <= daysize+1; i++, c.add(Calendar.DATE, 1)) {
				String tempkey = sdf.format(c.getTime());
				String value = tempmap.get(tempkey) == null ? "0" : tempmap.get(tempkey).toString();
				listLabel.add(tempkey);
				listData.add(value);
				summary = summary.add(new BigDecimal(value));
			}
			map.put("summary", summary);
			map.put("labels", listLabel);
			map.put("data", listData);
			listMap.add(map);
		}
		return listMap;
	}
	
	private List<ProductRank> productRank(String sku, String marketplace, String beginDate, String endDate, String amazonAuthId, UserInfo user, String ftype) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sku", sku);
		param.put("marketplaceid", marketplace);
		param.put("begindate", beginDate);
		param.put("enddate", endDate);
		param.put("amazonAuthId", amazonAuthId);
		param.put("shopid", user.getCompanyid());
		List<ProductRank> list = productRankMapper.selectBySku(param);
		return list;
	}

	private List<Map<String, Object>> getCountRankBySku(String sku, String marketplace, String amazonAuthId, UserInfo user) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sku", sku);
		param.put("marketplaceid", marketplace);
		param.put("amazonAuthId", amazonAuthId);
		param.put("shopid", user.getCompanyid());
		List<Map<String, Object>> list = productRankMapper.selectCountRankBySku(param);
		return getProductRank(list);
	}

	private List<Map<String, Object>> getRankBySku(String sku, String marketplace, String beginDate, String endDate, String amazonAuthId, UserInfo user) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sku", sku);
		param.put("marketplaceid", marketplace);
		param.put("begindate", beginDate);
		param.put("enddate", endDate);
		param.put("amazonAuthId", amazonAuthId);
		param.put("shopid", user.getCompanyid());
		List<Map<String, Object>> list = productRankMapper.selectRankBySku(param);
		return getProductRank(list);
	}

	private List<Map<String, Object>> advInfo(String sku, String marketplace, String beginDate, String endDate, String amazonAuthId, UserInfo user, String ftype) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sku", sku);
		param.put("marketplaceid", marketplace);
		param.put("startdate", beginDate);
		param.put("enddate", endDate);
		param.put("amazonAuthId", amazonAuthId);
		param.put("shopid", user.getCompanyid());
		List<Map<String, Object>> list = null;
		//这里应该使用feign调用
		AmazonAuthority auth = iAmazonAuthorityService.getById(amazonAuthId);
		param.put("sellerid", auth.getSellerid());
		list=productInOptMapper.findAdvert(param);
		return list;
	}
	
	public List<Map<String, Object>> getProductRank(List<Map<String, Object>> list) {
		if (list != null) {
			Iterator<Map<String, Object>> iterator = list.iterator();
			while (iterator.hasNext()) {
				Map<String, Object> map = iterator.next();
				Object name = map.get("name");
				if (name == null) {
					Object categoryId = map.get("categoryId");
					try {
						new BigInteger(categoryId.toString());
						iterator.remove();
					} catch (Exception e) {
						String strName = categoryId.toString().replace("_display_on_website", "").replace("_and_", "&").replace("_", " ");
						String[] categoryName = strName.toString().split("&");
						String str = "";
						for (int i = 0; i < categoryName.length; i++) {
							String str1 = categoryName[i].substring(0, 1).toUpperCase() + categoryName[i].substring(1);
							if (i == categoryName.length - 1) {
								str += str1;
							} else {
								str += str1 + " & ";
							}
						}
						map.put("name", str);
					}
				}
			}
		}
		return list;
	}
	
	public List<AmzProductPageviews> sessionPage(String sku, String marketplaceid, String startdate, String enddate, String amazonAuthId, UserInfo user, String ftype) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("sku", sku);
		param.put("marketplaceid", marketplaceid);
		param.put("startDate", startdate);
		param.put("endDate", enddate);
		param.put("amazonAuthId", amazonAuthId);
		param.put("shopid", user.getCompanyid());
		List<AmzProductPageviews> list = iAmzProductPageviewsService.findPageviews(param);
		return list;
	}
}
