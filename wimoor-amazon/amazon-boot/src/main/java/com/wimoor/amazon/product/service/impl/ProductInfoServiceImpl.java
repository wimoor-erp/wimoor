package com.wimoor.amazon.product.service.impl;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.api.AdminClientOneFeignManager;
import com.wimoor.amazon.api.ErpClientOneFeignManager;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.common.mapper.AmzAmountDescriptionMapper;
import com.wimoor.amazon.common.mapper.DimensionsInfoMapper;
import com.wimoor.amazon.common.pojo.entity.AmzAmountDescription;
import com.wimoor.amazon.common.pojo.entity.DimensionsInfo;
import com.wimoor.amazon.common.service.IExchangeRateHandlerService;
import com.wimoor.amazon.feed.pojo.entity.AmzSubmitFeedQueue;
import com.wimoor.amazon.feed.service.ISubmitfeedService;
import com.wimoor.amazon.feed.service.impl.FeedFileProductPriceXML;
import com.wimoor.amazon.finances.mapper.FBAStorageFeeReportMapper;
import com.wimoor.amazon.finances.pojo.entity.FBAStorageFeeReport;
import com.wimoor.amazon.finances.pojo.entity.OrdersFinancial;
import com.wimoor.amazon.finances.service.IAmzFinUserItemDataService;
import com.wimoor.amazon.inbound.service.IShipInboundTransService;
import com.wimoor.amazon.inventory.mapper.AmzInventoryCountryReportMapper;
import com.wimoor.amazon.inventory.pojo.entity.AmzInventoryCountryReport;
import com.wimoor.amazon.orders.service.IOrderManagerService;
import com.wimoor.amazon.product.mapper.AmzProductPriceOptMapper;
import com.wimoor.amazon.product.mapper.FollowOfferChangeMapper;
import com.wimoor.amazon.product.mapper.ProductFollowMapper;
import com.wimoor.amazon.product.mapper.ProductInOptMapper;
import com.wimoor.amazon.product.mapper.ProductInOrderMapper;
import com.wimoor.amazon.product.mapper.ProductInProfitMapper;
import com.wimoor.amazon.product.mapper.ProductInTagsMapper;
import com.wimoor.amazon.product.mapper.ProductInfoMapper;
import com.wimoor.amazon.product.mapper.ProductInfoStatusDefineMapper;
import com.wimoor.amazon.product.mapper.ProductPriceLockedMapper;
import com.wimoor.amazon.product.mapper.ProductPriceMapper;
import com.wimoor.amazon.product.mapper.ProductRemarkHistoryMapper;
import com.wimoor.amazon.product.pojo.dto.ProductListDTO;
import com.wimoor.amazon.product.pojo.dto.ProductListQuery;
import com.wimoor.amazon.product.pojo.entity.AmzProductPriceOpt;
import com.wimoor.amazon.product.pojo.entity.ProductFollow;
import com.wimoor.amazon.product.pojo.entity.ProductInOpt;
import com.wimoor.amazon.product.pojo.entity.ProductInOrder;
import com.wimoor.amazon.product.pojo.entity.ProductInProfit;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.pojo.entity.ProductInfoStatusDefine;
import com.wimoor.amazon.product.pojo.entity.ProductPrice;
import com.wimoor.amazon.product.pojo.entity.ProductPriceLocked;
import com.wimoor.amazon.product.pojo.entity.ProductRemarkHistory;
import com.wimoor.amazon.product.pojo.vo.AmzProductListVo;
import com.wimoor.amazon.product.pojo.vo.ProductInfoListVo;
import com.wimoor.amazon.product.service.IProductInOptService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.amazon.profit.pojo.entity.ProfitConfig;
import com.wimoor.amazon.profit.pojo.vo.CostDetail;
import com.wimoor.amazon.profit.service.ICalculateProfitService;
import com.wimoor.amazon.profit.service.IProfitCfgService;
import com.wimoor.amazon.profit.service.IProfitService;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.user.UserInfo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import feign.FeignException;

/**
 * <p>
 * 产品信息 服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-27
 */
