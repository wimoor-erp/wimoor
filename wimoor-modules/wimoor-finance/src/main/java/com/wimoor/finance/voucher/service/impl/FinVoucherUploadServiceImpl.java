package com.wimoor.finance.voucher.service.impl;

import com.wimoor.common.mvc.BizException;
import com.wimoor.finance.api.AmazonClientOneFeignManager;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.finance.voucher.domain.FinVoucherEntries;
import com.wimoor.finance.voucher.domain.FinVoucherUpload;
import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.domain.dto.FinVoucherDTO;
import com.wimoor.finance.voucher.mapper.FinVoucherUploadMapper;
import com.wimoor.finance.voucher.service.IFinVoucherUploadService;
import com.wimoor.finance.voucher.service.IFinVouchersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 凭证分录明细，存储凭证的每一笔分录信息Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-28
 */
@Service
public class FinVoucherUploadServiceImpl implements IFinVoucherUploadService 
{
    @Autowired
    private FinVoucherUploadMapper finVoucherUploadMapper;
    @Autowired
    AmazonClientOneFeignManager amazonClientOneFeignManager;
    @Autowired
    private IFinAccountingSubjectsService finAccountingSubjectsService;
    @Autowired
    private IFinVouchersService finVouchersService;
    /**
     * 查询凭证分录明细，存储凭证的每一笔分录信息
     * 
     * @param id 凭证分录明细，存储凭证的每一笔分录信息主键
     * @return 凭证分录明细，存储凭证的每一笔分录信息
     */
    @Override
    public FinVoucherUpload selectFinVoucherUploadById(String id)
    {
        return finVoucherUploadMapper.selectFinVoucherUploadById(id);
    }

    /**
     * 查询凭证分录明细，存储凭证的每一笔分录信息列表
     * 
     * @param finVoucherUpload 凭证分录明细，存储凭证的每一笔分录信息
     * @return 凭证分录明细，存储凭证的每一笔分录信息
     */
    @Override
    public List<FinVoucherUpload> selectFinVoucherUploadList(FinVoucherUpload finVoucherUpload)
    {
        return finVoucherUploadMapper.selectFinVoucherUploadList(finVoucherUpload);
    }

    /**
     * 新增凭证分录明细，存储凭证的每一笔分录信息
     * 
     * @param finVoucherUpload 凭证分录明细，存储凭证的每一笔分录信息
     * @return 结果
     */
    @Override
    public int insertFinVoucherUpload(FinVoucherUpload finVoucherUpload)
    {
        return finVoucherUploadMapper.insertFinVoucherUpload(finVoucherUpload);
    }

    /**
     * 修改凭证分录明细，存储凭证的每一笔分录信息
     * 
     * @param finVoucherUpload 凭证分录明细，存储凭证的每一笔分录信息
     * @return 结果
     */
    @Override
    public int updateFinVoucherUpload(FinVoucherUpload finVoucherUpload)
    {
        return finVoucherUploadMapper.updateFinVoucherUpload(finVoucherUpload);
    }

    /**
     * 批量删除凭证分录明细，存储凭证的每一笔分录信息
     * 
     * @param ids 需要删除的凭证分录明细，存储凭证的每一笔分录信息主键
     * @return 结果
     */
    @Override
    public int deleteFinVoucherUploadByIds(String[] ids)
    {
        return finVoucherUploadMapper.deleteFinVoucherUploadByIds(ids);
    }

    /**
     * 删除凭证分录明细，存储凭证的每一笔分录信息信息
     * 
     * @param id 凭证分录明细，存储凭证的每一笔分录信息主键
     * @return 结果
     */
    @Override
    public int deleteFinVoucherUploadById(String id)
    {
        return finVoucherUploadMapper.deleteFinVoucherUploadById(id);
    }
    private Map<String,String> getGroupid(){
        List<Map<String,Object>> result=amazonClientOneFeignManager.getAmazonGroup();
        Map<String,String> map=new HashMap<String,String>();
        for(Map<String,Object> m:result){
            map.put(String.valueOf(m.get("company"))+"-"+String.valueOf(m.get("shopid")),String.valueOf(m.get("id")));
            map.put(String.valueOf(m.get("name"))+"-"+String.valueOf(m.get("shopid")),String.valueOf(m.get("id")));
        }
        return map;
    }

