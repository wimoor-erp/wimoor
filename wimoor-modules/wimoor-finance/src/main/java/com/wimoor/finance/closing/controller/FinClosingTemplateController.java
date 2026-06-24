package com.wimoor.finance.closing.controller;

import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.finance.closing.domain.FinClosingTemplate;
import com.wimoor.finance.closing.domain.FinClosingTemplateVouchers;
import com.wimoor.finance.closing.service.IFinClosingTemplateService;
import com.wimoor.finance.closing.service.IFinClosingTemplateVouchersService;
import com.wimoor.finance.closing.service.strategy.FinClosingTemplateStrategyFactory;
import com.wimoor.finance.closing.service.strategy.IFinClosingTemplateStrategy;
import com.wimoor.finance.setting.domain.FinAccountingPeriods;
import com.wimoor.finance.setting.service.IFinAccountingPeriodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/closing_template")
public class FinClosingTemplateController extends BaseController
{
    @Autowired
    private IFinClosingTemplateService finClosingTemplateService;
    @Autowired
    private FinClosingTemplateStrategyFactory strategyFactory;
    @Autowired
    private IFinClosingTemplateVouchersService finClosingTemplateVouchersService;
    @Autowired
    private IFinAccountingPeriodsService finAccountingPeriodsService;

    @GetMapping("/list")
    public TableDataInfo list(FinClosingTemplate finClosingTemplate)
    {
        startPage();
        List<FinClosingTemplate> list = finClosingTemplateService.selectFinClosingTemplateList(finClosingTemplate);
        for(FinClosingTemplate item:list){
            FinAccountingPeriods period = finAccountingPeriodsService.getCurrentPeriod(item.getGroupid());
            Date voucherDate = period.getEndDate();
            String templateId = item.getId();
            String groupid=item.getGroupid();
            FinClosingTemplateVouchers query=new FinClosingTemplateVouchers();
            query.setGroupid(groupid);
            query.setTemplateId(templateId);
            query.setVoucherDate(voucherDate);
            List<FinClosingTemplateVouchers> voucher = finClosingTemplateVouchersService.selectFinClosingTemplateVouchersList(query);
            if(voucher!=null && !voucher.isEmpty()){
                item.setVourchesId(voucher.get(0).getVourchesId());
            }
        }
        return getDataTable(list);
    }

    @GetMapping("/templateVouchers")
    public Result templateVouchers(String templateid,String period){
            FinClosingTemplate template=finClosingTemplateService.selectFinClosingTemplateById(templateid);
            if(template==null){return error("模版不存在");}
            FinAccountingPeriods accPeriod = finAccountingPeriodsService.selectByPeriod(template.getGroupid(), period);
            if(accPeriod==null){return error("会计期间不存在");}
            if(accPeriod.getPeriodStatus()==3){return error("会计期间已关闭");}
            Date voucherDate = accPeriod.getEndDate();
            FinClosingTemplateVouchers query=new FinClosingTemplateVouchers();
            query.setGroupid(template.getGroupid());
            query.setTemplateId(template.getId());
            query.setVoucherDate(voucherDate);
            List<FinClosingTemplateVouchers> voucher = finClosingTemplateVouchersService.selectFinClosingTemplateVouchersList(query);
            if(voucher!=null && !voucher.isEmpty()){
                template.setVourchesId(voucher.get(0).getVourchesId());
            }
        return success(template);
    }

