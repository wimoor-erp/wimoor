package com.wimoor.erp.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.order.mapper.ErpOverseaCycleMapper;
import com.wimoor.erp.order.mapper.OrderShipCycleMapper;
import com.wimoor.erp.order.pojo.entity.ErpOverseaCycle;
import com.wimoor.erp.order.pojo.entity.OrderShipCycle;
import com.wimoor.erp.order.service.IErpOverseaCycleService;
import com.wimoor.erp.order.service.IOrderShipCycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderShipCycleServiceImpl extends ServiceImpl<OrderShipCycleMapper, OrderShipCycle> implements IOrderShipCycleService {

    public int updateStockCycleTransType(OrderShipCycle cycle, UserInfo user) {
        int result = 0;
        OrderShipCycle stockcy =  this.getFbaShipCycle(cycle.getWarehouseid().toString(),cycle.getSku());
        if (stockcy == null) {
            stockcy =cycle;
            stockcy.setOperator(new BigInteger(user.getId()));
            stockcy.setOpttime(new Date());
            if( this.save(stockcy)) {
                result++;
            }
        }else {
            stockcy.setStockingcycle(cycle.getStockingcycle());
            stockcy.setTranstype(cycle.getTranstype());
            stockcy.setOperator(new BigInteger(user.getId()));
            stockcy.setOpttime(new Date());
            this.baseMapper.deleteById(stockcy.getId());
            return this.baseMapper.insert(stockcy);
        }
        return result;
    }
    public int updateStockCycle(String warehouseid,  String sku, Integer stockcycle,  UserInfo user) {
        int result = 0;
        OrderShipCycle stockcy =  this.getFbaShipCycle(sku, warehouseid);
        if (stockcy == null) {
            stockcy = new OrderShipCycle();
            stockcy.setStockingcycle(stockcycle);
            stockcy.setSku(sku);
            stockcy.setWarehouseid(new BigInteger(warehouseid));
            stockcy.setOperator(new BigInteger(user.getId()));
            stockcy.setOpttime(new Date());
            if( this.save(stockcy)) {
                result++;
            }
        }else {
            stockcy.setStockingcycle(stockcycle);
            stockcy.setOperator(new BigInteger(user.getId()));
            stockcy.setOpttime(new Date());
            if(this.updateById(stockcy)) {
                result++;
            }
        }
        return result;
    }

    public int updateStockCycle(String warehouseid,  String sku, Integer stockcycle, Integer mincycle, BigDecimal fee, UserInfo user) {
        int result = 0;
        OrderShipCycle stockcy =  this.getFbaShipCycle(sku, warehouseid);
        if (stockcy == null) {
            stockcy = new OrderShipCycle();
            stockcy.setStockingcycle(stockcycle);
            stockcy.setMinCycle(mincycle);
            stockcy.setFirstLegCharges(fee);
            stockcy.setSku(sku);
            stockcy.setWarehouseid(new BigInteger(warehouseid));
            stockcy.setOperator(new BigInteger(user.getId()));
            stockcy.setOpttime(new Date());
            if( this.save(stockcy)) {
                result++;
            }
        }else {
            stockcy.setStockingcycle(stockcycle);
            stockcy.setMinCycle(mincycle);
            stockcy.setFirstLegCharges(fee);
            stockcy.setOperator(new BigInteger(user.getId()));
            stockcy.setOpttime(new Date());
            if(this.updateById(stockcy)) {
                result++;
            }
        }
        return result;
    }
    @CacheEvict(value = { "FbaCycle" }, allEntries = true)
    public boolean updateMinCycle(String warehouseid, String type,String sku,Integer num, UserInfo user) {
        OrderShipCycle stockcy =  this.getFbaShipCycle(sku, warehouseid);
        if (stockcy == null) {
            stockcy = new OrderShipCycle();
            stockcy.setMinCycle(num);
            stockcy.setSku(sku);
            stockcy.setWarehouseid(new BigInteger(warehouseid));
            stockcy.setOperator(new BigInteger(user.getId()));
            stockcy.setOpttime(new Date());
            return this.save(stockcy);
        }else {
            stockcy.setMinCycle(num);
            stockcy.setOperator(new BigInteger(user.getId()));
            stockcy.setOpttime(new Date());
            return this.updateById(stockcy);
        }
    }
    @CacheEvict(value = { "FbaCycle" }, allEntries = true)
    public boolean updateFirstLegCharges(String warehouseid,String type,String sku,Integer num, UserInfo user) {
        OrderShipCycle stockcy =  this.getFbaShipCycle(sku, warehouseid);
        if (stockcy == null) {
            stockcy = new OrderShipCycle();
            stockcy.setFirstLegCharges(new BigDecimal(num));
            stockcy.setSku(sku);
            stockcy.setWarehouseid(new BigInteger(warehouseid));
            stockcy.setOperator(new BigInteger(user.getId()));
            stockcy.setOpttime(new Date());
            return this.save(stockcy);
        }else {
            stockcy.setFirstLegCharges(new BigDecimal(num));
            stockcy.setOperator(new BigInteger(user.getId()));
            stockcy.setOpttime(new Date());
            return this.updateById(stockcy);
        }
    }

    public OrderShipCycle getFbaShipCycle(String warehouseid,String sku) {
        // TODO Auto-generated method stub
        LambdaQueryWrapper<OrderShipCycle> query=new LambdaQueryWrapper<OrderShipCycle>();
        query.eq(OrderShipCycle::getWarehouseid, warehouseid);
        query.eq(OrderShipCycle::getSku, sku);
        return this.getOne(query);
    }

    public Map<String,OrderShipCycle> getFbaShipCycle(String warehouseid) {
        // TODO Auto-generated method stub
        LambdaQueryWrapper<OrderShipCycle> query=new LambdaQueryWrapper<OrderShipCycle>();
        query.eq(OrderShipCycle::getWarehouseid, warehouseid);
        List<OrderShipCycle> list = this.list(query);
        Map<String,OrderShipCycle> result=new HashMap<String,OrderShipCycle>();
        for(OrderShipCycle item:list) {
            result.put(item.getSku(), item);
        }
        return result;
    }

}
