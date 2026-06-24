package com.wimoor.finance.ledger.service.impl;

import com.wimoor.finance.ledger.domain.dto.SubjectBalanceDTO;
import com.wimoor.finance.ledger.domain.dto.SubjectBalanceQueryDTO;
import com.wimoor.finance.ledger.domain.dto.SubjectBalanceTableDTO;
import com.wimoor.finance.ledger.mapper.FinDetailLedgerMapper;
import com.wimoor.finance.ledger.service.ISubjectBalanceReportService;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.mapper.FinAccountingSubjectsMapper;
import com.wimoor.finance.setting.mapper.FinSubjectTypeMapper;
import com.wimoor.finance.util.QueryParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubjectBalanceReportServiceImpl implements ISubjectBalanceReportService {

    @Autowired
    private FinDetailLedgerMapper finDetailLedgerMapper;

    @Autowired
    private FinAccountingSubjectsMapper finAccountingSubjectsMapper;

    @Autowired
    private FinSubjectTypeMapper finSubjectTypeMapper;

    private static final DateTimeFormatter PERIOD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    @Override
    public List<SubjectBalanceTableDTO> querySubjectBalanceTree(SubjectBalanceQueryDTO queryDTO) {
        // 解析期间（支持多期间查询）
        LocalDate[] dateRange = QueryParamUtil.parsePeriodRange(queryDTO.getBeginPeriod(), queryDTO.getEndPeriod());
        LocalDate startDate = dateRange[0];
        LocalDate endDate = dateRange[1];

        // 获取所有科目信息
        List<FinAccountingSubjects> subjects = finAccountingSubjectsMapper.selectByTenantIdAndStatus(queryDTO.getGroupid());
        
        // 过滤科目
        if (queryDTO.getSubjectCode() != null && !queryDTO.getSubjectCode().isEmpty()) {
            subjects = subjects.stream()
                    .filter(s -> s.getSubjectCode().contains(queryDTO.getSubjectCode()))
                    .collect(Collectors.toList());
        }
        if (queryDTO.getSubjectName() != null && !queryDTO.getSubjectName().isEmpty()) {
            subjects = subjects.stream()
                    .filter(s -> s.getSubjectName().contains(queryDTO.getSubjectName()))
                    .collect(Collectors.toList());
        }
        if (queryDTO.getSubjectType() != null) {
            subjects = subjects.stream()
                    .filter(s -> queryDTO.getSubjectType().equals(s.getSubjectType()))
                    .collect(Collectors.toList());
        }
        if (queryDTO.getLeafOnly() != null && queryDTO.getLeafOnly()) {
            subjects = subjects.stream()
                    .filter(s -> s.getIsLeaf() != null && s.getIsLeaf() == 1)
                    .collect(Collectors.toList());
        }

        // 获取科目余额
        List<String> subjectCodes = subjects.stream()
                .map(FinAccountingSubjects::getSubjectCode)
                .collect(Collectors.toList());

          Map<String,Object> params=new HashMap<String,Object>();
        params.put("groupid", queryDTO.getGroupid());
        params.put("subjectCodes", subjectCodes);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        List<SubjectBalanceDTO> balanceDTOs = finDetailLedgerMapper.selectSubjectBalances(
                params);

        // 转换为结果DTO，过滤掉subjectCode为null的记录
        Map<String, SubjectBalanceDTO> balanceMap = balanceDTOs.stream()
                .filter(dto -> dto.getSubjectCode() != null && !dto.getSubjectCode().isEmpty())
                .collect(Collectors.toMap(SubjectBalanceDTO::getSubjectCode, dto -> dto));

        // 转换为表格DTO
        List<SubjectBalanceTableDTO> list = subjects.stream()
                .map(subject -> convertToTableDTO(subject, balanceMap.get(subject.getSubjectCode()), startDate))
                .collect(Collectors.toList());
     
        // 构建树形结构
        Map<String, SubjectBalanceTableDTO> nodeMap = new HashMap<>();
        List<SubjectBalanceTableDTO> rootList = new ArrayList<>();

        // 先将所有节点放入map
        for (SubjectBalanceTableDTO node : list) {
            nodeMap.put(node.getSubjectCode(), node);
            node.setChildren(new ArrayList<>());
        }

        // 构建父子关系
        for (SubjectBalanceTableDTO node : list) {
            if (node.getParentCode() != null && !node.getParentCode().isEmpty() && nodeMap.containsKey(node.getParentCode())) {
                SubjectBalanceTableDTO parent = nodeMap.get(node.getParentCode());
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(node);
            } else {
                rootList.add(node);
            }
        }

        // 汇总子科目余额到父科目
        for (SubjectBalanceTableDTO node : rootList) {
            accumulateChildrenBalance(node);
        }

        // 按科目编码排序
        rootList.sort(Comparator.comparing(SubjectBalanceTableDTO::getSubjectCode));
        sortChildren(rootList);

        // 过滤掉整个子树都没有数据的科目（从父到子都为0的不展示）
        if(queryDTO.getIsFilterZero()){
            rootList = filterEmptySubtree(rootList);
        }
        return rootList;
    }
    
    /**
     * 过滤掉整个子树都没有数据的科目
     * 
     * @param nodes 节点列表
     * @return 过滤后的节点列表
     */
    private List<SubjectBalanceTableDTO> filterEmptySubtree(List<SubjectBalanceTableDTO> nodes) {
        List<SubjectBalanceTableDTO> filtered = new ArrayList<>();
        
        for (SubjectBalanceTableDTO node : nodes) {
            // 递归过滤子节点
            if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                node.setChildren(filterEmptySubtree(node.getChildren()));
            }
            
            // 如果当前节点有数据，或者有非空的子节点，则保留
            if (hasData(node) || (node.getChildren() != null && !node.getChildren().isEmpty())) {
                filtered.add(node);
            }
        }
        
        return filtered;
    }
    
    /**
     * 判断节点是否有数据（余额不为0）
     * 
     * @param node 节点
     * @return 是否有数据
     */
    private boolean hasData(SubjectBalanceTableDTO node) {
        return (node.getBeginDebit() != null && node.getBeginDebit().compareTo(BigDecimal.ZERO) != 0) ||
               (node.getBeginCredit() != null && node.getBeginCredit().compareTo(BigDecimal.ZERO) != 0) ||
               (node.getDebitAmount() != null && node.getDebitAmount().compareTo(BigDecimal.ZERO) != 0) ||
               (node.getCreditAmount() != null && node.getCreditAmount().compareTo(BigDecimal.ZERO) != 0) ||
               (node.getEndDebit() != null && node.getEndDebit().compareTo(BigDecimal.ZERO) != 0) ||
               (node.getEndCredit() != null && node.getEndCredit().compareTo(BigDecimal.ZERO) != 0);
    }

    /**
     * 递归汇总子科目余额到父科目
     * 
     * @param node 父科目节点
     */
    private void accumulateChildrenBalance(SubjectBalanceTableDTO node) {
        if (node.getChildren() == null || node.getChildren().isEmpty()) {
            return;
        }
        
        // 父科目自身的净额（借方为正，贷方为负）
        BigDecimal beginNet = (node.getBeginDebit() != null ? node.getBeginDebit() : BigDecimal.ZERO)
                .subtract(node.getBeginCredit() != null ? node.getBeginCredit() : BigDecimal.ZERO);
        BigDecimal endNet = (node.getEndDebit() != null ? node.getEndDebit() : BigDecimal.ZERO)
                .subtract(node.getEndCredit() != null ? node.getEndCredit() : BigDecimal.ZERO);
        
        // 发生额可以同时有借方和贷方，直接累加
        BigDecimal debitAmount = node.getDebitAmount() != null ? node.getDebitAmount() : BigDecimal.ZERO;
        BigDecimal creditAmount = node.getCreditAmount() != null ? node.getCreditAmount() : BigDecimal.ZERO;
        BigDecimal yearDebitAmount = node.getYearDebitAmount() != null ? node.getYearDebitAmount() : BigDecimal.ZERO;
        BigDecimal yearCreditAmount = node.getYearCreditAmount() != null ? node.getYearCreditAmount() : BigDecimal.ZERO;
        
        for (SubjectBalanceTableDTO child : node.getChildren()) {
            // 先递归处理子节点的子节点
            accumulateChildrenBalance(child);
            
            // 累加子科目的发生额
            debitAmount = debitAmount.add(child.getDebitAmount() != null ? child.getDebitAmount() : BigDecimal.ZERO);
            creditAmount = creditAmount.add(child.getCreditAmount() != null ? child.getCreditAmount() : BigDecimal.ZERO);
            yearDebitAmount = yearDebitAmount.add(child.getYearDebitAmount() != null ? child.getYearDebitAmount() : BigDecimal.ZERO);
            yearCreditAmount = yearCreditAmount.add(child.getYearCreditAmount() != null ? child.getYearCreditAmount() : BigDecimal.ZERO);
            
            // 累加子科目的余额净额（借方为正，贷方为负）
            BigDecimal childBeginNet = (child.getBeginDebit() != null ? child.getBeginDebit() : BigDecimal.ZERO)
                    .subtract(child.getBeginCredit() != null ? child.getBeginCredit() : BigDecimal.ZERO);
            beginNet = beginNet.add(childBeginNet);
            
            BigDecimal childEndNet = (child.getEndDebit() != null ? child.getEndDebit() : BigDecimal.ZERO)
                    .subtract(child.getEndCredit() != null ? child.getEndCredit() : BigDecimal.ZERO);
            endNet = endNet.add(childEndNet);
        }
        
        // 期初余额：净额为正显示在借方，为负显示在贷方，余额只能在一个方向
        if (beginNet.compareTo(BigDecimal.ZERO) >= 0) {
            node.setBeginDebit(beginNet);
            node.setBeginCredit(BigDecimal.ZERO);
        } else {
            node.setBeginDebit(BigDecimal.ZERO);
            node.setBeginCredit(beginNet.negate());
        }
        
        // 发生额可以同时有借方和贷方
        node.setDebitAmount(debitAmount);
        node.setCreditAmount(creditAmount);
        node.setYearDebitAmount(yearDebitAmount);
        node.setYearCreditAmount(yearCreditAmount);
        
        // 期末余额：净额为正显示在借方，为负显示在贷方，余额只能在一个方向
        if (endNet.compareTo(BigDecimal.ZERO) >= 0) {
            node.setEndDebit(endNet);
            node.setEndCredit(BigDecimal.ZERO);
        } else {
            node.setEndDebit(BigDecimal.ZERO);
            node.setEndCredit(endNet.negate());
        }
    }

    private void sortChildren(List<SubjectBalanceTableDTO> list) {
        for (SubjectBalanceTableDTO node : list) {
            if (node.getChildren() != null && !node.getChildren().isEmpty()) {
                node.getChildren().sort(Comparator.comparing(SubjectBalanceTableDTO::getSubjectCode));
                sortChildren(node.getChildren());
            }
        }
    }

    @Override
    public Map<String, Object> getBalanceSummary(String groupid, String period) {
        LocalDate[] dateRange = parsePeriod(period); // 保持兼容旧版
        
        Map<String, Object> summary = new HashMap<>();
        
        // 获取所有科目信息
        List<FinAccountingSubjects> subjects = finAccountingSubjectsMapper.selectByTenantIdAndStatus(groupid);
        List<String> subjectCodes = subjects.stream()
                .map(FinAccountingSubjects::getSubjectCode)
                .collect(Collectors.toList());
        
        // 查询所有科目的余额数据
        List<SubjectBalanceDTO> balanceDTOs = finDetailLedgerMapper.selectSubjectBalances(
                Map.of("groupid", groupid, "subjectCodes", subjectCodes, "startDate", dateRange[0], "endDate", dateRange[1]));
        
        BigDecimal totalBeginDebit = BigDecimal.ZERO;
        BigDecimal totalBeginCredit = BigDecimal.ZERO;
        BigDecimal totalDebitAmount = BigDecimal.ZERO;
        BigDecimal totalCreditAmount = BigDecimal.ZERO;
        BigDecimal totalEndDebit = BigDecimal.ZERO;
        BigDecimal totalEndCredit = BigDecimal.ZERO;
        
        for (SubjectBalanceDTO dto : balanceDTOs) {
            // 期初余额
            if (dto.getBeginBalance() != null && dto.getBeginBalance().compareTo(BigDecimal.ZERO) != 0) {
                if (dto.getBeginDirection() == null || dto.getBeginDirection() == 1) {
                    totalBeginDebit = totalBeginDebit.add(dto.getBeginBalance());
                } else {
                    totalBeginCredit = totalBeginCredit.add(dto.getBeginBalance());
                }
            }
            
            // 本期发生额
            totalDebitAmount = totalDebitAmount.add(dto.getDebitTotal() != null ? dto.getDebitTotal() : BigDecimal.ZERO);
            totalCreditAmount = totalCreditAmount.add(dto.getCreditTotal() != null ? dto.getCreditTotal() : BigDecimal.ZERO);
            
            // 期末余额（使用带符号的期末余额，由SQL公式计算）
            BigDecimal endBalanceSigned = dto.getEndBalanceSigned();
            if (endBalanceSigned != null && endBalanceSigned.compareTo(BigDecimal.ZERO) != 0) {
                if (endBalanceSigned.compareTo(BigDecimal.ZERO) > 0) {
                    totalEndDebit = totalEndDebit.add(endBalanceSigned);
                } else {
                    totalEndCredit = totalEndCredit.add(endBalanceSigned.abs());
                }
            }
        }
        
        summary.put("totalBeginDebit", totalBeginDebit);
        summary.put("totalBeginCredit", totalBeginCredit);
        summary.put("totalDebitAmount", totalDebitAmount);
        summary.put("totalCreditAmount", totalCreditAmount);
        summary.put("totalEndDebit", totalEndDebit);
        summary.put("totalEndCredit", totalEndCredit);
        summary.put("period", period);
        
        return summary;
    }

    @Override
    public List<Map<String, Object>> exportSubjectBalance(SubjectBalanceQueryDTO queryDTO) {
        List<SubjectBalanceTableDTO> list = querySubjectBalanceTree(queryDTO);
        
        return list.stream()
                .map(dto -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("subjectCode", dto.getSubjectCode());
                    map.put("subjectName", dto.getSubjectName());
                    map.put("beginDebit", dto.getBeginDebit());
                    map.put("beginCredit", dto.getBeginCredit());
                    map.put("debitAmount", dto.getDebitAmount());
                    map.put("creditAmount", dto.getCreditAmount());
                    map.put("endDebit", dto.getEndDebit());
                    map.put("endCredit", dto.getEndCredit());
                    return map;
                })
                .collect(Collectors.toList());
    }

    private SubjectBalanceTableDTO convertToTableDTO(FinAccountingSubjects subject, SubjectBalanceDTO balance, LocalDate startDate) {
        SubjectBalanceTableDTO dto = new SubjectBalanceTableDTO();
        
        dto.setSubjectCode(subject.getSubjectCode());
        dto.setSubjectName(subject.getSubjectName());
        dto.setSubjectType(subject.getSubjectType());
        // 直接使用SQL查询返回的科目类别名称
        dto.setDirection(subject.getDirection());
        dto.setLevel(subject.getSubjectLevel());
        dto.setSubjectLevel(subject.getSubjectLevel());
        dto.setParentCode(subject.getParentCode());
        dto.setIsLeaf(subject.getIsLeaf() != null && subject.getIsLeaf() == 1);

        BigDecimal beginDebit = BigDecimal.ZERO;
        BigDecimal beginCredit = BigDecimal.ZERO;
        BigDecimal debitAmount = BigDecimal.ZERO;
        BigDecimal creditAmount = BigDecimal.ZERO;
        BigDecimal endDebit = BigDecimal.ZERO;
        BigDecimal endCredit = BigDecimal.ZERO;
        BigDecimal beginBalance = BigDecimal.ZERO;
        BigDecimal endBalance = BigDecimal.ZERO;
        Integer beginDirection = subject.getDirection();
        Integer endDirection = subject.getDirection();

        if (balance != null) {
            debitAmount = balance.getDebitTotal() != null ? balance.getDebitTotal() : BigDecimal.ZERO;
            creditAmount = balance.getCreditTotal() != null ? balance.getCreditTotal() : BigDecimal.ZERO;
            dto.setSubjectTypeName(balance.getSubjectTypeName());
            // 处理期初余额
            if (balance.getBeginBalance() != null && balance.getBeginBalance().compareTo(BigDecimal.ZERO) != 0) {
                beginBalance = balance.getBeginBalance();
                beginDirection = balance.getBeginDirection() != null ? balance.getBeginDirection() : subject.getDirection();
                if (beginDirection == null || beginDirection == 1) {
                    beginDebit = balance.getBeginBalance();
                } else {
                    beginCredit = balance.getBeginBalance();
                }
            }

            // 处理期末余额（使用带符号的期末余额，由SQL公式计算：期初+借方-贷方）
            BigDecimal endBalanceSigned = balance.getEndBalanceSigned();
            if (endBalanceSigned != null && endBalanceSigned.compareTo(BigDecimal.ZERO) != 0) {
                endBalance = endBalanceSigned.abs();
                if (endBalanceSigned.compareTo(BigDecimal.ZERO) > 0) {
                    endDirection = 1; // 借方
                    endDebit = endBalance;
                } else {
                    endDirection = 2; // 贷方
                    endCredit = endBalance;
                }
            }
        }

        dto.setBeginDebit(beginDebit);
        dto.setBeginCredit(beginCredit);
        dto.setBeginBalance(beginBalance);
        dto.setBeginDirection(beginDirection);
        dto.setDebitAmount(debitAmount);
        dto.setCreditAmount(creditAmount);
        dto.setEndDebit(endDebit);
        dto.setEndCredit(endCredit);
        dto.setEndBalance(endBalance);
        dto.setEndDirection(endDirection);

        // 设置本年累计发生额
        BigDecimal yearDebitAmount = balance != null && balance.getYearDebitTotal() != null ? balance.getYearDebitTotal() : BigDecimal.ZERO;
        BigDecimal yearCreditAmount = balance != null && balance.getYearCreditTotal() != null ? balance.getYearCreditTotal() : BigDecimal.ZERO;
        dto.setYearDebitAmount(yearDebitAmount);
        dto.setYearCreditAmount(yearCreditAmount);

        return dto;
    }

    private LocalDate[] parsePeriod(String period) {
        if (period == null || period.length() != 6) {
            throw new IllegalArgumentException("期间格式错误，应为YYYYMM: " + period);
        }

        int year = Integer.parseInt(period.substring(0, 4));
        int month = Integer.parseInt(period.substring(4, 6));

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return new LocalDate[]{startDate, endDate};
    }

}
