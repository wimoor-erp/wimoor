package com.wimoor.erp.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.erp.order.pojo.entity.OrderInvoice;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;

public interface IOrderInvoiceService extends IService<OrderInvoice> {
    void treatShipmentTemplate(Workbook workbook, Map<String,Object> param, List<Map<String,Object>> list);
}
