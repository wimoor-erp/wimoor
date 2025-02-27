package com.wimoor.amazon.adv.common.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.adv.common.dao.AmzAdvProfileMapper;
import com.wimoor.amazon.adv.common.dao.AmzAdvertInvoicesMapper;
import com.wimoor.amazon.adv.common.pojo.AmzAdvAuth;
import com.wimoor.amazon.adv.common.pojo.AmzAdvProfile;
import com.wimoor.amazon.adv.common.pojo.AmzAdvertInvoices;
import com.wimoor.amazon.adv.common.service.ApiBuildService;
import com.wimoor.amazon.adv.common.service.IAmzAdvAuthService;
import com.wimoor.amazon.adv.common.service.IAmzAdvertInvoicesService;
import com.wimoor.amazon.adv.utils.AdvUtils;
import com.wimoor.amazon.base.BaseService;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.user.UserInfo;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("amzAdvertInvoicesService")
public class AmzAdvertInvoicesServiceImpl   extends BaseService<AmzAdvertInvoices>  implements IAmzAdvertInvoicesService {

    @Resource
    IAmzAdvAuthService amzAdvAuthService;
    @Resource
    ApiBuildService apiBuildService;
    @Resource
    AmzAdvProfileMapper amzAdvProfileMapper;
    @Resource
    AmzAdvertInvoicesMapper amzAdvertInvoicesMapper;
    private String amzAdvInvoicesRunnable(AmzAdvAuth amzadvauth,AmzAdvProfile profile){
                   amzGetlistInvoices(amzadvauth, profile);
        return null;
    }
    public  String amzAdvInvoicesHandle() {
        //查询全部advauth
        List<Runnable> runnables = new ArrayList<Runnable>();
        List<AmzAdvAuth>  advauthlist=amzAdvAuthService.list();
        if(advauthlist==null || advauthlist.size()==0){
            return null;
        }
        for (AmzAdvAuth advauth : advauthlist) {
            if (advauth.getDisable()
                    || advauth.getRegion().equals("TT")
                    ||(advauth.getDisableTime()!=null&&GeneralUtil.distanceOfHour(advauth.getDisableTime(), new Date())<3)) {
                continue;
            }
            List<AmzAdvProfile> profiles = amzAdvAuthService.getAmzAdvProfile(advauth);
            for (AmzAdvProfile profile : profiles) {
                runnables.add(new Runnable() {
                    public void run() {
                        try{
                            amzAdvInvoicesRunnable(advauth,profile);
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }

                });
            }
        }
        if(runnables.size()>0){
            AdvUtils.executThreadForAdv(runnables);
        }
       return null;
    }

    @Override
    public List<Map<String,Object>> getInvoicesSummary(UserInfo user,String groupid, String marketplaceid, String fromDate, String toDate) {
        Map<String, Object> param=new HashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (StrUtil.isNotEmpty(fromDate)) {
            fromDate = fromDate.replaceAll("/", "-");
            String[] datear = fromDate.split("-");
            Integer year = Integer.valueOf(datear[0].trim());
            Integer month = Integer.valueOf((datear[1]).trim());
            if (month > 1) {
                month = month - 1;
            } else {
                month = 12;
                year = year - 1;
            }
            String monthstr = month.toString();
            if (month < 10) {
                monthstr = "0" + monthstr;
            }
            fromDate = year + "-" + monthstr;
            param.put("fromDate", year + "-" + monthstr + "-01");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -1);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            fromDate = GeneralUtil.formatDate(cal.getTime(), sdf);
            param.put("fromDate", fromDate.trim());
        }

        if (StrUtil.isNotEmpty(toDate)) {
            toDate = toDate.replaceAll("/", "-");
            Calendar ca = Calendar.getInstance();
            if(toDate.length()==7){
                ca.setTime(GeneralUtil.getDatez(toDate.trim() +"-01"));
            }else{
                ca.setTime(GeneralUtil.getDatez(toDate.trim() ));
            }
            ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
            String last = GeneralUtil.formatDate(ca.getTime(), sdf);
            param.put("toDate", last + " 23:59:59");
        } else {

            Calendar ca = Calendar.getInstance();
            ca.setTime(new Date());
            ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
            String last = GeneralUtil.formatDate(ca.getTime(), sdf);
            param.put("toDate", last + " 23:59:59");
        }
        param.put("groupid", StrUtil.isNotBlank(groupid)?groupid:null);
        param.put("marketplaceid", StrUtil.isNotBlank(marketplaceid)?marketplaceid:null);
        param.put("shopid", user.getCompanyid());
        List<Map<String,Object>> datas=amzAdvertInvoicesMapper.getInvoicesSummary(param);
        return datas;
    }


