package com.wimoor.finance.setting.service.impl;

import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.finance.closing.domain.FinClosingTemplateAmazon;
import com.wimoor.finance.closing.domain.FinClosingTemplateItem;
import com.wimoor.finance.setting.domain.FinAccountingSubjectAuxiliarySetting;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.domain.FinAuxiliaryTypes;
import com.wimoor.finance.setting.mapper.FinAccountingSubjectsMapper;
import com.wimoor.finance.setting.service.IFinAccountingPeriodsService;
import com.wimoor.finance.setting.service.IFinAccountingSubjectAuxiliarySettingService;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.finance.setting.service.IFinAuxiliaryTypesService;
import com.wimoor.finance.voucher.domain.FinVoucherEntries;
import com.wimoor.finance.voucher.domain.FinVouchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 会计科目Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-05
 */
@Service
public class FinAccountingSubjectsServiceImpl implements IFinAccountingSubjectsService
{
    @Autowired
    private FinAccountingSubjectsMapper finAccountingSubjectsMapper;
    @Autowired
    private IFinAccountingPeriodsService finAccountingPeriodsService;
    @Autowired
    private IFinAuxiliaryTypesService finAuxiliaryTypesService;
    @Autowired
    private IFinAccountingSubjectAuxiliarySettingService finAccountingSubjectAuxiliarySettingService;
    /**
     * 查询会计科目
     * 
     * @param subjectId 会计科目主键
     * @return 会计科目
     */
    @Override
    public FinAccountingSubjects selectFinAccountingSubjectsBySubjectId(String subjectId)
    {
        FinAccountingSubjects subject = finAccountingSubjectsMapper.selectFinAccountingSubjectsBySubjectId(subjectId);
        if (subject != null) {
            loadAuxiliaryTypes(subject);
        }
        return subject;
    }

    private void loadAuxiliaryTypes(FinAccountingSubjects subject) {
        FinAccountingSubjectAuxiliarySetting query = new FinAccountingSubjectAuxiliarySetting();
        query.setSubjectId(String.valueOf(subject.getSubjectId()));
        List<FinAccountingSubjectAuxiliarySetting> settings = finAccountingSubjectAuxiliarySettingService.selectFinAccountingSubjectAuxiliarySettingList(query);
        if (settings == null || settings.isEmpty()) {
            return;
        }
        FinAuxiliaryTypes typeQuery = new FinAuxiliaryTypes();
        typeQuery.setGroupid(subject.getGroupid());
        List<FinAuxiliaryTypes> allTypes = finAuxiliaryTypesService.selectFinAuxiliaryTypesList(typeQuery);
        Map<String, String> typeIdToCodeMap = new HashMap<>();
        for (FinAuxiliaryTypes type : allTypes) {
            typeIdToCodeMap.put(String.valueOf(type.getTypeId()), type.getTypeCode());
        }
        List<String> typeCodes = new ArrayList<>();
        for (FinAccountingSubjectAuxiliarySetting setting : settings) {
            String code = typeIdToCodeMap.get(setting.getAuxiliaryTypeId());
            if (code != null) {
                typeCodes.add(code);
            }
        }
        subject.setAuxiliaryTypeList(typeCodes);
    }

    /**
     * 查询会计科目列表
     * 
     * @param finAccountingSubjects 会计科目
     * @return 会计科目
     */
    @Override
    public List<FinAccountingSubjects> selectFinAccountingSubjectsList(FinAccountingSubjects finAccountingSubjects)
    {
        return finAccountingSubjectsMapper.selectFinAccountingSubjectsList(finAccountingSubjects);
    }
    /**
     * 查询会计科目列表
     *
     * @param finAccountingSubjects 会计科目
     * @return 会计科目
     */
    @Override
    public List<FinAccountingSubjects> selectFinAccountingSubjectsListAll(FinAccountingSubjects finAccountingSubjects)
    {
        List<FinAccountingSubjects> list = finAccountingSubjectsMapper.selectFinAccountingSubjectsList(finAccountingSubjects);
        // 构建科目名称映射，用于快速查找
        Map<String, FinAccountingSubjects> codeMap = new HashMap<>();
        for (FinAccountingSubjects subject : list) {
            codeMap.put(subject.getSubjectCode(), subject);
        }
        // 为每个科目设置完整的科目名称
        for (FinAccountingSubjects subject : list) {
            String fullName = buildFullSubjectName(subject, codeMap);
            subject.setSubjectName(fullName);
        }
        return list;
    }

