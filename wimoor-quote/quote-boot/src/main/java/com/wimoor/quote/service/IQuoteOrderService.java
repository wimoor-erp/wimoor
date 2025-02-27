package com.wimoor.quote.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.quote.api.dto.QuoteDTO;
import com.wimoor.quote.api.dto.QuotePriceDTO;
import com.wimoor.quote.api.entity.*;

import java.util.List;
import java.util.Map;

public interface IQuoteOrderService  extends IService<QuoteOrder> {

    UserBuyer getBuyerInfo(String token);
    UserSupplier getSupplierInfo(String token);
    Boolean saveShipment(UserBuyer buyer, QuoteDTO dto);
    IPage<Shipment> listShipment(UserBuyer buyer, QuoteDTO dto);
    Boolean addSupplier(UserBuyer buyer, UserSupplier dto);
    List<UserSupplier> listSupplier(String id);
    public List<Map<String,Object>> listPrice(String orderid);
    boolean saveOrder(QuoteOrder dto);
    IPage<QuoteOrder> listOrder(UserBuyer buyer, QuoteDTO dto);
    IPage<QuoteOrder> listSupplierOrder(UserSupplier supplier, QuoteDTO dto);
    boolean sendOrderToSupplier(QuotePriceDTO dto);
    boolean submitPrice(List<QuotationPrice> dto);
    boolean confirmPrice(String priceid);
    String addBuyer(UserBuyer dto);

    Boolean updatesupplier(UserSupplier dto);

    Boolean deleteSupplierPrice(String orderid,String supplierid);

    Boolean deleteSupplier(String id);

    Boolean updateShipmentRemark(String shipmentid, String remark);

    Boolean updateOrderRemark(String orderid, String remark);

    Boolean deleteShipment(List<String> shipmentIds);

    Boolean updateOrderSupplierBase(UserSupplier supplier, String orderId, String base);

    Boolean updateOrderStatus(String orderid, String status);
}
