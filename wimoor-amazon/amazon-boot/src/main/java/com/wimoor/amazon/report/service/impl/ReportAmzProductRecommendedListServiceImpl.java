package com.wimoor.amazon.report.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.amazon.profit.service.IProfitService;
import com.wimoor.amazon.profit.service.IReferralFeeService;
import com.wimoor.amazon.report.mapper.ProductRecommendedExtMapper;
import com.wimoor.amazon.report.mapper.ProductRecommendedMapper;
import com.wimoor.amazon.report.pojo.entity.ProductRecommended;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.report.service.IReportAmzProductRecommendedListService;
import com.wimoor.common.GeneralUtil;

import cn.hutool.core.util.StrUtil;

 

@Service("reportAmzProductRecommendedListService")
public class ReportAmzProductRecommendedListServiceImpl extends ReportServiceImpl implements IReportAmzProductRecommendedListService {

	@Resource
	private ProductRecommendedMapper productRecommendedMapper;
	@Resource
	private ProductRecommendedExtMapper  productRecommendedExtMapper;
	@Resource
	private IProductInfoService productInfoService;
	@Resource
	public IReferralFeeService referralFeeService;
	@Resource
	private IAmazonAuthorityService amazonAuthorityService;
	@Resource
	IProfitCfgService profitCfgService;
	@Resource
	IProfitService profitService;
	@Override
	public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br)   {
		StringBuffer log = new StringBuffer();
		int lineNumber = 0;
		String line;
		try {
			while ((line = br.readLine()) != null) {
				String[] info = line.split(",");
				int length = info.length;
				String asin ="";
				String name ="";
				int start=0;
				Boolean issplitcategory=false;
				Boolean isEUpoint=false;
				if(!amazonAuthority.getMarketPlace().getRegion().equals("EU") && !amazonAuthority.getMarketPlace().getRegion().equals("UK")) {
					//非欧洲站点处理逻辑
					if(length!=25) {
						if(line!=null&&line.contains("Sorry")) {
							return line;
						}
						asin=line.split(",\"")[0];
						name=line.split(",\"")[1];
						if(StrUtil.isEmpty(line.split(",\"")[2])){
							line=line.replace(asin+",\""+name+",\""+",", "");
						}else {
							line=line.replace(asin+",\""+name+",", "");
						}
						name=name.substring(0,name.length()-1);
						info = line.split(",");
						if(info.length!=23) {
							issplitcategory=true;
						}
						start=0;
					}else{
						asin = GeneralUtil.getIndexString(info, 0);
						name = GeneralUtil.getIndexString(info, 1); 
						start=2;
					}
				}else {
					//欧洲站点处理逻辑
					isEUpoint=true;
					if(length!=26) {
						if(line!=null&&line.contains("Sorry")) {
							return line;
						}
						asin=line.split(",\"")[0];
						name=line.split(",\"")[1];
						if(StrUtil.isEmpty(line.split(",\"")[2])){
							line=line.replace(asin+",\""+name+",\""+",", "");
						}else {
							line=line.replace(asin+",\""+name+",", "");
						}
						name=name.substring(0,name.length()-1);
						info = line.split(",");
						if(info.length!=24) {
							issplitcategory=true;
						}
						start=0;
					}else{
						asin = GeneralUtil.getIndexString(info, 0);
						name = GeneralUtil.getIndexString(info, 1); 
						start=2;
					}
				}
				
				if (lineNumber > 0) {
						/*
						 *  "ASIN",
							"Product title",
							"Link",
							"Brand",
							"Category",
							"Sub-category",
							"Lowest price in last week (USD)",
							"FBA offer? ",
							"Number of offers",
							"Amazon offer? ",
							"Number of reviews",
							"Business sales rank",
							"Business sales rank growth",
							"Page views",
							"Manufacturer part number",
							"EAN",
							"UPC",
							"Model number",
							"ISBN",
							"Brand you offer",
							"Category you offer",
							"Product performance",
							"Top sales rank",
							"Low offer count",
							"Product not yet on Amazon"
						 */
					    String Sub_category=null;
						String link= GeneralUtil.getIndexString(info,start+ 0);
						String Brand= GeneralUtil.getIndexString(info,start+ 1);
						String Category= GeneralUtil.getIndexString(info, start+2);
						if(issplitcategory) {
							Sub_category= GeneralUtil.getIndexString(info,start+3)+GeneralUtil.getIndexString(info,start+4);
							start+=1;
						}else {
							Sub_category= GeneralUtil.getIndexString(info,start+3);
						}
						BigDecimal Lowestprice = GeneralUtil.getIndexBigDecimal(info,start+ 4);
						String FBAoffer= GeneralUtil.getIndexString(info, start+5);
						String Numberofoffers= GeneralUtil.getIndexString(info, start+6);
						String Amazonoffer= GeneralUtil.getIndexString(info, start+7);
						if(isEUpoint) {
							start+=1;
						}
						String reviews=GeneralUtil.getIndexString(info, start+8);
						String rank=GeneralUtil.getIndexString(info, start+9);
						String rankgrowth= GeneralUtil.getIndexString(info,start+ 10);
						String Pageviews= GeneralUtil.getIndexString(info, start+11);
						String Manufacturernumber= GeneralUtil.getIndexString(info, start+12);
						String EAN= GeneralUtil.getIndexString(info, start+13);
						String UPC= GeneralUtil.getIndexString(info, start+14);
						String Modelnumber= GeneralUtil.getIndexString(info, start+15);
						String ISBN= GeneralUtil.getIndexString(info, start+16);
						String Brandoffer= GeneralUtil.getIndexString(info,start+17);
						String Categoryoffer= GeneralUtil.getIndexString(info, start+18);
						String performance= GeneralUtil.getIndexString(info, start+19);
						String toprank= GeneralUtil.getIndexString(info, start+20);
						String lowcount= GeneralUtil.getIndexString(info, start+21);
						String onamazon= GeneralUtil.getIndexString(info, start+22);
						
					if(name!=null && name.length()>=1000){
						name = name.substring(0, 1000);
					} 
					if (StrUtil.isEmpty(asin)) {
						continue;
					}
					ProductRecommended productRecommend = new ProductRecommended();
					productRecommend.setRefreshtime(new Date());
					productRecommend.setAmazonauthid(amazonAuthority.getId());
					productRecommend.setMarketplaceid(amazonAuthority.getMarketPlace().getMarketplaceid());
					productRecommend.setAsin(replaceStrquot(asin));
					productRecommend.setName(replaceStrquot(name));
					productRecommend.setBrand(replaceStrquot(Brand));
					productRecommend.setEan(replaceStrquot(EAN));
					productRecommend.setCategory(replaceStrquot(Category));
					productRecommend.setLink(replaceStrquot(link));
					productRecommend.setLowestprice(Lowestprice);
					productRecommend.setManufacturerPartNumber(replaceStrquot(Manufacturernumber));
					productRecommend.setModelNumber(replaceStrquot(Modelnumber));
					if(StrUtil.isNotEmpty(Numberofoffers)) {
						if(Numberofoffers.matches(".*[a-z]+.*")) {
							productRecommend.setOffers(null);
						}else {
							productRecommend.setOffers(Integer.parseInt(Numberofoffers));
						}
					}else {
						productRecommend.setOffers(null);
					}
					if(StrUtil.isNotEmpty(rank)) {
						if(rank.matches(".*[a-z]+.*")) {
							productRecommend.setRank(null);
						}else {
							productRecommend.setRank(Integer.parseInt(rank));
						}
					}else {
						productRecommend.setOffers(null);
					}
					if(StrUtil.isNotEmpty(reviews)) {
						if(reviews.matches(".*[a-z]+.*")) {
							productRecommend.setReviews(null);
						}else {
							productRecommend.setReviews(Integer.parseInt(reviews));
						}
					}else {
						productRecommend.setOffers(null);
					}
					productRecommend.setPageViews(replaceStrquot(Pageviews));
					productRecommend.setPerformance(replaceStrquot(performance));
					productRecommend.setSalesRankGrowth(replaceStrquot(rankgrowth));
					productRecommend.setSubcategory(replaceStrquot(Sub_category));
					productRecommend.setUpc(replaceStrquot(UPC));
					if("Yes".equals(onamazon)) {
						productRecommend.setOnamazon(true);
					}else {
						productRecommend.setOnamazon(false);
					}
					if("Yes".equals(lowcount)) {
						productRecommend.setIslowprice(true);
					}else {
						productRecommend.setIslowprice(false);
					}
					if("Yes".equals(toprank)) {
						productRecommend.setIstoprank(true);
					}else {
						productRecommend.setIstoprank(false);
					}
					if("Yes".equals(Categoryoffer)) {
						productRecommend.setCategoryoffer(true);
					}else {
						productRecommend.setCategoryoffer(false);
					}
					if("Yes".equals(FBAoffer)) {
						productRecommend.setFbaoffer(true);
					}else {
						productRecommend.setFbaoffer(false);
					}
					if(StrUtil.isNotEmpty(ISBN)) {
						productRecommend.setIsbn(ISBN);
					}else {
						productRecommend.setIsbn(null);
					}
					if("Yes".equals(Brandoffer)) {
						productRecommend.setBrandoffer(true);
					}else {
						productRecommend.setBrandoffer(false);
					}
					if("Yes".equals(Amazonoffer)) {
						productRecommend.setAmzoffer(true);
					}else {
						productRecommend.setAmzoffer(false);
					}
					try {
						QueryWrapper<ProductRecommended> query = new QueryWrapper<ProductRecommended>();
						query.eq("marketplaceid", amazonAuthority.getMarketPlace().getMarketplaceid());
						query.eq("amazonauthid", amazonAuthority.getId());
						query.eq("asin", productRecommend.getAsin());
						ProductRecommended oldone = productRecommendedMapper.selectOne(query);
						int result = 0;
						if(oldone==null) {
							result=productRecommendedMapper.insert(productRecommend);
						}else {
							productRecommend.setId(oldone.getId());
							productRecommend.setIsrefresh(oldone.getIsrefresh());
							result=productRecommendedMapper.updateById(productRecommend);
						}
						if(result>0) {
							//同时插入ext表
							//handlerExtData(productRecommend,amazonAuthority);
						}
					} catch (Exception e) {
						e.printStackTrace();
						log.append("productRecCommend 新增失败，" + e.getMessage());
					}finally {
						productRecommend=null;
					}
					 
				}
				lineNumber++;
			}

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		 
			System.gc();
		}
		return log.toString();
	}
	