    @Override
    public void setFinVoucherSubject(List<FinVouchers> list) {
        if(list != null && list.size() > 0){
            FinVouchers one = list.get(0);
            FinAccountingSubjects finAccountingSubjects=new FinAccountingSubjects();
            finAccountingSubjects.setGroupid(one.getGroupid());
            finAccountingSubjects.setStatus(1);
            List<FinAccountingSubjects> listSubject = finAccountingSubjectsMapper.selectFinAccountingSubjectsList(finAccountingSubjects);
            // 构建科目编码映射和ID映射，用于快速查找
            Map<String, FinAccountingSubjects> codeMap = new HashMap<>();
            Map<String, FinAccountingSubjects> idMap = new HashMap<>();
            for (FinAccountingSubjects subject : listSubject) {
                codeMap.put(subject.getSubjectCode(), subject);
                idMap.put(String.valueOf(subject.getSubjectId()), subject);
            }
            // 为每个科目设置完整的科目名称（使用内存映射代替逐条查询）
            for (FinVouchers voucher : list) {
                for(FinVoucherEntries entries:voucher.getEntries()){
                    FinAccountingSubjects subject = idMap.get(entries.getSubjectId());
                    if(subject!=null){
                        String fullName = buildFullSubjectName(subject, codeMap);
                        entries.setSubjectName(subject.getSubjectCode()+" "+fullName);
                    }
                }
            }
        }
    }

    @Override
    public void setFinLedgerSubject(TableDataInfo page) {
        if(page != null && !page.getRows().isEmpty()){
            Map<String,Object> one = (Map<String, Object>) page.getRows().get(0);
            FinAccountingSubjects finAccountingSubjects=new FinAccountingSubjects();
            finAccountingSubjects.setGroupid(one.get("groupid").toString());
            finAccountingSubjects.setStatus(1);
            List<FinAccountingSubjects> listSubject = finAccountingSubjectsMapper.selectFinAccountingSubjectsList(finAccountingSubjects);
            // 构建科目名称映射，用于快速查找
            Map<String, FinAccountingSubjects> codeMap = new HashMap<>();
            for (FinAccountingSubjects subject : listSubject) {
                codeMap.put(subject.getSubjectCode(), subject);
            }
            // 为每个科目设置完整的科目名称
            for (Object row : page.getRows()) {
                    Map<String,Object> voucher =row!=null? (Map<String, Object>) row:new HashMap<>();
                    FinAccountingSubjects subject = finAccountingSubjectsMapper.selectFinAccountingSubjectsBySubjectId(voucher.get("subjectId").toString());
                    if(subject!=null){
                        String fullName = buildFullSubjectName(subject, codeMap);
                        voucher.put("subjectName",(subject.getSubjectCode()+" "+fullName));
                    }
            }

        }
    }

    @Override
    public void setFinTemplateSubject(List<FinClosingTemplateAmazon> list) {
        if(list != null && !list.isEmpty()){
            FinClosingTemplateAmazon one = list.get(0);
            FinAccountingSubjects subjectOne = finAccountingSubjectsMapper.selectFinAccountingSubjectsBySubjectId(one.getSubjectId());
            FinAccountingSubjects finAccountingSubjects=new FinAccountingSubjects();
            finAccountingSubjects.setGroupid(subjectOne.getGroupid());
            finAccountingSubjects.setStatus(1);
            List<FinAccountingSubjects> listSubject = finAccountingSubjectsMapper.selectFinAccountingSubjectsList(finAccountingSubjects);
            // 构建科目名称映射，用于快速查找
            Map<String, FinAccountingSubjects> codeMap = new HashMap<>();
            for (FinAccountingSubjects subject : listSubject) {
                codeMap.put(subject.getSubjectCode(), subject);
            }
            // 为每个科目设置完整的科目名称
            for (FinClosingTemplateAmazon voucher : list) {
                FinAccountingSubjects subject = finAccountingSubjectsMapper.selectFinAccountingSubjectsBySubjectId(voucher.getSubjectId());
                if(subject!=null){
                    String fullName = buildFullSubjectName(subject, codeMap);
                    voucher.setSubjectName(subject.getSubjectCode()+" "+fullName);
                }
            }

        }
    }

