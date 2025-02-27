package com.wimoor.erp.order.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.pojo.entity.BasePageQuery;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.ISerialNumService;
import com.wimoor.common.service.impl.PictureServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.material.pojo.dto.MaterialDTO;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IMaterialService;
import com.wimoor.erp.order.pojo.entity.Order;
import com.wimoor.erp.order.pojo.entity.OrderPlan;
import com.wimoor.erp.order.pojo.entity.OrderPlatform;
import com.wimoor.erp.order.service.IOrderInvoiceService;
import com.wimoor.erp.order.service.IOrderPlanService;
import com.wimoor.erp.order.service.IOrderPlatformService;
import com.wimoor.erp.order.service.IOrderService;
import com.wimoor.erp.stock.pojo.entity.InWarehouseForm;
import com.wimoor.erp.stock.pojo.entity.OutWarehouseForm;
import com.wimoor.erp.stock.service.IInWarehouseFormService;
import com.wimoor.erp.stock.service.IOutWarehouseFormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.wimoor.erp.order.pojo.entity.OrderInvoice;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "订单发票接口")
@RestController
@RequestMapping("/api/v1/order/invoice")
@RequiredArgsConstructor
public class OrderInvoiceController {
    private final IOrderInvoiceService orderInvoiceService;
    private final IPictureService pictureService;
    private final FileUpload fileUpload;
    @GetMapping("/getInvoice")
    public Result<OrderInvoice> getInvoiceAction(String country)   {
        UserInfo userinfo = UserInfoContext.get();
        return Result.success(orderInvoiceService.lambdaQuery().eq(OrderInvoice::getCountry,country).eq(OrderInvoice::getShopid,userinfo.getCompanyid()).one());
    }
    @ApiOperation("编辑发票信息")
    @PostMapping(value="/saveInvoice",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<?> saveInvoiceAction(    @ApiParam("国家")@RequestParam String country,
                                           @ApiParam("公司")@RequestParam String companySimple,
                                           @ApiParam("公司")@RequestParam String company,
                                           @ApiParam("电话")@RequestParam String phone,
                                           @ApiParam("公司logo")@RequestParam String logoUrl,
                                           @ApiParam("图片")@RequestParam String image,
                                           @ApiParam("rate")@RequestParam String rate,
                                           @ApiParam("abn")@RequestParam String abn,
                                           @ApiParam("billto")@RequestParam String billto,
                                           @ApiParam("account")@RequestParam String account,
                                           @ApiParam("城市")@RequestParam String city,
                                           @ApiParam("公司地址")@RequestParam String address,
                                           @ApiParam("bank") String bank ,
                                           @ApiParam("province") String province,
                                           @RequestParam(value="file",required=false)MultipartFile file
    ) throws FileNotFoundException {
        UserInfo userinfo = UserInfoContext.get();
        OrderInvoice dto=new OrderInvoice();
        dto.setCountry(country);
        dto.setCompany(company);
        dto.setPhone(phone);
        dto.setLogoUrl(logoUrl);
        dto.setRate(rate);
        dto.setAbn(abn);
        dto.setBillto(billto);
        dto.setAccount(account);
        dto.setCity(city);
        dto.setAddress(address);
        dto.setImage(image);
        dto.setBank(bank);
        dto.setCompanySimple(companySimple);
        dto.setProvince(province);
        dto.setShopid(userinfo.getCompanyid());
        dto.setOperator(userinfo.getId());
        dto.setCreator(userinfo.getId());
        dto.setOpttime(new Date());
        dto.setCreatetime(new Date());
        Picture picture =null;
        String shopid=userinfo.getCompanyid();
        if(file!=null){
            String filename=file.getOriginalFilename();
            try {
                InputStream stream = file.getInputStream();
                if(StrUtil.isNotEmpty(filename)&&stream!=null) {
                    String filePath = PictureServiceImpl.logoImgPath + shopid;

                    int len = filename.lastIndexOf(".");
                    String filenames = filename.substring(0, len);
                    String imgtype=filename.substring(len, filename.length());
                    filename=filenames+System.currentTimeMillis()+imgtype;
                    picture = pictureService.uploadPicture(stream, null, filePath, filename, dto.getImage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        OrderInvoice old = orderInvoiceService.lambdaQuery()
                                              .eq(OrderInvoice::getCountry,dto.getCountry())
                                              .eq(OrderInvoice::getShopid, userinfo.getCompanyid())
                                              .one();
        if(picture!=null) {
            dto.setImage(picture.getId());
            dto.setLogoUrl(fileUpload.getPictureImage(picture.getLocation()));
        }else{
            if(old!=null&&old.getImage()!=null){
                dto.setImage(old.getImage());
                picture=pictureService.getById(old.getImage());
                dto.setLogoUrl(fileUpload.getPictureImage(picture.getLocation()));
            }
        }
        if(old!=null){
            dto.setId(old.getId());
            return Result.success(orderInvoiceService.updateById(dto));
        }else{
             return Result.success(orderInvoiceService.save(dto))  ;
        }
    }

    @PostMapping("/downInvoice")
    public void getInvoiceAction(@RequestBody Map<String,Object> param, HttpServletResponse response)   {
        UserInfo userinfo = UserInfoContext.get();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Workbook workbook = null;
        try {
            ServletOutputStream fOut = response.getOutputStream();
            ClassPathResource cpr = new ClassPathResource("template/Invoice.xlsx");
            if(cpr!=null&&cpr.exists()) {
                // 创建新的Excel工作薄
                InputStream is =cpr.getInputStream();
                if(is!=null) {
                    workbook = WorkbookFactory.create(is);
                }
            }
            CellStyle style=workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.LEFT);
            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
            font.setFontHeightInPoints((short)10);
            style.setFont(font);
            style.setWrapText(true);
            List<Map<String,Object>> list= (List<Map<String, Object>>) param.get("列表");
            orderInvoiceService.treatShipmentTemplate(workbook,param,list);
            workbook.write(fOut);
            workbook.close();
        } catch (InvalidFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
