package com.wimoor.finance.closing.service.strategy.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.finance.api.AdminClientOneFeignManager;
import com.wimoor.finance.closing.domain.FinClosingTemplate;
import com.wimoor.finance.closing.domain.FinClosingTemplateFeishu;
import com.wimoor.finance.closing.domain.FinClosingTemplateItem;
import com.wimoor.finance.closing.domain.FinClosingTemplateVouchers;
import com.wimoor.finance.closing.service.IFinClosingTemplateFeishuService;
import com.wimoor.finance.closing.service.IFinClosingTemplateItemService;
import com.wimoor.finance.closing.service.IFinClosingTemplateService;
import com.wimoor.finance.closing.service.IFinClosingTemplateVouchersService;
import com.wimoor.finance.closing.service.strategy.IFinClosingTemplateStrategy;
import com.wimoor.finance.setting.domain.FinAccountingPeriods;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.service.IFinAccountingPeriodsService;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.finance.setting.service.IFinCurrencyService;
import com.wimoor.finance.voucher.domain.FinVoucherEntries;
import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.service.IFinVouchersService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.PatternSyntaxException;

@Service
public class FeiShuTemplateStrategy implements IFinClosingTemplateStrategy {

    private static final String FTYPE = "feishu";

    @Resource
    IFinClosingTemplateService finClosingTemplateService;
    @Resource
    IFinClosingTemplateItemService finClosingTemplateItemService;
    @Resource
    AdminClientOneFeignManager adminClientOneFeignManager;
    @Resource
    IFinVouchersService iFinVouchersService;
    @Resource
    IFinCurrencyService iFinCurrencyService;
    @Resource
    IFinClosingTemplateVouchersService finClosingTemplateVouchersService;
    @Resource
    IFinAccountingPeriodsService iFinAccountingPeriodsService;
    @Resource
    IFinAccountingSubjectsService finAccountingSubjectsService;
    @Resource
    IFinClosingTemplateFeishuService finClosingTemplateFeishuService;