    @Override
    public void setFinTemplateItemSubject(List<FinClosingTemplateItem> list) {
        if(list != null && !list.isEmpty()){
            FinClosingTemplateItem one = list.get(0);
            FinAccountingSubjects subjectOne = finAccountingSubjectsMapper.selectFinAccountingSubjectsBySubjectId(one.getSubjectId());
            FinAccountingSubjects finAccountingSubjects=new FinAccountingSubjects();
            finAccountingSubjects.setGroupid(subjectOne.getGroupid());
            finAccountingSubjects.setStatus(1);
            List<FinAccountingSubjects> listSubject = finAccountingSubjectsMapper.selectFinAccountingSubjectsList(finAccountingSubjects);
            // 构建科目名称映射，用于快速查找
            Map<String, FinAccountingSubjects> codeMap = new HashMap<>();
            for (FinAccountingSubjects subject : listSubject) {
                codeMap.put(subject.getSubjectCode(), subject);
            }
            // 为每个科目设置完整的科目名称
            for (FinClosingTemplateItem voucher : list) {
                FinAccountingSubjects subject = finAccountingSubjectsMapper.selectFinAccountingSubjectsBySubjectId(voucher.getSubjectId());
                if(subject!=null){
                    String fullName = buildFullSubjectName(subject, codeMap);
                    voucher.setSubjectName(subject.getSubjectCode()+" "+fullName);
                }
            }

        }
    }
    /**
     * 递归构建完整的科目名称
     * @param subject 当前科目
     * @param codeMap 科目编码映射
     * @return 完整的科目名称，用下划线分割
     */
    public String buildFullSubjectName(FinAccountingSubjects subject, Map<String, FinAccountingSubjects> codeMap) {
        // 如果是顶级科目，直接返回科目名称
        if (subject.getParentCode() == null || subject.getParentCode().isEmpty()) {
            return subject.getSubjectName();
        }
        // 查找父科目
        FinAccountingSubjects parent = codeMap.get(subject.getParentCode());
        if (parent == null) {
            // 如果找不到父科目，直接返回当前科目名称
            return subject.getSubjectName();
        }
        // 递归获取父科目的完整名称，然后与当前科目名称拼接
        String parentFullName = buildFullSubjectName(parent, codeMap);
        return parentFullName + "_" + subject.getSubjectName();
    }

    @Override
    public FinAccountingSubjects selectByGroupCode(String groupid, String subjectCode) {
        return finAccountingSubjectsMapper.selectByGroupCode(groupid, subjectCode);
    }

    @Override
    public int initFinAccountingSubjects(String groupId) {
        finAccountingPeriodsService.initAccountingPeriods(groupId);
        return finAccountingSubjectsMapper.initFin(groupId);
    }

    /**
     * 获取所有损益类科目（基于科目编码识别）
     * 通常损益类科目的编码以 6 开头（收入类）和 5 开头（费用类）
     */
    public List<FinAccountingSubjects> getProfitLossSubjects(String groupid) {
        // 损益类科目通常包括：
        // - 5开头：费用类科目（如：5601销售费用、5602管理费用等）
        // - 6开头：收入类科目（如：6001主营业务收入、6051其他业务收入等）
        List<String> profitLossPrefixes = Arrays.asList("5", "6");
        return finAccountingSubjectsMapper.selectBySubjectCodePrefixes(groupid, profitLossPrefixes);
    }

