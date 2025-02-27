package com.wimoor.erp.order.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.pojo.entity.BasePageQuery;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.material.service.impl.MaterialServiceImpl;
import com.wimoor.erp.order.pojo.entity.Order;
import com.wimoor.erp.order.pojo.entity.OrderPlan;
import com.wimoor.erp.order.pojo.entity.OrderPlatform;
import com.wimoor.erp.order.service.IOrderPlanService;
import com.wimoor.erp.order.service.IOrderPlatformService;
import com.wimoor.erp.order.service.IOrderService;
import com.wimoor.erp.stock.pojo.entity.InWarehouseForm;
import com.wimoor.erp.stock.pojo.entity.OutWarehouseForm;
import com.wimoor.erp.stock.service.IInWarehouseFormService;
import com.wimoor.erp.stock.service.IOutWarehouseFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "订单接口")
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    private final IOrderPlatformService orderPlatformService;
    private final IOrderPlanService orderPlanService;
    private final ISerialNumService iSerialNumService;
    private final IOutWarehouseFormService iOutWarehouseFormService;
    private final IMaterialService iMaterialsService;
    private final IOutWarehouseFormService outWarehouseFormService;
    private final IInWarehouseFormService iInWarehouseFormService;
    @GetMapping("/listPlatform")
    public Result<List<OrderPlatform>> getListPlatformListAction()   {
        UserInfo userinfo = UserInfoContext.get();
        return Result.success(orderPlatformService.lambdaQuery()
                                                  .eq(OrderPlatform::getShopid,userinfo.getCompanyid())
                                                  .eq(OrderPlatform::getDisabled, false)
                                                  .list());
    }
    @GetMapping("/countrys")
    public Result<List<Map<String,Object>>> countrysAction()   {
        UserInfo userinfo = UserInfoContext.get();
        return Result.success(iMaterialsService.getCountrys());
    }

    @GetMapping("/getPlatform")
    public Result<OrderPlatform> getPlatformAction(String id)   {
        return Result.success(orderPlatformService.getById(id));
    }
    @GetMapping("/removePlatform")
    public Result<?> removePlatformAction(String id)   {
        OrderPlatform old = orderPlatformService.getById(id);
        old.setDisabled(true);
        return Result.success(orderPlatformService.updateById(old));
    }

    @PostMapping("/savePlatform")
    public Result<OrderPlatform> savePlatformAction(@RequestBody OrderPlatform dto)   {
        UserInfo userinfo = UserInfoContext.get();
        if(dto.idIsNULL()){
            dto.setShopid(userinfo.getCompanyid());
            dto.setOperator(userinfo.getId());
            dto.setOpttime(new Date());
            dto.setDisabled(false);
            OrderPlatform old = orderPlatformService.lambdaQuery().eq(OrderPlatform::getShopid, userinfo.getCompanyid()).eq(OrderPlatform::getName, dto.getName()).one();
            if(old!=null){
                dto.setId(old.getId());
                orderPlatformService.updateById(dto);
            }else{
                orderPlatformService.save(dto);
            }
        }else{
            dto.setDisabled(false);
            orderPlatformService.updateById(dto);
        }
        return Result.success(dto);
    }

    @PostMapping("/save")
    public Result<Order> saveAction(@RequestBody Order dto)   {
        UserInfo userinfo = UserInfoContext.get();
        if(dto.idIsNULL()){
            dto.setShopid(userinfo.getCompanyid());
            dto.setOperator(userinfo.getId());
            dto.setOpttime(new Date());
            dto.setIsout(false);
            Order old = orderService.lambdaQuery()
                                    .eq(Order::getShopid, userinfo.getCompanyid())
                                    .eq(Order::getSku, dto.getSku())
                                    .eq(Order::getOrderId, dto.getOrderId())
                                    .eq(Order::getPlatformId, dto.getPlatformId())
                                    .one();
            if(old!=null){
                dto.setId(old.getId());
                orderService.updateById(dto);
            }else{
                OutWarehouseForm param=new OutWarehouseForm();
                param.setWarehouseid(dto.getWarehouseid());
                param.setShopid(dto.getShopid());
                param.setAuditor(userinfo.getId());
                param.setAudittime(new Date());
                param.setAuditstatus(1);
                param.setFtype(3);
                param.setOperator(userinfo.getId());
                param.setOpttime(new Date());
                param.setRemark("多平台订单创建："+dto.getOrderId());
                try {
                    param.setNumber(iSerialNumService.readSerialNumber(dto.getShopid(), "O"));
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        param.setNumber(iSerialNumService.readSerialNumber(dto.getShopid(), "O"));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        throw new BizException("编码获取失败,请联系管理员");
                    }
                }
                HashMap<String, Object> skuMap = new HashMap<String, Object>();
                Material material = iMaterialsService.getBySku(dto.getShopid(), dto.getSku());
                skuMap.put(material.getId(), dto.getQuantity());
                iOutWarehouseFormService.saveForm(param, skuMap, userinfo);
                orderService.save(dto);
            }
        }else{
            orderService.updateById(dto);
        }
        return Result.success(dto);
    }

    @GetMapping("/list")
    public Result<List<Order>> getListAction()   {
        UserInfo userinfo = UserInfoContext.get();
        return Result.success(orderService.lambdaQuery()
                .eq(Order::getShopid,userinfo.getCompanyid())
                .list());
    }

    @GetMapping("/get")
    public Result<Order> getAction(String id)   {
        return Result.success(orderService.getById(id));
    }

    @GetMapping("/remove")
    public Result<?> removeAction(String id)   {
        Order order = orderService.getById(id);
        UserInfo user = UserInfoContext.get();
        OutWarehouseForm outWarehouseForm = new OutWarehouseForm();
        outWarehouseForm.setShopid(user.getCompanyid());
        Material material = iMaterialsService.getBySku(user.getCompanyid(), order.getSku());
        Map<String,Object> map = new HashMap<String, Object>();
        map.put(material.getId(), order.getQuantity());
        String skumapstr= JSONUtil.toJsonStr(map);
        if (StrUtil.isEmpty(skumapstr)) {
            throw new BizException("出库商品不能为空！");
        }
        InWarehouseForm inWarehouseForm = new InWarehouseForm();
        String shopid = user.getCompanyid();
        inWarehouseForm.setShopid(shopid);
            try {
                inWarehouseForm.setNumber(iSerialNumService.readSerialNumber(shopid, "IN"));
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    inWarehouseForm.setNumber(iSerialNumService.readSerialNumber(shopid, "IN"));
                } catch (Exception e1) {
                    e1.printStackTrace();
                    throw new BizException("编码获取失败,请联系管理员");
                }
            }
        inWarehouseForm.setCreator(user.getId());
        inWarehouseForm.setCreatedate(new Date());
        inWarehouseForm.setAuditor(user.getId());
        inWarehouseForm.setAuditstatus(2);// 0：未提交，1：提交未审核，2：已审核
        inWarehouseForm.setOperator(user.getId());
        inWarehouseForm.setOpttime(new Date());
        inWarehouseForm.setWarehouseid(order.getWarehouseid());
        inWarehouseForm.setRemark("多平台订单删除恢复库存："+order.getOrderId());
        inWarehouseForm.setAudittime(new Date());
        iInWarehouseFormService.saveAction(inWarehouseForm, skumapstr, user);
        return Result.success(orderService.removeById(id));
    }

    @PostMapping("/findByCondition")
    public Result<?> findByConditionAction(@RequestBody BasePageQuery dto)   {
        UserInfo userinfo = UserInfoContext.get();
        Map<String, String> param = dto.getParamother();
        if(param.get("sku")!=null && StrUtil.isNotBlank(param.get("sku").toString())){
            param.put("sku","%"+param.get("sku").toString().trim()+"%");
        }else{
            param.put("sku",null);
        }
        param.put("shopid",userinfo.getCompanyid());
        return Result.success(orderService.findByCondition(dto));
    }
    @PostMapping("/findOrderByCondition")
    public Result<?> findOrderByConditionAction(@RequestBody BasePageQuery dto)   {
        UserInfo userinfo = UserInfoContext.get();
        Map<String, String> param = dto.getParamother();
        if(param.get("sku")!=null && StrUtil.isNotBlank(param.get("sku").toString())){
            param.put("sku","%"+param.get("sku").toString().trim()+"%");
        }else{
            param.put("sku",null);
        }
        param.put("shopid",userinfo.getCompanyid());
        return Result.success(orderService.findOrderByCondition(dto));
    }
    @ApiOperation(value = "下载多平台订单文件")
    @PostMapping("/downloadOrderByCondition")
    public void downloadOrderByConditionAction(@RequestBody BasePageQuery dto, HttpServletResponse response) {
        try {
            // 创建新的Excel工作薄
            UserInfo user = UserInfoContext.get();
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=temp");// 设置文件名
            ServletOutputStream fOut = response.getOutputStream();
            fOut = response.getOutputStream();
            //插入记录条
            UserInfo userinfo = UserInfoContext.get();
            Map<String, String> param = dto.getParamother();
            if(param.get("sku")!=null && StrUtil.isNotBlank(param.get("sku").toString())){
                param.put("sku","%"+param.get("sku").toString().trim()+"%");
            }else{
                param.put("sku",null);
            }
            param.put("shopid",userinfo.getCompanyid());
            List<Map<String, Object>> list = orderService.listOrderByCondition(dto);
            Workbook workbook = new SXSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet1");
            Row row = sheet.createRow(0);
            Cell cell=row.createCell(0);
            cell.setCellValue("平台");
            cell = row.createCell(1);
            cell.setCellValue("仓库");
            cell = row.createCell(2);
            cell.setCellValue("SKU");
            cell = row.createCell(3);
            cell.setCellValue("名称");
            cell = row.createCell(4);
            cell.setCellValue("订单");
            cell = row.createCell(5);
            cell.setCellValue("购买日期");
            cell = row.createCell(6);
            cell.setCellValue("销量");
            cell = row.createCell(7);
            cell.setCellValue("销售额");
            cell=row.createCell(8);
            cell.setCellValue("邮费");
            cell=row.createCell(9);
            cell.setCellValue("平台佣金");
            cell=row.createCell(10);
            cell.setCellValue("平台佣金百分比");
            cell=row.createCell(11);
            cell.setCellValue("利润");
            for(int i=0;list!=null&&i<list.size();i++) {
                Map<String, Object> item = list.get(i);
                row = sheet.createRow(i + 1);
                cell = row.createCell(0); // 在索引0的位置创建单元格(左上端) {100-9.9},{500-9.5}
                cell.setCellValue(GeneralUtil.getValue(item.get("platformname")));
                cell = row.createCell(1);
                cell.setCellValue(GeneralUtil.getValue(item.get("warehousename")));
                cell = row.createCell(2);
                cell.setCellValue(GeneralUtil.getValue(item.get("sku")));
                cell = row.createCell(3);
                cell.setCellValue(GeneralUtil.getValue(item.get("name")));
                cell = row.createCell(4);
                cell.setCellValue(GeneralUtil.getValue(item.get("order_id")));
                cell = row.createCell(5);
                cell.setCellValue(GeneralUtil.getValue(item.get("quantity")));
                cell = row.createCell(6);
                cell.setCellValue(GeneralUtil.getValue(item.get("price")));
                cell = row.createCell(7);
                cell.setCellValue(GeneralUtil.getValue(item.get("ship_fee")));
                cell = row.createCell(8);
                cell.setCellValue(GeneralUtil.getValue(item.get("referral_fee")));
                cell = row.createCell(9);
                cell.setCellValue(GeneralUtil.getValue(item.get("referral_rate")));
                cell = row.createCell(10);
                cell.setCellValue(GeneralUtil.getValue(item.get("profit")));
            }
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @PostMapping("/findMaterialByCondition")
    public Result<?> findMaterialByConditionAction(@RequestBody BasePageQuery dto) {
        UserInfo user = UserInfoContext.get();
        Map<String, String> param = dto.getParamother();
        if(StrUtil.isBlank(param.get("sku"))) {
            param.put("sku",null);
        }
        if(StrUtil.isBlank(param.get("platformid"))) {
            param.put("platformid",null);
        }
        if(StrUtil.isBlank(param.get("warehouseid"))) {
            param.put("warehouseid",null);
        }
        if(StrUtil.isBlank(param.get("ischeck"))) {
            param.put("ischeck",null);
        }
        if(StrUtil.isBlank(param.get("categoryid"))) {
            param.put("categoryid",null);
        }
        param.put("shopid",user.getCompanyid());
        return Result.success(orderService.findMaterialByCondition(dto.getPage(),param));
    }
    @ApiOperation(value = "下载feedfile文件")
    @PostMapping("/downloadMaterialByCondition")
    public void downloadOrderPlanFormAction(@RequestBody BasePageQuery dto, HttpServletResponse response) {
        try {
            // 创建新的Excel工作薄
            UserInfo user = UserInfoContext.get();
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=temp");// 设置文件名
            ServletOutputStream fOut = response.getOutputStream();
            fOut = response.getOutputStream();
            //插入记录条
            Map<String, String> param = dto.getParamother();
            if(StrUtil.isBlank(param.get("sku"))) {
                param.put("sku",null);
            }
            if(StrUtil.isBlank(param.get("platformid"))) {
                param.put("platformid",null);
            }
            if(StrUtil.isBlank(param.get("warehouseid"))) {
                param.put("warehouseid",null);
            }
            if(StrUtil.isBlank(param.get("ischeck"))) {
                param.put("ischeck",null);
            }
            if(StrUtil.isBlank(param.get("categoryid"))) {
                param.put("categoryid",null);
            }
            param.put("shopid",user.getCompanyid());
            List<Map<String, Object>> list = orderService.findMaterialBySelect(param);
            Workbook workbook = new SXSSFWorkbook();
            Sheet sheet = workbook.createSheet("Sheet1");
               Row row = sheet.createRow(0);
               Cell cell=row.createCell(0);
                cell.setCellValue("SKU");
                cell=row.createCell(1);
                cell.setCellValue("名称");
                cell=row.createCell(2);
                cell.setCellValue("品类");
                cell=row.createCell(3);
                cell.setCellValue("库存");
                cell=row.createCell(4);
                cell.setCellValue("待出库");
                cell=row.createCell(5);
                cell.setCellValue("当月库存周期");
                cell=row.createCell(6);
                cell.setCellValue("采购成本");
                cell=row.createCell(7);
                cell.setCellValue("箱规");
                cell=row.createCell(8);
                cell.setCellValue("单箱尺寸");
                cell=row.createCell(9);
                cell.setCellValue("箱数");
                cell=row.createCell(10);
                cell.setCellValue("单箱体积cm³");
                for(int i=0;list!=null&&i<list.size();i++) {
                    Map<String, Object> item = list.get(i);
                    row = sheet.createRow(i + 1);
                    cell = row.createCell(0); // 在索引0的位置创建单元格(左上端) {100-9.9},{500-9.5}
                    cell.setCellValue(GeneralUtil.getValue(item.get("sku")));
                    cell = row.createCell(1);
                    cell.setCellValue(GeneralUtil.getValue(item.get("name")));
                    cell = row.createCell(2);
                    cell.setCellValue(GeneralUtil.getValue(item.get("category")));
                    cell = row.createCell(3);
                    cell.setCellValue(GeneralUtil.getValue(item.get("qty")));
                    cell = row.createCell(4);
                    cell.setCellValue(GeneralUtil.getValue(item.get("outbound")));
                    cell = row.createCell(5);
                    cell.setCellValue(GeneralUtil.getValue(item.get("quantity")));
                    cell = row.createCell(6);
                    cell.setCellValue(GeneralUtil.getValue(item.get("price")));
                    cell = row.createCell(7);
                    cell.setCellValue(GeneralUtil.getValue(item.get("boxnum")));
                    cell = row.createCell(8);
                    cell.setCellValue(GeneralUtil.getValue(item.get("plength")) + "*" + GeneralUtil.getValue(item.get("pwidth")) + "*" + GeneralUtil.getValue(item.get("pheight")));
                    cell = row.createCell(9);
                    cell.setCellValue(GeneralUtil.getValue(item.get("boxqty")));
                    cell = row.createCell(10);
                    cell.setCellValue(GeneralUtil.getValue(item.get("boxsize")));

                }
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @PostMapping("/findMaterialBySelect")
    public Result<?> findMaterialByPlanAction(@RequestBody Map<String, String> param ) {
        UserInfo user = UserInfoContext.get();
        if(StrUtil.isBlank(param.get("sku"))) {
            param.put("sku",null);
        }
        if(StrUtil.isBlank(param.get("platformid"))) {
            param.put("platformid",null);
        }
        if(StrUtil.isBlank(param.get("warehouseid"))) {
            param.put("warehouseid",null);
        }
        if(StrUtil.isBlank(param.get("ischeck"))) {
            param.put("ischeck",null);
        }
        param.put("shopid",user.getCompanyid());
        return Result.success(orderService.findMaterialBySelect(param));
    }

    @GetMapping("/set")
    public Result<?> setAction(String materialId,String operate)   {
                        UserInfo user = UserInfoContext.get();
                        OrderPlan plan=new OrderPlan();
                        plan.setShopid(user.getCompanyid());
                        plan.setMaterialid(materialId);
                        if(operate.equals("add")){
                            return Result.success(orderPlanService.save(plan));
                        }else{
                            return Result.success(orderPlanService.lambdaUpdate().eq(OrderPlan::getMaterialid, materialId).eq(OrderPlan::getShopid, user.getCompanyid()).remove());
                        }
              }
    @GetMapping("/clear")
    public Result<?> clearAction()   {
        UserInfo user = UserInfoContext.get();
        return Result.success(orderPlanService.lambdaUpdate().eq(OrderPlan::getShopid, user.getCompanyid()).remove());
    }


    @GetMapping("/downExcelTemp")
    public void downExcelTempAction(HttpServletResponse response) {
        try {
            // 创建新的Excel工作薄
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=stock-change-template.xlsx");// 设置文件名
            ServletOutputStream fOut = response.getOutputStream();
            // 将数据写入Excel
            Sheet sheet = workbook.createSheet("模板格式");
            Row row = sheet.createRow(0);
            Cell cell = row.createCell(0);
            cell.setCellValue("平台");
            cell = row.createCell(1);
            cell.setCellValue("仓库");
            cell = row.createCell(2);
            cell.setCellValue("SKU");
            cell = row.createCell(3);
            cell.setCellValue("订单");
            cell = row.createCell(4);
            cell.setCellValue("购买日期");
            cell = row.createCell(5);
            cell.setCellValue("销量");
            cell = row.createCell(6);
            cell.setCellValue("销售额");
            cell = row.createCell(7);
            cell.setCellValue("邮费");
            cell = row.createCell(8);
            cell.setCellValue("平台佣金");
            cell = row.createCell(9);
            cell.setCellValue("平台佣金百分比");
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("文件读写错误");
        } catch (EncryptedDocumentException e) {
            e.printStackTrace();
            throw new BizException("文件解码错误");
        }
    }

    @PostMapping(value = "/uploadExcel",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> uploadExcelAction(@RequestParam("file") MultipartFile file) {
        UserInfo user = UserInfoContext.get();
        if (file != null) {
            try {
                InputStream inputStream = file.getInputStream();
                Workbook workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                String result = orderService.uploadOrder(sheet, user);
                workbook.close();
                return Result.success(result);
            } catch (IOException e) {
                e.printStackTrace();
                throw new BizException("文件读写错误");
            } catch (EncryptedDocumentException e) {
                e.printStackTrace();
                throw new BizException("文件解码错误");
            } catch (InvalidFormatException e) {
                e.printStackTrace();
                throw new BizException("文件解析错误");
            }
        }
        return Result.failed();
    }
}
