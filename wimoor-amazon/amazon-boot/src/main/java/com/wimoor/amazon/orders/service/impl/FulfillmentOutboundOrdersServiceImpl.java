package com.wimoor.amazon.orders.service.impl;

import cn.hutool.json.JSONUtil;
import com.amazon.spapi.api.FbaOutboundApi;
import com.amazon.spapi.model.fulfillmentoutbound.*;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.service.IAmazonAuthorityService;
import com.wimoor.amazon.auth.service.IRunAmazonService;
import com.wimoor.amazon.auth.service.impl.ApiBuildService;
import com.wimoor.amazon.orders.mapper.FulfillmentOutboundOrderItemsMapper;
import com.wimoor.amazon.orders.mapper.FulfillmentOutboundOrderMapper;
import com.wimoor.amazon.orders.mapper.FulfillmentOutboundOrderReturnItemMapper;
import com.wimoor.amazon.orders.mapper.FulfillmentOutboundOrderShipmentMapper;
import com.wimoor.amazon.orders.pojo.dto.OrdersFulfillmentDTO;
import com.wimoor.amazon.orders.pojo.entity.FulfillmentOutboundOrder;
import com.wimoor.amazon.orders.pojo.entity.FulfillmentOutboundOrderItems;
import com.wimoor.amazon.orders.pojo.entity.FulfillmentOutboundOrderReturnItem;
import com.wimoor.amazon.orders.pojo.entity.FulfillmentOutboundOrderShipment;
import com.wimoor.amazon.orders.service.IFulfillmentOutboundOrdersService;
import com.wimoor.amazon.util.AmzDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class FulfillmentOutboundOrdersServiceImpl extends ServiceImpl<FulfillmentOutboundOrderMapper, FulfillmentOutboundOrder> implements IFulfillmentOutboundOrdersService {
    @Autowired
    IAmazonAuthorityService amazonAuthorityService;
    @Autowired
    ApiBuildService apiBuildService;
    @Autowired
    FulfillmentOutboundOrderItemsMapper fulfillmentOutboundOrderItemsMapper;
    @Autowired
    FulfillmentOutboundOrderShipmentMapper fulfillmentOutboundOrderShipmentMapper;
    @Autowired
    FulfillmentOutboundOrderReturnItemMapper fulfillmentOutboundOrderReturnItemMapper;
    @Override
    public String getFulfillmentOrders(String authid) {
        AmazonAuthority amazonAuthority = amazonAuthorityService.getById(authid);
        FbaOutboundApi fbaOutboundApi = apiBuildService.getOutboundApi(amazonAuthority);
        Calendar c= Calendar.getInstance();
        c.add(Calendar.DATE,-60);
        try{
            ListAllFulfillmentOrdersResponse response = fbaOutboundApi.listAllFulfillmentOrders(AmzDateUtils.getOffsetDateTimeUTC(c.getTime()), null);
            response.getPayload().getFulfillmentOrders().forEach(item->{
                setFulfillmentOutboundOrder(amazonAuthority, item,null);
            });
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IPage<FulfillmentOutboundOrder> getList(OrdersFulfillmentDTO dto) {
        IPage<FulfillmentOutboundOrder> list  = this.baseMapper.findList(dto.getPage(), dto);
        return list;
    }


    private void setFulfillmentOutboundOrder(AmazonAuthority amazonAuthority, FulfillmentOrder item, Date refreshtime){
        if(item==null)return ;
        String sellerFulfillmentOrderId=item.getSellerFulfillmentOrderId();
        FulfillmentOutboundOrder fulfillmentOutboundOrder = this.baseMapper.selectById(sellerFulfillmentOrderId);
        boolean fulfillmentOutboundOrderExists = true;
        if(fulfillmentOutboundOrder==null){
            fulfillmentOutboundOrderExists = false;
            fulfillmentOutboundOrder=new FulfillmentOutboundOrder();
        }
        fulfillmentOutboundOrder.setDisplayableOrderId(item.getDisplayableOrderId());
        fulfillmentOutboundOrder.setDisplayableOrderComment(item.getDisplayableOrderComment());
        fulfillmentOutboundOrder.setFulfillmentOrderStatus(item.getFulfillmentOrderStatus()!=null ?item.getFulfillmentOrderStatus().getValue():null);
        fulfillmentOutboundOrder.setFulfillmentPolicy(item.getFulfillmentPolicy()!=null ?item.getFulfillmentPolicy().getValue():null);
        fulfillmentOutboundOrder.setDisplayableOrderDate(AmzDateUtils.getDate(item.getDisplayableOrderDate()));
        fulfillmentOutboundOrder.setDestinationAddress(JSONUtil.toJsonStr(item.getDestinationAddress()));
        fulfillmentOutboundOrder.setDeliveryStart(AmzDateUtils.getDate(item.getDeliveryWindow()!=null?item.getDeliveryWindow().getStartDate():null));
        fulfillmentOutboundOrder.setDeliveryEnd(AmzDateUtils.getDate(item.getDeliveryWindow()!=null?item.getDeliveryWindow().getEndDate():null));
        fulfillmentOutboundOrder.setFeatureConstraints(JSONUtil.toJsonStr(item.getFeatureConstraints()));
        fulfillmentOutboundOrder.setFulfillmentAction(item.getFulfillmentAction()!=null ?item.getFulfillmentAction().getValue():null);
        fulfillmentOutboundOrder.setMarketplaceId(item.getMarketplaceId());
        fulfillmentOutboundOrder.setNotificationEmails(item.getNotificationEmails()!=null?JSONUtil.toJsonStr(item.getNotificationEmails()):null);
        fulfillmentOutboundOrder.setSellerFulfillmentOrderId(item.getSellerFulfillmentOrderId());
        fulfillmentOutboundOrder.setReceivedDate(AmzDateUtils.getDate(item.getReceivedDate()));
        fulfillmentOutboundOrder.setStatusUpdatedDate(AmzDateUtils.getDate(item.getStatusUpdatedDate()));
        fulfillmentOutboundOrder.setAmazonauthid(amazonAuthority.getId());
        if(refreshtime!=null){
            fulfillmentOutboundOrder.setRefreshtime(refreshtime);
        }
        FulfillmentOutboundOrder old = this.baseMapper.selectById(item.getSellerFulfillmentOrderId());
        if(old!=null){
            this.baseMapper.updateById(fulfillmentOutboundOrder);
        }else{
            this.baseMapper.insert(fulfillmentOutboundOrder);
        }
    }
   private void setFulfillmentOutboundOrderItems(String sellerFulfillmentOrderId, FulfillmentOrderItemList list){
        for(FulfillmentOrderItem item:list){
            LambdaQueryWrapper<FulfillmentOutboundOrderItems> query=new LambdaQueryWrapper<FulfillmentOutboundOrderItems>();
            query.eq(FulfillmentOutboundOrderItems::getSellerFulfillmentOrderId,sellerFulfillmentOrderId);
            query.eq(FulfillmentOutboundOrderItems::getSellerFulfillmentOrderItemId,item.getSellerFulfillmentOrderItemId());
            FulfillmentOutboundOrderItems fulfillmentOutboundOrderItems = fulfillmentOutboundOrderItemsMapper.selectOne(query);
            boolean fulfillmentOutboundOrderItemsExist = true;
            if(fulfillmentOutboundOrderItems==null){
                fulfillmentOutboundOrderItemsExist = false;
                fulfillmentOutboundOrderItems=new FulfillmentOutboundOrderItems();
            }
            fulfillmentOutboundOrderItems.setSellerFulfillmentOrderId(sellerFulfillmentOrderId);
            fulfillmentOutboundOrderItems.setSellerFulfillmentOrderItemId(item.getSellerFulfillmentOrderItemId());
            fulfillmentOutboundOrderItems.setFulfillmentNetworkSku(item.getFulfillmentNetworkSku());
            fulfillmentOutboundOrderItems.setQuantity(item.getQuantity());
            fulfillmentOutboundOrderItems.setGiftMessage(item.getGiftMessage());
            fulfillmentOutboundOrderItems.setDisplayableComment(item.getDisplayableComment());
            fulfillmentOutboundOrderItems.setEstimatedShipDate(AmzDateUtils.getDate(item.getEstimatedShipDate()));
            fulfillmentOutboundOrderItems.setSellerSku(item.getSellerSku());
            fulfillmentOutboundOrderItems.setCancelledQuantity(item.getCancelledQuantity());
            fulfillmentOutboundOrderItems.setUnfulfillableQuantity(item.getUnfulfillableQuantity());
            fulfillmentOutboundOrderItems.setPerUnitDeclaredValue(item.getPerUnitPrice()!=null?new BigDecimal(item.getPerUnitDeclaredValue().getValue()):null);
            fulfillmentOutboundOrderItems.setPerUnitPrice(item.getPerUnitPrice()!=null?new BigDecimal(item.getPerUnitPrice().getValue()):null);
            fulfillmentOutboundOrderItems.setOrderItemDisposition(item.getOrderItemDisposition());
            fulfillmentOutboundOrderItems.setEstimatedArrivalDate(AmzDateUtils.getDate(item.getEstimatedArrivalDate()));
            fulfillmentOutboundOrderItems.setPerUnitTax(item.getPerUnitTax()!=null?new BigDecimal(item.getPerUnitTax().getValue()):null);
            if(fulfillmentOutboundOrderItemsExist){
                fulfillmentOutboundOrderItemsMapper.update(fulfillmentOutboundOrderItems,query);
            }else{
                fulfillmentOutboundOrderItemsMapper.insert(fulfillmentOutboundOrderItems);
            }
        }

   }
    public String getFulfillmentOrder(AmazonAuthority amazonAuthority,String sellerFulfillmentOrderId) {
        FbaOutboundApi fbaOutboundApi = apiBuildService.getOutboundApi(amazonAuthority);
        try{
            GetFulfillmentOrderResponse response = fbaOutboundApi.getFulfillmentOrder(sellerFulfillmentOrderId);
            if(response!=null&&response.getPayload()!=null){
                this.setFulfillmentOutboundOrder(amazonAuthority,response.getPayload().getFulfillmentOrder(),new Date());
                this.setFulfillmentOutboundOrderItems(sellerFulfillmentOrderId,response.getPayload().getFulfillmentOrderItems());
                this.setFulfillmentOutboundOrderShipment(sellerFulfillmentOrderId,response.getPayload().getFulfillmentShipments());
                this.setFulfillmentOutboundOrderReturnItems(sellerFulfillmentOrderId,response.getPayload().getReturnItems());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void setFulfillmentOutboundOrderReturnItems(String sellerFulfillmentOrderId, ReturnItemList returnItems) {
        for(ReturnItem item:returnItems){
            LambdaQueryWrapper<FulfillmentOutboundOrderReturnItem> query=new LambdaQueryWrapper<FulfillmentOutboundOrderReturnItem>();
            query.eq(FulfillmentOutboundOrderReturnItem::getSellerFulfillmentOrderItemId,sellerFulfillmentOrderId);
            query.eq(FulfillmentOutboundOrderReturnItem::getSellerReturnItemId,item.getSellerReturnItemId());
            FulfillmentOutboundOrderReturnItem fulfillmentOutboundOrderReturnItems =fulfillmentOutboundOrderReturnItemMapper.selectOne(query);
            boolean fulfillmentOutboundOrderReturnItemsExists = true;
            if(fulfillmentOutboundOrderReturnItems==null){
                fulfillmentOutboundOrderReturnItems=new FulfillmentOutboundOrderReturnItem();
                fulfillmentOutboundOrderReturnItemsExists=false;
            }
            fulfillmentOutboundOrderReturnItems.setReturnComment(item.getReturnComment());
            fulfillmentOutboundOrderReturnItems.setReturnReceivedCondition(item.getReturnReceivedCondition()!=null?item.getReturnReceivedCondition().getValue():null);
            fulfillmentOutboundOrderReturnItems.setSellerFulfillmentOrderItemId(item.getSellerFulfillmentOrderItemId());
            fulfillmentOutboundOrderReturnItems.setSellerReturnItemId(item.getSellerReturnItemId());
            fulfillmentOutboundOrderReturnItems.setStatus(item.getStatus()!=null?item.getStatus().getValue():null);
            fulfillmentOutboundOrderReturnItems.setStatusChangedDate(AmzDateUtils.getDate(item.getStatusChangedDate()));
            fulfillmentOutboundOrderReturnItems.setSellerReturnReasonCode(item.getSellerReturnReasonCode());
            fulfillmentOutboundOrderReturnItems.setAmazonReturnReasonCode(item.getAmazonReturnReasonCode());
            fulfillmentOutboundOrderReturnItems.setAmazonShipmentId(item.getAmazonShipmentId());
            fulfillmentOutboundOrderReturnItems.setFulfillmentCenterId(item.getFulfillmentCenterId());
            fulfillmentOutboundOrderReturnItems.setReturnAuthorizationId(item.getReturnAuthorizationId());
            if(fulfillmentOutboundOrderReturnItemsExists){
                fulfillmentOutboundOrderReturnItemMapper.update(fulfillmentOutboundOrderReturnItems,query);
            }else{
                fulfillmentOutboundOrderReturnItemMapper.insert(fulfillmentOutboundOrderReturnItems);
            }
        }
    }

    private void setFulfillmentOutboundOrderShipment(String sellerFulfillmentOrderId, FulfillmentShipmentList fulfillmentShipments) {
        for(FulfillmentShipment item:fulfillmentShipments){
            LambdaQueryWrapper<FulfillmentOutboundOrderShipment> query=new LambdaQueryWrapper<FulfillmentOutboundOrderShipment>();
            query.eq(FulfillmentOutboundOrderShipment::getSellerFulfillmentOrderId,sellerFulfillmentOrderId);
            query.eq(FulfillmentOutboundOrderShipment::getAmazonShipmentId,item.getAmazonShipmentId());
            FulfillmentOutboundOrderShipment fulfillmentOutboundOrderShipment =fulfillmentOutboundOrderShipmentMapper.selectOne(query);
            boolean fulfillmentOutboundOrderExists = true;
            if(fulfillmentOutboundOrderShipment==null){
                fulfillmentOutboundOrderShipment=new FulfillmentOutboundOrderShipment();
                fulfillmentOutboundOrderExists=false;
            }
            fulfillmentOutboundOrderShipment.setAmazonShipmentId(item.getAmazonShipmentId());
            fulfillmentOutboundOrderShipment.setFulfillmentCenterId(item.getFulfillmentCenterId());
            fulfillmentOutboundOrderShipment.setSellerFulfillmentOrderId(sellerFulfillmentOrderId);
            fulfillmentOutboundOrderShipment.setShippingNotes(item.getShippingNotes()!=null?item.getShippingNotes().toString():null);
            fulfillmentOutboundOrderShipment.setEstimatedArrivalDate(AmzDateUtils.getDate(item.getEstimatedArrivalDate()));
            fulfillmentOutboundOrderShipment.setFulfillmentShipmentStatus(item.getFulfillmentShipmentStatus()!=null?item.getFulfillmentShipmentStatus().getValue():null);
            fulfillmentOutboundOrderShipment.setShippingDate(AmzDateUtils.getDate(item.getShippingDate()));
            fulfillmentOutboundOrderShipment.setShippingDate(AmzDateUtils.getDate(item.getShippingDate()));
            if(fulfillmentOutboundOrderExists){
                fulfillmentOutboundOrderShipmentMapper.update(fulfillmentOutboundOrderShipment,query);
            }else{
                fulfillmentOutboundOrderShipmentMapper.insert(fulfillmentOutboundOrderShipment);
            }
        }

    }

    @Override
    public void runApi(AmazonAuthority amazonAuthority) {
        FulfillmentOutboundOrder order=this.baseMapper.getOrders(amazonAuthority.getId());
        getFulfillmentOrder(amazonAuthority,order.getSellerFulfillmentOrderId());
    }
}
