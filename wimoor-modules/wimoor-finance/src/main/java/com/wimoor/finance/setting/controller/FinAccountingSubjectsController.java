package com.wimoor.finance.setting.controller;

import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * 会计科目Controller
 * 
 * @author wimoor
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/subjects")
public class FinAccountingSubjectsController extends BaseController
{
    @Autowired
    private IFinAccountingSubjectsService finAccountingSubjectsService;
 /**
     * 查询会计科目列表并将列表转换成树形结构
     */
    @GetMapping("/list")
    public Result list(FinAccountingSubjects finAccountingSubjects)
    {
        if(finAccountingSubjects.getSubjectType() != null){
            if(finAccountingSubjects.getSubjectType()==1){
                finAccountingSubjects.setSubjectTypeCode("ASSET");
            }else if(finAccountingSubjects.getSubjectType()==2){
                finAccountingSubjects.setSubjectTypeCode("LIAB");
            }else if(finAccountingSubjects.getSubjectType()==3){
                finAccountingSubjects.setSubjectTypeCode("EQUITY");
            }else if(finAccountingSubjects.getSubjectType()==4){
                finAccountingSubjects.setSubjectTypeCode("COST");
            }else if(finAccountingSubjects.getSubjectType()==5){
                finAccountingSubjects.setSubjectTypeCode("PL");
            }
        }
        List<FinAccountingSubjects> list = finAccountingSubjectsService.selectFinAccountingSubjectsList(finAccountingSubjects);
        // 构建树形结构
        List<FinAccountingSubjects> treeList = buildTree(list);
        return success(treeList);
    }
    