    public void amzGetlistInvoices(AmzAdvAuth amzadvauth, AmzAdvProfile profile) {
        // TODO Auto-generated method stub
        String result = apiBuildService.adzAdvInvoicesGet(amzadvauth, profile, null);
        if(StringUtil.isNotEmpty(result)){
            JSONObject jsonObject = GeneralUtil.getJsonObject(result);
            if(jsonObject!=null){
                String status = jsonObject.getString("status");
                if("success".equals(status)){
                    JSONObject payload = jsonObject.getJSONObject("payload");
                    JSONArray jsonArray = payload.getJSONArray("invoiceSummaries");
                    if(jsonArray!=null && jsonArray.size()>0){
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject invoiceObj = jsonArray.getJSONObject(i);
                            AmzAdvertInvoices amzAdvertInvoices=new AmzAdvertInvoices();
                            amzAdvertInvoices.setProfileid(profile.getId());
                            amzAdvertInvoices.setInvoiceId(invoiceObj.getString("id"));
                            amzAdvertInvoices.setStatus(invoiceObj.getString("status"));
                            amzAdvertInvoices.setFromDate(invoiceObj.getString("fromDate"));
                            amzAdvertInvoices.setToDate(invoiceObj.getString("toDate"));
                            amzAdvertInvoices.setInvoiceDate(invoiceObj.getString("invoiceDate"));
                            amzAdvertInvoices.setOpttime(new Date());
                            JSONObject amountDueObj = invoiceObj.getJSONObject("amountDue");
                            if(amountDueObj!=null){
                                amzAdvertInvoices.setAmountDueAmount(amountDueObj.getString("amount"));
                                amzAdvertInvoices.setAmountDueCode(amountDueObj.getString("currencyCode"));
                            }
                            JSONObject taxAmountDueObj = invoiceObj.getJSONObject("taxAmount Due");
                            if(taxAmountDueObj!=null){
                                amzAdvertInvoices.setTaxAmountDueAmount(taxAmountDueObj.getString("amount"));
                                amzAdvertInvoices.setTaxAmountDueCode(taxAmountDueObj.getString("currencyCode"));
                            }
                            JSONObject remainingAmountDueObj = invoiceObj.getJSONObject("remainingAmount Due");
                            if(remainingAmountDueObj!=null){
                                amzAdvertInvoices.setRemainingAmountDueAmount(remainingAmountDueObj.getString("amount"));
                                amzAdvertInvoices.setRemainingAmountDueCode(remainingAmountDueObj.getString("currencyCode"));
                            }
                            JSONObject remainingTaxAmountDueObj = invoiceObj.getJSONObject("remainingTax Amount Due");
                            if(remainingTaxAmountDueObj!=null){
                                amzAdvertInvoices.setRemainingTaxAmountDueAmount(remainingTaxAmountDueObj.getString("amount"));
                                amzAdvertInvoices.setRemainingTaxAmountDueCode(remainingTaxAmountDueObj.getString("currencyCode"));
                            }
                            amzAdvertInvoices.setFees(invoiceObj.getString("fees"));
                            amzAdvertInvoices.setRemainingFees(invoiceObj.getString("remainingFees"));
                            amzAdvertInvoices.setDownloadableDocuments(invoiceObj.getString("downloadable Documents"));
                            String dateStr = amzAdvertInvoices.getInvoiceDate();
                            if(StrUtil.isNotBlank(dateStr)){
                                String year = dateStr.substring(0, 4);
                                String month = dateStr.substring(4, 6);
                                String day = dateStr.substring(6, 8);
                                amzAdvertInvoices.setInvoiceDay(GeneralUtil.getDatez(year+"-"+month+"-"+day));
                            }
                            Example example=new Example(AmzAdvertInvoices.class);
                            Example.Criteria crit = example.createCriteria();
                            if(amzAdvertInvoices.getInvoiceId()!=null){
                                crit.andEqualTo("invoiceId",amzAdvertInvoices.getInvoiceId());
                            }else{
                                crit.andIsNull("invoiceId");
                            }
                            crit.andEqualTo("fromDate",amzAdvertInvoices.getFromDate());
                            crit.andEqualTo("profileid",amzAdvertInvoices.getProfileid());
                            List<AmzAdvertInvoices> one = this.selectByExample(example);
                            if(one!=null&&one.size()>0){

                            }else{
                                this.save(amzAdvertInvoices);
                            }

                        }
                    }
                }

            }
        }

    }


}
