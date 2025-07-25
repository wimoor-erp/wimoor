package com.wimoor.amazon.inboundV2.service.impl;

import cn.hutool.core.util.StrUtil;
import com.wimoor.amazon.auth.service.IMarketplaceService;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddress;
import com.wimoor.amazon.inbound.pojo.entity.ShipAddressTo;
import com.wimoor.amazon.inboundV2.service.IShipCustomsService;
import com.wimoor.amazon.product.service.IProductInfoService;
import com.wimoor.common.user.UserInfo;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
@Service
@RequiredArgsConstructor
public class ShipCustomsServiceImpl implements IShipCustomsService {
    final IMarketplaceService marketplaceService;
    final IProductInfoService productInfoService;

    private Map<String,Object> getBoxInfo(Map<String,Object> box, String prefix){
        if(box==null)return null;
        Map<String, Object> result=new HashMap<String,Object>();
        result.put("{"+prefix+".箱号}",box.get("shipmentid").toString()+getBoxNumber(box.get("boxnum")));
        result.put("{"+prefix+".装箱数量}",box.get("qty"));
        result.put("{"+prefix+".箱子重量}",box.get("weight"));
        result.put("{"+prefix+".箱子宽度}",box.get("width"));
        result.put("{"+prefix+".箱子高度}",box.get("height"));
        result.put("{"+prefix+".箱子长度}",box.get("length"));
        result.put("{"+prefix+".箱子体积}", new BigDecimal(box.get("width").toString()).multiply(new BigDecimal(box.get("height").toString())).multiply(new BigDecimal(box.get("length").toString())));
        return result;
    }

    private Map<String,Object> getFromeAddress(ShipAddress fromaddress){
        if(fromaddress==null)return null;
        Map<String, Object> result=new HashMap<String,Object>();
        result.put("{发货地址.城市}",fromaddress.getCity());
        result.put("{发货地址.国家}",fromaddress.getCountrycode());
        result.put("{发货地址.电话}",fromaddress.getPhone());
        result.put("{发货地址.邮编}",fromaddress.getPostalcode());
        result.put("{发货地址.省份编码}",fromaddress.getStateorprovincecode());
        result.put("{发货地址.地区}",fromaddress.getDistrictorcounty());
        result.put("{发货地址.详情1}",fromaddress.getAddressline1());
        result.put("{发货地址.详情2}",fromaddress.getAddressline2());
        result.put("{发货地址.名称}",fromaddress.getName());
        return result;
    }

    private Map<String,Object> getToAddress(ShipAddressTo toaddress){
        if(toaddress==null)return null;
        Map<String, Object> result=new HashMap<String,Object>();
        result.put("{收货地址.城市}",toaddress.getCity());
        result.put("{收货地址.国家}",toaddress.getCountrycode());
        result.put("{收货地址.电话}",toaddress.getPhone());
        result.put("{收货地址.邮编}",toaddress.getPostalcode());
        result.put("{收货地址.省份编码}",toaddress.getStateorprovincecode());
        result.put("{收货地址.地区}",toaddress.getDistrictorcounty());
        result.put("{收货地址.详情1}",toaddress.getAddressline1());
        result.put("{收货地址.详情2}",toaddress.getAddressline2());
        result.put("{收货地址.名称}",toaddress.getName());
        return result;
    }