    private Long getSubjectId(String groupid,String subjectCode){
    FinAccountingSubjects query=new FinAccountingSubjects();
    query.setSubjectCode(subjectCode);
    query.setGroupid(groupid);
    Long subjectId=null;
    List<FinAccountingSubjects> list=finAccountingSubjectsService.selectFinAccountingSubjectsList(query);
    if(list.size()>0) {
        subjectId=list.get(0).getSubjectId();
    }else {
        throw new BizException("科目编码:"+subjectCode+"不存在");
    }
    return subjectId;
   }
    @Override
    public void handleVoucherUpload() {
        FinVoucherUpload finquery=new FinVoucherUpload();
        finquery.setStatus(0);
        Map<String, String> groupNameMap = getGroupid();
        List<FinVoucherUpload> list = this.finVoucherUploadMapper.selectNeedTread();
        if(!list.isEmpty()) {
            Map<String,FinVouchers> vochers=new HashMap<>();
            Map<String,List<FinVoucherUpload>> vouchersMap=new HashMap<>();
            for(FinVoucherUpload finVoucherUpload:list){
                finVoucherUpload.setStatus(1);
                finVoucherUpload.setStatusLog("处理中");
                this.updateFinVoucherUpload(finVoucherUpload);
                String shopId=finVoucherUpload.getShopid();
                String groupName=finVoucherUpload.getGroupName();
                String groupid=groupNameMap.get(groupName+"-"+shopId);
                finVoucherUpload.setGroupid(groupid);
                if(groupid==null){
                    finVoucherUpload.setStatus(3);
                    finVoucherUpload.setStatusLog("无法获取到对应账套："+groupName);
                    this.updateFinVoucherUpload(finVoucherUpload);
                    continue;
                }
                String subjectCode=finVoucherUpload.getSubjectCode();
                Long subjectId=null;
                try{
                      subjectId=getSubjectId(groupid,subjectCode);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                if(subjectId==null){
                    finVoucherUpload.setStatus(3);
                    finVoucherUpload.setStatusLog("无法获取对应的会计科目编码："+subjectCode);
                    this.updateFinVoucherUpload(finVoucherUpload);
                    continue;
                }
                String key=groupid+"-"+finVoucherUpload.getVoucherNo()+finVoucherUpload.getVoucherType();
                FinVouchers finVouchers=vochers.get(key);
                if(finVouchers==null){
                    finVouchers = new FinVouchers();
                    finVouchers.setVoucherNo(finVoucherUpload.getVoucherNo());
                    finVouchers.setVoucherDate(finVoucherUpload.getVoucherDate());
                    finVouchers.setVoucherType(finVoucherUpload.getVoucherType());
                    finVouchers.setPreparerBy(finVoucherUpload.getPreparerBy());
                    finVouchers.setUpdateBy(finVoucherUpload.getUpdateBy());
                    finVouchers.setUpdateTime(finVoucherUpload.getUpdateTime());
                    finVouchers.setGroupid(finVoucherUpload.getGroupid());
                    finVouchers.setRemark(finVoucherUpload.getRemark());
                    finVouchers.setVoucherStatus(3);
                    finVouchers.setDataSource(2); // 导入
                }
                List<FinVoucherEntries> entries = finVouchers.getEntries();
                if(entries==null){
                    entries=new LinkedList<FinVoucherEntries>();
                }
                FinVoucherEntries e=new FinVoucherEntries();
                e.setCreatedTime(finVoucherUpload.getCreatedTime());
                e.setGroupid(finVoucherUpload.getGroupid());
                e.setCreditAmount(finVoucherUpload.getCreditAmount());
                e.setDebitAmount(finVoucherUpload.getDebitAmount());
                e.setSummary(finVoucherUpload.getSummary());
                e.setEntryNo(finVoucherUpload.getRowIndex().longValue());
                e.setSubjectId(subjectId.toString());
                e.setCurrency(finVoucherUpload.getCurrency());
                e.setExchangeRate(finVoucherUpload.getExchangeRate());
                e.setQuantity(finVoucherUpload.getQuantity());
                e.setUnitPrice(finVoucherUpload.getUnitPrice());
                entries.add(e);
                finVouchers.setEntries(entries);
                vochers.put(key, finVouchers);
                List<FinVoucherUpload> uploadList = vouchersMap.get(key);
                if(uploadList==null){
                    uploadList=new LinkedList<>();
                }
                uploadList.add(finVoucherUpload);
                vouchersMap.put(key, uploadList);
            }
            for(FinVouchers finVouchers:vochers.values()){
                String oldno=finVouchers.getVoucherNo();
                String no=this.finVouchersService.selectNextVoucherNo(finVouchers);
                finVouchers.setVoucherNo(no);
                long i=1;
                BigDecimal debitTotal=BigDecimal.ZERO;
                BigDecimal creditTotal=BigDecimal.ZERO;
                for(FinVoucherEntries entry:finVouchers.getEntries()){
                    entry.setEntryNo(i++);
                    debitTotal=debitTotal.add(entry.getDebitAmount());
                    creditTotal=creditTotal.add(entry.getCreditAmount());
                }
                finVouchers.setTotalAmount(debitTotal);
                String key=finVouchers.getGroupid()+"-"+oldno+finVouchers.getVoucherType();
                if(debitTotal.compareTo(creditTotal)!=0){
                    List<FinVoucherUpload> uplist=vouchersMap.get(key);
                    int j=1;
                    for(FinVoucherUpload up:uplist){
                        up.setStatus(3);
                        up.setEntryNo(j);
                        up.setStatusLog(up.getGroupName()+"对应凭证："+oldno +"账目不平");
                        this.updateFinVoucherUpload(up);
                    }
                    continue;
                }
                try{
                    if(oldno.contains("*")){
                        oldno=oldno.replace("*","");
                        FinVoucherDTO oldquery=new FinVoucherDTO();
                        oldquery.setVoucherNo(oldno);
                        oldquery.setGroupid(finVouchers.getGroupid());
                        oldquery.setVoucherDate(finVouchers.getVoucherDate());
                        oldquery.setStartDate(finVouchers.getVoucherDate());
                        oldquery.setEndDate(finVouchers.getVoucherDate());
                        List<FinVouchers> oldList=this.finVouchersService.selectFinVouchersList(oldquery);
                        if(oldList!=null&& !oldList.isEmpty()){
                            finVouchers.setVoucherId(oldList.get(0).getVoucherId());
                            finVouchers.setVoucherNo(oldno);
                            finVouchersService.updateFinVouchers(finVouchers);
                        }
                    }else{
                        finVouchersService.insertFinVouchers(finVouchers);
                    }

                    List<FinVoucherUpload> uplist=vouchersMap.get(key);
                    int entry_no=1;
                    for(FinVoucherUpload up:uplist){
                        up.setStatus(2);
                        up.setEntryNo(entry_no++);
                        up.setVoucherId(String.valueOf(finVouchers.getVoucherId()));
                        up.setStatusLog("处理完成");
                        this.updateFinVoucherUpload(up);
                    }
                }catch(Exception e){
                    List<FinVoucherUpload> uplist=vouchersMap.get(key);
                    for(FinVoucherUpload up:uplist){
                        up.setStatus(3);
                        up.setStatusLog(e.getMessage());
                        this.updateFinVoucherUpload(up);
                    }
                }


            }
        }
    }
}
