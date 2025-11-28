package com.wimoor.finance.ledger.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.wimoor.finance.ledger.domain.FinGeneralLedger;
import com.wimoor.finance.ledger.domain.dto.FinLedgerDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 总账Mapper接口
 *
 * @author wimoor
 * @date 2025-11-04
 */
public interface FinGeneralLedgerMapper
{
    /**
     * 查询总账
     *
     * @param ledgerId 总账主键
     * @return 总账
     */
    public FinGeneralLedger selectFinGeneralLedgerByLedgerId(Long ledgerId);

    /**
     * 查询总账列表
     *
     * @param finGeneralLedger 总账
     * @return 总账集合
     */
    public List<FinGeneralLedger> selectFinGeneralLedgerList(FinGeneralLedger finGeneralLedger);
    /**
     * 查询明细账表列表
     *
     * @param finDetailLedger 明细账表
     * @return 明细账表集合
     */
    public List<Map<String,Object>> selectList(FinLedgerDTO finDetailLedger);
    /**
     * 新增总账
     *
     * @param finGeneralLedger 总账
     * @return 结果
     */
    public int insertFinGeneralLedger(FinGeneralLedger finGeneralLedger);

    /**
     * 修改总账
     *
     * @param finGeneralLedger 总账
     * @return 结果
     */
    public int updateFinGeneralLedger(FinGeneralLedger finGeneralLedger);

    /**
     * 删除总账
     *
     * @param ledgerId 总账主键
     * @return 结果
     */
    public int deleteFinGeneralLedgerByLedgerId(Long ledgerId);

    /**
     * 批量删除总账
     *
     * @param ledgerIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinGeneralLedgerByLedgerIds(Long[] ledgerIds);

    /**
     * 根据科目和期间查询总账
     */
    FinGeneralLedger selectBySubjectAndPeriod(@Param("groupid") String groupid,
                                              @Param("subjectId") String subjectId,
                                              @Param("period") String period);

    /**
     * 根据科目代码和期间查询总账
     */
    FinGeneralLedger selectBySubjectCodeAndPeriod(@Param("groupid") String groupid,
                                                  @Param("subjectCode") String subjectCode,
                                                  @Param("period") String period);

    /**
     * 根据期间查询总账列表
     */
    List<FinGeneralLedger> selectByPeriod(@Param("groupid") String groupid,
                                          @Param("period") String period);

    int deleteByPeriod(@Param("groupid") String groupid,
                       @Param("period") String period);

    List<Map<String, Object>> selectBalanceSummaryByType(@Param("groupid") String groupid,
                                                         @Param("period") String period);

    List<Map<String, Object>> getAllSubjectBalance(@Param("groupid") String groupid,
                                                    @Param("period") String period);

}