    @Override
    public String getFtype() {
        return FTYPE;
    }
    private FinAccountingPeriods getPeriod(String groupid,String periodCode){
        FinAccountingPeriods period=null;
        if(StrUtil.isNotBlank(periodCode)){
            period= iFinAccountingPeriodsService.selectByPeriod(groupid,periodCode);
            if(period==null){
                //请trae 帮我把periodCode转换成日期格式
                String periodDate=periodCode+"01";
                SimpleDateFormat FMT_YMD = new SimpleDateFormat("yyyyMMdd");
                try {
                    period= iFinAccountingPeriodsService.selectFinAccountingPeriodsByDate(groupid,FMT_YMD.parse(periodDate));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }else{
            period= iFinAccountingPeriodsService.getCurrentPeriod(groupid);
        }
        if(period==null){
            throw new BizException("未找到指定的会计期间");
        }
        if(period.getPeriodStatus()==3){
            throw new BizException("会计期间状态错误");
        }
        return period;
    }

    @Override
    public void generateVoucher(UserInfo userInfo, String templateId, String periodCode) {
        //第一步：通过模版ID获取模版对象
        FinClosingTemplate template = finClosingTemplateService.selectFinClosingTemplateById(templateId);
        if(template == null) {
            return;
        }
        //第二步：通过模版ID获取fin_closing_template_feishu 对象
        FinClosingTemplateFeishu finClosingTemplateFeishu = finClosingTemplateFeishuService.selectFinClosingTemplateFeishuByTemplateid(templateId);
        if(finClosingTemplateFeishu == null) {
            return;
        }
        //第三步：通过模版ID获取Item对象
        FinClosingTemplateItem query = new FinClosingTemplateItem();
        query.setClosingTemplateId(templateId);
        List<FinClosingTemplateItem> finClosingTemplateItems = finClosingTemplateItemService.selectFinClosingTemplateItemList(query);
        if(finClosingTemplateItems == null || finClosingTemplateItems.isEmpty()) {
            return;
        }
        String feishuTableId = finClosingTemplateFeishu.getFeishuTableId();
        String filter = finClosingTemplateFeishu.getFilter();
        Object tableInfo = adminClientOneFeignManager.getFeishuTableById(feishuTableId);
        if(tableInfo == null) {
            return;
        }
        Map<String,Object> tableInfoMap = (Map<String,Object>)tableInfo;
        String tableName = (String)tableInfoMap.get("name");
        String tableId = (String)tableInfoMap.get("id");
        String tableUrl = (String)tableInfoMap.get("url");
        String voucherDateField =finClosingTemplateFeishu.getVoucherDateField();
        JSONObject filterJson = JSONObject.parseObject(filter);
        FinAccountingPeriods period = getPeriod(template.getGroupid(),periodCode);
        Date currentDate =   period.getEndDate();
        LocalDate localDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate firstDayMonth = localDate.withDayOfMonth(1);
        LocalDate lastDayMonth = localDate.withDayOfMonth(localDate.lengthOfMonth());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fromDate = firstDayMonth.format(formatter);
        String toDate = lastDayMonth.format(formatter);
        // 添加日期范围条件

        long firstDayMonthTimeStamp = firstDayMonth.atStartOfDay(ZoneId.of("Asia/Shanghai"))
                .toInstant()
                .toEpochMilli();
        long lastDayMonthTimeStamp = lastDayMonth.atStartOfDay(ZoneId.of("Asia/Shanghai"))
                .toInstant()
                .toEpochMilli();

        if (StrUtil.isNotBlank(voucherDateField)) {
            JSONObject fromCondition = new JSONObject();
            fromCondition.put("field_name", voucherDateField.trim());
            fromCondition.put("operator", "isGreater");
            fromCondition.put("value", new String[]{"ExactDate",firstDayMonthTimeStamp+""});
            
            JSONObject toCondition = new JSONObject();
            toCondition.put("field_name", voucherDateField.trim());
            toCondition.put("operator", "isLess");
            toCondition.put("value", new String[]{"ExactDate",lastDayMonthTimeStamp+""});
            
            filterJson.getJSONArray("conditions").add(fromCondition);
            filterJson.getJSONArray("conditions").add(toCondition);
        }
        Object result = adminClientOneFeignManager.getRecord(tableUrl, filterJson.toJSONString());

        if(result == null) {
            return;
        }
        JSONObject recordMap = JSONObject.parseObject(result.toString());
        JSONArray records = recordMap.getJSONArray("items");
        if(records == null || records.isEmpty()) {
            return;
        }
        //将同步的数据根据Item的规则Summary里面是借方Subject,subjectid里面是贷方凭证，amountField是金额字段
        FinVouchers finVouchers = new FinVouchers();
        finVouchers.setVoucherType(template.getVoucherType());
        finVouchers.setGroupid(template.getGroupid());
        finVouchers.setVoucherDate(GeneralUtil.getDatez(lastDayMonth.format(formatter)));
        finVouchers.setVoucherNo(iFinVouchersService.selectNextVoucherNo(finVouchers));
        finVouchers.setVoucherStatus(3);
        finVouchers.setDataSource(3); // 结账模版生成
        List<FinVoucherEntries> entryList = new ArrayList<FinVoucherEntries>();
        List<String> fieldkeys = new ArrayList<>();
        BigDecimal debitTotal = BigDecimal.ZERO;
        BigDecimal creditTotal = BigDecimal.ZERO;
        StringBuilder datalog = new StringBuilder();
        for(int i=0;i<records.size();i++) {
            JSONObject recordItem = records.getJSONObject(i);
            String fieldKey=recordItem.get("record_id").toString();
            fieldkeys.add(fieldKey);
            JSONObject recordItemFields=recordItem.getJSONObject("fields");
            String summary = extractFieldValue(recordItemFields, finClosingTemplateFeishu.getSummaryField());
            BigDecimal amount = recordItemFields.getBigDecimal(finClosingTemplateFeishu.getAmountField());
            String subjectTitle = extractFieldValue(recordItemFields, finClosingTemplateFeishu.getSubjectField());
            if(amount==null||amount.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            long voucher_no=1L;
            for(FinClosingTemplateItem item : finClosingTemplateItems) {
                System.out.println("Item Amount Field: " + item.getAmountField() + ", Subject Title: " + subjectTitle);
                boolean matches = matchesPattern(item.getAmountField(),subjectTitle );
                if(matches) {
                    //TODO:生成凭证
                   String debitSubject = item.getSummary();//借方科目
                   String creditSubject =item.getSubjectId();//贷方科目
                   //这里生成凭证Item
                   FinVoucherEntries entry = new FinVoucherEntries();
                   entry.setSubjectId(debitSubject);
                   entry.setDebitAmount(amount);
                   entry.setSummary(summary);
                   entry.setEntryNo(voucher_no++);
                   datalog.append(entry.getEntryNo()).append("-").append(recordItem).append(";");
                   entryList.add(entry);
                    //这里生成凭证Item
                    FinVoucherEntries entry2 = new FinVoucherEntries();
                    entry2.setSubjectId(creditSubject);
                    entry2.setCreditAmount(amount);
                    entry2.setSummary(summary);
                    entry2.setEntryNo(voucher_no++);
                    datalog.append(entry2.getEntryNo()).append("-").append(recordItem).append(";");
                    entryList.add(entry2);

                    // 累加借贷金额
                    if(entry.getDebitAmount() != null) {
                        debitTotal = debitTotal.add(entry.getDebitAmount());
                    }
                    if(entry2.getCreditAmount() != null) {
                        creditTotal = creditTotal.add(entry2.getCreditAmount());
                    }
                    break;
                }
           }
        }

        // 检查借贷平衡
        if (!debitTotal.equals(creditTotal)) {
            throw new RuntimeException("凭证生成失败：借贷不平衡，借方金额：" + debitTotal + "，贷方金额：" + creditTotal);
        }

        // 设置凭证总金额
        finVouchers.setTotalAmount(debitTotal);
        finVouchers.setEntries(entryList);
        FinClosingTemplateVouchers queryVouchers = new FinClosingTemplateVouchers();
        queryVouchers.setTemplateId(templateId);
        queryVouchers.setGroupid(template.getGroupid());
        queryVouchers.setVoucherDate(finVouchers.getVoucherDate());
        FinClosingTemplateVouchers existingTemplateVouchers = finClosingTemplateVouchersService.selectByTemplate(queryVouchers);
        // 同步插入或更新 FinClosingTemplateVouchers 数据
        if (existingTemplateVouchers != null) {

            // 更新现有凭证
            FinVouchers oldFinVouchers = iFinVouchersService.selectFinVouchersByVoucherId(Long.valueOf(existingTemplateVouchers.getVourchesId()));
            finVouchers.setVoucherNo(oldFinVouchers.getVoucherNo());
            finVouchers.setUpdateBy(userInfo.getUserName());
            finVouchers.setUpdatedTime(new Date());
            finVouchers.setVoucherId(Long.valueOf(existingTemplateVouchers.getVourchesId()));
            for(FinVoucherEntries entry:finVouchers.getEntries()){
                entry.setVoucherId(Long.valueOf(existingTemplateVouchers.getVourchesId()));
            }
            iFinVouchersService.updateFinVouchers(finVouchers);

            // 更新关联记录
            existingTemplateVouchers.setDatalog(datalog.toString());
            existingTemplateVouchers.setUpdateBy(userInfo.getUserName());
            existingTemplateVouchers.setVoucherDate(finVouchers.getVoucherDate());
            existingTemplateVouchers.setUpdatedTime(new Date());
            finClosingTemplateVouchersService.updateFinClosingTemplateVouchers(existingTemplateVouchers);
        } else {
            finVouchers.setCreateBy(userInfo.getUserName());
            finVouchers.setUpdateBy(userInfo.getUserName());
            finVouchers.setCreatedTime(new Date());
            finVouchers.setUpdatedTime(new Date());
            // 插入新凭证
            iFinVouchersService.insertFinVouchers(finVouchers);

            // 插入新关联记录
            FinClosingTemplateVouchers templateVouchers = new FinClosingTemplateVouchers();
            templateVouchers.setTemplateId(templateId);
            templateVouchers.setGroupid(template.getGroupid());
            templateVouchers.setDatalog(datalog.toString());
            templateVouchers.setVourchesId(finVouchers.getVoucherId().toString());
            templateVouchers.setVoucherDate(finVouchers.getVoucherDate());
            templateVouchers.setCreatedTime(new Date());
            templateVouchers.setUpdatedTime(new Date());
            templateVouchers.setCreateBy(userInfo.getUserName());
            templateVouchers.setUpdateBy(userInfo.getUserName());
            finClosingTemplateVouchersService.insertFinClosingTemplateVouchers(templateVouchers);
        }
        //反写凭证Record,将凭证中的凭证日期-凭证子和凭证号写入飞书记录中

        Map<String, Object> param = new HashMap<>();
        param.put("tableId", tableId);
        param.put("fieldkeys", fieldkeys);
        param.put("value","["+GeneralUtil.formatDate(finVouchers.getVoucherDate())+"]-"+finVouchers.getVoucherType()+"-"+finVouchers.getVoucherNo());
        adminClientOneFeignManager.updateCallback(param);
    }
    private String extractFieldValue(JSONObject recordItemFields, String fieldName) {
    if (recordItemFields == null || StrUtil.isBlank(fieldName)) {
        return null;
    }
    
    if (!recordItemFields.containsKey(fieldName)) {
        return null;
    }
    
    Object fieldValue = recordItemFields.get(fieldName);
    
    // String 类型直接返回
    if (fieldValue instanceof String) {
        return (String) fieldValue;
    }
    
    // JSONArray 类型，遍历所有元素并提取值，用逗号连接
    if (fieldValue instanceof JSONArray) {
        JSONArray jsonArray = (JSONArray) fieldValue;
        if (jsonArray.isEmpty()) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jsonArray.size(); i++) {
            Object item = jsonArray.get(i);
            // 如果是 JSONObject，尝试从 text、name、value 字段中提取值
            if (item instanceof JSONObject) {
                JSONObject jsonItem = (JSONObject) item;
                String value = null;
                if (jsonItem.containsKey("text")) {
                    value = jsonItem.getString("text");
                } else if (jsonItem.containsKey("name")) {
                    value = jsonItem.getString("name");
                } else if (jsonItem.containsKey("value")) {
                    value = jsonItem.getString("value");
                }
                
                if (StrUtil.isNotBlank(value)) {
                    if (sb.length() > 0) {
                        sb.append(",");
                    }
                    sb.append(value);
                }
            } else if (item instanceof String) {
                // 如果是字符串直接添加
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append((String) item);
            }
        }
        
        return sb.length() > 0 ? sb.toString() : null;
    }
    
    // 其他类型返回 null
    return null;
}
    public static boolean matchesPattern(String pattern, String input) {
        // 空值保护
        if (pattern == null || input == null) {
            return false;
        }

        // 1. 星号：匹配所有
        if ("*".equals(pattern)) {
            return true;
        }

        // 2. 逗号分隔列表：input 精确等于其中一项（trim 前后空格后比较）
        if (pattern.contains(",")) {
            String[] items = pattern.split(",");
            for (String item : items) {
                // 去除每个选项的前后空白，然后与输入精确比较（输入不 trim，保留原样）
                // 若希望输入也忽略前后空白，可将 input 改为 input.trim()，但会丢失名字本身的空格语义
                if (input.equals(item.trim())) {
                    return true;
                }
            }
            return false;
        }

        // 3. 其余情况视为正则表达式
        try {
            return WildcardMatcher.match(pattern, input);
        } catch (PatternSyntaxException e) {
            // 正则表达式语法错误 -> 不匹配
            return false;
        }
    }

    @Override
    public void initTemplateItem(FinClosingTemplate template) {

    }
    
    @Override
    public Map<String, Object> getCalculationDetail(String templateId, String periodCode) {
        Map<String, Object> result = new HashMap<>();
        
        FinClosingTemplate template = finClosingTemplateService.selectFinClosingTemplateById(templateId);
        if (template == null) {
            return result;
        }
        
        String groupid = template.getGroupid();
        FinAccountingPeriods period = getPeriod(groupid, periodCode);

        FinAccountingSubjects querySubject = new FinAccountingSubjects();
        querySubject.setGroupid(groupid);
        querySubject.setStatus(1);
        List<FinAccountingSubjects> subjects = finAccountingSubjectsService.selectFinAccountingSubjectsList(querySubject);
        Map<String, FinAccountingSubjects> codeMap = new HashMap<>();
        for (FinAccountingSubjects subject : subjects) {
            codeMap.put(subject.getSubjectCode(), subject);
        }

        // 获取飞书配置
        FinClosingTemplateFeishu query = new FinClosingTemplateFeishu();
        query.setTemplateid(templateId);
        List<FinClosingTemplateFeishu> feishuConfigList = finClosingTemplateFeishuService.selectFinClosingTemplateFeishuList(query);
        result.put("templateName", template.getName());
        result.put("periodName", period.getPeriodName());
        result.put("formula", "从飞书表格读取数据，按模板配置生成凭证");
        result.put("dataSource", "飞书表格");
        
        // 飞书表格详细配置
        if (feishuConfigList != null && !feishuConfigList.isEmpty()) {
            FinClosingTemplateFeishu feishuConfig = feishuConfigList.get(0);
            Map<String, Object> feishuDetail = new HashMap<>();
            feishuDetail.put("feishuTableId", feishuConfig.getFeishuTableId());
            feishuDetail.put("filter", feishuConfig.getFilter());
            feishuDetail.put("summaryField", feishuConfig.getSummaryField());
            feishuDetail.put("voucherDateField", feishuConfig.getVoucherDateField());
            feishuDetail.put("subjectField", feishuConfig.getSubjectField());
            feishuDetail.put("amountField", feishuConfig.getAmountField());
            feishuDetail.put("datetype", feishuConfig.getDatetype());
            // datetype 中文说明
            String datetypeDesc = "按月";
            if (feishuConfig.getDatetype() != null) {
                switch (feishuConfig.getDatetype()) {
                    case 0: datetypeDesc = "按日"; break;
                    case 1: datetypeDesc = "按月"; break;
                    case 2: datetypeDesc = "单笔生成凭证"; break;
                }
            }
            feishuDetail.put("datetypeDesc", datetypeDesc);
            result.put("feishuConfig", feishuDetail);
        }
        
        // 获取模板科目配置（飞书模板中 summary 存储的是字段映射，amount_field 存储的是过滤条件）
        FinClosingTemplateItem itemQuery = new FinClosingTemplateItem();
        itemQuery.setClosingTemplateId(templateId);
        List<FinClosingTemplateItem> items = finClosingTemplateItemService.selectFinClosingTemplateItemList(itemQuery);
        
        List<Map<String, Object>> itemDetails = new ArrayList<>();
        if (items != null) {
            for (FinClosingTemplateItem item : items) {
                Map<String, Object> itemDetail = new HashMap<>();
                // 飞书模板中 summary 存储的是字段映射/条件
                itemDetail.put("fieldMapping", item.getSummary());
                itemDetail.put("subjectId", item.getSubjectId());
                // 查询科目名称
                if (item.getSubjectId() != null) {
                    FinAccountingSubjects subject = finAccountingSubjectsService.getSubjectById(item.getSubjectId());
                    if (subject != null) {
                        itemDetail.put("subjectName", subject.getSubjectCode() + " " + finAccountingSubjectsService.buildFullSubjectName(subject, codeMap));
                    }
                    FinAccountingSubjects subject2 = finAccountingSubjectsService.getSubjectById( item.getSummary());
                    if (subject != null) {
                        itemDetail.put("fieldMapping",subject2.getSubjectCode() + " " + finAccountingSubjectsService.buildFullSubjectName(subject2, codeMap));
                    }
                }
                itemDetail.put("direction", item.getDirection());
                // 飞书模板中 amount_field 存储的是过滤条件
                itemDetail.put("filterCondition", item.getAmountField());
                itemDetails.add(itemDetail);
            }
        }
        result.put("items", itemDetails);
        
        return result;
    }
}
