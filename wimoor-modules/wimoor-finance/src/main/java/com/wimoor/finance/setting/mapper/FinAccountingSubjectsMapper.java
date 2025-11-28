package com.wimoor.finance.setting.mapper;

import java.util.List;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 会计科目Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-05
 */
public interface FinAccountingSubjectsMapper 
{
    /**
     * 查询会计科目
     * 
     * @param subjectId 会计科目主键
     * @return 会计科目
     */
    public FinAccountingSubjects selectFinAccountingSubjectsBySubjectId(String subjectId);

    /**
     * 查询会计科目列表
     * 
     * @param finAccountingSubjects 会计科目
     * @return 会计科目集合
     */
    public List<FinAccountingSubjects> selectFinAccountingSubjectsList(FinAccountingSubjects finAccountingSubjects);

    /**
     * 新增会计科目
     * 
     * @param finAccountingSubjects 会计科目
     * @return 结果
     */
    public int insertFinAccountingSubjects(FinAccountingSubjects finAccountingSubjects);

    /**
     * 修改会计科目
     * 
     * @param finAccountingSubjects 会计科目
     * @return 结果
     */
    public int updateFinAccountingSubjects(FinAccountingSubjects finAccountingSubjects);

    /**
     * 删除会计科目
     * 
     * @param subjectId 会计科目主键
     * @return 结果
     */
    public int deleteFinAccountingSubjectsBySubjectId(String subjectId);

    /**
     * 批量删除会计科目
     * 
     * @param subjectIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinAccountingSubjectsBySubjectIds(String[] subjectIds);

    /**
     * 初始化会计科目
     *
     * @param groupId 集团ID
     * @return 结果
     */
    public int initFin(@Param("groupId") String groupId);


    FinAccountingSubjects selectByTenantIdAndSubjectCode(
            @Param("groupid") String groupid,
            @Param("subjectCode") String subjectCode);

    List<FinAccountingSubjects> selectByTenantIdAndStatus(
            @Param("groupid") String groupid);

    List<FinAccountingSubjects> selectByTenantIdAndSubjectCodes(
            @Param("groupid") String groupid,
            @Param("subjectCodes") List<String> subjectCodes);

    List<FinAccountingSubjects> selectByTenantIdAndSubjectCodeLike(
            @Param("groupid") String groupid,
            @Param("subjectCodePattern") String subjectCodePattern);

    List<FinAccountingSubjects> selectByTenantIdAndParentCode(
            @Param("groupid") String groupid,
            @Param("parentCode") String parentCode);

    /**
     * 根据科目编码前缀查询科目
     */
    @Select("<script>" +
            "SELECT * FROM fin_accounting_subjects " +
            "WHERE groupid = #{groupid} " +
            "AND status = 1 " + // 启用状态
            "AND (" +
            "<foreach collection='prefixes' item='prefix' separator=' OR '>" +
            "subject_code LIKE CONCAT(#{prefix}, '%')" +
            "</foreach>" +
            ") " +
            "ORDER BY subject_code" +
            "</script>")
    List<FinAccountingSubjects> selectBySubjectCodePrefixes(@Param("groupid") String groupid,
                                                            @Param("prefixes") List<String> prefixes);

    /**
     * 根据具体科目编码查询
     */
    @Select("SELECT * FROM fin_accounting_subjects " +
            "WHERE groupid = #{groupid} " +
            "AND subject_code = #{subjectCode} " +
            "AND status = 1")
    FinAccountingSubjects selectBySubjectCode(@Param("groupid") String groupid,
                                                    @Param("subjectCode") String subjectCode);

    /**
     * 根据多个科目编码查询
     */
    @Select("<script>" +
            "SELECT * FROM fin_accounting_subjects " +
            "WHERE groupid = #{groupid} " +
            "AND subject_code IN " +
            "<foreach collection='subjectCodes' item='code' open='(' separator=',' close=')'>" +
            "#{code}" +
            "</foreach>" +
            "AND status = 1 " +
            "ORDER BY subject_code" +
            "</script>")
    List<FinAccountingSubjects> selectBySubjectCodes(@Param("groupid") String groupid,
                                                     @Param("subjectCodes") List<String> subjectCodes);

    /**
     * 根据科目编码范围查询（更精确的控制）
     */
    @Select("SELECT * FROM fin_accounting_subjects " +
            "WHERE groupid = #{groupid} " +
            "AND subject_code BETWEEN #{startCode} AND #{endCode} " +
            "AND status = 1 " +
            "ORDER BY subject_code")
    List<FinAccountingSubjects> selectBySubjectCodeRange(@Param("groupid") String groupid,
                                                         @Param("startCode") String startCode,
                                                         @Param("endCode") String endCode);

}