//	public void handlerExtData(String sellerid, Marketplace marketplace, GetMatchingProductForIdResponse resp) {
//		if(resp!=null) {
//			AmazonAuthority amazonauth = amazonAuthorityService.selectBySellerId(sellerid);
//			List<GetMatchingProductForIdResult> list = resp.getGetMatchingProductForIdResult();
//			if(list!=null && list.size()>0) {
//				for (int i = 0; i < list.size(); i++) {
//					GetMatchingProductForIdResult result = list.get(i);
//					ProductList productlist = result.getProducts();
//					if (productlist == null) {
//						continue;
//					}
//					List<Product> listproduct = productlist.getProduct();
//					for(int j=0;j<listproduct.size();j++) {
//						Product product=listproduct.get(j);
//						if (product == null) {
//							continue;
//						}
//						String asin=product.getIdentifiers().getMarketplaceASIN().getASIN();
//						ProductRecommended productRecommend = productRecommendedMapper.findbyOne(amazonauth.getId(),marketplace.getMarketplaceid(),asin);
//						AttributeSetList attributeSet = product.getAttributeSets();
//						if (attributeSet != null && attributeSet.getAny() != null) {
//							String xml = attributeSet.toXMLFragment();
//							JAXBContext context = null;
//							try {
//								context = JAXBContext.newInstance(ItemAttributes.class);
//								Unmarshaller unmarsheller = context.createUnmarshaller();
//								ItemAttributes itemAttributeses = (ItemAttributes) unmarsheller.unmarshal(new StringReader(xml));
//								
//								String dimId=null;
//								int dimresult=0;
//								ProductRcdDimensions dim=null;
//								if (itemAttributeses != null && itemAttributeses.getPackageDimensions() != null) {
//									//先保存pkgdim对象 得到dimid
//									dimId=userService.getShortUUID().toString();
//									dim=new ProductRcdDimensions();
//									if(itemAttributeses.getPackageDimensions().getLength()!=null) {
//										if(StringUtil.isNotEmpty(itemAttributeses.getPackageDimensions().getLength().getValue())) {
//											dim.setLength(new BigDecimal(itemAttributeses.getPackageDimensions().getLength().getValue()));
//										}
//										if(StringUtil.isNotEmpty(itemAttributeses.getPackageDimensions().getLength().getUnits())) {
//											dim.setLengthUnits(itemAttributeses.getPackageDimensions().getLength().getUnits());
//										}
//									}
//									if(itemAttributeses.getPackageDimensions().getWidth()!=null) {
//										if(StringUtil.isNotEmpty(itemAttributeses.getPackageDimensions().getWidth().getValue())) {
//											dim.setWidth(new BigDecimal(itemAttributeses.getPackageDimensions().getWidth().getValue()));
//										}
//										if(StringUtil.isNotEmpty(itemAttributeses.getPackageDimensions().getWidth().getUnits())) {
//											dim.setWidthUnits(itemAttributeses.getPackageDimensions().getWidth().getUnits());
//										}
//									}
//									if(itemAttributeses.getPackageDimensions().getHeight()!=null) {
//										if(StringUtil.isNotEmpty(itemAttributeses.getPackageDimensions().getHeight().getValue())) {
//											dim.setHeight(new BigDecimal(itemAttributeses.getPackageDimensions().getHeight().getValue()));
//										}
//										if(StringUtil.isNotEmpty(itemAttributeses.getPackageDimensions().getHeight().getUnits())) {
//											dim.setHeightUnits(itemAttributeses.getPackageDimensions().getHeight().getUnits());
//										}
//									}
//									if(itemAttributeses.getPackageDimensions().getWeight()!=null) {
//										if(StringUtil.isNotEmpty(itemAttributeses.getPackageDimensions().getWeight().getValue())) {
//											dim.setWeight(new BigDecimal(itemAttributeses.getPackageDimensions().getWeight().getValue()));
//										}
//										if(StringUtil.isNotEmpty(itemAttributeses.getPackageDimensions().getWeight().getUnits())) {
//											dim.setWeightUnits(itemAttributeses.getPackageDimensions().getWeight().getUnits());
//										}
//									}
//									if(dim.getLength()!=null && dim.getWidth()!=null && dim.getHeight()!=null && dim.getWeight()!=null) {
//										dim.setId(dimId);
//										dimresult = productRcdDimensionsMapper.insertSelective(dim);
//									}
//								}
//								ProductRecommendedExt ext=new ProductRecommendedExt();
//								ext.setRid(productRecommend.getId());
//								ext.setAsin(productRecommend.getAsin());
//								if(itemAttributeses.getProductGroup()!=null) {
//									ext.setCategory(itemAttributeses.getProductGroup());
//								}else {
//									ext.setCategory(productRecommend.getCategory());
//								}
//								if(itemAttributeses.getListPrice()==null) {
//									ext.setPrice(productRecommend.getLowestprice());
//								}else {
//									ext.setPrice(itemAttributeses.getListPrice().getAmount());
//								}
//								ext.setCurrency(marketplace.getCurrency());
//								ext.setMarketplaceid(marketplace.getMarketplaceid());
//								if(dimresult>0) {
//									ext.setDim(dimId);
//									if(ext.getPrice()!=null) {
//										//通过costDetail计算获得margin和profit set到表中
//										Map<String, Object> maps = this.calculateAmazonCostDetail(ext, dim, null, null);
//										if(maps.get("costDetail")!=null) {
//											CostDetail costdetail = (CostDetail) maps.get("costDetail");
//											//代表拿到了margin和profit
//											if(costdetail!=null) {
//												String marginStr = costdetail.getMargin();
//												marginStr = marginStr.substring(0, marginStr.indexOf("%"));
//												ext.setMargin(new BigDecimal(marginStr));
//												ext.setProfit(costdetail.getProfit());
//											}
//										}
//									}
//								}
//								ext.setImgurl(itemAttributeses.getSmallImage().getUrl());
//								ext.setRefreshtime(new Date());
//								//先删除以前的对象 再insert
//								productRecommendedExtMapper.deleteByPrimaryKey(productRecommend.getId());
//								int res = productRecommendedExtMapper.insertSelective(ext);
//								if(res>0) {
//									productRecommend.setIsrefresh(true);
//									productRecommendedMapper.updateByPrimaryKey(productRecommend);
//								}
//							} catch (JAXBException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				}
//			}
//		}
//	}
//
// 
	@Override
	public String myReportType() {
		return ReportType.ProductRecommendedListings;
	}
