package com.wimoor.amazon.transparency.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.AmazonGroup;
import com.wimoor.amazon.orders.pojo.dto.AmazonOrdersShipDTO;
import com.wimoor.amazon.transparency.pojo.dto.TransparencyDTO;
import com.wimoor.amazon.transparency.pojo.entity.TransparencyTask;
import com.wimoor.amazon.transparency.pojo.entity.TransparencyTcode;
import com.wimoor.amazon.transparency.service.ITransparencyTaskService;
import com.wimoor.amazon.transparency.service.ITransparencyTcodeService;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单抓取 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Api(tags = "transparencySkuinfo接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/transparency/tcode")
public class TransparencyTCodeController {
    final ITransparencyTcodeService transparencyTcodeService;

    @ApiOperation(value = "保存")
    @PostMapping("/save")
    @Transactional
    public Result<?> saveAction(@RequestBody List<TransparencyTcode> dto) {
        UserInfo userinfo = UserInfoContext.get();
        for(TransparencyTcode tcode : dto){
            transparencyTcodeService.saveTCode(userinfo,tcode);
        }
        return Result.success();
    }

    @ApiOperation(value = "保存任务")
    @PostMapping("/usedByTask")
    @Transactional
    public Result<?> usedByTaskAction(@RequestBody TransparencyTcode dto) {
        UserInfo userinfo = UserInfoContext.get();
        List<TransparencyTcode> list = transparencyTcodeService.lambdaQuery().eq(TransparencyTcode::getTaskid, dto.getTaskid()).list();
        for(TransparencyTcode tcode : list){
            tcode.setUsetime(new Date());
            transparencyTcodeService.saveTCode(userinfo,tcode);
        }
        return Result.success();
    }

    @ApiOperation(value = "保存任务")
    @PostMapping("/unUsedByTask")
    @Transactional
    public Result<?> unUsedByTaskAction(@RequestBody TransparencyTcode dto) {
        UserInfo userinfo = UserInfoContext.get();
        List<TransparencyTcode> list = transparencyTcodeService.lambdaQuery().eq(TransparencyTcode::getTaskid, dto.getTaskid()).list();
        for(TransparencyTcode tcode : list){
            tcode.setUsetime(null);
            transparencyTcodeService.saveTCode(userinfo,tcode);
        }
        return Result.success();
    }

    @ApiOperation("下载订单发货跟踪列表")
    @PostMapping("/downloadList")
    public void downloadListAction(
            @RequestBody TransparencyDTO dto, HttpServletResponse response)  throws BizException {
        // 创建新的Excel工作薄
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        // 将数据写入Excel
        UserInfo userinfo = UserInfoContext.get();
        try {
            transparencyTcodeService.setExcelBook(workbook, dto,userinfo);
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=orderAddress" + System.currentTimeMillis() + ".xlsx");// 设置文件名
            ServletOutputStream fOut = response.getOutputStream();
            workbook.write(fOut);
            workbook.close();
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("下载订单发货跟踪列表")
    @PostMapping("/downloadPdfList")
    public void downloadPdfListAction(@RequestBody TransparencyDTO dto, HttpServletResponse response)  throws BizException {
        // 创建新的Excel工作薄
        // 将毫米换算成点 (Point)
        float widthMM = 28.6f; // 宽 28.6mm
        float heightMM = 28.6f; // 高 28.6mm
         if(dto.getFtype()!=null){
             if(dto.getFtype().equals("H1")){
                 widthMM = 28.6f;
                 heightMM = 28.6f;
             }
             if(dto.getFtype().equals("H2")){
                 widthMM = 44.5f;
                 heightMM = 19f;
             }
             if(dto.getFtype().equals("H3")){
                 widthMM = 35f; // 宽 100mm
                 heightMM = 35f; // 高 150mm
             }
             if(dto.getFtype().equals("H4")){
                 widthMM = 99.5f; // 宽 100mm
                 heightMM = 35f; // 高 150mm
             }

         }
        float widthPt = widthMM * 72 / 25.4f; // 换算成点
        float heightPt = heightMM * 72 / 25.4f; // 换算成点
        // 创建自定义大小的 Rectangle 对象
        Rectangle customSize = new Rectangle(widthPt, heightPt);
        Document document = new Document(customSize);
        // 将数据写入Excel
        UserInfo userinfo = UserInfoContext.get();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            transparencyTcodeService.setPdfBook(document,writer, dto,userinfo);
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=tcode" + System.currentTimeMillis() + ".pdf");// 设置文件名
            ServletOutputStream fOut = response.getOutputStream();
            document.close();
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "报表分类")
    @PostMapping("/list")
    public Result<?> listAction(@RequestBody TransparencyDTO dto) {
        UserInfo userinfo = UserInfoContext.get();
        dto.setAuthid(StrUtil.isBlankOrUndefined(dto.getAuthid())?null:dto.getAuthid());
        dto.setSku(StrUtil.isBlankOrUndefined(dto.getSku())?null:dto.getSku());
        dto.setGroupid(StrUtil.isBlankOrUndefined(dto.getGroupid())?null:dto.getGroupid());
        dto.setTcodelist(dto.getTcodelist()!=null&& !dto.getTcodelist().isEmpty() ?dto.getTcodelist():null);
        dto.setTaskid(StrUtil.isNullOrUndefined(dto.getTaskid())?null:dto.getTaskid());
        IPage<Map<String,Object>> result= transparencyTcodeService.listTCode(userinfo,dto);
        return Result.success(result);
    }
}

