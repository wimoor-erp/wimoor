package com.wimoor.amazon.inboundV2.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.wimoor.amazon.inboundV2.XmlPojo.CrossBorderXmlMessageType;
import com.wimoor.amazon.inboundV2.XmlPojo.CustomsDeclaration;
import com.wimoor.amazon.inboundV2.XmlPojo.InventoryData;
import com.wimoor.amazon.inboundV2.XmlPojo.OrderData;
import com.wimoor.amazon.inboundV2.pojo.dto.ShipCustomXmlDTO;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundCustomsXml;
import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundCustomsXmlItem;
import com.wimoor.amazon.inboundV2.service.ICustomDataService;
import com.wimoor.amazon.inboundV2.service.IShipCrossborderXmlSevice;
import com.wimoor.amazon.inboundV2.service.IShipInboundCustomsXmlItemService;
import com.wimoor.amazon.inboundV2.service.IShipInboundCustomsXmlService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.Date;

@Api(tags = "发货海关XML申报")
@RestController
@RequestMapping("/api/v2/ship/customsxml")
@SystemControllerLog("发货海关XML申报")
@RequiredArgsConstructor
public class ShipInboundCustomsXmlController {
    final IShipCrossborderXmlSevice shipCrossborderXmlSevice;
    final IShipInboundCustomsXmlService shipInboundCustomsXmlService;
    final ICustomDataService customDataService;
    final IShipInboundCustomsXmlItemService shipInboundCustomsXmlItemService;


    @GetMapping("/customsData")
    public Result<?> customsDataAction(@RequestParam String type){
         UserInfo userinfo = UserInfoContext.get();
         return Result.success(customDataService.getByType(type));
    }

    @PostMapping("/listCustomsXml")
    public Result<?> listCustomsXmlAction(@RequestBody ShipCustomXmlDTO dto){
        UserInfo userinfo = UserInfoContext.get();
        dto.setShopid(userinfo.getCompanyid());
        if(StrUtil.isBlankOrUndefined(dto.getMarketplaceid())){
            dto.setMarketplaceid(null);
        }
        return Result.success(shipInboundCustomsXmlService.findList(dto));
    }
    @PostMapping("/summaryCustomsXml")
    public Result<?> summaryCustomsXmlAction(@RequestBody ShipCustomXmlDTO dto){
        UserInfo userinfo = UserInfoContext.get();
        dto.setShopid(userinfo.getCompanyid());
        if(StrUtil.isBlankOrUndefined(dto.getMarketplaceid())){
            dto.setMarketplaceid(null);
        }
        return Result.success(shipInboundCustomsXmlService.summaryList(dto));
    }

