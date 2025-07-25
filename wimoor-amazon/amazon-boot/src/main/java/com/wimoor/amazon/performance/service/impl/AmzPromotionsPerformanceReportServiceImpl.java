package com.wimoor.amazon.performance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inventory.mapper.InventoryReportMapper;
import com.wimoor.amazon.inventory.pojo.vo.ProductInventoryVo;
import com.wimoor.amazon.performance.mapper.AmzCouponAsinMapper;
import com.wimoor.amazon.performance.mapper.AmzPromotionsAsinMapper;
import com.wimoor.amazon.performance.mapper.AmzPromotionsPerformanceReportMapper;
import com.wimoor.amazon.performance.pojo.dto.PerformanceReportListDTO;
import com.wimoor.amazon.performance.pojo.entity.AmzPromotionsAsin;
import com.wimoor.amazon.performance.pojo.entity.AmzPromotionsPerformanceReport;
import com.wimoor.amazon.performance.service.IAmzPromotionsPerformanceReportService;
import com.wimoor.amazon.product.pojo.entity.ProductInfo;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.service.IPictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wimoor team
 * @since 2022-06-17
 */
@Service
@RequiredArgsConstructor
public class AmzPromotionsPerformanceReportServiceImpl extends ServiceImpl<AmzPromotionsPerformanceReportMapper, AmzPromotionsPerformanceReport> implements IAmzPromotionsPerformanceReportService {
   final AmzPromotionsAsinMapper amzPromotionsAsinMapper;
   final IProductInfoService productInfoService;
   final InventoryReportMapper inventoryReportMapper;
   final IMarketplaceService marketplaceService;
   final IPictureService pictureService;
   final FileUpload fileUpload;
    @Override
    public IPage<Map<String, Object>> findByCondition(PerformanceReportListDTO dto) {
        IPage<Map<String, Object>> page = this.baseMapper.findByCondition(dto.getPage(), dto);
        if(page!=null&&page.getRecords().size()>0){
            for(Map<String, Object> map:page.getRecords()){
                LambdaQueryWrapper<AmzPromotionsAsin> query=new LambdaQueryWrapper<AmzPromotionsAsin>();
                String promotionid=map.get("promotionid").toString();
                String marketplaceid=map.get("marketplaceid").toString();
                String amazonauthid=map.get("amazonauthid").toString();
                Marketplace market = marketplaceService.findMapByMarketplaceId().get(marketplaceid);
                query.eq(AmzPromotionsAsin::getPromotionid, promotionid);
                List<AmzPromotionsAsin> list = amzPromotionsAsinMapper.selectList(query);
                List<Map<String,Object>> asindetail=new LinkedList<Map<String,Object>>();
                for(AmzPromotionsAsin asin:list){
                    List<ProductInfo> infos = productInfoService.selectByAsin( amazonauthid,asin.getAsin(), marketplaceid);
                    if(infos!=null&&infos.size()>0){
                        ProductInfo info=infos.get(0);
                        Map<String, Object> param=new HashMap<String,Object>();
                        param.put("shopid", dto.getShopid());
                        if(market.getRegion().equals("EU")){
                            param.put("marketplaceid", "EU");
                        }else{
                            param.put("marketplaceid", marketplaceid);
                        }
                        param.put("groupid", dto.getGroupid());
                        param.put("sku", info.getSku());
                        List<ProductInventoryVo> fbainv = inventoryReportMapper.findFBA(param);
                        Map<String, Object> item = BeanUtil.beanToMap(asin);
                        item.put("asin",asin.getAsin());
                        item.put("sku",info.getSku());
                        item.put("price",info.getPrice());
                        if(fbainv!=null&&fbainv.size()>0){
                            ProductInventoryVo fba = fbainv.get(0);
                             item.put("name",fba.getPname());
                             item.put("image",fileUpload.getPictureImage(fba.getImage()));
                             item.put("afnWarehouseQuantity",fba.getAfnWarehouseQuantity());
                             item.put("afnFulfillableQuantity",fba.getAfnFulfillableQuantity());
                             item.put("afnReservedQuantity",fba.getAfnReservedQuantity());
                             item.put("afnInboundWorkingQuantity",fba.getAfnInboundWorkingQuantity());
                             item.put("afnInboundShippedQuantity",fba.getAfnInboundShippedQuantity());
                             item.put("afnInboundReceivingQuantity",fba.getAfnInboundReceivingQuantity());
                             item.put("afnUnsellableQuantity",fba.getAfnUnsellableQuantity());
                             item.put("afnResearchingQuantity",fba.getAfnResearchingQuantity());
                        }else{
                            item.put("name",info.getName());
                            if(info.getImage()!=null){
                                Picture picture = pictureService.getById(info.getImage());
                                if(picture!=null){
                                    String image=picture.getLocation()!=null?picture.getLocation():picture.getUrl();
                                    item.put("image",fileUpload.getPictureImage(image));
                                }
                            }
                        }
                        asindetail.add(item);
                    }
                }
                map.put("asindetail",asindetail);
            }
        }
        return page;
    }
}
