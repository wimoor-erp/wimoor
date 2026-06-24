package com.wimoor.finance.ledger.service;

import com.wimoor.finance.ledger.domain.dto.SubjectBalanceQueryDTO;
import com.wimoor.finance.ledger.domain.dto.SubjectBalanceTableDTO;

import java.util.List;
import java.util.Map;

/**
 * 科目余额表服务接口
 */
public interface ISubjectBalanceReportService {
    
    /**
     * 查询科目余额表（树形结构）
     *
     * @param queryDTO 查询条件
     * @return 树形结构科目余额
     */
    List<SubjectBalanceTableDTO> querySubjectBalanceTree(SubjectBalanceQueryDTO queryDTO);
    
    /**
     * 获取科目余额汇总信息
     *
     * @param groupid 集团ID
     * @param period  期间
     * @return 汇总统计
     */
    Map<String, Object> getBalanceSummary(String groupid, String period);
    
    /**
     * 导出科目余额表数据
     *
     * @param queryDTO 查询条件
     * @return Excel数据
     */
    List<Map<String, Object>> exportSubjectBalance(SubjectBalanceQueryDTO queryDTO);
}