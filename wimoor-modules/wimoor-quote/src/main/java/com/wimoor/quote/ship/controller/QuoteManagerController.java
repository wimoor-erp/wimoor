package com.wimoor.quote.ship.controller;


import cn.hutool.core.util.StrUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.quote.api.dto.QuoteDTO;
import com.wimoor.quote.ship.pojo.entity.QuoteOrder;
import com.wimoor.quote.ship.pojo.entity.QuoteOrderShipment;
import com.wimoor.quote.ship.pojo.entity.UserBuyer;
import com.wimoor.quote.ship.service.IQuoteOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(tags = "报价系统接口")
@RestController
@RequestMapping("/api/v1/quote")
@RequiredArgsConstructor
public class QuoteManagerController {

    final IQuoteOrderService quoteOrderService;


    //获取货件 生成订单
    @ApiOperation(value = "保存货件信息")
    @PostMapping("/save")
    public Result<?> saveAction(@RequestBody QuoteDTO dto){
        UserBuyer buyer =quoteOrderService.getBuyerInfo(dto.getToken());
        return Result.judge(quoteOrderService.saveShipment(buyer,dto));
    }

    @ApiOperation(value = "保存货件信息")
    @PostMapping("/addBuyer")
    public Result<String> addBuyerAction(@RequestBody UserBuyer dto){
        return Result.success( quoteOrderService.addBuyer(dto));
    }

    @ApiOperation(value = "保存货件信息")
    @GetMapping("/getBuyer")
    public Result<UserBuyer> addBuyerAction(String token){
        UserBuyer buyer =quoteOrderService.getBuyerInfo(token);
        return Result.success(buyer);
    }

    @ApiOperation(value = "获取订单信息")
    @PostMapping("/saveOrder/{token}")
    public Result<?> saveOrderAction(@PathVariable String token,@RequestBody QuoteOrder dto){
        UserBuyer buyer =quoteOrderService.getBuyerInfo(token);
        dto.setBuyerid(buyer.getId());
        return Result.judge( quoteOrderService.saveOrder(dto));
    }
    @ApiOperation(value = "获取货件信息")
    @PostMapping("/listShipment")
    public Result<?> listShipmentAction(@RequestBody QuoteDTO dto){
        if(StrUtil.isBlank(dto.getToken())){
            throw new BizException("未绑定询价商，无法查询");
        }
        UserBuyer buyer =quoteOrderService.getBuyerInfo(dto.getToken());
        return Result.success( quoteOrderService.listShipment(buyer,dto));
    }

    @ApiOperation(value = "获取货件信息")
    @GetMapping("/updateShipmentRemark")
    public Result<?> updateShipmentRemarkAction(String shipmentid,String remark){
        return Result.success( quoteOrderService.updateShipmentRemark(shipmentid,remark));
    }

    @ApiOperation(value = "获取货件信息")
    @PostMapping("/deleteShipment")
    public Result<?> deleteShipmentAction(@RequestBody List<String> shipmentids){
        return Result.success( quoteOrderService.deleteShipment(shipmentids));
    }

    @ApiOperation(value = "获取货件信息")
    @PostMapping("/updateOrderRemark")
    public Result<?> updateOrderAction(@RequestBody QuoteOrder dto){
        return Result.success( quoteOrderService.updateOrder(dto));
    }

    @ApiOperation(value = "修改订单状态")
    @GetMapping("/updateOrderStatus")
    public Result<?> updateOrderStatusAction(String orderid,String status){
        return Result.success( quoteOrderService.updateOrderStatus(orderid,status));
    }

    @ApiOperation(value = "删除订单货件")
    @GetMapping("/deleteOrderShipment")
    public Result<?> deleteOrderShipmentAction(String orderid,String shipmentid){
        return Result.success( quoteOrderService.deleteOrderShipment(orderid,shipmentid));
    }
    @ApiOperation(value = "添加订单货件")
    @PostMapping("/addOrderShipment")
    public Result<?> addOrderShipmentAction(@RequestBody List<QuoteOrderShipment> list) {
        return Result.success( quoteOrderService.addOrderShipment(list));
    }

    @ApiOperation(value = "获取订单信息")
    @PostMapping("/listOrder")
    public Result<?> listAction(@RequestBody QuoteDTO dto){
        if(StrUtil.isBlank(dto.getToken())){
            throw new BizException("未绑定询价商，无法查询");
        }
        if(dto.getStatus()==0){
            dto.setStatus(null);
        }
        UserBuyer buyer =quoteOrderService.getBuyerInfo(dto.getToken());
        dto.setBuyerid(buyer.getId());
        return Result.success( quoteOrderService.listOrder(buyer,dto));
    }

    @ApiOperation(value = "获取订单对应的报价信息")
    @GetMapping("/listprice")
    public Result<?> addSupplierAction(@RequestParam String orderid ){
        return Result.success( quoteOrderService.listPrice(orderid));
    }

    @ApiOperation(value = "获取订单对应的报价信息")
    @GetMapping("/confirmPrice")
    public Result<?> confirmPriceAction(@RequestParam String priceid ){
        return Result.success( quoteOrderService.confirmPrice(priceid));
    }

    @ApiOperation("订单导出")
    @GetMapping("/downloadOrderExport")
    public  void downloadOrderExportAction(@RequestParam String orderid, HttpServletResponse response){
        try {
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=OrdersDetails.xlsx");// 设置文件名
            quoteOrderService.downloadOrderExport(workbook,orderid);
            ServletOutputStream fOut = response.getOutputStream();
            workbook.write(fOut);
            workbook.close();
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("订单报价详情")
    @GetMapping("/showDetail")
    public  Result<?>  showDetailAction(@RequestParam String orderid, @RequestParam String supplierid){
         return Result.success(quoteOrderService.selectQuoteInfo(orderid,supplierid)) ;
    }

}