@Service
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements IProductInfoService {
    @Autowired
    IProductInOptService iProductInOptService;
    @Autowired
    IAmazonAuthorityService amazonAuthorityService;
    @Autowired
    ErpClientOneFeignManager erpClientOneFeign;
    @Autowired
    AdminClientOneFeignManager adminClientOneFeign;
    @Autowired
    ProductInfoStatusDefineMapper productInfoStatusDefineMapper;
    @Autowired
    ProductPriceLockedMapper productPriceLockedMapper;
    @Autowired
    IMarketplaceService marketplaceService;
    @Autowired
    ISubmitfeedService submitfeedService;
    @Autowired
    ProductPriceMapper productPriceMapper;
    @Autowired
    ProductInOptMapper productInOptMapper;
    @Autowired
    AmzProductPriceOptMapper amzProductPriceOptMapper;
    @Autowired
    ProductRemarkHistoryMapper productRemarkHistoryMapper;
    @Autowired
    AmzInventoryCountryReportMapper amzInventoryCountryReportMapper;
	@Autowired
	ProductInProfitMapper productInProfitMapper;
	@Autowired
	ProductInOrderMapper productInOrderMapper;
	@Autowired
	IProfitCfgService profitCfgService;
	@Autowired
	DimensionsInfoMapper dimensionsInfoMapper;
	@Autowired
	ICalculateProfitService calculateProfitService;
	@Autowired
	IProfitService profitService;
	@Autowired
	IExchangeRateHandlerService exchangeRateHandlerService;
	@Autowired
	IShipInboundTransService shipInboundTransService;
	@Autowired
	AmzAmountDescriptionMapper amzAmountDescriptionMapper;
	@Resource
	IOrderManagerService orderManagerService;
	@Autowired
	FBAStorageFeeReportMapper fBAStorageFeeReportMapper;
	@Autowired
	IAmzFinUserItemDataService iAmzFinUserItemDataService;
	@Autowired
	IPictureService iPictureService;
	@Autowired
	ProductInTagsMapper productInTagsMapper;
	@Autowired
	ProductFollowMapper productFollowMapper;
	@Autowired
	FollowOfferChangeMapper followOfferChangeMapper;
	@Autowired
	FileUpload fileUpload;
	@Override
	public List<ProductInfo> selectBySku(String sku, String marketplaceid, String amazonAuthId) {
		// TODO Auto-generated method stub
		QueryWrapper<ProductInfo> query=new QueryWrapper<ProductInfo>();
		query.eq("sku", sku);
		if(StrUtil.isNotBlank(marketplaceid)) {
			query.eq("marketplaceid", marketplaceid);
		}
		query.eq("amazonAuthId", amazonAuthId);
		List<ProductInfo> list = this.baseMapper.selectList(query);
		if(list!=null&&list.size()>0) {
			return list;
		}else {
			return null;
		}
	}
	@Override
	public List<ProductInfo> selectByMSku(String sku, String marketplaceid,String groupid,String shopid) {
		// TODO Auto-generated method stub
		if(StrUtil.isBlank(groupid)) {
			groupid=null;
		}
		List<ProductInfo> list = this.baseMapper.selectByMSku(sku,marketplaceid,groupid,shopid);
		if(list!=null&&list.size()>0) {
			return list;
		}else {
			return null;
		}
	}
	@Override
	public ProductInfo productOnlyone(String amazonAuthId, String sku, String marketplaceid) {
		// TODO Auto-generated method stub
		List<ProductInfo> list = selectBySku(sku,marketplaceid,amazonAuthId);
		if(list!=null&&list.size()>0) {
			return list.get(0);
		}else {
			return null;
		}
	}
	
	@Override
	public List<ProductInfo> selectByAsin(String amazonAuthId, String asin, String marketplaceid) {
		// TODO Auto-generated method stub
		QueryWrapper<ProductInfo> query=new QueryWrapper<ProductInfo>();
		query.eq("asin", asin);
		query.eq("marketplaceid", marketplaceid);
		query.eq("amazonAuthId", amazonAuthId);
		List<ProductInfo> list = this.baseMapper.selectList(query);
		if(list!=null&&list.size()>0) {
			return list;
		}else {
			return null;
		}
	 
	}
	@Override
	public String getMSKU(String amazonAuthId,  String marketplaceid,String sku) {
		// TODO Auto-generated method stub
		QueryWrapper<ProductInfo> query=new QueryWrapper<ProductInfo>();
		query.eq("sku", sku);
		query.eq("marketplaceid", marketplaceid);
		query.eq("amazonAuthId", amazonAuthId);
		 List<ProductInfo> result = this.baseMapper.selectList(query);
		 ProductInfo info =null;
		 if(result!=null&&result.size()>0) {
			 info=result.get(0);
		 }
		if(info==null) {
			return null;
		}
		ProductInOpt opt = iProductInOptService.getById(info.getId());
		if(opt!=null&&StrUtil.isNotBlank(opt.getMsku())) {
			return opt.getMsku();
		}else {
			return info.getSku();
		}
	 
	}
	
	public List<Map<String, Object>> findShopSku(String shopid, String sku) {
		return this.baseMapper.findShopSku(shopid, sku);
	}
	
	
    public List<String> getPidListByTagList(List<String> taglist,Object shopid,Object amazonAuthId,Object groupid,Object groupList,Object marketplace) {
    	if(taglist!=null && taglist.size()>0) {
			Result<List<String>> results=null;
			HashMap<String,Object> param=new HashMap<String,Object>();
			try {
				if(taglist!=null&&taglist.size()>0) {
					results = erpClientOneFeign.getMskuByTagList(taglist);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(Result.isSuccess(results)&&results!=null ){
				List<String> mskulist=results.getData();
				if( mskulist!=null && mskulist.size()>0) {
					param.put("mskulist", mskulist);
				}
				
			}
			param.put("taglist", taglist);
			param.put("shopid", shopid);
			param.put("amazonAuthId", amazonAuthId);
			if(groupid!=null&&StrUtil.isNotBlank(groupid.toString())) {
				param.put("groupid", groupid);
			}
			param.put("groupList", groupList);
			//param.put("marketplace", marketplace);
			List<String> pidlist=	productInTagsMapper.getPidList(param);
			return pidlist;
		}
    	return null;
    }
    
	@Override
//	@Cacheable(value = "ProductInfoCache#240",key="#key")
	public IPage<AmzProductListVo> getListByUser(UserInfo userinfo, ProductListQuery query, Map<String, Object> parameter,String key) {
		// TODO Auto-generated method stub
		parameter.put("shopid", userinfo.getCompanyid());
		List<String> taglist = query.getTaglist();//123,456,122,
		if(parameter.get("groupList")!=null) {
			@SuppressWarnings("unchecked")
			List<String> groupList = (List<String>)parameter.get("groupList");
			if(groupList.size()==0) {
				parameter.put("groupList",null);
			}
		}
		if(taglist!=null&&taglist.size()>0) {
			parameter.put("pidlist",getPidListByTagList(taglist
					,parameter.get("shopid")
					,parameter.get("amazonAuthId")
					,parameter.get("groupid")
					,parameter.get("groupList")
					,Arrays.asList(parameter.get("marketplace"))));
		}
		IPage<AmzProductListVo> productList = this.baseMapper.selectDetialByAuth(query.getPage(),parameter);
		List<String> mskuList=new ArrayList<String>();
		if(productList!=null && productList.getRecords()!=null && productList.getRecords().size()>0) {
			Result<Map<String,Object>> tagnamelistResult=adminClientOneFeign.findTagsName(userinfo.getCompanyid());
			for(AmzProductListVo item:productList.getRecords()) {
				item.setChangeprice(item.getLandedAmount());
				item.setItemshow(false);
				if(item.getOptstatuscolor()!=null) {
					item.setOptstatuscolor(GeneralUtil.getColorType(item.getOptstatuscolor()).toString());
				}
				if(item.getRegion().equals("EU")) {
					LambdaQueryWrapper<AmzInventoryCountryReport> queryWrapper2=new LambdaQueryWrapper<AmzInventoryCountryReport>();
					queryWrapper2.eq(AmzInventoryCountryReport::getAuthid, item.getAmazonAuthId());
					queryWrapper2.eq(AmzInventoryCountryReport::getSku, item.getSku());
					List<AmzInventoryCountryReport> eulist = amzInventoryCountryReportMapper.selectList(queryWrapper2);
					if(eulist!=null && eulist.size()>0) {
						int sum=0;
						for (int i = 0; i < eulist.size(); i++) {
							AmzInventoryCountryReport items = eulist.get(i);
							if(items.getCountry().equals("DE") || items.getCountry().equals("PL")) {
								sum+=items.getQuantity();
							}
							if(items.getCountry().equals(item.getCountry())) {
								item.setCountryinv(items.getQuantity());
							}
						}
						item.setCzinv(sum);
					}
				}
				mskuList.add(item.getMsku());
			}
			
			try {
				if(mskuList.size()>0) {
					Result<Map<String,String>> materialTagsIdsListResult  =erpClientOneFeign.getTagsIdsListByMsku(userinfo.getCompanyid(),mskuList);
					if(materialTagsIdsListResult!=null&&Result.isSuccess(materialTagsIdsListResult)
							&&tagnamelistResult!=null&&Result.isSuccess(tagnamelistResult)) {
						Map<String, Object> tagsNameMap = tagnamelistResult.getData();
						Map<String,String> mskuTagsIdsMap=materialTagsIdsListResult.getData();
							for(AmzProductListVo item:productList.getRecords()) {
							    List<String> tags=new ArrayList<String>();
								if(item.getTagids()!=null) {
									tags.addAll(Arrays.asList(item.getTagids().split(",")));
								}
								if(mskuTagsIdsMap!=null && mskuTagsIdsMap.get(item.getMsku())!=null) {
									String mtags=mskuTagsIdsMap.get(item.getMsku());
									List<String> tagids = Arrays.asList(mtags.split(","));
									for(String tagid:tagids) {
                                        if(!tags.contains(tagid)) {
                                        	tags.add(tagid);
                                        }
									}
									
								}
								if(tags.size()>0) {
									List<Map<String, Object>> tagNameList = new ArrayList<Map<String,Object>>();
									for(String id:tags) {
										if(StrUtil.isNotBlank(id)) {
											Object tagobj = tagsNameMap.get(id);
											if(tagobj!=null) {
												tagNameList.add(BeanUtil.beanToMap(tagobj));
											}
										}
									}
									if(tagNameList.size()>0) {
										tagNameList.sort(new Comparator<Map<String, Object>>() {
											@Override
											public int compare(Map<String, Object> o1, Map<String, Object> o2) {
												// TODO Auto-generated method stub
												return o1.get("sort").toString().compareTo(o2.get("sort").toString());
											}
											
										});
										item.setTagNameList(tagNameList);
									}
								}
							}
						 
					}
					
				
					 
					
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	     getWeekOtherCostForSKU(userinfo, productList);
		//handleParentListByproductList(userinfo.getCompanyid(), productList);
		return productList;
	}

	@Override
	public IPage<ProductInfoListVo> findByCondition(ProductListDTO dto) {
		if(StrUtil.isEmpty(dto.getSearch())) {
			dto.setSearch(null);
		}else {
			dto.setSearch("%"+dto.getSearch()+"%");
		}
		AmazonAuthority auth =null;
		if(StrUtil.isBlankOrUndefined(dto.getSellerid())) {
			  auth = amazonAuthorityService.selectByGroupAndMarket(dto.getGroupid(), dto.getMarketplaceid());
		}else {
			  auth = amazonAuthorityService.selectBySellerId(dto.getSellerid());
			  if(auth!=null) {
				  dto.setGroupid(auth.getGroupid());
			  }
		}
		if(auth!=null)dto.setAmazonauthid(auth.getId());
		IPage<ProductInfoListVo> list = this.baseMapper.findByCondition(dto.getPage(),dto);
		String ftype=dto.getFtype();
		if(dto.getWarehouseid()!=null&&"shipment".equals(ftype)) {
			String warehouseid = dto.getWarehouseid();
			for(ProductInfoListVo item:list.getRecords()) {
				if(item.getId()!=null) {
					if("1".equals(item.getIssfg())) {
						String materialid=item.getId();
						Result<Integer> data = erpClientOneFeign.getAssemblyCanAction(materialid, warehouseid,dto.getShopid());
						item.setWillfulfillable(data.getData());
					}
				}
			}
		}
		return list;
	}
	
	@Override
	public Map<String, Object> findNameAndPicture(String sku_p, String marketplaceid, String groupid) {
		// TODO Auto-generated method stub
		return this.baseMapper.findNameAndPicture(sku_p,marketplaceid,groupid);
	}
//	private void handleParentListByproductList(String shopid, IPage<AmzProductListVo> productList) {
//		if (productList != null) {
//			for (AmzProductListVo map : productList.getRecords()) {
//				Boolean isparent = null;
//				if (map.get("isparent") != null) {
//					isparent = (Boolean) map.get("isparent");
//					if (isparent == true) {
//						// 查出其下所有的子产品pid sum其子项的数据
//						String amazonAuthId = map.get("amazonAuthId").toString();
//						String parentasin = map.get("asin").toString();
//						List<Product_Info> list = product_InfoMapper.selectPidByParent(amazonAuthId, parentasin);
//						if (list != null && list.size() > 0) {
//							// sum数据
//							Integer SumaverageSalesDay = 0;
//							Integer Sumsummonth = 0;
//							Integer Sumordermonth = 0;
//							Integer Sumsumweek = 0;
//							Integer Sumafnquantity = 0;
//							Integer HighRank = 0;
//							BigDecimal Sumsessionrate = new BigDecimal("0");
//							BigDecimal Sumadvimpr = new BigDecimal("0");
//							BigDecimal Sumadvclick = new BigDecimal("0");
//							BigDecimal Sumadvsales = new BigDecimal("0");
//							BigDecimal Sumadvspend = new BigDecimal("0");
//							BigDecimal Sumsessions = new BigDecimal("0");
//							BigDecimal Sumadvcpc = new BigDecimal("0");
//							BigDecimal Sumadvctr = new BigDecimal("0");
//							BigDecimal Sumadvacos = new BigDecimal("0");
//							BigDecimal Sumadvspc = new BigDecimal("0");
//							BigDecimal Sumprorate = new BigDecimal("0");
//							BigDecimal Sumproprate = new BigDecimal("0");
//							BigDecimal Sumacoas = new BigDecimal("0");
//							BigDecimal Sumprofits = new BigDecimal("0");
//							BigDecimal Sumothercost = new BigDecimal("0");
//							BigDecimal Sumprofitall = new BigDecimal("0");
//							for (Product_Info info : list) {
//								String pid = info.getId();
//								Map<String, Object> dataMap = product_InfoMapper.selectDetialById(pid, shopid);
//								// 销售数据汇总
//								if (dataMap.get("averageSalesDay") != null) {
//									SumaverageSalesDay += (Integer) dataMap.get("averageSalesDay");
//								}
//								if (dataMap.get("sumweek") != null) {
//									Sumsumweek += (Integer) dataMap.get("sumweek");
//								}
//								if (dataMap.get("sessions") != null) {
//									Sumsessions = Sumsessions.add((BigDecimal) dataMap.get("sessions"));
//								}
//								if (dataMap.get("sessionrate") != null) {
//									Sumsessionrate = Sumsessionrate.add((BigDecimal) dataMap.get("sessionrate"));
//								}
//								if (dataMap.get("afn_fulfillable_quantity") != null) {
//									Sumafnquantity += (Integer) dataMap.get("afn_fulfillable_quantity");
//								}
//								if (dataMap.get("summonth") != null) {
//									Sumsummonth += (Integer) dataMap.get("summonth");
//								}
//								if (dataMap.get("ordermonth") != null) {
//									Sumordermonth += (Integer) dataMap.get("ordermonth");
//								}
//								// 广告数据汇总
//								if (dataMap.get("advimpr") != null) {
//									Sumadvimpr = Sumadvimpr.add((BigDecimal) dataMap.get("advimpr"));
//								}
//								if (dataMap.get("advclick") != null) {
//									Sumadvclick = Sumadvclick.add((BigDecimal) dataMap.get("advclick"));
//								}
//								if (dataMap.get("advsales") != null) {
//									Sumadvsales = Sumadvsales.add((BigDecimal) dataMap.get("advsales"));
//								}
//								if (dataMap.get("advspend") != null) {
//									Sumadvspend = Sumadvspend.add((BigDecimal) dataMap.get("advspend"));
//								}
//								if (dataMap.get("advcpc") != null) {
//									Sumadvcpc = Sumadvcpc.add((BigDecimal) dataMap.get("advcpc"));
//								}
//								if (dataMap.get("advctr") != null) {
//									Sumadvctr = Sumadvctr.add((BigDecimal) dataMap.get("advctr"));
//								}
//								if (dataMap.get("advacos") != null) {
//									Sumadvacos = Sumadvacos.add((BigDecimal) dataMap.get("advacos"));
//								}
//								if (dataMap.get("advspc") != null) {
//									Sumadvspc = Sumadvspc.add((BigDecimal) dataMap.get("advspc"));
//								}
//								// 利润数据汇总
//								if (dataMap.get("prorate") != null) {
//									Sumprorate = Sumprorate.add((BigDecimal) dataMap.get("prorate"));
//								}
//								if (dataMap.get("proprate") != null) {
//									Sumproprate = Sumproprate.add((BigDecimal) dataMap.get("proprate"));
//								}
//								if (dataMap.get("acoas") != null) {
//									Sumacoas = Sumacoas.add((BigDecimal) dataMap.get("acoas"));
//								}
//								if (dataMap.get("profits") != null) {
//									Sumprofits = Sumprofits.add((BigDecimal) dataMap.get("profits"));
//								}
//								if (dataMap.get("othercost") != null) {
//									Sumothercost = Sumothercost.add((BigDecimal) dataMap.get("othercost"));
//								}
//								if (dataMap.get("profitall") != null) {
//									Sumprofitall = Sumprofitall.add((BigDecimal) dataMap.get("profitall"));
//								}
//								if (dataMap.get("rank") != null) {
//									Integer nowrank = (Integer) dataMap.get("rank");
//									if (HighRank == 0) {
//										HighRank = nowrank;
//									} else {
//										if (nowrank < HighRank)
//											HighRank = nowrank;
//									}
//								}
//							}
//							Sumsessionrate = Sumsessionrate.divide(new BigDecimal(list.size()), 4, BigDecimal.ROUND_HALF_UP);
//							Sumadvcpc = Sumadvcpc.divide(new BigDecimal(list.size()), 4, BigDecimal.ROUND_HALF_UP);
//							Sumadvctr = Sumadvctr.divide(new BigDecimal(list.size()), 4, BigDecimal.ROUND_HALF_UP);
//							Sumadvacos = Sumadvacos.divide(new BigDecimal(list.size()), 4, BigDecimal.ROUND_HALF_UP);
//							Sumadvspc = Sumadvspc.divide(new BigDecimal(list.size()), 4, BigDecimal.ROUND_HALF_UP);
//							Sumprorate = Sumprorate.divide(new BigDecimal(list.size()), 4, BigDecimal.ROUND_HALF_UP);
//							Sumproprate = Sumproprate.divide(new BigDecimal(list.size()), 4, BigDecimal.ROUND_HALF_UP);
//							Sumacoas = Sumacoas.divide(new BigDecimal(list.size()), 4, BigDecimal.ROUND_HALF_UP);
//
//							map.put("averageSalesDay", SumaverageSalesDay);
//							map.put("sumweek", Sumsumweek);
//							map.put("sessions", Sumsessions);
//							map.put("sessionrate", Sumsessionrate);
//							map.put("afn_fulfillable_quantity", Sumafnquantity);
//							map.put("summonth", Sumsummonth);
//							map.put("ordermonth", Sumordermonth);
//							map.put("advimpr", Sumadvimpr);
//							map.put("advclick", Sumadvclick);
//							map.put("advimpr", Sumadvimpr);
//							map.put("advsales", Sumadvsales);
//							map.put("advspend", Sumadvspend);
//							map.put("advcpc", Sumadvcpc);
//							map.put("advctr", Sumadvctr);
//							map.put("advacos", Sumadvacos);
//							map.put("advspc", Sumadvspc);
//							map.put("prorate", Sumprorate);
//							map.put("proprate", Sumproprate);
//							map.put("acoas", Sumacoas);
//							map.put("profits", Sumprofits);
//							map.put("othercost", Sumothercost);
//							map.put("profitall", Sumprofitall);
//							map.put("rank", HighRank);
//						}
//					}
//				}
//			}
//		}
//	}

	public void getWeekOtherCostForSKU(UserInfo user, IPage<AmzProductListVo> productList) {
		if (productList != null) {
			List<Map<String,Object>> skulist=new LinkedList<Map<String,Object>>();
			for (AmzProductListVo  item : productList.getRecords()) {
				Map<String, Object> parameter = new HashMap<String, Object>();
				String sku = item.getSku();
				String marketplaceid = item.getMarketplaceid();
				String groupid = item.getGroupid();
				parameter.put("sku", sku);
				parameter.put("marketplaceid", marketplaceid);
				parameter.put("groupid", groupid);
				parameter.put("shopid", user.getCompanyid());
				skulist.add(parameter);
			}
			List<Map<String, Object>> allItemList = null;
			if(skulist!=null&&skulist.size()>0) {
				allItemList=iAmzFinUserItemDataService.getFinDataLastWeek(skulist);
			}
			Map<String,List<Map<String, Object>>> itemMap=new HashMap<String,List<Map<String, Object>>>();
			if(allItemList!=null) {
				for(Map<String, Object> item:allItemList) {
					String sku=item.get("sku").toString();
					String marketplaceid=item.get("marketplaceid").toString();
					String groupid=item.get("groupid").toString();
					String key=sku+marketplaceid+groupid;
					List<Map<String, Object>> list = itemMap.get(key);
					if(list==null) {
						list=new LinkedList<Map<String,Object>>();
					}
					list.add(item);
					itemMap.put(key, list);
				}
			}
			for (AmzProductListVo  item : productList.getRecords()) {
				Map<String, Object> parameter = new HashMap<String, Object>();
				String sku = item.getSku();
				String marketplaceid = item.getMarketplaceid();
				String groupid = item.getGroupid();
				parameter.put("sku", sku);
				parameter.put("marketplaceid", marketplaceid);
				parameter.put("groupid", groupid);
				parameter.put("shopid", user.getCompanyid());
				BigDecimal otherCost = new BigDecimal("0");// 每个sku对应的othercost
				String key=sku+marketplaceid+groupid;
				List<Map<String, Object>> itemList = itemMap.get(key);
				if (itemList != null) {
					for (Map<String, Object> itemFin : itemList) {
						String currency = itemFin.get("currency").toString();
						String amount = itemFin.get("amount").toString();
						// 货币转换，转换为当前sku所对应的国家货币
						otherCost = otherCost.add(exchangeRateHandlerService.changeCurrencyByLocal(currency, item.getLandedCurrency(), new BigDecimal(amount), 2));
					}
					item.setOthercost(otherCost);
				}
				parameter.put("pid", item.getId());
				parameter.put("myprice", item.getLandedAmount());
				parameter.put("productpricetype", "has");
				parameter.put("onlymargin", "has");
				this.showProfitDetial(parameter);
			 
			    if(parameter.get("profit" )!=null) {
			    		item.setNewprorate(parameter.get("profit").toString());
			    }
				item.setLandedCurrency(GeneralUtil.formatCurrency(item.getLandedCurrency()));
			} 
		}
	}



	@Override
	public List<ProductInfoStatusDefine> getProStatusList(String shopid) {
		List<ProductInfoStatusDefine> list = productInfoStatusDefineMapper.getProStatusByShop(shopid);
		return list;
	}

	@Override
	@CacheEvict(value = { "ProductInfoCache" }, allEntries = true)
	public Map<String, Object> updateProductPrice(UserInfo user, Map<String, Object> map, String ftype) {
		String productid= map.get("productid").toString();
		ProductPriceLocked locked = productPriceLockedMapper.selectById(productid);
		if(locked!=null) {
			if(locked.getDisable()==true && locked.getEndtime().after(new Date())==true) {
				throw new BizException("当前含有未失效的冻结调价,如需操作请先去取消冻结!");
			}
		}
		ProductInfo proinfo = this.baseMapper.selectById(productid);
		BigDecimal oldprice = proinfo.getPrice();
		// String groupid=request.getParameter("groupid");
		String authid = proinfo.getAmazonAuthId().toString();
		String sku = proinfo.getSku();
		String marketplaceid = proinfo.getMarketplaceid();
		AmazonAuthority auth = null;
		if (StrUtil.isNotEmpty(authid)) {
			auth = amazonAuthorityService.getById(authid);
		}
		if(!auth.getShopId().equals(user.getCompanyid())) {
			throw new BizException("您已切换用户请刷新后重试!");
		}
		map.put("auth", auth);
		map.put("sku", sku);
		ByteArrayOutputStream priceXml=null;
		try {
			priceXml = FeedFileProductPriceXML.getFile(map, ftype);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			throw new BizException("XML格式生成失败!");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("XML格式生成失败!");
		}
		AmzSubmitFeedQueue response = null;
		if (priceXml != null && auth != null && user != null && StrUtil.isNotEmpty(marketplaceid)) {
			Marketplace marketplace = marketplaceService.getById(marketplaceid);
			auth.setMarketPlace(marketplace);
			response = submitfeedService.SubmitFeedQueue(priceXml, "productPrice"+sku, auth, FeedFileProductPriceXML.type, user,null);
		} else {
			throw new BizException("系统改价出现异常，未能正常提交至亚马逊!");
		}
		Map<String, Object> maps = new HashMap<String, Object>();
		// 当永久调价时同步更新价格。
		String standardPrice=map.get("standardPrice").toString();
		String businessprice = null;
		String businesstype=null;
		String bussinesslist=null;
		if("isBusiness".equals(ftype)) {
			if(map.containsKey("businessprice")&&map.get("businessprice")!=null) {
				businessprice=map.get("businessprice").toString();
				if(map.containsKey("businesslist")&&map.get("businesslist")!=null) {
					businesstype=map.get("businesstype").toString();
					bussinesslist=map.get("businesslist").toString();
				}
			}
			
		}
		if (StrUtil.isNotEmpty(sku) && StrUtil.isNotEmpty(marketplaceid) && auth != null&&standardPrice!=null) {
			BigDecimal updateprice = new BigDecimal(standardPrice);
			List<ProductPrice> propriceList = productPriceMapper.selectBySkuAndSeller(sku, marketplaceid, auth.getSellerid());
			if (propriceList != null && propriceList.size() > 0) {
				for (int i = 0; i < propriceList.size(); i++) {
					propriceList.get(i).setLandedAmount(updateprice);
					propriceList.get(i).setListingAmount(updateprice);
					propriceList.get(i).setShippingAmount(updateprice);
					productPriceMapper.updateById(propriceList.get(i));
				}
			}
				ProductInOpt productOpt = null;
				if (StrUtil.isNotEmpty(productid)) {
					productOpt = productInOptMapper.selectById(productid);
				}
				if (productOpt != null) {
					productOpt.setBuyprice(updateprice);
					if("isBusiness".equals(ftype)) {
						if(map.get("submittype")!=null&&"deletebusiness".equals(map.get("submittype").toString())) {
							productOpt.setBusinessprice(null);	
							productOpt.setBusinesstype(null);
							productOpt.setBusinesslist(null);
						}else {
							if(businessprice!=null) {
								productOpt.setBusinessprice(new BigDecimal(businessprice));	
							}
							if(bussinesslist!=null) {
								productOpt.setBusinesstype(businesstype);
								productOpt.setBusinesslist(bussinesslist);
							}
						}
						
					}
					productInOptMapper.updateById(productOpt);
				}else {
					productOpt = new ProductInOpt();
					productOpt.setPid(new BigInteger(productid));
					productOpt.setBuyprice(updateprice);
					if("isBusiness".equals(ftype)) {
						if(map.get("submittype")!=null&&"deletebusiness".equals(map.get("submittype").toString())) {
							productOpt.setBusinessprice(null);	
							productOpt.setBusinesstype(null);
							productOpt.setBusinesslist(null);
						}else {
							if(businessprice!=null) {
								productOpt.setBusinessprice(new BigDecimal(businessprice));	
							}
							if(bussinesslist!=null) {
								productOpt.setBusinesstype(businesstype);
								productOpt.setBusinesslist(bussinesslist);
							}
						}
					}
					productInOptMapper.insert(productOpt);
				} 
				if (proinfo != null) {
					proinfo.setPrice(updateprice);
					this.baseMapper.updateById(proinfo);
				}
			}
		

		if ("isForever".equals(ftype)) {
			ftype = "forever";
		} else if("isFortime".equals(ftype)) {
			ftype = "times";
		} else if("isBusiness".equals(ftype)) {
			ftype = "business";
		}
		String submissionid =  response.getId();
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("oldstandardprice",oldprice);
		map2.put("standardprice", map.get("standardPrice"));
		map2.put("saleprice", map.get("salesPrice"));
		map2.put("starttime", map.get("startDate"));
		map2.put("endtime", map.get("endDate"));
		map2.put("businessprice",map.get("businessprice"));
		if(map.get("submittype")!=null&&"deletebusiness".equals(map.get("submittype").toString())) {
			map2.put("businesstype", "delete");
			map2.put("businesslist","");
		}else {
			map2.put("businesstype", map.get("businesstype"));
			map2.put("businesslist", map.get("businesslist"));
		}
		map2.put("userid", user.getId());
		map2.put("ftype", ftype);
		 if (StrUtil.isNotEmpty(productid) && StrUtil.isNotEmpty(submissionid)) {
			map2.put("pid", productid);
			map2.put("submissionid", submissionid);
			// 向历史操作表中插入数据
			this.optProductPrice(map2);
			maps.put("msg", "操作成功");
			if(StrUtil.isNotEmpty(submissionid)) {
				maps.put("submissionid", submissionid);
			}
		 }
		return maps;
		
	}
	
	 
		
		public void optProductPrice(Map<String, Object> map) {
			AmzProductPriceOpt productprice=new AmzProductPriceOpt();
			productprice.setPid(map.get("pid").toString());
			productprice.setFeedSubmissionId(map.get("submissionid").toString());
			productprice.setOperator(map.get("userid").toString());
			productprice.setOpttime(new Date());
			String ftype=map.get("ftype").toString();
			productprice.setFtype(ftype);
			if(map.get("saleprice")!=null && StrUtil.isNotBlank(map.get("saleprice").toString())){
				BigDecimal saleprice=new BigDecimal(map.get("saleprice").toString());
				productprice.setSaleprice(saleprice);
			}
			if( ftype.equals("forever")&&map.get("oldstandardprice")!=null){
				BigDecimal oldstandardprice=new BigDecimal(map.get("oldstandardprice").toString());
				productprice.setOldstandardprice(oldstandardprice);
			}
			if(map.get("standardprice")!=null&&StrUtil.isNotBlank(map.get("standardprice").toString())){
				BigDecimal standardprice=new BigDecimal(map.get("standardprice").toString());
				productprice.setStandardprice(standardprice);
			}
		
			if(map.get("businessprice")!=null&&StrUtil.isNotBlank(map.get("businessprice").toString())){
				BigDecimal businessprice=new BigDecimal(map.get("businessprice").toString());
				productprice.setBusinessprice(businessprice);
			}
			if(map.get("businesstype")!=null){
				productprice.setBusinesstype(map.get("businesstype").toString());
			}
			if(map.get("businesslist")!=null){
				productprice.setBusinesslist(map.get("businesslist").toString());
			}
			if(map.get("starttime")!=null &&StrUtil.isNotBlank(map.get("starttime").toString())){
				Date starttime = GeneralUtil.getDatez(map.get("starttime").toString()) ;
				productprice.setStarttime(starttime);
			}
			if(map.get("endtime")!=null&&StrUtil.isNotBlank(map.get("endtime").toString())){
				Date endtime = GeneralUtil.getDatez(map.get("endtime").toString()) ;
				productprice.setEndtime(endtime);
			}
			if(map.get("remark")!=null&&StrUtil.isNotBlank(map.get("remark").toString())){
				productprice.setRemark(map.get("remark").toString());
			}
			amzProductPriceOptMapper.insert(productprice);
		}

		@Override
		@CacheEvict(value = { "ProductInfoCache" }, allEntries = true)
		public int updateUpdateRemark(String id, String remark, String ftype, String userid) {
			int result=0;
			ProductInOpt old = productInOptMapper.selectById(id);
			if (old == null) {
				ProductInOpt product = new ProductInOpt();
				product.setPid(new BigInteger(id));
				if ("pro".equals(ftype)) {
					product.setRemark(remark);
				}
				if ("price".equals(ftype)) {
					product.setPriceremark(remark);
				}
				product.setDisable(false);
				product.setLastupdate(new Date());
				result= productInOptMapper.insert(product);
			} else {
				if ("pro".equals(ftype)) {
					old.setRemark(remark);
				}
				if ("price".equals(ftype)) {
					old.setPriceremark(remark);
				}
				old.setLastupdate(new Date());
				result=productInOptMapper.updateById(old);
			}
			if(result>0) {
				ProductRemarkHistory history=new ProductRemarkHistory();
				history.setPid(id);
				history.setFtype(ftype);
				history.setOperator(userid);
				history.setOpttime(new Date());
				history.setRemark(remark);
				productRemarkHistoryMapper.insert(history);
				return result;
			}else {
				return 0;
			}
		}

		@Override
		public Map<String, Object> saveProductPriceLocked(String pid, String userid, String price, String days) {
			Map<String,Object> map=new HashMap<String, Object>();
			ProductPriceLocked oldentity = productPriceLockedMapper.selectById(pid);
			Date times = GeneralUtil.getDatez(days);
			int result=0;
			if(oldentity!=null) {
				oldentity.setDisable(true);
				oldentity.setPid(pid);
				oldentity.setOperator(userid);
				oldentity.setOpttime(new Date());
				oldentity.setPrice(new BigDecimal(price));
				oldentity.setStarttime(new Date());
				oldentity.setEndtime(times);
				result = productPriceLockedMapper.updateById(oldentity);
				map.put("obj", oldentity);
			}else {
				ProductPriceLocked record=new ProductPriceLocked();
				record.setDisable(true);
				record.setPid(pid);
				record.setOperator(userid);
				record.setOpttime(new Date());
				record.setPrice(new BigDecimal(price));
				record.setStarttime(new Date());
				record.setEndtime(times);
				result = productPriceLockedMapper.insert(record);
				map.put("obj", record);
			}
			if(result>0) {
				map.put("isok", "true");
			}else {
				map.put("isok", "false");
				map.put("obj", null);
			}
			return map;
		}

		@Override
		public Map<String, Object> deleteProductPriceLocked(String pid, String userid) {
			Map<String,Object> map=new HashMap<String, Object>();
			ProductPriceLocked entity = productPriceLockedMapper.selectById(pid);
			entity.setDisable(false);
			entity.setOperator(userid);
			entity.setOpttime(new Date());
			entity.setEndtime(new Date());
			int res = productPriceLockedMapper.updateById(entity);
			if(res>0) {
				map.put("isok", "true");
			}else {
				map.put("isok", "false");
			}
			return map;
		}
		
		@Override
		public void showProfitDetial(Map<String, Object> map) {
			if(map.get("pid")==null||map.get("myprice")==null) {
				return;
			}
			String pid = map.get("pid").toString();
			BigDecimal myprice = new BigDecimal(map.get("myprice").toString());
			ProductInProfit productInProfit = productInProfitMapper.selectById(pid);
			ProductInfo info = this.baseMapper.selectById(pid);
			ProductInOpt proopt = iProductInOptService.getCacheableById(pid);
			AmazonAuthority auth = amazonAuthorityService.getById(info.getAmazonAuthId());
			String profitcfgid=null;
			//使用opt上面带的利润id(假如设置了)
			if(proopt!=null) {
				if(proopt.getProfitid()!=null) {
					profitcfgid=proopt.getProfitid().toString();
				}
			} 
			if(StrUtil.isEmpty(profitcfgid)) {
				profitcfgid = profitCfgService.findDefaultPlanIdByGroup(auth.getGroupid());
			}
			String costDetailStr = null;
			 String msku=info.getSku();
		        if(proopt!=null&&StrUtil.isNotBlank(proopt.getMsku())) {
		        	msku=proopt.getMsku();
		        }
		    	BigDecimal cost =new BigDecimal("0");
		    	Result<Map<String, Object>> result =null;
		    	try {
		    	    if(msku!=null) {
		    	    	result = erpClientOneFeign.getMaterialBySKUAction(msku, auth.getShopId());
		    	    }
				} catch (FeignException e) {
					 throw new BizException(BizException.getMessage(e, "本地SKU异常"));
				}
				DimensionsInfo dim_local = null;
				if(result!=null && Result.isSuccess(result)) {
					Map<String, Object> data = result.getData();
					if(data!=null) {
						String length=data.get("length")!=null?data.get("length").toString():"0";
						String width=data.get("width")!=null?data.get("width").toString():"0";
						String height=data.get("height")!=null?data.get("height").toString():"0";
						String weight=data.get("weight")!=null?data.get("weight").toString():"0";
						dim_local = new DimensionsInfo();
						dim_local.setHeight(new BigDecimal(height));
						dim_local.setLength(new BigDecimal(length));
						dim_local.setWidth(new BigDecimal(width));
						dim_local.setWeight(new BigDecimal(weight));
						dim_local.setHeightUnits("cm");
						dim_local.setLengthUnits("cm");
						dim_local.setWidthUnits("cm");
						dim_local.setWeightUnits("kg");
						cost=data.get("cost")!=null?new BigDecimal(data.get("cost").toString()):null;
					}
				}
				
			if (productInProfit != null&&map.get("productpricetype")==null) {
				ProductInOrder order = productInOrderMapper.selectById(pid);
				if (order != null) {
					BigDecimal priceweek = order.getPriceWeek();
					Integer saleseven = order.getSalesSeven();
					if (saleseven != 0) {
						BigDecimal avgsales = priceweek.divide(new BigDecimal(saleseven), 2, RoundingMode.HALF_EVEN);
						map.put("avgsales", avgsales);
					} else {
						map.put("avgsales", myprice);
						map.put("isLocalPrice", 1);
					}
				}
				map.put("productopt", proopt);
				map.put("profit", productInProfit.getMarginWeek() + "%");
				map.put("profitcfg", productInProfit.getPlanName());
				map.put("dimensionsInfo", productInProfit.getDimensions());
				map.put("dimensionsWeight", productInProfit.getWeight());
				costDetailStr = productInProfit.getCostdetail();
			} else {
				ProfitConfig profitcfg = profitCfgService.findConfigAction(profitcfgid);
				CostDetail costDetail = calculateProfitService.getProfit(info, myprice, auth,dim_local,cost);
				if(costDetail==null) {
					return ;
				}
				costDetailStr = GeneralUtil.toJSON(costDetail);
				map.put("avgsales", myprice);
				map.put("productopt", proopt);
				map.put("isLocalPrice", 1);
				map.put("profit", costDetail.getMargin());
				map.put("profitcfg", profitcfg.getName());
				 if (dim_local!=null) {
						String lengthValue = dim_local.getInputDimensions().getLength().value.toString() + dim_local.getInputDimensions().getLength().units;
						String widthValue = dim_local.getInputDimensions().getWidth().value.toString() + dim_local.getInputDimensions().getWidth().units;
						String heightValue = dim_local.getInputDimensions().getHeight().value.toString() + dim_local.getInputDimensions().getHeight().units;
						String weightValue = dim_local.getInputDimensions().getWeight().value.toString() + dim_local.getInputDimensions().getWeight().units;
						map.put("dimensionsInfo", lengthValue + "-" + widthValue + "-" + heightValue);
						map.put("dimensionsWeight", weightValue);
				} else {
					DimensionsInfo dim_amz = dimensionsInfoMapper.selectById(info.getPageDimensions());
					 if (dim_amz != null) {
						 String lengthValue = dim_amz.getInputDimensions().getLength().value.toString() + dim_amz.getInputDimensions().getLength().units;
						 String widthValue = dim_amz.getInputDimensions().getWidth().value.toString() + dim_amz.getInputDimensions().getWidth().units;
						 String heightValue = dim_amz.getInputDimensions().getHeight().value.toString() + dim_amz.getInputDimensions().getHeight().units;
						 String weightValue = dim_amz.getInputDimensions().getWeight().value.toString() + dim_amz.getInputDimensions().getWeight().units;
						 map.put("dimensionsInfo", lengthValue + "-" + widthValue + "-" + heightValue);
						 map.put("dimensionsWeight", weightValue);
					 }
				} 
			}
			Map<String, String> costDetailMap = profitService.jsonToMap(costDetailStr);
			CostDetail costDetail =null;
			if (costDetailMap != null) {
				costDetail = new CostDetail(costDetailMap);
				map.put("costDetailMap", costDetail);
			}
			map.put("profitcfgid", profitcfgid);
			if(map.get("onlymargin")==null) {
				setLocalValue(map,pid,msku);
			}
		}
		
	    public void setLocalValue(Map<String, Object> map,String pid ,String msku) {
			ProductInfo info = this.baseMapper.selectById(pid);
			AmazonAuthority auth = amazonAuthorityService.getById(info.getAmazonAuthId());
			BigDecimal currShipmentFee= new BigDecimal("0");
			BigDecimal currtotal= new BigDecimal("0");
			Marketplace market = marketplaceService.findMapByMarketplaceId().get(info.getMarketplaceid());
			Result<Map<String, Object>> result =null;
			try {
				 result =erpClientOneFeign.getRealityPrice(auth.getShopId(), msku);
			}catch(FeignException e) {
				throw new BizException(BizException.getMessage(e, "获取本地产品价格失败"));
			}
			 
			if(Result.isSuccess(result)) {
				Map<String, Object> mpriceMap = result.getData();
					if(mpriceMap!=null) {
						if(mpriceMap.get("totalamount")!=null) {
							BigDecimal currPrice=new BigDecimal(mpriceMap.get("totalamount").toString());
							BigDecimal repriceRMB=new BigDecimal(mpriceMap.get("totalamount").toString());
							currPrice=exchangeRateHandlerService.changeCurrencyByLocal("CNY", market.getCurrency(), currPrice);
							currPrice=currPrice.setScale(2, RoundingMode.HALF_UP);
							map.put("reprice",currPrice);
							map.put("repricetime","实时计算,最近10个采购单的均价,第一笔订单的时间:"
							+GeneralUtil.formatDate(AmzDateUtils.getDate(mpriceMap.get("audittime")))+"，对应金额RMB："+repriceRMB);
						}
					}
				}
		 
			Map<String, Object> transFeeMap = shipInboundTransService.shipmentTransFee(auth.getShopId(),info.getMarketplaceid(),auth.getGroupid(),info.getSku());
			if(transFeeMap!=null&&transFeeMap.get("skufeeavg")!=null) {
				Date opttime = AmzDateUtils.getDate(transFeeMap.get("opttime"));
				Double mcurrShipmentFee = (Double)transFeeMap.get("skufeeavg");
				if(mcurrShipmentFee!=null&&!mcurrShipmentFee.isNaN()) {
					currShipmentFee=new BigDecimal(mcurrShipmentFee);
					BigDecimal currShipmentFeeRMB=new BigDecimal(mcurrShipmentFee);
					currShipmentFeeRMB=currShipmentFeeRMB.setScale(2, RoundingMode.HALF_UP);
					currShipmentFee=exchangeRateHandlerService.changeCurrencyByLocal("CNY", market.getCurrency(), currShipmentFee);
					currShipmentFee=currShipmentFee.setScale(2, RoundingMode.HALF_UP);
					currtotal=currtotal.add(currShipmentFee);
					map.put("reshipmentfee",currShipmentFee);
					map.put("reshipmentfeetime",GeneralUtil.formatDate(opttime));
					map.put("reshipmentfeeRMB",currShipmentFeeRMB);
				}
			}
	 
			  List<OrdersFinancial> listfin = orderManagerService.lastShippedOrderFin(auth,info);
			List<AmzAmountDescription> deslist = amzAmountDescriptionMapper.selectList(null);
			Map<String, String> descName = new HashMap<String, String>();
			for (AmzAmountDescription item : deslist) {
				descName.put(item.getEname(), item.getCname());
			}
			if(listfin!=null) {
				for(int i=0;i<listfin.size();i++) {
					   OrdersFinancial fin = listfin.get(i);
					 if("Commission".equals(fin.getFtype())){
						 BigDecimal ovalue = fin.getAmount().multiply(new BigDecimal("-1"));
						 currtotal=currtotal.add(ovalue);
						 map.put("order_Commission",ovalue);
						 
					 }
		             if("FBAPerUnitFulfillmentFee".equals(fin.getFtype())){
		            	 BigDecimal ovalue = fin.getAmount().multiply(new BigDecimal("-1"));
		            	 currtotal=currtotal.add(ovalue);
		            	 map.put("order_FBAPerUnitFulfillmentFee",ovalue);
					 }
		             if("MarketplaceFacilitatorVAT-Shipping".equals(fin.getFtype())){
		            	 BigDecimal ovalue = fin.getAmount().multiply(new BigDecimal("-1"));
		            	 currtotal=currtotal.add(ovalue);
		            	 map.put("order_vat",ovalue);
		             }
		             map.put("orderid",fin.getAmazonOrderId());
		             if(descName.get(fin.getFtype())!=null) {
		            	 fin.setFtype(descName.get(fin.getFtype()));
		             }
				}
				LambdaQueryWrapper<FBAStorageFeeReport> query=new LambdaQueryWrapper<FBAStorageFeeReport>();
				query.eq(FBAStorageFeeReport::getAmazonauthid, info.getAmazonAuthId());
				query.eq(FBAStorageFeeReport::getAsin, info.getAsin());
				query.eq(FBAStorageFeeReport::getCountryCode, market.getMarket());
				query.orderByDesc(FBAStorageFeeReport::getMonthOfCharge);
				List<FBAStorageFeeReport> fbastorageList = fBAStorageFeeReportMapper.selectList(query);
				if(fbastorageList!=null&&fbastorageList.size()>0) {
					map.put("fbastorage",null);
				}else {
					map.put("fbastorage",null);
				}
				map.put("orderFinDetailList",listfin);
			}
			map.put("groupid",auth.getGroupid());
			map.put("marketplaceid",info.getMarketplaceid());
			map.put("sku",info.getSku());
			map.put("currtotal",currtotal);
	    }
	    
		@Override
		//@Cacheable(value = "productSimpleInfoOnlyone#600" )
		public Map<String, Object> productSimpleInfoOnlyone(String amazonAuthId, String sku, String marketplaceid) {
			// TODO Auto-generated method stub
			Map<String,Object> result=new HashMap<String,Object>();
			ProductInfo info = this.productOnlyone(amazonAuthId, sku, marketplaceid);
			ProductInOpt opt =null;
			String msku=sku;
			if(info!=null&&info.getId()!=null) {
				result.put("name", info.getName());
				result.put("opendate",GeneralUtil.formatDate(AmzDateUtils.getDate(info.getOpenDate())) );
				if(info.getImage()!=null) {
					Picture picture = iPictureService.getById(info.getImage());
					if(picture.getLocation()!=null) {
						result.put("image",picture.getLocation());
					}else {
						result.put("image",picture.getUrl());
					}
				}
				
				opt=iProductInOptService.getById(info.getId());
				
				if(opt!=null&&opt.getMsku()!=null) {
					msku=opt.getMsku();
				}
			}
			result.put("msku",msku);
			return result;
		}
		
		@Override
		public String findMSKUBySKUMarket(String psku, String marketplaceid, String id) {
			// TODO Auto-generated method stub
			return this.baseMapper.findMSKUBySKUMarket(psku, marketplaceid, id);
		}
		@Override
		@CacheEvict(value = { "ProductInfoCache" }, allEntries = true)
		public int productDisable(UserInfo user, String id) {
			ProductInOpt oldone = productInOptMapper.selectById(id);
			if (oldone != null) {
				oldone.setPid(new BigInteger(id));
				oldone.setDisable(true);
				oldone.setLastupdate(new Date());
				oldone.setOperator(new BigInteger(user.getId()));
				return productInOptMapper.updateById(oldone);
			} else {
				ProductInOpt product = new ProductInOpt();
				product.setPid(new BigInteger(id));
				product.setDisable(true);
				product.setLastupdate(new Date());
				product.setOperator(new BigInteger(user.getId()));
				return productInOptMapper.insert(product);
			}
		}
		
		@Override
		@CacheEvict(value = { "ProductInfoCache" }, allEntries = true)
		public int productUnDisable(UserInfo user,String id) {
			ProductInOpt oldone = productInOptMapper.selectById(id);
			if (oldone != null) {
				oldone.setPid(new BigInteger(id));
				oldone.setDisable(false);
				oldone.setLastupdate(new Date());
				oldone.setOperator(new BigInteger(user.getId()));
				return productInOptMapper.updateById(oldone);
			} else {
				ProductInOpt product = new ProductInOpt();
				product.setPid(new BigInteger(id));
				product.setDisable(false);
				product.setLastupdate(new Date());
				product.setOperator(new BigInteger(user.getId()));
				return productInOptMapper.insert(product);
			}
		}
		
		@Override
		@CacheEvict(value = { "ProductInfoCache" }, allEntries = true)
		public List<Map<String, Object>> productFollow(String followid) {
			// TODO Auto-generated method stub
			ProductFollow follow = productFollowMapper.selectById(followid);
			if (follow!=null&&!follow.getIsread()) {
				follow.setIsread(true);
				productFollowMapper.updateById(follow);
			} 
			Integer limit=follow.getFlownumber()!=null?follow.getFlownumber()+5:5;
			return followOfferChangeMapper.findByFollowID(followid,limit);
		}
		@Override
		public IPage<Map<String, Object>> getProductInfoWithFnSKU(Page<Object> page, Map<String, Object> map) {
			// TODO Auto-generated method stub
			AmazonAuthority auth = amazonAuthorityService.selectByGroupAndMarket(map.get("groupid").toString(), map.get("marketplaceid").toString());
			if(auth!=null) {
				map.put("authid", auth.getId());
			}
			IPage<Map<String, Object>> list = this.baseMapper.getProductInfoWithFnSKU(page, map);
			if(list!=null&&list.getRecords()!=null&&list.getRecords().size()>0) {
				for(Map<String, Object> item:list.getRecords()) {
					if(item.get("pimage")!=null) {
						String pimage=item.get("pimage").toString();
						item.put("pimage",fileUpload.getPictureImage(pimage));
					}
				}
			}
			return list;
		}
		@Override
		public IPage<Map<String, Object>> getAsinList(Page<?> page, Map<String, Object> parameter) {
			// TODO Auto-generated method stub
			return this.baseMapper.getAsinList(page, parameter) ;
		}
		@Override
		public List<Map<String, Object>> getInfoSimple(Map<String, Object> parameter) {
			// TODO Auto-generated method stub
			String sellerid=parameter.get("sellerid").toString();
			AmazonAuthority auth = amazonAuthorityService.selectBySellerId(sellerid);
			parameter.put("amazonauthid", auth.getId());
			List<Map<String, Object>> result=this.baseMapper.getInfoSimple(parameter);
			return result;
		}
		@Override
		public boolean updateProInvlidByPid(String pid) {
			// TODO Auto-generated method stub
			return this.lambdaUpdate().set(ProductInfo::getInvalid, false).eq(ProductInfo::getId, pid).update();
			 
		}
}
