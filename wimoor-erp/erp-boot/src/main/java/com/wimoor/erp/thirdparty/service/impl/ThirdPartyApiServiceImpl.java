package com.wimoor.erp.thirdparty.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyAPIMapper;
import com.wimoor.erp.thirdparty.mapper.ThirdPartyQuoteBuyerMapper;
import com.wimoor.erp.thirdparty.mapper.ThirdPartySystemMapper;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyAPI;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartyQuoteBuyer;
import com.wimoor.erp.thirdparty.pojo.entity.ThirdPartySystem;
import com.wimoor.erp.thirdparty.service.IThirdPartyAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("thirdPartyApiService")
@RequiredArgsConstructor
public class ThirdPartyApiServiceImpl extends ServiceImpl<ThirdPartyAPIMapper, ThirdPartyAPI> implements IThirdPartyAPIService {
    final ThirdPartySystemMapper thirdPartySystemMapper;
    final ThirdPartyQuoteBuyerMapper thirdPartyQuoteBuyerMapper;
    private String support;

    public List<ThirdPartySystem> getAllSupportSystem(){
        return thirdPartySystemMapper.selectList(null);
    }

    @Override
    public List<ThirdPartyAPI> getSupportApi(UserInfo user,String support) {
        this.support = support;
        LambdaQueryWrapper<ThirdPartySystem> query=new LambdaQueryWrapper<ThirdPartySystem>();
        if(StrUtil.isNotBlank(support)){
            support="%"+support+"%";
        }
        query.like(ThirdPartySystem::getSupport,support);
        List<ThirdPartySystem> list = thirdPartySystemMapper.selectList(query);
        List<String> systems = new ArrayList<String>();
        for(ThirdPartySystem thirdPartySystem:list){
            systems.add(thirdPartySystem.getId());
        }
        LambdaQueryWrapper<ThirdPartyAPI> queryApi=new LambdaQueryWrapper<ThirdPartyAPI>();
        queryApi.eq(ThirdPartyAPI::getShopid,user.getCompanyid());
        if(systems!=null&&systems.size()>0){
            queryApi.in(ThirdPartyAPI::getSystem,systems);
            return this.baseMapper.selectList(queryApi);
        }else{
            return new ArrayList<ThirdPartyAPI>();
        }

    }

    public ThirdPartySystem getApiSystemById(String id){
        LambdaQueryWrapper<ThirdPartySystem> query=new LambdaQueryWrapper<ThirdPartySystem>();
        query.eq(ThirdPartySystem::getId,id);
        return thirdPartySystemMapper.selectOne(query);
    }



}