    @GetMapping("/voucherLog")
    public Result getVoucherLog(String templateid, String period){
        FinClosingTemplate template = finClosingTemplateService.selectFinClosingTemplateById(templateid);
        if(template == null){ return error("模版不存在"); }
        FinAccountingPeriods accPeriod = finAccountingPeriodsService.selectByPeriod(template.getGroupid(), period);
        if(accPeriod == null){ return error("会计期间不存在"); }
        Date voucherDate = accPeriod.getEndDate();
        FinClosingTemplateVouchers query = new FinClosingTemplateVouchers();
        query.setGroupid(template.getGroupid());
        query.setTemplateId(template.getId());
        query.setVoucherDate(voucherDate);
        List<FinClosingTemplateVouchers> vouchers = finClosingTemplateVouchersService.selectFinClosingTemplateVouchersList(query);
        Map<String, Object> result = new HashMap<>();
        result.put("ftype", template.getFtype());
        result.put("templateName", template.getName());
        if(vouchers != null && !vouchers.isEmpty()){
            FinClosingTemplateVouchers voucher = vouchers.get(0);
            result.put("datalog", voucher.getDatalog());
            result.put("vourchesId", voucher.getVourchesId());
            result.put("voucherDate", voucher.getVoucherDate());
            result.put("createBy", voucher.getCreateBy());
            result.put("createdTime", voucher.getCreatedTime());
        }
        return success(result);
    }

    @PostMapping("/export")
    public void export(HttpServletResponse response, FinClosingTemplate finClosingTemplate)
    {
        List<FinClosingTemplate> list = finClosingTemplateService.selectFinClosingTemplateList(finClosingTemplate);
        ExcelUtil<FinClosingTemplate> util = new ExcelUtil<FinClosingTemplate>(FinClosingTemplate.class);
        util.exportExcel(response, list, "财务结算的各个模版名称数据");
    }

    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") String id)
    {
        return success(finClosingTemplateService.selectFinClosingTemplateById(id));
    }

    @PostMapping
    public Result add(@RequestBody FinClosingTemplate finClosingTemplate)
    {
        UserInfo userinfo = UserInfoContext.get();
        finClosingTemplate.setCreateBy(userinfo.getUserName());
        finClosingTemplate.setCreatedTime(new Date());
        finClosingTemplate.setModifyBy(userinfo.getUserName());
        finClosingTemplate.setUpdatedTime(new Date());
        return toResult(finClosingTemplateService.insertFinClosingTemplate(finClosingTemplate));
    }

    @PutMapping
    public Result edit(@RequestBody FinClosingTemplate finClosingTemplate)
    {
        UserInfo userinfo = UserInfoContext.get();
        finClosingTemplate.setModifyBy(userinfo.getUserName());
        finClosingTemplate.setUpdatedTime(new Date());
        return toResult(finClosingTemplateService.updateFinClosingTemplate(finClosingTemplate));
    }

	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable String[] ids)
    {
        return toResult(finClosingTemplateService.deleteFinClosingTemplateByIds(ids));
    }
    @GetMapping("calculationDetail")
    public Result getCalculationDetail(String templateId, String period) {
        FinClosingTemplate template = finClosingTemplateService.selectFinClosingTemplateById(templateId);
        if (template == null) {
            return Result.error("模板不存在");
        }
        String fType = template.getFtype();
        if (!strategyFactory.hasStrategy(fType)) {
            return Result.error("未找到模板类型为 [" + fType + "] 的处理策略");
        }
        IFinClosingTemplateStrategy strategy = strategyFactory.getStrategy(fType);
        Map<String, Object> detail = strategy.getCalculationDetail(templateId, period);
        return success(detail);
    }
    
    @GetMapping("initTemplateItem")
    public Result initTemplateItem(String templateid){
        FinClosingTemplate finClosingTemp = finClosingTemplateService.selectFinClosingTemplateById(templateid);
        if(finClosingTemp == null){
            return Result.error("模板不存在");
        }
        strategyFactory.initTemplateItem(finClosingTemp);
        return success();
    }

    @GetMapping("voucher")
    @Transactional
    public Result generateVoucher(String templateId,String period){
        FinClosingTemplate finClosingTemp = finClosingTemplateService.selectFinClosingTemplateById(templateId);
        if(finClosingTemp == null){
            return Result.error("模板不存在");
        }
        String fType = finClosingTemp.getFtype();
        if(!strategyFactory.hasStrategy(fType)){
            return Result.error("未找到模板类型为 [" + fType + "] 的处理策略");
        }
        UserInfo userinfo = UserInfoContext.get();
        IFinClosingTemplateStrategy strategy = strategyFactory.getStrategy(fType);
        strategy.generateVoucher(userinfo,templateId,period);
        return success();
    }
}
