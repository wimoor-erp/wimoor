package com.wimoor.erp.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.admin.api.AdminClientOneFeign;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.material.pojo.dto.PlanDTO;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.order.mapper.OrderListingMapper;
import com.wimoor.erp.order.mapper.OrderPlanMapper;
import com.wimoor.erp.order.mapper.OrderShipPlanMapper;
import com.wimoor.erp.order.pojo.entity.OrderListing;
import com.wimoor.erp.order.pojo.entity.OrderPlan;
import com.wimoor.erp.order.pojo.entity.OrderShipPlan;
import com.wimoor.erp.order.service.IOrderPlanService;
import com.wimoor.erp.order.service.IOrderShipPlanService;
import com.wimoor.erp.purchase.service.IPurchasePlanService;
import com.wimoor.erp.stock.mapper.ErpDispatchOverseaFormMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*; 

@Service
@RequiredArgsConstructor
public class OrderPlanServiceImpl extends ServiceImpl<OrderPlanMapper, OrderPlan> implements IOrderPlanService {

    final IPurchasePlanService iPurchasePlanService;
    final OrderListingMapper orderListingMapper;
    final ErpDispatchOverseaFormMapper erpDispatchOverseaFormMapper;
    final AdminClientOneFeign adminClientOneFeign;
    final IMaterialService iMaterialService;
    @Override
    public IPage<Map<String, Object>> findByCondition(PlanDTO dto) {
        Map<String, Object> param = BeanUtil.beanToMap(dto);
		Result<Map<String,Object>> tagnamelistResult=adminClientOneFeign.findTagsName(dto.getShopid());
        IPage<Map<String, Object>> list = this.baseMapper.findByCondition(dto.getPage(), param);
        List<String> skulist=new ArrayList<String>();
        for (Map<String, Object> item : list.getRecords()){
            item.put("warehouseid",dto.getWarehouseid());
            item.put("last",iPurchasePlanService.getLastForm(item));
            int sales7=item.get("sales7")!=null?Integer.parseInt(item.get("sales7").toString()):0;
            int sales15=item.get("sales15")!=null?Integer.parseInt(item.get("sales15").toString()):0;
            int sales30=item.get("sales30")!=null?Integer.parseInt(item.get("sales30").toString()):0;
            String country=item.get("country")!=null?item.get("country").toString():null;
            String sku=item.get("sku")!=null?item.get("sku").toString():null;
            int overseaqty=item.get("overseaqty")!=null? Integer.parseInt(item.get("overseaqty").toString()):0;
            int fulfillable=item.get("localqty")!=null? Integer.parseInt(item.get("localqty").toString()):0;
            skulist.add(sku);
            int stocking_cycle=item.get("stocking_cycle")!=null? Integer.parseInt(item.get("stocking_cycle").toString()):0;
            int min_cycle=item.get("min_cycle")!=null? Integer.parseInt(item.get("min_cycle").toString()):0;
            int first_leg_days=item.get("first_leg_days")!=null? new BigDecimal(item.get("first_leg_days").toString()).intValue():0;
            int put_on_days=item.get("put_on_days")!=null? new BigDecimal(item.get("put_on_days").toString()).intValue():0;
            int shipday=stocking_cycle+min_cycle+first_leg_days+put_on_days;
            int delivery_cycle=item.get("delivery_cycle")!=null? Integer.parseInt(item.get("delivery_cycle").toString()):0;
            int qty = OrderSkuPresaleServiceImpl.getAvgSales( OrderSkuPresaleServiceImpl.getDaySalesFormula(), sales30, sales7, sales15, null);
            item.put("salesavg",qty);
            if(qty>0){
                    int salesday = overseaqty / qty;
                    int aftersalesday = overseaqty+fulfillable / qty;
                    item.put("aftersalesday",aftersalesday);
                    item.put("salesday",salesday);
            }
            int needship = qty * shipday;
            item.put("shipday",shipday);
            int needpurchase=qty*(shipday+delivery_cycle);
            if(needship<=overseaqty){
                needship=0;
                needpurchase=0;
            }else{
                needship=needship-overseaqty;
                needpurchase=needpurchase-overseaqty;
            }
            item.put("needship",needship);
            item.put("needpurchase",needpurchase);
            if(needpurchase>=fulfillable){
                item.put("planamount",needpurchase-fulfillable);
            }
            if(dto.getExpendall()!=null&&dto.getExpendall()==true){
                Map<String,Object> paramDto=new HashMap<String,Object>();
                paramDto.put("shopid",dto.getShopid());
                paramDto.put("sku",item.get("sku"));
                item.put("expendData",getExpandCountryData(paramDto));
            }
        }
         
        Map<String,String> mskuTagsIdsMap=iMaterialService.getTagsIdsListByMsku(dto.getShopid(), skulist);
        if(Result.isSuccess(tagnamelistResult)&&tagnamelistResult.getData()!=null) {
							Map<String, Object> tagsNameMap = tagnamelistResult.getData();
			for(Map<String, Object> record : list.getRecords()) {
                List<String> tags = new ArrayList<>();
                if(record.get("sku") == null) {
                    continue;
                }
                String tagsids = mskuTagsIdsMap.get(record.get("sku").toString());
                if(tagsids != null) {
                    tags.addAll(Arrays.asList(tagsids.split(",")));
                }
                if(tags.size() > 0) {
                    List<Map<String, Object>> tagNameList = new ArrayList<>();
                    for(String id : tags) {
                        if(StrUtil.isNotBlank(id)) {
                            Object tagobj = tagsNameMap.get(id);
                            if(tagobj != null) {
                                tagNameList.add(BeanUtil.beanToMap(tagobj));
                            }
                        }
                    } 
                    if(tagNameList.size() > 0) {
                        record.put("tagNameList", tagNameList);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getExpandCountryData(Map<String, Object> param) {
        List<Map<String, Object>> list = this.baseMapper.getExpandCountryData(param);
        if(list!=null&&list.size()>0){
            for(Map<String,Object> item:list){
                int msalesavg=item.get("msalesavg") != null ? Integer.parseInt(item.get("msalesavg").toString()) : 0;
                int salesavg=item.get("salesavg") != null ? Integer.parseInt(item.get("salesavg").toString()) : 0;
                int fulfillable=item.get("fulfillable") != null ? Integer.parseInt(item.get("fulfillable").toString()) : 0;
                int amount=item.get("amount") != null ? Integer.parseInt(item.get("amount").toString()) : 0;
                int qty=msalesavg>0?msalesavg:salesavg;
                if(qty>0){
                    int salesday = fulfillable / qty;
                    int aftersalesday = fulfillable+amount / qty;
                    item.put("aftersalesday",aftersalesday);
                    item.put("salesday",salesday);
                }else if(fulfillable>0){
                    item.put("salesday",180);
                    item.put("aftersalesday",180);
                }else{
                    item.put("salesday","-");
                    item.put("aftersalesday","-");
                }
                int puOnDay=item.get("put_on_days") != null ? Integer.parseInt(item.get("put_on_days").toString()) : 0;
                String msku=item.get("msku") != null ? StrUtil.isNotBlank(item.get("msku").toString())? item.get("msku").toString():item.get("sku").toString():item.get("sku").toString();
                setShipRecord(item,param.get("shopid").toString(),msku,puOnDay);
            }
        }
        return list;
    }
    public void setShipRecord(Map<String,Object> item,String shopid,String sku,Integer puOnDay) {
        String warehouseid=item.get("warehouseid")!=null?item.get("warehouseid").toString():null;
        List<Map<String, Object>> shipRecord = erpDispatchOverseaFormMapper.getShipArrivalTimeRecord(shopid,warehouseid,null ,sku,null);
        if (shipRecord != null && shipRecord.size() > 0) {
            int i=0;
            Map<String, Object> ship=shipRecord.get(i++);
            while(ship.get("auditstatus")!=null&&ship.get("auditstatus").toString().equals("5")){
                ship=shipRecord.get(i++);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String nowday = GeneralUtil.getStrDate(new Date());
            String shiprecord = GeneralUtil.formatDate(GeneralUtil.getDate(ship.get("createdate")), sdf);
            if (nowday.indexOf(shiprecord) >= 0) {
                shiprecord = "今日";
                item.put("dayship", "istoday");
            } else {
                item.put("dayship", "notoday");
            }
            item.put("shipRecordQuantity", ship.get("Quantity").toString());
            item.put("shipRecordStatusName", getStatusName(Integer.parseInt(ship.get("auditstatus").toString())));
            item.put("shipRecordStatus", ship.get("auditstatus").toString());
            item.put("shipRecordDay", shiprecord);// 开始只展示一条记录，点击记录会显示最近3条发货记录
            if (ship.get("arrivalTime") != null) {
                Calendar today = Calendar.getInstance();
                Calendar cal = today;
                cal.setTime(GeneralUtil.getDatez(ship.get("arrivalTime").toString()));
                cal.add(Calendar.DAY_OF_MONTH, puOnDay);// 预计上架时间
                double arrivalday = GeneralUtil.distanceOfDay(cal, today);
                item.put("shipRecordArrivalday", arrivalday);
            }
        }
    }

    String getStatusName(Integer  auditstatus){
        	if(auditstatus==1) return "待审核";
            if(auditstatus==2) return "配货中";
            if(auditstatus==3) return "已发货";
            if(auditstatus==4) return "已完成";
            if(auditstatus==5) return "已驳回";
             return "";
    }
    @Override
    public Object bindMSku(Map<String, Object> param) {
        String sku=param.get("sku").toString();
        String msku=param.get("msku").toString();
        String shopid=param.get("shopid").toString();
        String country=param.get("country")!=null?param.get("country").toString():null;
        String name=param.get("name")!=null?param.get("name").toString():null;
        String warehouseid=param.get("warehouseid").toString();
        OrderListing ol=new OrderListing();
        ol.setShopid(shopid);
        if(country!=null){
            ol.setCountry(country);
        }
        if(name!=null){
            ol.setName(name);
        }
        ol.setWarehouseid(warehouseid);
        ol.setSku(sku);
        ol.setMsku(msku);
        LambdaQueryWrapper<OrderListing> query=new LambdaQueryWrapper<OrderListing>();
        query.eq(OrderListing::getShopid,ol.getShopid());
        query.eq(OrderListing::getWarehouseid,ol.getWarehouseid());
        query.eq(OrderListing::getSku,ol.getSku());
        OrderListing orderListing = orderListingMapper.selectOne(query);
        if(orderListing==null){
            orderListingMapper.insert(ol);
        }else{
            orderListingMapper.update(ol,query);
        }
        return true;
    }

    @Override
    public Object refreshData(Map<String, Object> param) {
        List<Map<String, Object>> list = this.baseMapper.getPlanData(param);
        for (Map<String, Object> item : list){
            
            int msalesavg=item.get("msalesavg") != null ? Integer.parseInt(item.get("msalesavg").toString()) : 0;
            int sales15 = item.get("sales15") != null ? Integer.parseInt(item.get("sales15").toString()) : 0;
            int sales7 = item.get("sales7") != null ? Integer.parseInt(item.get("sales7").toString()) : 0;
            int sales30 = item.get("sales30") != null ? Integer.parseInt(item.get("sales30").toString()) : 0;
            String country=item.get("country")!=null?item.get("country").toString():null;
            String localwarehouseid=item.get("localwarehouseid")!=null?item.get("localwarehouseid").toString():null;
            if(localwarehouseid==null){
                continue;
            }
            String sku=item.get("sku")!=null?item.get("sku").toString():null;
            if(sku==null){continue;}
            int fulfillable=item.get("fulfillable")!=null? Integer.parseInt(item.get("fulfillable").toString()):0;
            int stocking_cycle=item.get("stocking_cycle")!=null? Integer.parseInt(item.get("stocking_cycle").toString()):0;
            int min_cycle=item.get("min_cycle")!=null? Integer.parseInt(item.get("min_cycle").toString()):0;
            int first_leg_days=item.get("first_leg_days")!=null? new BigDecimal(item.get("first_leg_days").toString()).intValue():0;
            int shipday=item.get("shipday")!=null? Integer.parseInt(item.get("shipday").toString()):0;
            int delivery_cycle=item.get("delivery_cycle")!=null? Integer.parseInt(item.get("delivery_cycle").toString()):0;
            LambdaQueryWrapper<OrderListing> olquery=new LambdaQueryWrapper<OrderListing>();
            olquery.eq(OrderListing::getShopid,item.get("shopid"));
            olquery.eq(OrderListing::getWarehouseid,localwarehouseid);
            olquery.eq(OrderListing::getSku,item.get("sku"));
            OrderListing ol=orderListingMapper.selectOne(olquery);
            int qty = OrderSkuPresaleServiceImpl.getAvgSales( OrderSkuPresaleServiceImpl.getDaySalesFormula(), sales30, sales7, sales15, null);
            int mqty=msalesavg>0?msalesavg:qty;
            int needship = mqty * shipday;
            int needpurchase=mqty*(shipday+delivery_cycle);
            if(needship<=fulfillable){
                needship=0;
                needpurchase=0;
            }else{
                needship=needship-fulfillable;
                needpurchase=needpurchase-fulfillable;
            }
            if(ol==null){
                ol=new OrderListing();
                ol.setWarehouseid(localwarehouseid);
                ol.setShopid(param.get("shopid").toString());
                ol.setSku(sku);
                ol.setName(item.get("name")!=null?item.get("name").toString():null);
                ol.setCountry(country);
                ol.setQty(fulfillable);
                ol.setSales7(sales7);
                ol.setSales15(sales15);
                ol.setSales30(sales30);
                ol.setShipqty(needship);
                ol.setPurchaseqty(needpurchase);
                ol.setSalesavg(qty);
                orderListingMapper.insert(ol);
            }else{
                ol.setQty(fulfillable);
                ol.setSales7(sales7);
                ol.setSales15(sales15);
                ol.setSales30(sales30);
                ol.setShipqty(needship);
                ol.setPurchaseqty(needpurchase);
                ol.setSalesavg(qty);
                orderListingMapper.update(ol,olquery);

            }
        }
        return null;
    }

    @Override
    public Object clear(Map<String, Object> param) {
        LambdaQueryWrapper<OrderPlan> query=new LambdaQueryWrapper<OrderPlan>();
        query.eq(OrderPlan::getShopid,param.get("shopid"));
        return this.baseMapper.delete(query);
    }

    @Override
    public Object getPurchase(UserInfo user, String warehouseid) {
        return   this.baseMapper.getPurchase(user.getCompanyid(),warehouseid);
    }

    @Override
    public Object getShip(String shopid) {
        List<Map<String, Object>> list = this.baseMapper.getShip(shopid);
        Map<String,Map<String,Object>>  result = new HashMap<String,Map<String,Object>>();
        for(Map<String, Object> item:list){
            String warehouseid=item.get("warehouseid")!=null?item.get("warehouseid").toString():null;
            if(warehouseid==null){continue;}
            Map<String, Object> warehouse = result.get(warehouseid);
            if(warehouse==null){
                warehouse = new HashMap<String, Object>();
                warehouse.put("list",new ArrayList<Map<String,Object>>());
                warehouse.put("warehouseid",warehouseid);
                warehouse.put("warehousename",item.get("warehousename"));
                result.put(warehouseid,warehouse);
            }
            List<Map<String,Object>> mlist=(ArrayList<Map<String,Object>>)warehouse.get("list");
            mlist.add(item);
        }
        for(Map.Entry<String, Map<String, Object>> entry:result.entrySet()){
            Map<String, Object> warehouse = entry.getValue();
            List<Map<String,Object>> mlist=(ArrayList<Map<String,Object>>)warehouse.get("list");
            Integer summaryship=0;
            Integer count=0;
            BigDecimal totalvolume=new BigDecimal(0);
            BigDecimal totalweight=new BigDecimal(0);
            BigDecimal totalprice=new BigDecimal(0);
            for(Map<String,Object> item:mlist){
                summaryship=summaryship+Integer.parseInt(item.get("num").toString());
                totalvolume=totalvolume.add(new BigDecimal(item.get("totalvolume").toString()));
                totalweight=totalweight.add(new BigDecimal(item.get("totalweight").toString()));
                totalprice=totalprice.add(new BigDecimal(item.get("price").toString()));
            }
            warehouse.put("summaryship",summaryship);//总发货数量
            warehouse.put("totalvolume",totalvolume);//总体积
            warehouse.put("totalweight",totalweight);//总重量
            warehouse.put("totalprice",totalprice);//总货值
            warehouse.put("count",mlist.size());//sku个数
        }
        return result.values();
    }
}