    /**
     * 获取income科目（基于科目编码识别）
     * 收入类科目编码通常以 6 开头
     */
    public List<FinAccountingSubjects> getIncomeSubjects(String groupid) {
        // 收入类科目编码前缀：6
        List<String> incomePrefixes = Arrays.asList("6");
        return finAccountingSubjectsMapper.selectBySubjectCodePrefixes(groupid, incomePrefixes);
    }

    /**
     * 获取费用类科目（基于科目编码识别）
     * 费用类科目编码通常以 5 开头
     */
    public List<FinAccountingSubjects> getExpenseSubjects(String groupid) {
        // 费用类科目编码前缀：5
        List<String> expensePrefixes = Arrays.asList("5");
        return finAccountingSubjectsMapper.selectBySubjectCodePrefixes(groupid, expensePrefixes);
    }

    /**
     * 获取主营业务收入科目（更精确的编码匹配）
     */
    public FinAccountingSubjects getMainBusinessIncomeSubjects(String groupid) {
        // 主营业务收入科目：6001
        return finAccountingSubjectsMapper.selectBySubjectCode(groupid, "6001");
    }

    /**
     * 获取期间费用科目
     */
    public List<FinAccountingSubjects> getPeriodExpenseSubjects(String groupid) {
        // 期间费用科目：5601销售费用、5602管理费用、5603财务费用
        List<String> periodExpenseCodes = Arrays.asList("5601", "5602", "5603");
        return finAccountingSubjectsMapper.selectBySubjectCodes(groupid, periodExpenseCodes);
    }

    /**
     * 获取成本类科目（基于科目编码识别）
     * 成本类科目编码通常以 4 开头
     */
    public List<FinAccountingSubjects> getCostSubjects(String groupid) {
        // 成本类科目编码前缀：4
        List<String> costPrefixes = Arrays.asList("4");
        return finAccountingSubjectsMapper.selectBySubjectCodePrefixes(groupid, costPrefixes);
    }
    /**
     * 新增会计科目
     * 
     * @param finAccountingSubjects 会计科目
     * @return 结果
     */
    @Override
    public int insertFinAccountingSubjects(FinAccountingSubjects finAccountingSubjects)
    {
        int result = finAccountingSubjectsMapper.insertFinAccountingSubjects(finAccountingSubjects);
        // 保存辅助核算设置
        saveAuxiliarySettings(finAccountingSubjects.getSubjectId(), finAccountingSubjects.getGroupid(), finAccountingSubjects.getAuxiliaryTypeList());
        return result;
    }

    /**
     * 修改会计科目
     * 
     * @param finAccountingSubjects 会计科目
     * @return 结果
     */
    @Override
    public int updateFinAccountingSubjects(FinAccountingSubjects finAccountingSubjects)
    {
        int result = finAccountingSubjectsMapper.updateFinAccountingSubjects(finAccountingSubjects);
        // 差异对比更新辅助核算设置
        updateAuxiliarySettings(finAccountingSubjects.getSubjectId(), finAccountingSubjects.getGroupid(), finAccountingSubjects.getAuxiliaryTypeList());
        return result;
    }
    