    String getBoxNumber(Object boxnum){
        if(boxnum==null)return null;
        int num = Integer.parseInt(boxnum.toString());
        return String.format("U%06d", num);
    }

//    private Map<String, Object> getProductInfo(Map<String, Object> item,
//                                               ShipInboundPlan inboundplan,
//                                               ShipTransDetail transdetal,
//                                               String prefix){
//        Map<String, Object> pmap=new HashMap<String,Object>();
//        Integer qty= Integer.parseInt(item.get("QuantityShipped").toString());
//        pmap.put("{"+prefix+".sku}", item.get("SellerSKU"));
//        pmap.put("{"+prefix+".发货数量}",qty);
//        pmap.put("{"+prefix+".图片}", item.get("image"));
//        String materialid=item.get("mid").toString();
//        Material materialobj = materialService.selectByKey(materialid);
//        if(materialobj==null) {return pmap;}
//        MaterialCustoms customs = materialCustomsMapper.selectByPrimaryKey(materialid);
//        String cateid = materialobj.getCategoryid();
//        MaterialCategory category = materialCategoryMapper.selectByPrimaryKey(cateid);
//        if(category!=null) {
//            pmap.put("{"+prefix+".分类名称}", category.getName());
//        }else {
//            pmap.put("{"+prefix+".分类名称}","");
//        }
//        if(materialobj.getBoxnum()!=null) {
//            pmap.put("{"+prefix+".单箱数量}",materialobj.getBoxnum());
//        }else {
//            pmap.put("{"+prefix+".单箱数量}","");
//        }
//
//        String suppr = materialobj.getSupplier();
//        Customer customer = customerService.selectByKey(suppr);
//        if(customer!=null) {
//            pmap.put("{"+prefix+".供应商名称}",customer.getName());
//            pmap.put("{"+prefix+".供应商地址}",customer.getAddress());
//            pmap.put("{"+prefix+".供应商电话}",customer.getPhoneNum());
//        }else {
//            pmap.put("{"+prefix+".供应商名称}","");
//            pmap.put("{"+prefix+".供应商地址}","");
//            pmap.put("{"+prefix+".供应商电话}","");
//        }
//        DimensionsInfo dim = dimensionsInfoService.selectByKey( materialobj.getPkgdimensions());
//        Product_Info info = productInfoMapper.findProductInfo(item.get("SellerSKU").toString(), inboundplan.getMarketplaceid(), inboundplan.getShopid(), inboundplan.getAmazongroupid());
//        if(customs!=null && StringUtil.isNotEmpty(customs.getNameCn())) {
//            pmap.put("{"+prefix+".中文名}", customs.getNameCn());
//        }else {
//            pmap.put("{"+prefix+".中文名}", materialobj.getName());
//        }
//        if(customs!=null && StrUtil.isNotEmpty(customs.getNameEn())) {
//            pmap.put("{"+prefix+".英文名}", customs.getNameEn());
//        }else {
//            pmap.put("{"+prefix+".英文名}", info.getName());
//        }
//        if(customs!=null) {
//            if(customs.getUnitprice()!=null) {
//                pmap.put("{"+prefix+".申报单价}",customs.getUnitprice());
//                pmap.put("{"+prefix+".总申报价}",customs.getUnitprice().multiply(new BigDecimal(qty)));
//            }
//            pmap.put("{"+prefix+".产品材质}",customs.getMaterial());
//            pmap.put("{"+prefix+".产品型号}",customs.getModel());
//            pmap.put("{"+prefix+".产品品牌}",customs.getBrand());
//            pmap.put("{"+prefix+".产品用途}",customs.getMaterialUse());
//            pmap.put("{"+prefix+".海关编码}",customs.getCustomsCode());
//            pmap.put("{"+prefix+".是否带电}",booleanToCN(customs.getIselectricity()));
//            pmap.put("{"+prefix+".是否危险}",booleanToCN(customs.getIsdanger()));
//        }
//        if(materialobj.getPrice()!=null&&pmap.get("{"+prefix+".申报单价}")==null) {
//            pmap.put("{"+prefix+".申报单价}",materialobj.getPrice());
//            pmap.put("{"+prefix+".总申报价}",materialobj.getPrice().multiply(new BigDecimal(qty)));
//        }
//        if(materialobj.getBrand()!=null&&pmap.get("{"+prefix+".产品品牌}")==null) {
//            pmap.put("{"+prefix+".产品品牌}",materialobj.getBrand());
//        }
//
//        pmap.put("{"+prefix+".售价}",info.getPrice());
//        pmap.put("{"+prefix+".单位重量}",dim.getWeight());
//        pmap.put("{"+prefix+".总重量}",dim.getWeight().multiply(new BigDecimal(item.get("QuantityShipped").toString())));
//        pmap.put("{"+prefix+".长度}",dim.getLength());
//        pmap.put("{"+prefix+".宽度}",dim.getWidth());
//        pmap.put("{"+prefix+".高度}",dim.getHeight());
//        pmap.put("{"+prefix+".asin}",info.getAsin());
//        Marketplace marketplace = marketplaceService.findMapByMarketplaceId().get(info.getMarketplaceid());
//        pmap.put("{"+prefix+".销售连接}", "https://www."+marketplace.getPointName()+"/dp/"+info.getAsin()+"?th=1&psc=1");
//        if(dim.getHeight()!=null&&dim.getWidth()!=null&&dim.getHeight()!=null) {
//            if(transdetal!=null&&transdetal.getDrate()!=null) {
//                double cbm = dim.getHeight().doubleValue()*dim.getWidth().doubleValue()*dim.getHeight().doubleValue()/transdetal.getDrate();
//                pmap.put("{"+prefix+".单位材积}",String.format("%.2f", cbm));
//                pmap.put("{"+prefix+".总材积}",String.format("%.2f", cbm*qty));
//            }else {
//                double cbm = dim.getHeight().doubleValue()*dim.getWidth().doubleValue()*dim.getHeight().doubleValue()/5000.00;
//                pmap.put("{"+prefix+".单位材积}",String.format("%.2f", cbm));
//                pmap.put("{"+prefix+".总材积}",String.format("%.2f", cbm*qty));
//            }
//        }
//        return pmap;
//
//    }
//
//    String booleanToCN(Boolean value) {
//        if(value==null||value==false)return "否";
//        else return "是";
//    }
//
//    private static boolean deleteDir(File dir) {
//        if (dir.isDirectory()) {
//            String[] children = dir.list();
//            for (int i=0; i<children.length; i++) {
//                boolean success = deleteDir(new File(dir, children[i]));
//                if (!success) {
//                    return false;
//                }
//            }
//        }
//        // 目录此时为空，可以删除
//        return dir.delete();
//    }
//
//    private Map<String, Object> getProductBoxItem(Map<String, Object> box,
//                                                  ShipInboundCase caseinfo,
//                                                  Map<String, Object> pitem,
//                                                  ShipInboundPlan inboundplan,
//                                                  ShipTransDetail transdetal) {
//        // TODO Auto-generated method stub
//        Map<String,Object> result=new HashMap<String,Object>();
//        Map<String, Object> pmap = getProductInfo(pitem,  inboundplan,   transdetal,"箱内商品列表");
//        if(pmap!=null) {
//            result.putAll(pmap);
//        }
//        Map<String, Object> boxmap = getBoxInfo(box,"箱内商品列表");
//        if(boxmap!=null) {
//            result.putAll(boxmap);
//        }
//        result.put("{箱内商品列表.箱内数量}", caseinfo.getQuantity());
//        return result;
//    }
//
//
//
//    private List<String> splitValue(String value) {
//        List<String> ls=new ArrayList<String>();
//        Pattern pattern = Pattern.compile("\\{([^}]*)\\}");
//        Matcher matcher = pattern.matcher(value);
//        while(matcher.find()) {
//            ls.add(matcher.group());
//        }
//        return ls;
//    }
//
//
//    private void generateParam(UserInfo user, String shipmentid,
//                               HashMap<String, Object> mainparam,
//                               List<Map<String, Object>> plist,
//                               List<Map<String, Object>> boxlist,
//                               List<Map<String, Object>> pboxlist) {
//        // TODO Auto-generated method stub
//        ShipInboundShipment shipment = shipInboundShipmentService.selectByKey(shipmentid);
//        ShipInboundPlan inboundplan = shipInboundPlanService.selectByKey(shipment.getInboundplanid());
//        AmazonGroup amzgroup = amazonGroupMapper.selectByPrimaryKey(inboundplan.getAmazongroupid());
//        Marketplace marketplace = marketplaceService.findMapByMarketplaceId().get(inboundplan.getMarketplaceid());
//        ShipAddressTo toaddress = shipAddressToService.selectByKey(shipment.getShiptoaddressid());
//        ShipAddress fromaddress = shipAddressService.selectByKey(inboundplan.getShipfromaddressid());
//        List<Map<String, Object>> listmap = shipInboundShipmentService.findInboundItemByShipmentId(shipmentid);
//        List<Map<String, Object>> boxobjlist = shipInboundBoxService.findShipInboundBox(shipmentid);
//
//
//        Example exampleShipTran=new Example(ShipInboundTrans.class);
//        exampleShipTran.createCriteria().andEqualTo("shipmentid", shipmentid);
//        List<ShipInboundTrans> tranlist=shipInboundTransService.selectByExample(exampleShipTran);
//        ShipInboundTrans trans = null;
//        ShipTransDetail transdetal =null;
//        if(tranlist!=null&&tranlist.size()>0) {
//            trans = tranlist.get(0);
//            transdetal = shipTransDetailMapper.selectByPrimaryKey(trans.getChannel());
//        }
//
//        mainparam.put("{货件ID}", shipment.getShipmentid());
//        mainparam.put("{货件状态}", shipment.getStatus());
//        mainparam.put("{发货日期}", shipment.getShipedDate());
//        mainparam.put("{店铺名称}", amzgroup.getName());
//        mainparam.put("{站点名称}", marketplace.getName());
//        mainparam.put("{国家编码}", marketplace.getMarket());
//        mainparam.put("{币种编码}", marketplace.getCurrency());
//        mainparam.put("{币种符号}",GeneralUtil.formatCurrency(marketplace.getCurrency()));
//        Map<String, Object> fromaddressMap = getFromeAddress(fromaddress);
//        if(fromaddressMap!=null) {
//            mainparam.putAll(fromaddressMap);
//        }
//
//        Map<String, Object> toaddressMap = getToAddress(toaddress);
//        if(toaddressMap!=null) {
//            mainparam.putAll(toaddressMap);
//        }
//        int indexnum=1;
//        for(Map<String, Object> item:listmap) {
//            Map<String, Object> pmap = getProductInfo(item,inboundplan,transdetal,"商品列表");
//            pmap.put("{商品列表.序号}", indexnum++);
//            plist.add(pmap);
//        }
//        for(Map<String, Object> item:plist) {
//            if(mainparam.get("{货件带电}")==null) {
//                if("是".equals(item.get("{商品列表.是否带电}"))) {
//                    mainparam.put("{货件带电}","是");
//                }else {
//                    mainparam.put("{货件带电}", "否");
//                }
//            }
//            if(mainparam.get("{货件危险}")==null) {
//                if("是".equals(item.get("{商品列表.是否危险}"))) {
//                    mainparam.put("{货件危险}","是");
//                }else {
//                    mainparam.put("{货件危险}", "否");
//                }
//            }
//        }
//        if(boxobjlist!=null){
//            indexnum=1;
//            int indexpboxnum = 1;
//            for(Map<String, Object> box:boxobjlist) {
//                Map<String, Object> result = getBoxInfo(box,"箱子列表");
//                result.put("{箱子列表.序号}", indexnum++);
//                boxlist.add(result);
//                int boxnum=(Integer)box.get("boxnum");
//                List<ShipInboundCase> caseinfolist = shipInboundCaseService.findByShipmentBox(shipmentid, boxnum);
//                if(caseinfolist!=null && caseinfolist.size()>0) {
//                    for(ShipInboundCase caseinfo:caseinfolist) {
//                        if(caseinfo.getQuantity()<=0) {
//                            continue;
//                        }
//                        for(Map<String, Object> pitem:listmap) {
//                            if(pitem.get("SellerSKU")!=null&&caseinfo.getMerchantsku().equals(pitem.get("SellerSKU").toString())) {
//                                result=getProductBoxItem(box,caseinfo,pitem,inboundplan,transdetal);
//                                result.put("{箱内商品列表.序号}", indexpboxnum++);
//                                pboxlist.add(result);
//                            }
//                        }
//                    }
//                }
//
//
//            }
//        }
//
//    }



}
