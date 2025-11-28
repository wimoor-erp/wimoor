package com.wimoor.erp.stock.controller;


import cn.hutool.core.util.StrUtil;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.stock.service.IErpDispatchOverseaFormEntryService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.map.LinkedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2023-02-24
 */
@RestController
@RequestMapping("/api/v1/inventory/dispatch/overseaitem")
public class ErpDispatchOverseaFormEntryController {
    @Resource
    IErpDispatchOverseaFormEntryService iErpDispatchOverseaFormEntryService;

    @Transactional
    @GetMapping("/getPrintLabel")
    public Result<?> getPrintLabel(String id){
        UserInfo user = UserInfoContext.get();
        List<Map<String, Object>> list = iErpDispatchOverseaFormEntryService.getPrintLabel(user, id);
        return  Result.success(list);
    }

    @ApiOperation(value = "下载产品标签-Excel")
    @PostMapping("/downExcelLabel")
    public void downExcelLabelAction(@RequestBody List<Map<String,Object>> skulist,  HttpServletResponse response) {
        UserInfo user = UserInfoContext.get();
        try {
            // 创建新的Excel工作薄
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition", "attachment;fileName=labeltemplate.xlsx");// 设置文件名
            ServletOutputStream fOut = response.getOutputStream();
            Sheet sheet = workbook.createSheet("sheet1");
            Map<String, Integer> titlemap = new LinkedMap<String, Integer>();
            titlemap.put("No.", 0);
            titlemap.put("sku", 1);
            titlemap.put("ename", 2);
            titlemap.put("quantity", 3);
            titlemap.put("remark", 4);
            // 在索引0的位置创建行（最顶端的行）
            Row row = sheet.createRow(0);
            for (String key : titlemap.keySet()) {
                Cell cell = row.createCell(titlemap.get(key)); // 在索引0的位置创建单元格(左上端)
                if (!"No.".equals(key)) {
                    cell.setCellValue(key.toUpperCase());
                } else {
                    cell.setCellValue(key);
                }
            }
            for (int i = 0; i < skulist.size(); i++) {
                Row skurow = sheet.createRow(i + 1);
                Map<String, Object> skumap = skulist.get(i);
                Cell cell = skurow.createCell(titlemap.get("No.")); // 在索引0的位置创建单元格(左上端)
                cell.setCellValue(i + 1);
                for (String key : skumap.keySet()) {
                    if(skumap.get(key)!=null&&titlemap.get(key)!=null){
                        cell = skurow.createCell(titlemap.get(key));
                        cell.setCellValue(skumap.get(key).toString());
                    }
                }
            }
            iErpDispatchOverseaFormEntryService.saveEname(user,skulist);
            workbook.write(fOut);
            workbook.close();
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ;
    }

    @SuppressWarnings("deprecation")
    public static String formatterName(String title) {
        char[] array = title.toCharArray();
        String path = new ClassPathResource("font/simsun.ttc").getPath();
        Font f = new Font(path, Font.PLAIN, 12);
        FontMetrics fm =Toolkit.getDefaultToolkit().getFontMetrics (f) ;
        String pre = "";
        int begin = 0;
        for (; begin < array.length; begin++) {
            pre = pre + array[begin];
            if (fm.stringWidth(pre) >= 114)
                break;
        }
        String rear = "";
        for (int end = array.length - 1; end > begin; end--) {
            rear = array[end] + rear;
            if (fm.stringWidth(rear) >= 114)
                break;
        }
        return pre + "..." + rear;
    }
}