    /**
     * 差异对比更新辅助核算设置：保留已有的，删除不再需要的，新增加的才插入
     */
    private void updateAuxiliarySettings(Long subjectId, String groupid, List<String> auxiliaryTypeList) {
        if (subjectId == null) {
            return;
        }
        // 查询现有设置
        FinAccountingSubjectAuxiliarySetting existQuery = new FinAccountingSubjectAuxiliarySetting();
        existQuery.setSubjectId(String.valueOf(subjectId));
        List<FinAccountingSubjectAuxiliarySetting> existingSettings = finAccountingSubjectAuxiliarySettingService.selectFinAccountingSubjectAuxiliarySettingList(existQuery);
        
        // 现有设置按 auxiliaryTypeId 索引
        Map<String, FinAccountingSubjectAuxiliarySetting> existingMap = new HashMap<>();
        if (existingSettings != null) {
            for (FinAccountingSubjectAuxiliarySetting s : existingSettings) {
                existingMap.put(s.getAuxiliaryTypeId(), s);
            }
        }
        
        // 将新的 typeCode 列表转换为 typeId 集合
        Set<String> newTypeIds = new HashSet<>();
        if (auxiliaryTypeList != null && !auxiliaryTypeList.isEmpty()) {
            FinAuxiliaryTypes typeQuery = new FinAuxiliaryTypes();
            typeQuery.setGroupid(groupid);
            List<FinAuxiliaryTypes> allTypes = finAuxiliaryTypesService.selectFinAuxiliaryTypesList(typeQuery);
            Map<String, Long> typeCodeToIdMap = new HashMap<>();
            for (FinAuxiliaryTypes type : allTypes) {
                typeCodeToIdMap.put(type.getTypeCode(), type.getTypeId());
            }
            for (String typeCode : auxiliaryTypeList) {
                Long typeId = typeCodeToIdMap.get(typeCode);
                if (typeId != null) {
                    newTypeIds.add(String.valueOf(typeId));
                }
            }
        }
        
        Date now = new Date();
        
        // 1. 保留已有的（只更新 updated_time）
        for (String typeId : newTypeIds) {
            FinAccountingSubjectAuxiliarySetting existing = existingMap.get(typeId);
            if (existing != null) {
                existing.setUpdatedTime(now);
                finAccountingSubjectAuxiliarySettingService.updateFinAccountingSubjectAuxiliarySetting(existing);
            } else {
                // 2. 新增加的才插入
                FinAccountingSubjectAuxiliarySetting setting = new FinAccountingSubjectAuxiliarySetting();
                setting.setSubjectId(String.valueOf(subjectId));
                setting.setAuxiliaryTypeId(typeId);
                setting.setGroupid(groupid);
                setting.setCreatedTime(now);
                setting.setUpdatedTime(now);
                finAccountingSubjectAuxiliarySettingService.insertFinAccountingSubjectAuxiliarySetting(setting);
            }
        }
        
        // 3. 删除不再需要的
        for (Map.Entry<String, FinAccountingSubjectAuxiliarySetting> entry : existingMap.entrySet()) {
            if (!newTypeIds.contains(entry.getKey())) {
                finAccountingSubjectAuxiliarySettingService.deleteFinAccountingSubjectAuxiliarySettingById(entry.getValue().getId());
            }
        }
    }
    
    /**
     * 保存辅助核算设置
     * @param subjectId 科目ID
     * @param groupid 租户ID
     * @param auxiliaryTypeList 辅助核算类型编码列表
     */
    private void saveAuxiliarySettings(Long subjectId, String groupid, List<String> auxiliaryTypeList) {
        if (subjectId == null || auxiliaryTypeList == null || auxiliaryTypeList.isEmpty()) {
            return;
        }
        // 根据 typeCode 查询 typeId
        FinAuxiliaryTypes query = new FinAuxiliaryTypes();
        query.setGroupid(groupid);
        List<FinAuxiliaryTypes> types = finAuxiliaryTypesService.selectFinAuxiliaryTypesList(query);
        
        Map<String, Long> typeCodeToIdMap = new HashMap<>();
        for (FinAuxiliaryTypes type : types) {
            typeCodeToIdMap.put(type.getTypeCode(), type.getTypeId());
        }
        
        // 保存关联关系
        for (String typeCode : auxiliaryTypeList) {
            Long typeId = typeCodeToIdMap.get(typeCode);
            if (typeId != null) {
                FinAccountingSubjectAuxiliarySetting setting = new FinAccountingSubjectAuxiliarySetting();
                setting.setSubjectId(String.valueOf(subjectId));
                setting.setAuxiliaryTypeId(String.valueOf(typeId));
                setting.setGroupid(groupid);
                setting.setCreatedTime(new Date());
                setting.setUpdatedTime(new Date());
                finAccountingSubjectAuxiliarySettingService.insertFinAccountingSubjectAuxiliarySetting(setting);
            }
        }
    }
    
