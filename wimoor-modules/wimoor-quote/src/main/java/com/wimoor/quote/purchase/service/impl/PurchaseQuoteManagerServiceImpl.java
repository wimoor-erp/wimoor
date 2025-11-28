package com.wimoor.quote.purchase.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wimoor.quote.api.dto.QuoteDTO;
import com.wimoor.quote.purchase.mapper.PurchaseAlibabaOrderItemMapper;
import com.wimoor.quote.purchase.mapper.PurchaseAlibabaOrderMapper;
import com.wimoor.quote.ship.pojo.entity.UserBuyer;
import com.wimoor.quote.purchase.pojo.dto.PurchaseQuoteDTO;
import com.wimoor.quote.purchase.pojo.entity.*;
import com.wimoor.quote.purchase.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PurchaseQuoteManagerServiceImpl implements IPurchaseQuoteManagerService {
    final IPurchaseAlibabaBuyerService purchaseAlibabaBuyerService;
    final IPurchaseAlibabaOrderService purchaseAlibabaOrderService;
    final IPurchaseAlibabaReceiverService purchaseAlibabaReceiverService;
    final IPurchaseAlibabaOrderItemService purchaseAlibabaOrderItemService;
    final IPurchaseOrderEntryService purchaseOrderEntryService;
    final IPurchaseAlibabaSellerService purchaseAlibabaSellerService;
    final PurchaseAlibabaOrderMapper purchaseAlibabaOrderMapper;
    final PurchaseAlibabaOrderItemMapper purchaseAlibabaOrderItemMapper;

    @Override
    public boolean savePurchase(UserBuyer sysBuyer, PurchaseQuoteDTO dto) {
        PurchaseAlibabaBuyer buyer=dto.getBuyer();
        PurchaseAlibabaBuyer oldBuyer = purchaseAlibabaBuyerService.getById(buyer.getId());
        if(oldBuyer == null){
            purchaseAlibabaBuyerService.save(buyer);
        }else{
            purchaseAlibabaBuyerService.updateById(buyer);
        }
        PurchaseAlibabaSeller seller = dto.getSeller();
        PurchaseAlibabaSeller oldSeller = purchaseAlibabaSellerService.getById(seller.getId());
        if(oldSeller == null){
            purchaseAlibabaSellerService.save(seller);
        }else{
            purchaseAlibabaSellerService.updateById(seller);
        }
        PurchaseAlibabaReceiver recever = dto.getReceiver();
        PurchaseAlibabaReceiver oldRecever = purchaseAlibabaReceiverService.getById(recever.getOrderid());
        if(oldRecever == null){
            purchaseAlibabaReceiverService.save(recever);
        }else{
            purchaseAlibabaReceiverService.updateById(recever);
        }
        List<PurchaseAlibabaOrderItem> list  = dto.getOrderItems();
        for(PurchaseAlibabaOrderItem orderItem : list){
            PurchaseAlibabaOrderItem oldOrderItem = purchaseAlibabaOrderItemService.getById(orderItem.getId());
            if(oldOrderItem == null){
                purchaseAlibabaOrderItemService.save(orderItem);
            }else{
                purchaseAlibabaOrderItemService.updateById(orderItem);
            }
        }
        PurchaseOrderEntry entry = dto.getEntry();
        PurchaseOrderEntry oldEntry = purchaseOrderEntryService.getById(entry.getId());
        if(oldEntry == null){
            purchaseOrderEntryService.save(entry);
        }else{
            purchaseOrderEntryService.updateById(entry);
        }
        PurchaseAlibabaOrder order = dto.getOrder();
        PurchaseAlibabaOrder oldOrder = purchaseAlibabaOrderService.getById(order.getId());
        order.setBuyerid(buyer.getId());
        order.setBuyerothername(dto.getOthername());
        order.setBuyercompany(sysBuyer.getName());
        order.setSysbuyerid(sysBuyer.getId());
        if(oldOrder == null){
            purchaseAlibabaOrderService.save(order);
        }else{
            purchaseAlibabaOrderService.updateById(order);
        }
        return true;
    }

    @Override
    public IPage<Map<String, Object>> listPurchase(UserBuyer buyer, QuoteDTO dto) {
        return purchaseAlibabaOrderMapper.findByCondition(dto.getPage(),dto.getSearch(), buyer.getId(), dto.getFromDate(),dto.getToDate());
    }


    @Override
    public List<PurchaseAlibabaOrderItem>
    getEntryList(String orderid) {
        LambdaQueryWrapper<PurchaseAlibabaOrderItem> query=new LambdaQueryWrapper<PurchaseAlibabaOrderItem>();
        query.eq(PurchaseAlibabaOrderItem::getOrderid,orderid);
        return  purchaseAlibabaOrderItemMapper.selectList(query);
    }
}