    @PostMapping(value = "/downExcelCustomsXml")
    public void downExcelCustomsXmlAction(@RequestBody ShipCustomXmlDTO dto, HttpServletResponse response)  {
        // 创建新的Excel工作薄
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        UserInfo userinfo = UserInfoContext.get();
        dto.setShopid(userinfo.getCompanyid());
        if(StrUtil.isBlankOrUndefined(dto.getMarketplaceid())){
            dto.setMarketplaceid(null);
        }
        shipInboundCustomsXmlService.setExcelBook(workbook, dto);
        try {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=ShipmentReportByLoistics" + System.currentTimeMillis() + ".xlsx");// 设置文件名
            ServletOutputStream fOut = response.getOutputStream();
            workbook.write(fOut);
            workbook.close();
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @PostMapping("/listCustomsXmlItem")
    public Result<?> listCustomsXmlItemAction(@RequestBody ShipCustomXmlDTO dto){
        UserInfo userinfo = UserInfoContext.get();
        dto.setShopid(userinfo.getCompanyid());
        if(StrUtil.isBlankOrUndefined(dto.getMarketplaceid())){
            dto.setMarketplaceid(null);
        }
        return Result.success(shipInboundCustomsXmlItemService.findList(dto));
    }

    @GetMapping("/getCustomsXml")
    public Result<?> getCustomsXmlAction(@RequestParam String guid){
        UserInfo userinfo = UserInfoContext.get();
        try {
            ShipInboundCustomsXml xml = shipInboundCustomsXmlService.getById(guid);
            return Result.success(xml);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/viewXmlData")
    public Result<?> viewXmlDataAction(@RequestBody ShipInboundCustomsXml xml){
        UserInfo userinfo = UserInfoContext.get();
        try {
            if(CrossBorderXmlMessageType.ORDER.getValue().equals(xml.getXmlType())){
                if("init".equals(xml.getOptType())){
                    return Result.success(shipCrossborderXmlSevice.createOrder(userinfo,xml));
                }else{
                    ShipInboundCustomsXml oldxml = shipInboundCustomsXmlService.getById(xml.getGuid());
                    if(oldxml!=null&&xml.getContent()!=null){
                        xml.setOrderData(JSONUtil.toBean(xml.getContent(), OrderData.class));
                        return   Result.success(xml);
                    }else{
                        return Result.success(shipCrossborderXmlSevice.createOrder(userinfo,xml));
                    }
                }
            }
            if(CrossBorderXmlMessageType.INVENTORY.getValue().equals(xml.getXmlType())){
                if("init".equals(xml.getOptType())){
                    return Result.success(shipCrossborderXmlSevice.createInventory(userinfo,xml));
                }else{
                    ShipInboundCustomsXml oldxml = shipInboundCustomsXmlService.getById(xml.getGuid());
                    if(oldxml!=null&&xml.getContent()!=null){
                        xml.setInventoryData(JSONUtil.toBean(xml.getContent(), InventoryData.class));
                        return   Result.success(xml);
                    }else{
                        return Result.success(shipCrossborderXmlSevice.createInventory(userinfo,xml));
                    }
                }
            }
            if(CrossBorderXmlMessageType.DECLARATION.getValue().equals(xml.getXmlType())||CrossBorderXmlMessageType.DECLARATION_DRAFT.getValue().equals(xml.getXmlType())){
                if("init".equals(xml.getOptType())){
                    return Result.success(shipCrossborderXmlSevice.createDeclaration(userinfo,xml));
                }else{
                    ShipInboundCustomsXml oldxml = shipInboundCustomsXmlService.getById(xml.getGuid());
                    if(oldxml!=null&&xml.getContent()!=null){
                        xml.setCustomsDeclaration(JSONUtil.toBean(xml.getContent(), CustomsDeclaration.class));
                        return   Result.success(xml);
                    }else{
                        return Result.success(shipCrossborderXmlSevice.createDeclaration(userinfo,xml));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.failed("XML类型错误");
    }



    @GetMapping("/disabledCustomsXml")
    @Transactional(rollbackFor = Exception.class)
    public Result<?> disabledCustomsXmlAction(@RequestParam String groupid, @RequestParam String ftype, @RequestParam String number){
        UserInfo userinfo = UserInfoContext.get();
        try {
            ShipInboundCustomsXml xml = shipInboundCustomsXmlService.lambdaQuery()
                    .eq(ShipInboundCustomsXml::getGroupid, groupid)
                    .eq(ShipInboundCustomsXml::getXmlType, ftype)
                    .eq(ShipInboundCustomsXml::getNumber, number).one();
            if(xml!=null){
                xml.setDisabled(true);
                shipCrossborderXmlSevice.markShipment(xml);
                shipInboundCustomsXmlItemService.lambdaUpdate().eq(ShipInboundCustomsXmlItem::getGuid,xml.getGuid()).remove();
                shipInboundCustomsXmlService.removeById(xml.getGuid());
            }
            return Result.success(xml);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/generateCustomsXml")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> generateShipmentCustomsXmlAction(@RequestBody ShipInboundCustomsXml xml){
        UserInfo userinfo = UserInfoContext.get();
        try {
            return Result.success(shipCrossborderXmlSevice.downloadShipXmlFile(userinfo,xml));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/generateFileName")
    public Result<String> generateFileNameAction(@RequestBody ShipInboundCustomsXml xml){
        UserInfo userinfo = UserInfoContext.get();
        try {
            return Result.success(shipCrossborderXmlSevice.getFileName(xml));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/updateShipmentCustomsXml")
    public Result<?> getShipmentCustomsXmlAction(@RequestBody ShipInboundCustomsXml xml) {
        UserInfo userinfo = UserInfoContext.get();
        try {
            xml.setOperator(new BigInteger(userinfo.getId()));
            xml.setOpttime(new Date());
            if(CrossBorderXmlMessageType.ORDER.getValue().equals(xml.getXmlType())){
                xml.setContent(JSONUtil.toJsonStr(xml.getOrderData()));
            }else if(CrossBorderXmlMessageType.INVENTORY.getValue().equals(xml.getXmlType())){
                xml.setContent(JSONUtil.toJsonStr(xml.getInventoryData()));
            }else if(CrossBorderXmlMessageType.DECLARATION.getValue().equals(xml.getXmlType())||CrossBorderXmlMessageType.DECLARATION_DRAFT.getValue().equals(xml.getXmlType())){
                xml.setContent(JSONUtil.toJsonStr(xml.getCustomsDeclaration()));
            }
            shipInboundCustomsXmlService.updateById(xml);
        }  catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.success();
    }


    @PostMapping(value = "/downloadCustomsExcel")
    public void downExcelCustomsExcelAction(@RequestBody ShipCustomXmlDTO dto, HttpServletResponse response)  {

        try {
            // 创建新的Excel工作薄
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=boxDetail.xlsx");// 设置文件名
            ServletOutputStream fOut = response.getOutputStream();
            // 将数据写入Excel
            UserInfo user = UserInfoContext.get();
            dto.setShopid(user.getCompanyid());
            if(StrUtil.isBlankOrUndefined(dto.getMarketplaceid())){
                dto.setMarketplaceid(null);
            }
            // 1. 从resources读取模板文件
            InputStream templateStream = ExcelWriter.class
                    .getClassLoader()
                    .getResourceAsStream("template/customsTemplate.xlsx");

            if (templateStream == null) {
                throw new BizException("模板文件未找到：template/customsTemplate.xlsx");
            }
            Workbook workbook = WorkbookFactory.create(templateStream);
            Sheet sheet = workbook.getSheetAt(0);
            // 3. 调用服务方法设置数据
            shipInboundCustomsXmlService.setCustomsExcelBook(sheet, dto);
            workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
            workbook.write(fOut);

            workbook.close();
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/importReturnStatus")
    public Result<?> importReturnStatusAction(@RequestParam("files") MultipartFile[] files) {
        try {
            UserInfo user = UserInfoContext.get();
            return Result.success(shipInboundCustomsXmlService.importReturnStatus(user,files));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