    /**
     * 删除科目对应的辅助核算设置
     * @param subjectId 科目ID
     */
    private void deleteAuxiliarySettings(Long subjectId) {
        if (subjectId == null) {
            return;
        }
        FinAccountingSubjectAuxiliarySetting query = new FinAccountingSubjectAuxiliarySetting();
        query.setSubjectId(String.valueOf(subjectId));
        List<FinAccountingSubjectAuxiliarySetting> settings = finAccountingSubjectAuxiliarySettingService.selectFinAccountingSubjectAuxiliarySettingList(query);
        for (FinAccountingSubjectAuxiliarySetting setting : settings) {
            finAccountingSubjectAuxiliarySettingService.deleteFinAccountingSubjectAuxiliarySettingById(setting.getId());
        }
    }

    /**
     * 批量删除会计科目
     * 
     * @param subjectIds 需要删除的会计科目主键
     * @return 结果
     */
    @Override
    public int deleteFinAccountingSubjectsBySubjectIds(String[] subjectIds)
    {
        // 删除关联的辅助核算设置
        for (String subjectId : subjectIds) {
            deleteAuxiliarySettings(Long.parseLong(subjectId));
        }
        return finAccountingSubjectsMapper.deleteFinAccountingSubjectsBySubjectIds(subjectIds);
    }

    /**
     * 删除会计科目信息
     * 
     * @param subjectId 会计科目主键
     * @return 结果
     */
    @Override
    public int deleteFinAccountingSubjectsBySubjectId(String subjectId)
    {
        // 删除关联的辅助核算设置
        deleteAuxiliarySettings(Long.parseLong(subjectId));
        return finAccountingSubjectsMapper.deleteFinAccountingSubjectsBySubjectId(subjectId);
    }

    @Override
    public FinAccountingSubjects getSubjectById(String subjectId) {
        // 获取科目信息
        FinAccountingSubjects subject = finAccountingSubjectsMapper.selectFinAccountingSubjectsBySubjectId(subjectId);
        return subject;
    }

    /**
     * 获取本年利润科目
     * 本年利润科目通常编码为 4103
     */
    @Override
    public FinAccountingSubjects getProfitSubject(String groupid) {
        // 本年利润科目：4103
        return finAccountingSubjectsMapper.selectBySubjectCode(groupid, "4103");
    }

    /**
     * 获取利润分配科目
     * 利润分配科目通常编码为 4104
     */
    @Override
    public FinAccountingSubjects getProfitDistributionSubject(String groupid) {
        // 利润分配科目：4104
        return finAccountingSubjectsMapper.selectBySubjectCode(groupid, "4104");
    }

