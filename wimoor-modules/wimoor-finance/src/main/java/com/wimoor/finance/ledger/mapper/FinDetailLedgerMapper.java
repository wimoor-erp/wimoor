package com.wimoor.finance.ledger.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wimoor.finance.ledger.domain.FinDetailLedger;
import com.wimoor.finance.ledger.domain.dto.FinLedgerDTO;
import com.wimoor.finance.ledger.domain.dto.SubjectBalanceDTO;
import org.apache.ibatis.annotations.Param;

/**
 * 明细账表Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public interface FinDetailLedgerMapper 
{
    /**
     * 查询明细账表
     * 
     * @param detailId 明细账表主键
     * @return 明细账表
     */
    public FinDetailLedger selectFinDetailLedgerByDetailId(Long detailId);

    /**
     * 查询明细账表列表
     * 
     * @param finDetailLedger 明细账表
     * @return 明细账表集合
     */
    public List<FinDetailLedger> selectFinDetailLedgerList(FinDetailLedger finDetailLedger);


    /**
     * 查询明细账表列表
     *
     * @param finDetailLedger 明细账表
     * @return 明细账表集合
     */
    public List<Map<String,Object>> selectList(FinLedgerDTO finDetailLedger);

    /**
     * 新增明细账表
     * 
     * @param finDetailLedger 明细账表
     * @return 结果
     */
    public int insertFinDetailLedger(FinDetailLedger finDetailLedger);

    /**
     * 修改明细账表
     * 
     * @param finDetailLedger 明细账表
     * @return 结果
     */
    public int updateFinDetailLedger(FinDetailLedger finDetailLedger);

    /**
     * 删除明细账表
     * 
     * @param detailId 明细账表主键
     * @return 结果
     */
    public int deleteFinDetailLedgerByDetailId(Long detailId);

    /**
     * 批量删除明细账表
     * 
     * @param detailIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinDetailLedgerByDetailIds(Long[] detailIds);

    /**
     * 查询某个科目的最后一条明细账记录
     */
    FinDetailLedger selectLastDetailLedger(@Param("groupid") String groupid,
                                           @Param("subjectId") String subjectId,
                                           @Param("voucherDate") Date voucherDate);

    /**
     * 根据凭证ID删除明细账记录
     */
    int deleteByVoucherId(@Param("voucherId") Long voucherId);

    // 获取指定期间内科目的明细记录
    List<FinDetailLedger> selectBySubjectAndPeriod(
            @Param("groupid") String groupid,
            @Param("subjectCode") String subjectCode,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // 获取指定期间内多个科目的明细记录
    List<FinDetailLedger> selectBySubjectCodesAndPeriod(
            @Param("groupid") String groupid,
            @Param("subjectCodes") List<String> subjectCodes,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // 获取指定科目的期初余额（上期最后一条记录的余额）
    List<FinDetailLedger> selectLatestBalanceBeforeDate(
            @Param("groupid") String groupid,
            @Param("subjectCode") String subjectCode,
            @Param("startDate") LocalDate startDate);

    // 计算指定期间内科目的借方发生额合计
    BigDecimal sumDebitAmountBySubjectAndPeriod(
            @Param("groupid") String groupid,
            @Param("subjectCode") String subjectCode,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // 计算指定期间内科目的贷方发生额合计
    BigDecimal sumCreditAmountBySubjectAndPeriod(
            @Param("groupid") String groupid,
            @Param("subjectCode") String subjectCode,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // 获取指定科目在指定日期的最后余额
    List<FinDetailLedger> selectLatestBalanceByDate(
            @Param("groupid") String groupid,
            @Param("subjectCode") String subjectCode,
            @Param("endDate") LocalDate endDate);

    // 批量计算多个科目的余额信息
    List<SubjectBalanceDTO> selectSubjectBalances(
            @Param("groupid") String groupid,
            @Param("subjectCodes") List<String> subjectCodes,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    // 计算科目组合的余额（支持科目编码模式匹配）
    BigDecimal calculateSubjectGroupBalance(
            @Param("groupid") String groupid,
            @Param("subjectCodePattern") String subjectCodePattern,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("amountType") String amountType);


    List<FinDetailLedger> selectByVoucherId(Long voucherId);

}