//	
	public String replaceStrquot(String str) {
		if(StrUtil.isNotEmpty(str) && str.length()>=3) {
			if(str.substring(0,1).equals("\"") && str.substring(str.length()-1,str.length()).equals("\"")) {
				str=str.substring(1,str.length()-1);
			}
			return str;
		}else {
			return str;
		}
	}
//
//	@Override
//	public List<ProductRecommended> findNeedRefreshList(String id, String marketplaceid) {
//		return productRecommendedMapper.findNeedRefreshList(id, marketplaceid);
//	}
//	
//	public Map<String, Object> calculateAmazonCostDetail(ProductRecommendedExt ext,ProductRcdDimensions dim, String profitCfgId, String cost_) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		BigDecimal cost = new BigDecimal("0");
//		if (cost_ != null && !"".equals(cost_)) {
//			cost = new BigDecimal(cost_);
//		}
//		if(StringUtil.isEmpty(profitCfgId)) {
//			profitCfgId="26138972982277800";
//		}
//		ProfitConfig profitcfg = profitCfgService.findConfigAction(profitCfgId);
//		BigDecimal shipment = null;
//		BigDecimal price = ext.getPrice();
//		if (price==null || price.compareTo(BigDecimal.ZERO)==-1) {
//			map.put("costDetail", null);
//			map.put("msg", "产品暂时没有售价信息，不符合计算条件！");
//			return map;
//		}
//		String currency = ext.getCurrency();
//		Marketplace marketplace = marketplaceService.selectByPKey(ext.getMarketplaceid());
//		String country = marketplace.getMarket();
//		InputDimensions inputDimensions = null;
//		if (dim != null) {
//			inputDimensions = InputDimensions.getTrueInputDimesionsDivH(dim.getDimensions());
//		}else if(ext.getDim()!=null && dim==null) {
//			ProductRcdDimensions rcddim = productRcdDimensionsMapper.selectByPrimaryKey(ext.getDim());
//			inputDimensions = InputDimensions.getTrueInputDimesionsDivH(rcddim.getDimensions());
//		} else {
//			map.put("costDetail", null);
//			map.put("msg", "产品没有长宽高等信息，不符合计算条件！");
//			return map;
//		}
//		if (inputDimensions.getWeight() == null) {
//			map.put("costDetail", null);
//			map.put("msg", "产品没有重量信息，不符合计算条件！");
//			return map;
//		}
//		int typeId = 41;
//		String category = ext.getCategory();
//		List<ReferralFee> reflist = referralFeeService.findAllType();
//		if (reflist != null && reflist.size()>0) {
//			for(int i=0;i<reflist.size();i++) {
//				String[] categoryStr = category.split(" and ");
//				String types = reflist.get(i).getType();
//				for(int j=0;j<categoryStr.length;j++) {
//					if(types.contains(categoryStr[j])) {
//						typeId = reflist.get(i).getId();
//						category=categoryStr[j];
//						break;
//					}
//				}
//			}
//			if (typeId == 0) {
//				typeId = 41;// 如果表中没有对应ReferralfeeId ，默认和everything else 匹配
//			}
//		}
//		String isMedia = this.profitService.isMedia(typeId);// 是否为媒介
//		CostDetail costDetail = null;
//		try {
//			costDetail = profitService.initCostDetail(country, profitcfg, inputDimensions, isMedia, category, typeId, cost,
//					shipment, currency, null, "national", null, "", null, null, null, null, false);
//		} catch (BaseException e) {
//			e.printStackTrace();
//			map.put("costDetail", null);
//			map.put("msg", e);
//			return map;
//		}
//		costDetail.setSellingPrice(price);
//		costDetail = profitService.getCostDetail(costDetail, typeId, null, false,profitcfg);
//		map.put("costDetail", costDetail);
//		map.put("msg", "ok");
//		return map;
//	}
//	
//	@Override
//	@Cacheable(value = "materialListCache")
//	public PageList<Map<String, Object>> getProductRecommendByGrouplist(Map<String, Object> map, PageBounds pageBounds) {
//		return productRecommendedMapper.selectProductRecommendByGrouplist(map,pageBounds);
//	}

	@Override
	public List<ProductRecommended> findNeedRefreshList(String id, String marketplaceid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Map<String, Object>> getProductRecommendByGrouplist(IPage<?> page, Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

}