    /**
     * 导入会计科目
     * @param inputStream Excel文件输入流
     * @return 导入成功的记录数
     * @throws IOException 异常
     */
    @Override
    public int importSubjects(InputStream inputStream, String groupId) throws IOException {
        ExcelUtil<FinAccountingSubjects> util = new ExcelUtil<FinAccountingSubjects>(FinAccountingSubjects.class);
        List<FinAccountingSubjects> list = util.importExcel(inputStream);
        int count = 0;
        for (FinAccountingSubjects subject : list) {
            // 检查科目是否已存在
            subject.setGroupid(groupId);
            
            // 转换类别：中文名称 -> 数字代码
            if (subject.getSubjectTypeName() != null) {
                switch (subject.getSubjectTypeName().trim()) {
                    // 子类别
                    case "流动资产":
                        subject.setSubjectType(6);
                        break;
                    case "非流动资产":
                        subject.setSubjectType(7);
                        break;
                    case "流动负债":
                        subject.setSubjectType(8);
                        break;
                    case "非流动负债":
                        subject.setSubjectType(9);
                        break;
                    case "所有者权益":
                        subject.setSubjectType(10);
                        break;
                    case "成本":
                        subject.setSubjectType(11);
                        break;
                    case "营业收入":
                        subject.setSubjectType(12);
                        break;
                    case "其他收益":
                        subject.setSubjectType(13);
                        break;
                    case "期间费用":
                        subject.setSubjectType(14);
                        break;
                    case "其他损失":
                        subject.setSubjectType(15);
                        break;
                    case "营业成本及税金":
                        subject.setSubjectType(16);
                        break;
                    case "以前年度损益调整":
                        subject.setSubjectType(17);
                        break;
                    case "所得税":
                        subject.setSubjectType(18);
                        break;
                }
            }
            
            // 转换余额方向：中文名称 -> 数字代码
            if (subject.getDirectionName() != null) {
                switch (subject.getDirectionName().trim()) {
                    case "借":
                        subject.setDirection(1);
                        break;
                    case "贷":
                        subject.setDirection(2);
                        break;
                }
            }else {
                subject.setDirection(2);
            }
            // 转换状态：中文名称 -> 数字代码
            if (subject.getStatusName() != null) {
                switch (subject.getStatusName().trim()) {
                    case "启用":
                        subject.setStatus(1);
                        break;
                    case "停用":
                        subject.setStatus(0);
                        break;
                }
            }else {
                subject.setStatus(1);
            }

            //是否调汇
            if (subject.getExchangeName() != null) {
                switch (subject.getExchangeName().trim()) {
                    case "是":
                        subject.setIsExchange(true);
                        break;
                    case "否":
                        subject.setIsExchange(false);
                        break;
                }
            }else {
                subject.setIsExchange(false);
            }

            // 转换是否现金科目：中文名称 -> 数字代码
            if (subject.getIsCashName() != null) {
                switch (subject.getIsCashName().trim()) {
                    case "是":
                        subject.setCash(true);
                        break;
                    case "否":
                        subject.setCash(false);
                        break;
                }
            }else {
                subject.setCash(false);
            }
            
            // 转换是否平行科目：中文名称 -> 数字代码
            if (subject.getIsParallelName() != null) {
                switch (subject.getIsParallelName().trim()) {
                    case "是":
                        subject.setIsParallel(true);
                        break;
                    case "否":
                        subject.setIsParallel(false);
                        break;
                }
            }else {
                subject.setIsParallel(false);
            }
            
            // 根据 subjectCode 计算 subjectLevel 和 parentCode
            if (subject.getSubjectCode() != null) {
                String subjectCode = subject.getSubjectCode().trim();
                int codeLength = subjectCode.length();
                
                // 计算 subjectLevel
                int subjectLevel = 0;
                if (codeLength == 4) {
                    subjectLevel = 1;
                } else if (codeLength == 6) {
                    subjectLevel = 2;
                } else if (codeLength == 8) {
                    subjectLevel = 3;
                } else if (codeLength == 10) {
                    subjectLevel = 4;
                }
                subject.setSubjectLevel(subjectLevel);
                
                // 计算 parentCode
                if (subjectLevel > 1) {
                    // parentCode 是当前 code 减去最后2位字符
                    String parentCode = subjectCode.substring(0, codeLength - 2);
                    subject.setParentCode(parentCode);
                } else {
                    // 一级科目没有父级
                    subject.setParentCode("0");
                }
            }
            subject.setUpdatedTime(new Date());
            subject.setStatus(1); // 设置状态为启用
            FinAccountingSubjects existing = finAccountingSubjectsMapper.selectByTenantIdAndSubjectCode(subject.getGroupid(), subject.getSubjectCode());
            if (existing == null) {
                // 新增科目
                subject.setCreatedTime(new Date());
                count += finAccountingSubjectsMapper.insertFinAccountingSubjects(subject);
            } else {
                // 更新科目
                subject.setSubjectId(existing.getSubjectId());
                count += finAccountingSubjectsMapper.updateFinAccountingSubjects(subject);
            }
        }
        return count;
    }

    /**
     * 下载会计科目模板
     * @param response 响应对象
     * @throws IOException 异常
     */
    @Override
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        // 创建一个空的科目对象作为模板
        FinAccountingSubjects subject = new FinAccountingSubjects();
        List<FinAccountingSubjects> list = Arrays.asList(subject);
        ExcelUtil<FinAccountingSubjects> util = new ExcelUtil<FinAccountingSubjects>(FinAccountingSubjects.class);
        util.exportExcel(response, list, "科目模板");
    }
}