    /**
     * 构建树形结构
     * @param list 科目列表
     * @return 树形结构列表
     */
    private List<FinAccountingSubjects> buildTree(List<FinAccountingSubjects> list) {
        // 构建科目编码到科目的映射
        java.util.Map<String, FinAccountingSubjects> codeMap = new java.util.HashMap<>();
        for (FinAccountingSubjects subject : list) {
            codeMap.put(subject.getSubjectCode(), subject);
        }
        
        // 构建树形结构
        java.util.List<FinAccountingSubjects> treeList = new java.util.ArrayList<>();
        for (FinAccountingSubjects subject : list) {
            String parentCode = subject.getParentCode();
            if (parentCode == null || parentCode.isEmpty() || "0".equals(parentCode)) {
                // 顶级科目
                treeList.add(subject);
            } else {
                // 子科目
                FinAccountingSubjects parent = codeMap.get(parentCode);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                    parent.setChildren(new java.util.ArrayList<FinAccountingSubjects>());
                }
                    parent.setHasChildren(true);
                ((java.util.List<FinAccountingSubjects>) parent.getChildren()).add(subject);
                }
            }
        }
        
        return treeList;
    }
    /**
     * 查询会计科目列表并将列表中的名称采用树形结构展示但是列表结构本身不变
     */
    @GetMapping("/listAll")
    public Result listAll(FinAccountingSubjects finAccountingSubjects)
    {
        List<FinAccountingSubjects> list = finAccountingSubjectsService.selectFinAccountingSubjectsListAll(finAccountingSubjects);

        return success(list);
    }
    

    /**
     * 导出会计科目列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinAccountingSubjects finAccountingSubjects)
    {
        List<FinAccountingSubjects> list = finAccountingSubjectsService.selectFinAccountingSubjectsList(finAccountingSubjects);
        
        // 反向转换：将数字代码转换为中文名称
        for (FinAccountingSubjects subject : list) {
            // 转换类别：优先使用categories字段转换为子类别名称
            if (subject.getSubjectType() != null) {
                switch (subject.getSubjectType()) {
                    case 6:
                        subject.setSubjectTypeName("流动资产");
                        break;
                    case 7:
                        subject.setSubjectTypeName("非流动资产");
                        break;
                    case 8:
                        subject.setSubjectTypeName("流动负债");
                        break;
                    case 9:
                        subject.setSubjectTypeName("非流动负债");
                        break;
                    case 10:
                        subject.setSubjectTypeName("所有者权益");
                        break;
                    case 11:
                        subject.setSubjectTypeName("成本");
                        break;
                    case 12:
                        subject.setSubjectTypeName("营业收入");
                        break;
                    case 13:
                        subject.setSubjectTypeName("其他收益");
                        break;
                    case 14:
                        subject.setSubjectTypeName("期间费用");
                        break;
                    case 15:
                        subject.setSubjectTypeName("其他损失");
                        break;
                    case 16:
                        subject.setSubjectTypeName("营业成本及税金");
                        break;
                    case 17:
                        subject.setSubjectTypeName("以前年度损益调整");
                        break;
                    case 18:
                        subject.setSubjectTypeName("所得税");
                        break;
                }
            }
            
            // 转换余额方向：数字代码 -> 中文名称
            if (subject.getDirection() != null) {
                switch (subject.getDirection()) {
                    case 1:
                        subject.setDirectionName("借");
                        break;
                    case 2:
                        subject.setDirectionName("贷");
                        break;
                }
            }
            //是否调汇
            if (subject.getIsExchange() != null) {
                subject.setExchangeName(subject.getIsExchange() ? "是" : "否");
            }
            // 转换状态：数字代码 -> 中文名称
            if (subject.getStatus() != null) {
                switch (subject.getStatus()) {
                    case 1:
                        subject.setStatusName("启用");
                        break;
                    case 0:
                        subject.setStatusName("停用");
                        break;
                }
            }
            // 转换是否现金科目：数字代码 -> 中文名称
            if (subject.getCash() != null) {
                subject.setIsCashName(subject.getCash() ? "是" : "否");
            }
            
            // 转换是否平行科目：数字代码 -> 中文名称
            if (subject.getIsParallel() != null) {
                subject.setIsParallelName(subject.getIsParallel() ? "是" : "否");
            }
        }
        
        ExcelUtil<FinAccountingSubjects> util = new ExcelUtil<FinAccountingSubjects>(FinAccountingSubjects.class);
        util.exportExcel(response, list, "会计科目数据");
    }

    /**
     * 获取会计科目详细信息
     */
    @GetMapping(value = "/{subjectId}")
    public Result getInfo(@PathVariable("subjectId") String subjectId)
    {
        return success(finAccountingSubjectsService.selectFinAccountingSubjectsBySubjectId(subjectId));
    }

    /**
     * 新增会计科目
     */
    @PostMapping
    public Result add(@RequestBody FinAccountingSubjects finAccountingSubjects)
    {
        if(finAccountingSubjects.getSubjectTypeId() != null){
            finAccountingSubjects.setSubjectType(finAccountingSubjects.getSubjectTypeId());
        }
        finAccountingSubjects.setCreatedTime(new Date());
        finAccountingSubjects.setUpdatedTime(new Date());
        return toResult(finAccountingSubjectsService.insertFinAccountingSubjects(finAccountingSubjects));
    }

    /**
     * 修改会计科目
     */
    @PutMapping
    public Result edit(@RequestBody FinAccountingSubjects finAccountingSubjects)
    {
        if(finAccountingSubjects.getSubjectTypeId() != null){
            finAccountingSubjects.setSubjectType(finAccountingSubjects.getSubjectTypeId());
        }
        finAccountingSubjects.setUpdatedTime(new Date());
        return toResult(finAccountingSubjectsService.updateFinAccountingSubjects(finAccountingSubjects));
    }

    /**
     * 删除会计科目
     */
	@DeleteMapping("/{subjectIds}")
    public Result remove(@PathVariable String[] subjectIds)
    {
        return toResult(finAccountingSubjectsService.deleteFinAccountingSubjectsBySubjectIds(subjectIds));
    }

     /**
      * 初始化会计科目
      *
      * @param groupId 集团ID
      * @return 结果
      */
    @GetMapping("/init/{groupId}")
    public Result init(@PathVariable("groupId") String groupId)
    {   finAccountingSubjectsService.initFinAccountingSubjects(groupId);
        return success();
    }

    /**
     * 上传会计科目文件
     */
    @PostMapping("/upload/{groupId}")
    public Result upload(@PathVariable("groupId") String groupId, MultipartFile file)
    {
        try (InputStream inputStream = file.getInputStream()) {
            int result = finAccountingSubjectsService.importSubjects(inputStream, groupId);
            return success("上传成功，导入" + result + "条数据");
        } catch (IOException e) {
            return error("上传失败：" + e.getMessage());
        }
    }

    /**
     * 下载会计科目模板
     */
    @PostMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response)
    {
        try {
            finAccountingSubjectsService.downloadTemplate(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
