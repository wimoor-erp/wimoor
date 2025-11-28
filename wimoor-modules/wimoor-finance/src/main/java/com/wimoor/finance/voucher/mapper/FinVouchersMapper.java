package com.wimoor.finance.voucher.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.domain.dto.FinVoucherDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 记账凭证Mapper接口
 *
 * @author wimoor
 * @date 2025-11-04
 */
public interface FinVouchersMapper
{
    /**
     * 查询记账凭证
     *
     * @param voucherId 记账凭证主键
     * @return 记账凭证
     */
    public FinVouchers selectFinVouchersByVoucherId(Long voucherId);

    /**
     * 查询记账凭证列表
     *
     * @param finVouchers 记账凭证
     * @return 记账凭证集合
     */
    public List<FinVouchers> selectFinVouchersList(FinVoucherDTO finVouchers);

    /**
     * 新增记账凭证
     *
     * @param finVouchers 记账凭证
     * @return 结果
     */
    public int insertFinVouchers(FinVouchers finVouchers);

    /**
     * 查询下一个凭证号
     *
     * @param finVouchers 记账凭证
     * @return 下一个凭证号
     */
    public String selectNextVoucherNo(FinVouchers finVouchers);

    /**
     * 修改记账凭证
     *
     * @param finVouchers 记账凭证
     * @return 结果
     */
    public int updateFinVouchers(FinVouchers finVouchers);

    /**
     * 删除记账凭证
     *
     * @param voucherId 记账凭证主键
     * @return 结果
     */
    public int deleteFinVouchersByVoucherId(Long voucherId);

    /**
     * 批量删除记账凭证
     *
     * @param voucherIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinVouchersByVoucherIds(Long[] voucherIds);

    /**
     * 根据凭证ID查询凭证及分录
     */
    FinVouchers selectVoucherWithEntries(@Param("voucherId") Long voucherId);

    /**
     * 查询待过账的凭证列表
     */
    List<FinVouchers> selectVouchersForPosting(@Param("groupid") String groupid,
                                               @Param("voucherStatus") Integer voucherStatus);

    Integer countVouchersByPeriod(@Param("groupid") String groupid,
                                  @Param("period") String period);

    Integer countUnpostedVouchers(@Param("groupid") String groupid,
                                  @Param("period") String period);

    List<FinVouchers> selectVouchersByPeriod(@Param("groupid") String groupid,
                                             @Param("period") String period);

    List<Map<String, Object>> selectVoucherSummaryByPeriod(@Param("groupid") String groupid,
                                                           @Param("period") String period);

    /**
     * 检查损益结转凭证 - 基于科目编码的实现
     * 通过检查是否包含损益类科目（5、6开头）的结转分录来判断
     */
    Integer countProfitTransferVouchers(@Param("groupid") String groupid, @Param("period") String period);

    List<FinVouchers> selectVouchersModifiedAfterClosing(String groupid, String period, Date closeTime);

    List<FinVouchers> selectProfitTransferVouchers(String groupid, String period);
}