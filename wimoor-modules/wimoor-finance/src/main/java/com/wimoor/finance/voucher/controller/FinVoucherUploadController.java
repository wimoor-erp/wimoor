package com.wimoor.finance.voucher.controller;

import com.wimoor.common.GeneralUtil;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.finance.api.AmazonClientOneFeignManager;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.finance.voucher.domain.FinVoucherUpload;
import com.wimoor.finance.voucher.service.IFinVoucherUploadService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * 凭证分录明细，存储凭证的每一笔分录信息Controller
 * 
 * @author wimoor
 * @date 2025-11-28
 */
@RestController
@RequestMapping("/vouchers/upload")
public class FinVoucherUploadController extends BaseController
{
    @Autowired
    private IFinVoucherUploadService finVoucherUploadService;
    @Autowired
    private IFinAccountingSubjectsService finAccountingSubjectsService;
    @Resource
    AmazonClientOneFeignManager amazonClientOneFeignManager;

    @GetMapping(value = "/downExcelTemp")
    public void downExcelTempAction(String ftype, HttpServletRequest request, HttpServletResponse response) {
        Workbook workbook = null;
        ServletOutputStream fOut = null;
        try {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=" + ftype +".xlsx");// 设置文件名
            fOut = response.getOutputStream();
            InputStream is = new ClassPathResource("template/"+ftype +".xlsx").getInputStream();
            // 创建新的Excel工作薄
            workbook = WorkbookFactory.create(is);
            workbook.write(fOut);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(fOut != null) {
                    fOut.flush();
                    fOut.close();
                }
                if(workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping(value = "/uploadFile/{groupid}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result uploadExcelAction(@PathVariable("groupid") String groupid,@RequestParam("file") MultipartFile file) {
        UserInfo user = UserInfoContext.get();
        List<Map<String,Object>> groupidlist = amazonClientOneFeignManager.getAmazonGroup();
        Map<String,Object> groupidmap = new HashMap<String,Object>();
        if(groupidlist!= null && groupidlist.size() > 0){
            for (Map<String,Object> map : groupidlist) {
                if(map.get("id") != null && map.get("id").toString().equals(groupid)){
                    groupidmap = map;
                }
            }
        }
        List<String> voucherDates = new ArrayList<>();
        String companyname = groupidmap.get("company").toString();
        if (file != null) {
            try {
                InputStream inputStream = file.getInputStream();
                Workbook workbook = WorkbookFactory.create(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                if(sheet.getLastRowNum()>0){
                    Row row = sheet.getRow(0);
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        row = sheet.getRow(i);
                        if (row == null) continue;
                        // 新模板列结构：日期、凭证字、凭证号、附件数、分类名称、摘要、科目代码、科目名称、借方金额、贷方金额、
                        // 客户、供应商、职员、项目、部门、存货、自定义辅助核算类型、自定义辅助核算编码、
                        // 自定义辅助核算类型1、自定义辅助核算编码1、数量、单价、原币金额、币别、汇率
                        ExcelUtil<FinVoucherUpload> util=new ExcelUtil<FinVoucherUpload>(FinVoucherUpload.class);
                        Date voucherDate= GeneralUtil.getDate(util.getCellValue(row, 0));// A: 日期
                        String voucherType=String.valueOf(util.getCellValue(row,1));// B: 凭证字
                        String voucherNo=String.valueOf(util.getCellValue(row,2));// C: 凭证号
                        // D: 附件数 - 暂不处理
                        // E: 分类名称 - 暂不处理
                        String summary=String.valueOf(util.getCellValue(row,5));// F: 摘要
                        String subjectCode=String.valueOf(util.getCellValue(row,6));// G: 科目代码
                        //String subjectName=String.valueOf(util.getCellValue(row,7));// H: 科目名称
                        BigDecimal debitAmount=GeneralUtil.getBigDecimal(util.getCellValue(row,8));// I: 借方金额
                        BigDecimal creditAmount=GeneralUtil.getBigDecimal(util.getCellValue(row,9));// J: 贷方金额
                        // K: 客户 - 辅助核算字段
                        // L: 供应商 - 辅助核算字段
                        // M: 职员 - 辅助核算字段
                        // N: 项目 - 辅助核算字段
                        // O: 部门 - 辅助核算字段
                        // P: 存货 - 辅助核算字段
                        // Q-T: 自定义辅助核算 - 暂不处理
                        Integer quantity=GeneralUtil.getInteger(util.getCellValue(row,20));// U: 数量
                        BigDecimal unitPrice=GeneralUtil.getBigDecimal(util.getCellValue(row,21));// V: 单价
                        BigDecimal originalAmount=GeneralUtil.getBigDecimal(util.getCellValue(row,22));// W: 原币金额
                        String currency=String.valueOf(util.getCellValue(row,23));// X: 币别
                        BigDecimal exchangeRate=GeneralUtil.getBigDecimal(util.getCellValue(row,24));// Y: 汇率
                        //通过groupid和subjectCode查询科目名称对象
                        FinAccountingSubjects subjectObj = finAccountingSubjectsService.selectByGroupCode(groupid, subjectCode);
                        // 其他字段
                        FinVoucherUpload finVoucherUpload = new FinVoucherUpload();
                        finVoucherUpload.setRowIndex(i);
                        finVoucherUpload.setShopid(user.getCompanyid());
                        finVoucherUpload.setGroupName(companyname);
                        finVoucherUpload.setVoucherType(voucherType);
                        finVoucherUpload.setVoucherNo(voucherNo);
                        finVoucherUpload.setVoucherDate(Objects.requireNonNullElseGet(voucherDate, Date::new));
                        finVoucherUpload.setSummary(summary);
                        finVoucherUpload.setSubjectCode(subjectCode);
                        if (subjectObj != null) {
                            finVoucherUpload.setSubjectName(subjectObj.getSubjectName());
                        }
                        finVoucherUpload.setCurrency(currency);
                        finVoucherUpload.setExchangeRate(exchangeRate);
                        finVoucherUpload.setDebitAmount(debitAmount);
                        finVoucherUpload.setCreditAmount(creditAmount);
                        finVoucherUpload.setCreatedTime(new Date());
                        finVoucherUpload.setUpdateTime(new Date());
                        finVoucherUpload.setCreateBy(user.getUserName());
                        finVoucherUpload.setUpdateBy(user.getUserName());
                        finVoucherUpload.setPreparerBy(user.getUserName());
                        finVoucherUpload.setOriginalAmount(originalAmount);
                        finVoucherUpload.setQuantity(quantity);
                        finVoucherUpload.setUnitPrice(unitPrice != null ? unitPrice.intValue() : null);
                        finVoucherUpload.setStatus(0);

                        finVoucherUploadService.insertFinVoucherUpload(finVoucherUpload);
                    }
                    finVoucherUploadService.handleVoucherUpload();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return success();
    }
    /**
     * 查询凭证分录明细，存储凭证的每一笔分录信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinVoucherUpload finVoucherUpload)
    {
        startPage();
        UserInfo user = UserInfoContext.get();
        finVoucherUpload.setShopid(user.getCompanyid());
        List<FinVoucherUpload> list = finVoucherUploadService.selectFinVoucherUploadList(finVoucherUpload);
        return getDataTable(list);
    }

    /**
     * 导出凭证分录明细，存储凭证的每一笔分录信息列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinVoucherUpload finVoucherUpload)
    {
        List<FinVoucherUpload> list = finVoucherUploadService.selectFinVoucherUploadList(finVoucherUpload);
        ExcelUtil<FinVoucherUpload> util = new ExcelUtil<FinVoucherUpload>(FinVoucherUpload.class);
        util.exportExcel(response, list, "凭证分录明细，存储凭证的每一笔分录信息数据");
    }


}
