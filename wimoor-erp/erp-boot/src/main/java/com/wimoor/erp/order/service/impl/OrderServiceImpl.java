package com.wimoor.erp.order.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.pojo.entity.BasePageQuery;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ChartLine;
import com.wimoor.erp.common.pojo.entity.ChartPoint;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.order.mapper.OrderMapper;
import com.wimoor.erp.order.pojo.entity.Order;
import com.wimoor.erp.order.pojo.entity.OrderPlatform;
import com.wimoor.erp.order.service.IOrderPlatformService;
import com.wimoor.erp.order.service.IOrderService;
import com.wimoor.erp.stock.pojo.entity.OutWarehouseForm;
import com.wimoor.erp.stock.service.IOutWarehouseFormService;
import com.wimoor.erp.util.CountryUtil;
import com.wimoor.erp.warehouse.pojo.entity.Warehouse;
import com.wimoor.erp.warehouse.service.IWarehouseService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    private final IOrderPlatformService orderPlatformService;
    private final ISerialNumService iSerialNumService;
    private final IWarehouseService iWarehouseService;
    private final IMaterialService iMaterialsService;
    private final IOutWarehouseFormService iOutWarehouseFormService;
    @Override
    public IPage<Map<String, Object>> findByCondition(BasePageQuery dto) {
        IPage<Map<String, Object>> list = this.baseMapper.findByCondition(dto.getPage(), dto.getParamother());
        if(list!=null&&list.getRecords().size()>0){
            for(Map<String, Object> map:list.getRecords()){
                if(map.get("country")!=null&&!map.get("country").equals("")){
                    String countryName=CountryUtil.getCountryName(map.get("country").toString());
                    if(countryName!=null){
                        countryName=map.get("country").toString();
                        map.put("country",countryName );
                    }
                }
            }
        }
        return list;
    }

    @Override
    public IPage<Map<String, Object>> findOrderByCondition(BasePageQuery dto) {
        IPage<Map<String, Object>> list =  this.baseMapper.findOrderByCondition(dto.getPage(),dto.getParamother());
        if(list!=null&&list.getRecords().size()>0){
            for(Map<String, Object> map:list.getRecords()){
                if(map.get("country")!=null&&!map.get("country").equals("")){
                    String countryName=CountryUtil.getCountryName(map.get("country").toString());
                    if(countryName!=null){
                        countryName=map.get("country").toString();
                    }
                    map.put("country",countryName );
                }
            }
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> listOrderByCondition(BasePageQuery dto) {
        List<Map<String, Object>> list = this.baseMapper.findOrderByCondition(dto.getParamother());
        if(list!=null&&list.size()>0){
            for(Map<String, Object> map:list){
                if(map.get("country")!=null&&!map.get("country").equals("")){
                    String countryName=CountryUtil.getCountryName(map.get("country").toString());
                    if(countryName!=null){
                        countryName=map.get("country").toString();
                    }
                    map.put("country",countryName );
                }
            }
        }
        return list;
    }

    public IPage<Map<String, Object>> findMaterialByCondition(Page<?> page, Map<String, String> map) {
        return  this.baseMapper.findMaterialByCondition(page,map);
    }
    public List<Map<String, Object>> findMaterialBySelect(Map<String, String> map) {
        return  this.baseMapper.findMaterialByCondition(map);
    }

    @Override
    public String uploadOrder(Sheet sheet, UserInfo user,String warehouseid) {
        OutWarehouseForm param=new OutWarehouseForm();
        param.setShopid(user.getCompanyid());
        param.setAuditor(user.getId());
        param.setAudittime(new Date());
        param.setAuditstatus(1);
        param.setFtype(3);
        param.setOperator(user.getId());
        param.setOpttime(new Date());
        param.setRemark("多平台订单创建");
        String shopid=user.getCompanyid();
        HashMap<String, Object> skuMap = new HashMap<String, Object>();
        Row titleRow = sheet.getRow(0);
        Cell namecell = titleRow.getCell(0);
        String name= namecell.getStringCellValue();
        if(StrUtil.isNotBlank(warehouseid)){
            Warehouse warehouse = iWarehouseService.getById(warehouseid);
            if(warehouse==null){
                warehouseid=null;
            }
        }
        if(name.equals("平台")){
            for(int i=0;i<sheet.getLastRowNum();i++){
                Row contentrow = sheet.getRow(i+1);
                Cell cell = contentrow.getCell(0);
                cell.setCellType(CellType.STRING);
                String platname = cell.getStringCellValue();
                if(StrUtil.isBlank(platname)){
                    continue;
                }
                Order order=new Order();
                OrderPlatform plat = orderPlatformService.lambdaQuery().eq(OrderPlatform::getName, platname).eq(OrderPlatform::getShopid, user.getCompanyid()).one();
                order.setPlatformId(plat.getId());
                cell = contentrow.getCell(1);
                cell.setCellType(CellType.STRING);
                String warehousename = cell.getStringCellValue();
                Warehouse warehouse = iWarehouseService.lambdaQuery().eq(Warehouse::getName, warehousename).eq(Warehouse::getShopid, user.getCompanyid()).one();
                if(StrUtil.isBlank(warehouseid)){
                    warehouseid=warehouse.getId();
                }
                order.setWarehouseid(warehouse.getId());
                cell = contentrow.getCell(2);
                cell.setCellType(CellType.STRING);
                String country = cell.getStringCellValue();
                order.setCountry(getCountryCode(country));
                cell = contentrow.getCell(2);
                cell.setCellType(CellType.STRING);
                String sku = cell.getStringCellValue();
                Material material = iMaterialsService.getBySku(shopid, sku);
                cell = contentrow.getCell(3);
                cell.setCellType(CellType.STRING);
                String orderid = cell.getStringCellValue();
                cell = contentrow.getCell(4);
                Date purchaseDate = cell.getDateCellValue();
                cell = contentrow.getCell(5);
                cell.setCellType(CellType.STRING);
                String quantity = cell.getStringCellValue();
                Integer qty= StrUtil.isNotBlank(quantity)?Integer.parseInt(quantity):null;
                cell = contentrow.getCell(6);
                cell.setCellType(CellType.STRING);
                String priceStr = cell.getStringCellValue();
                BigDecimal price=StrUtil.isNotBlank(priceStr)?new BigDecimal(priceStr):null;
                cell = contentrow.getCell(7);
                cell.setCellType(CellType.STRING);
                String shipStr = cell.getStringCellValue();
                BigDecimal shipfee=StrUtil.isNotBlank(shipStr)?new BigDecimal(shipStr):null;
                cell = contentrow.getCell(8);
                cell.setCellType(CellType.STRING);
                String refStr = cell.getStringCellValue();
                BigDecimal reffee=StrUtil.isNotBlank(refStr)?new BigDecimal(refStr):null;
                cell = contentrow.getCell(9);
                cell.setCellType(CellType.STRING);
                String refRateStr = cell.getStringCellValue();
                BigDecimal refRate=StrUtil.isNotBlank(refRateStr)?new BigDecimal(refRateStr):null;
                order.setShipFee(shipfee);
                order.setQuantity(qty);
                order.setSku(sku);
                order.setOrderId(orderid);
                order.setWarehouseid(warehouseid);
                order.setPurchaseDate(purchaseDate);
                order.setPrice(price);
                order.setReferralFee(reffee);
                order.setReferralRate(refRate);
                order.setShopid(shopid);
                order.setIsout(true);
                order.setOpttime(new Date());
                order.setOperator(user.getId());
                Order old = this.lambdaQuery()
                        .eq(Order::getShopid, shopid)
                        .eq(Order::getSku, order.getSku())
                        .eq(Order::getOrderId, order.getOrderId())
                        .eq(Order::getPlatformId, order.getPlatformId())
                        .one();
                if(old!=null){
                    order.setId(old.getId());
                    this.updateById(order);
                }else{
                    this.save(order);
                }
                skuMap.put(material.getId(), qty);
            }
        }else if(name.equals("Platform")){
            for(int i=0;i<sheet.getLastRowNum();i++){
                Row contentrow = sheet.getRow(i+1);
                Cell cell = contentrow.getCell(0);
                cell.setCellType(CellType.STRING);
                String platname = cell.getStringCellValue();
                if(StrUtil.isBlank(platname)){
                    continue;
                }
                Order order=new Order();
                OrderPlatform plat = orderPlatformService.lambdaQuery().eq(OrderPlatform::getName, platname).eq(OrderPlatform::getShopid, user.getCompanyid()).one();
                order.setPlatformId(plat.getId());
                cell = contentrow.getCell(1);
                cell.setCellType(CellType.STRING);
                String orderid = cell.getStringCellValue();
                order.setOrderId(orderid);


                cell = contentrow.getCell(3);
                cell.setCellType(CellType.STRING);
                Date orderdate = cell.getDateCellValue();
                order.setPurchaseDate(orderdate);

                cell = contentrow.getCell(8);
                cell.setCellType(CellType.STRING);
                String sku=cell.getStringCellValue();
                order.setSku(sku);
                Material material = iMaterialsService.getBySku(shopid, sku);
                cell = contentrow.getCell(34);
                String country = cell.getStringCellValue();
                order.setCountry(getCountryCode(country));

                cell = contentrow.getCell(12);
                cell.setCellType(CellType.STRING);
                String quantity = cell.getStringCellValue();
                Integer qty= StrUtil.isNotBlank(quantity)?Integer.parseInt(quantity):null;

                cell = contentrow.getCell(14);
                cell.setCellType(CellType.STRING);
                String priceStr = cell.getStringCellValue();
                BigDecimal price=StrUtil.isNotBlank(priceStr)?new BigDecimal(priceStr):null;

                cell = contentrow.getCell(15);
                cell.setCellType(CellType.STRING);
                String shipStr = cell.getStringCellValue();
                BigDecimal shipfee=StrUtil.isNotBlank(shipStr)?new BigDecimal(shipStr):null;

                cell = contentrow.getCell(16);
                cell.setCellType(CellType.STRING);
                String refStr = cell.getStringCellValue();
                BigDecimal reffee=StrUtil.isNotBlank(refStr)?new BigDecimal(refStr):null;

                order.setShipFee(shipfee);
                order.setQuantity(qty);
                order.setSku(sku);
                order.setOrderId(orderid);
                order.setWarehouseid(warehouseid);
                order.setPrice(price);
                order.setReferralFee(reffee);
                order.setShopid(shopid);
                order.setIsout(true);
                order.setOpttime(new Date());
                order.setOperator(user.getId());
                Order old = this.lambdaQuery()
                        .eq(Order::getShopid, shopid)
                        .eq(Order::getSku, order.getSku())
                        .eq(Order::getOrderId, order.getOrderId())
                        .eq(Order::getPlatformId, order.getPlatformId())
                        .one();
                if(old!=null){
                    order.setId(old.getId());
                    this.updateById(order);
                }else{
                    this.save(order);
                }
                skuMap.put(material.getId(), qty);
            }
        }
        try {
            param.setNumber(iSerialNumService.readSerialNumber(shopid, "O"));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                param.setNumber(iSerialNumService.readSerialNumber(shopid, "O"));
            } catch (Exception e1) {
                e1.printStackTrace();
                throw new BizException("编码获取失败,请联系管理员");
            }
        }
        param.setWarehouseid(warehouseid);
        iOutWarehouseFormService.saveForm(param, skuMap, user);
        return "";
    }
    public String getCountryCode(String countryName){
        String countryCode=null;
        countryCode= CountryUtil.getCountryCode(countryName);
        if(StrUtil.isBlank(countryCode)){
            countryCode= CountryUtil.getCountryCodeEn(countryName);
        }
        if(StrUtil.isBlank(countryCode)){
            countryCode=countryName;
        }
        return countryCode;
    }

    public void summary(){
        this.baseMapper.summary();
    }

    @Override
    public ChartLine findOrderSummaryBySku(String shopid, String warehouseid, String sku, String daysize, UserInfo userinfo) {
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM.dd");
        Map<String, Object> param = new HashMap<String, Object>();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, Integer.parseInt(daysize)*-1-1);
        param.put("warehouseid", warehouseid);
        param.put("purchaseDate", GeneralUtil.getStrDate(c.getTime()));
        param.put("shopid",shopid);
        param.put("sku",sku);
       List<Map<String,Object>>  list = this.baseMapper.listByDate(param);
        SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");
        Map<String, Object> tempmap = new HashMap<String, Object>();
        if (list != null && list.size() > 0) {
            for(int i = 0; i < list.size(); i++) {
                Map<String, Object> item = list.get(i);
                if(item.get("purchaseDate")!=null){
                    tempmap.put(item.get("sku").toString() + sdf.format(GeneralUtil.getDate(item.get("purchaseDate"))), item.get("quantity"));
                }
            }
        }
        Calendar today=Calendar.getInstance();
        today.add(Calendar.DATE,-1);
        String yesterday=sdf.format(today.getTime());
        List<Object> myskuData = new ArrayList<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        int summarySales = 0;
        List<ChartPoint> markpoint = new ArrayList<ChartPoint>();
        String mysku =  sku;
        for (int i = 0; i <= Integer.parseInt(daysize); i++, c.add(Calendar.DATE, 1)) {
            String tempkey = sdf.format(c.getTime());
            Integer value = tempmap.get(mysku + tempkey) == null ? Integer.valueOf("0") : Integer.valueOf(tempmap.get(mysku + tempkey).toString());
            myskuData.add(value);
            summarySales += value;
        }
        ChartLine chartline=new ChartLine();
        chartline.setMarkpoint(markpoint);
        chartline.setData(myskuData);
        chartline.setName(mysku);
        chartline.setSummary(summarySales);
        return chartline;
    }
}
