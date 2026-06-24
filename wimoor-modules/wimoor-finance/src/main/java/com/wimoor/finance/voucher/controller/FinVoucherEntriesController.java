package com.wimoor.finance.voucher.controller;

import com.wimoor.common.GeneralUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.finance.api.AmazonClientOneFeignManager;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.finance.voucher.domain.FinVoucherEntries;
import com.wimoor.finance.voucher.domain.FinVoucherEntriesAuxiliary;
import com.wimoor.finance.voucher.domain.dto.FinVoucherDTO;
import com.wimoor.finance.voucher.service.IFinVoucherEntriesAuxiliaryService;
import com.wimoor.finance.voucher.service.IFinVoucherEntriesService;
import com.wimoor.finance.voucher.service.IFinVouchersService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查凭证Controller
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/entries")
public class FinVoucherEntriesController extends BaseController
{
    @Autowired
    private IFinVoucherEntriesService finVoucherEntriesService;
     @Autowired
    private IFinVouchersService finVoucherService;
    @Resource
    AmazonClientOneFeignManager amazonClientOneFeignManager;
    @Resource
    private IFinAccountingSubjectsService finAccountingSubjectsService;
    @Autowired
    private IFinVoucherEntriesAuxiliaryService finVoucherEntriesAuxiliaryService;
    /**
     * 查询凭证分录明细列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinVoucherEntries finVoucherEntries)
    {
        startPage();
        List<FinVoucherEntries> list = finVoucherEntriesService.selectFinVoucherEntriesList(finVoucherEntries);
        TableDataInfo data = getDataTable(list);
        return data;
    }

    /**
     * 导出凭证分录明细列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response,FinVoucherDTO dto)
    {
        try {
            List<Map<String,Object>> groupidlist = amazonClientOneFeignManager.getAmazonGroup();
            Map<String,Object> groupidmap = new HashMap<String,Object>();
            if(groupidlist!= null && groupidlist.size() > 0){
                for (Map<String,Object> map : groupidlist) {
                    if(map.get("id") != null && map.get("id").toString().equals(dto.getGroupid())){
                        groupidmap = map;
                    }
                }
            }
            String companyname = groupidmap.get("company").toString();
            List<Map<String, Object>> list = finVoucherEntriesService.selectFinVoucherEntriesList(dto);

            // 构建辅助核算列映射：entry_id -> Map<type_name, item_code>
            Map<String, Map<String, String>> auxiliaryColumnMap = buildAuxiliaryColumnMap(list);

            ClassPathResource resource = new ClassPathResource("template/voucher_entries_template.xlsx");
            InputStream inputStream = resource.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            
            int startRow = 1;
            int rowNum = startRow;

            FinAccountingSubjects finAccountingSubjects=new FinAccountingSubjects();
            finAccountingSubjects.setGroupid(dto.getGroupid());
            finAccountingSubjects.setStatus(1);
            List<FinAccountingSubjects> listSubject = finAccountingSubjectsService.selectFinAccountingSubjectsList(finAccountingSubjects);
            // 构建科目名称映射，用于快速查找
            Map<String, FinAccountingSubjects> codeMap = new HashMap<>();
            for (FinAccountingSubjects subject : listSubject) {
                codeMap.put(subject.getSubjectCode(), subject);
            }


            // 辅助核算类型名称 -> 列号映射
            Map<String, Integer> typeColumnMap = new HashMap<>();
            typeColumnMap.put("客户", 10);
            typeColumnMap.put("供应商", 11);
            typeColumnMap.put("职员", 12);
            typeColumnMap.put("项目", 13);
            typeColumnMap.put("部门", 14);
            typeColumnMap.put("存货", 15);

            for (Map<String, Object> entry : list) {
                Row row = sheet.createRow(rowNum++);
                String subjectName=null;
                FinAccountingSubjects subject = finAccountingSubjectsService.selectFinAccountingSubjectsBySubjectId( entry.get("subject_id").toString());
                if(subject!=null){
                    String fullName = finAccountingSubjectsService.buildFullSubjectName(subject, codeMap);
                    subjectName=fullName;
                }
                // 日期  Date类型 转换为字符串格式 YYYY-MM-DD
                createCell(row, 0, entry.get("voucher_date") != null ? GeneralUtil.formatDate(GeneralUtil.getDate(entry.get("voucher_date"))) : "");
                // 凭证字
                createCell(row, 1, entry.get("voucher_type") != null ? entry.get("voucher_type").toString() : "");
                // 凭证号
                createCell(row, 2, entry.get("voucher_no") != null ? entry.get("voucher_no").toString() : "");
                // 附件数
                createCell(row, 3, entry.get("attachment_count") != null ? entry.get("attachment_count").toString() : "");
                // 分录序号
                createCell(row, 4, entry.get("entry_no") != null ? entry.get("entry_no").toString() : "");
                // 摘要
                createCell(row, 5, entry.get("summary") != null ? entry.get("summary").toString() : "");
                // 科目代码
                createCell(row, 6, entry.get("subject_code") != null ? entry.get("subject_code").toString() : "");
                // 科目名称
                createCell(row, 7, subjectName != null ? subjectName: "");
                // 借方金额
                createCell(row, 8, entry.get("debit_amount") != null ? Double.parseDouble(entry.get("debit_amount").toString()) : 0);
                // 贷方金额
                createCell(row, 9, entry.get("credit_amount") != null ? Double.parseDouble(entry.get("credit_amount").toString()) : 0);
                // 辅助核算列填充
                for (int col = 10; col <= 15; col++) {
                    createCell(row, col, "");
                }
                // 根据辅助核算数据填充对应列
                String entryId = entry.get("entry_id") != null ? entry.get("entry_id").toString() : "";
                Map<String, String> auxColumns = auxiliaryColumnMap.get(entryId);
                int customAuxIndex = 0;
                if (auxColumns != null) {
                    for (Map.Entry<String, String> auxEntry : auxColumns.entrySet()) {
                        Integer colIndex = typeColumnMap.get(auxEntry.getKey());
                        if (colIndex != null) {
                            // 固定辅助核算类型，填充到对应列
                            createCell(row, colIndex, auxEntry.getValue());
                        } else {
                            // 非固定辅助核算类型，填充到自定义列（最多2个）
                            if (customAuxIndex == 0) {
                                createCell(row, 16, auxEntry.getKey());   // 自定义辅助核算类别 Q列
                                createCell(row, 17, auxEntry.getValue()); // 自定义辅助核算编码 R列
                            } else if (customAuxIndex == 1) {
                                createCell(row, 18, auxEntry.getKey());   // 自定义辅助核算类别1 S列
                                createCell(row, 19, auxEntry.getValue()); // 自定义辅助核算编码1 T列
                            }
                            customAuxIndex++;
                            if (customAuxIndex >= 2) break;
                        }
                    }
                }
                // 未填充的自定义列设为空
                if (customAuxIndex == 0) {
                    createCell(row, 16, "");
                    createCell(row, 17, "");
                    createCell(row, 18, "");
                    createCell(row, 19, "");
                } else if (customAuxIndex == 1) {
                    createCell(row, 18, "");
                    createCell(row, 19, "");
                }
                // 数量
                createCell(row, 20, entry.get("quantity") != null ? Double.parseDouble(entry.get("quantity").toString()) : 0);
                // 单价
                createCell(row, 21, entry.get("unit_price") != null ? Double.parseDouble(entry.get("unit_price").toString()) : 0);
                // 原币金额
                if(entry.get("currency")==null) {
                    if(entry.get("debit_amount") != null&& Double.parseDouble(entry.get("debit_amount").toString())!=0){
                        createCell(row, 22,  Double.parseDouble(entry.get("debit_amount").toString()) );
                    }
                    else if(entry.get("credit_amount") != null&& Double.parseDouble(entry.get("credit_amount").toString())!=0){
                        createCell(row, 22,  Double.parseDouble(entry.get("credit_amount").toString()) );
                    }

                    // 币种
                    createCell(row, 23,  "RMB");
                    // 汇率
                    createCell(row, 24,1);
                }else {
                    createCell(row, 22, entry.get("original_amount") != null ? Double.parseDouble(entry.get("original_amount").toString()) : 0);
                    // 币种
                    createCell(row, 23, entry.get("currency") != null ? entry.get("currency").toString() : "");
                    // 汇率
                    createCell(row, 24, entry.get("exchange_rate") != null ? Double.parseDouble(entry.get("exchange_rate").toString()) : 0);
                }
            }
            
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("凭证分录明细数据.xlsx", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            
            workbook.write(response.getOutputStream());
            workbook.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出凭证列表记录
     */
    @PostMapping("/exportRecord")
    public void exportRecord(HttpServletResponse response, FinVoucherDTO dto)
    {
        try {
            List<Map<String,Object>> groupidlist = amazonClientOneFeignManager.getAmazonGroup();
            Map<String,Object> groupidmap = new HashMap<String,Object>();
            if(groupidlist!= null && groupidlist.size() > 0){
                for (Map<String,Object> map : groupidlist) {
                    if(map.get("id") != null && map.get("id").toString().equals(dto.getGroupid())){
                        groupidmap = map;
                    }
                }
            }
            List<String> voucherDates = new ArrayList<>();
            String companyname = groupidmap.get("company").toString();
            List<Map<String, Object>> list = finVoucherEntriesService.selectFinVoucherEntriesList(dto);

            // 构建辅助核算文本映射
            Map<String, String> auxiliaryTextMap = buildAuxiliaryTextMap(list);

            ClassPathResource resource = new ClassPathResource("template/voucher_entries_record_template.xlsx");
            InputStream inputStream = resource.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            // 先收集所有日期
            for (Map<String, Object> entry : list) {
                String voucherDate = entry.get("voucher_date") != null ? GeneralUtil.formatDate(GeneralUtil.getDate(entry.get("voucher_date"))) : "";
                if (!voucherDate.isEmpty()) {
                    String voucherDateStr = voucherDate.substring(0, 7);
                    if (!voucherDates.contains(voucherDateStr)) {
                        voucherDates.add(voucherDateStr);
                    }
                }
            }
            
            // 第二行：填入companyname和日期范围（模板已合并列，保留原有样式）
            Row row1 = sheet.getRow(1);
            if (row1 != null) {
                Cell cell1 = row1.getCell(0);
                if (cell1 != null) {
                    cell1.setCellValue(companyname);
                }
                
                Cell cell2 = row1.getCell(10);
                if (cell2 != null) {
                    cell2.setCellValue(voucherDates.toString());
                }
            }
            
            int startRow = 3;
            int rowNum = startRow;

            FinAccountingSubjects finAccountingSubjects=new FinAccountingSubjects();
            finAccountingSubjects.setGroupid(dto.getGroupid());
            finAccountingSubjects.setStatus(1);
            List<FinAccountingSubjects> listSubject = finAccountingSubjectsService.selectFinAccountingSubjectsList(finAccountingSubjects);
            // 构建科目名称映射，用于快速查找
            Map<String, FinAccountingSubjects> codeMap = new HashMap<>();
            for (FinAccountingSubjects subject : listSubject) {
                codeMap.put(subject.getSubjectCode(), subject);
            }


            for (Map<String, Object> entry : list) {
                Row row = sheet.createRow(rowNum++);
                String subjectName=null;
                FinAccountingSubjects subject = finAccountingSubjectsService.selectFinAccountingSubjectsBySubjectId( entry.get("subject_id").toString());
                if(subject!=null){
                    String fullName = finAccountingSubjectsService.buildFullSubjectName(subject, codeMap);
                    subjectName=fullName;
                }
                // 追加辅助核算后缀
                String entryId = entry.get("entry_id") != null ? entry.get("entry_id").toString() : "";
                if (!entryId.isEmpty() && auxiliaryTextMap.containsKey(entryId)) {
                    subjectName = (subjectName != null ? subjectName : "") + " - " + auxiliaryTextMap.get(entryId);
                }
                // 日期
                String voucherDate=entry.get("voucher_date") != null ? GeneralUtil.formatDate(GeneralUtil.getDate(entry.get("voucher_date"))) : "";
                createCell(row, 0, voucherDate);
                // 凭证字号
                String voucherType = entry.get("voucher_type") != null ? entry.get("voucher_type").toString() : "";
                String voucherNo = entry.get("voucher_no") != null ? entry.get("voucher_no").toString() : "";
                createCell(row, 1, voucherType + voucherNo);
                // 摘要
                createCell(row, 2, entry.get("summary") != null ? entry.get("summary").toString() : "");
                // 科目名称
                createCell(row, 3, subjectName != null ? subjectName : "");
                // 数量
                createCell(row, 4, entry.get("quantity") != null ? Double.parseDouble(entry.get("quantity").toString()) : 0);
                // 单价
                createCell(row, 5, entry.get("unit_price") != null ? Double.parseDouble(entry.get("unit_price").toString()) : 0);
                // 币别
                String currency = entry.get("currency") != null ? entry.get("currency").toString() : "RMB";
                createCell(row, 6, currency);
                // 原币金额
                double originalAmount = 0;
                if(entry.get("currency") == null) {
                    if(entry.get("debit_amount") != null && Double.parseDouble(entry.get("debit_amount").toString()) != 0){
                        originalAmount = Double.parseDouble(entry.get("debit_amount").toString());
                    } else if(entry.get("credit_amount") != null && Double.parseDouble(entry.get("credit_amount").toString()) != 0){
                        originalAmount = Double.parseDouble(entry.get("credit_amount").toString());
                    }
                } else {
                    originalAmount = entry.get("original_amount") != null ? Double.parseDouble(entry.get("original_amount").toString()) : 0;
                }
                createCell(row, 7, originalAmount);
                // 汇率
                double exchangeRate = entry.get("exchange_rate") != null ? Double.parseDouble(entry.get("exchange_rate").toString()) : 1;
                createCell(row, 8, exchangeRate);
                // 借方金额
                createCell(row, 9, entry.get("debit_amount") != null ? Double.parseDouble(entry.get("debit_amount").toString()) : 0);
                // 贷方金额
                createCell(row, 10, entry.get("credit_amount") != null ? Double.parseDouble(entry.get("credit_amount").toString()) : 0);
                // 附件
                createCell(row, 11, entry.get("attachment_count") != null ? entry.get("attachment_count").toString() : "");
                // 来源模块
                createCell(row, 12, "手工录入");
                // 原单据编号
                createCell(row, 13, "");
                // 制单人
                createCell(row, 14, entry.get("preparer_by") != null ? entry.get("preparer_by").toString() : "");
                // 审核人
                createCell(row, 15, entry.get("auditor_by") != null ? entry.get("auditor_by").toString() : "");
                // 冲销凭证
                createCell(row, 16, "");
                // 状态
                createCell(row, 17, "已审核");
                // 备注
                createCell(row, 18, entry.get("remark") != null ? entry.get("remark").toString() : "");
                // 制单日期
                createCell(row, 19, entry.get("voucher_date") != null ? GeneralUtil.formatDate(GeneralUtil.getDate(entry.get("voucher_date"))) : "");
            }
            
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("凭证列表.xlsx", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            
            workbook.write(response.getOutputStream());
            workbook.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 构建辅助核算文本映射：entry_id -> "itemCode_itemName - ..."
     */
    private Map<String, String> buildAuxiliaryTextMap(List<Map<String, Object>> list) {
        Map<String, String> result = new HashMap<>();
        List<Long> entryIds = collectEntryIds(list);
        if (entryIds.isEmpty()) {
            return result;
        }
        List<FinVoucherEntriesAuxiliary> auxList = finVoucherEntriesAuxiliaryService.selectByEntryIds(entryIds);
        Map<String, List<FinVoucherEntriesAuxiliary>> auxMap = new HashMap<>();
        for (FinVoucherEntriesAuxiliary aux : auxList) {
            auxMap.computeIfAbsent(aux.getEntryId(), k -> new ArrayList<>()).add(aux);
        }
        for (Map.Entry<String, List<FinVoucherEntriesAuxiliary>> e : auxMap.entrySet()) {
            List<String> parts = new ArrayList<>();
            for (FinVoucherEntriesAuxiliary aux : e.getValue()) {
                if (aux.getItemCode() != null && aux.getItemName() != null) {
                    parts.add(aux.getItemCode() + "_" + aux.getItemName());
                }
            }
            if (!parts.isEmpty()) {
                result.put(e.getKey(), String.join(" - ", parts));
            }
        }
        return result;
    }

    /**
     * 构建辅助核算列映射：entry_id -> Map<type_name, item_code>
     */
    private Map<String, Map<String, String>> buildAuxiliaryColumnMap(List<Map<String, Object>> list) {
        Map<String, Map<String, String>> result = new HashMap<>();
        List<Long> entryIds = collectEntryIds(list);
        if (entryIds.isEmpty()) {
            return result;
        }
        List<FinVoucherEntriesAuxiliary> auxList = finVoucherEntriesAuxiliaryService.selectByEntryIds(entryIds);
        for (FinVoucherEntriesAuxiliary aux : auxList) {
            if (aux.getTypeName() != null && aux.getItemCode() != null) {
                result.computeIfAbsent(aux.getEntryId(), k -> new HashMap<>())
                       .put(aux.getTypeName(), aux.getItemCode());
            }
        }
        return result;
    }

    private List<Long> collectEntryIds(List<Map<String, Object>> list) {
        List<Long> entryIds = new ArrayList<>();
        for (Map<String, Object> entry : list) {
            if (entry.get("entry_id") != null) {
                entryIds.add(Long.parseLong(entry.get("entry_id").toString()));
            }
        }
        return entryIds;
    }

    private void createCell(Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value != null ? value : "");
    }
    
    private void createCell(Row row, int column, double value) {
        if(value==0)return;
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
    }
    
    private void createCell(Row row, int column, int value) {
        if(value==0)return;
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
    }

    /**
     * 获取凭证分录明细详细信息
     */
    @GetMapping(value = "/{entryId}")
    public Result getInfo(@PathVariable("entryId") Long entryId)
    {
        return success(finVoucherEntriesService.selectFinVoucherEntriesByEntryId(entryId));
    }

    /**
     * 获取凭证分录明细详细信息
     */
    @GetMapping(value = "/getEntries")
    public Result getInfos( Long voucherId)
    {
        return success(finVoucherEntriesService.selectFinVoucherEntriesListByVoucherId(voucherId));
    }


    /**
     * 新增凭证分录明细
     */
    @PostMapping
    public Result add(@RequestBody FinVoucherEntries finVoucherEntries)
    {
        return toResult(finVoucherEntriesService.insertFinVoucherEntries(finVoucherEntries));
    }

    /**
     * 修改凭证分录明细
     */
    @PutMapping
    public Result edit(@RequestBody FinVoucherEntries finVoucherEntries)
    {
        return toResult(finVoucherEntriesService.updateFinVoucherEntries(finVoucherEntries));
    }

    /**
     * 删除凭证分录明细
     */
	@DeleteMapping("/{entryIds}")
    public Result remove(@PathVariable Long[] entryIds)
    {
        return toResult(finVoucherEntriesService.deleteFinVoucherEntriesByEntryIds(entryIds));
    }



}
