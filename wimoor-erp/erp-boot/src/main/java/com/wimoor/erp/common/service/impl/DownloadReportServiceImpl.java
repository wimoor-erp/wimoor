package com.wimoor.erp.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.mapper.DownloadReportMapper;
import com.wimoor.erp.common.pojo.entity.DownloadReport;
import com.wimoor.erp.common.service.IDownloadReportService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static cn.hutool.json.XMLTokener.entity;

@Service("downloadReportService")
public class DownloadReportServiceImpl  extends ServiceImpl< DownloadReportMapper, DownloadReport> implements IDownloadReportService {
    @Override
    public void addRun(UserInfo user) {
        this.lambdaUpdate().eq(DownloadReport::getIsrun,false).eq(DownloadReport::getUserid,user.getId()).eq(DownloadReport::getFtype,"purchase_time").remove();
        List<DownloadReport> entitys=this.lambdaQuery().eq(DownloadReport::getIsrun,true).eq(DownloadReport::getUserid,user.getId()).eq(DownloadReport::getFtype,"purchase_time").list();
        if(entitys!=null&&entitys.size()>0){
            throw new BizException("存在正在运行的报表，请稍后");
        }else{
            DownloadReport entity=new DownloadReport();
            entity.setCreatetime(new Date());
            entity.setUserid(user.getId());
            entity.setShopid(user.getCompanyid());
            entity.setFtype("purchase_time");
            entity.setIsrun(true);
            this.save(entity);
        }
    }

    @Override
    public void updateError(Throwable e, UserInfo user) {
        DownloadReport entity=this.lambdaQuery().eq(DownloadReport::getIsrun,true).eq(DownloadReport::getUserid,user.getId()).eq(DownloadReport::getFtype,"purchase_time").one();
        if(entity==null){
            entity=new DownloadReport();
            entity.setCreatetime(new Date());
            entity.setUserid(user.getId());
            entity.setShopid(user.getCompanyid());
            entity.setFtype("purchase_time");
            entity.setIsrun(false);
            entity.setLog(e.toString());
            this.save(entity);
        }else{
            entity.setLog(e.toString());
            entity.setIsrun(false);
            this.updateById(entity);
        }
    }

    @Override
    public void updateContent(byte[] byteArray, UserInfo user) {
        DownloadReport entity=this.lambdaQuery().eq(DownloadReport::getIsrun,true).eq(DownloadReport::getUserid,user.getId()).eq(DownloadReport::getFtype,"purchase_time").one();
        if(entity==null){
            entity=new DownloadReport();
            entity.setCreatetime(new Date());
            entity.setUserid(user.getId());
            entity.setShopid(user.getCompanyid());
            entity.setFtype("purchase_time");
            entity.setIsrun(false);
            entity.setContent(byteArray);
            this.save(entity);
        }else{
            entity.setContent(byteArray);
            entity.setIsrun(false);
            this.updateById(entity);
        }
    }
}
