package com.wimoor.amazon.transparency.service;

import com.alibaba.fastjson.JSONObject;
import com.wimoor.amazon.transparency.pojo.entity.TransparencyAuthority;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.amazon.transparency.pojo.entity.TransparencyTask;
import com.wimoor.common.user.UserInfo;

/**
* @author liufei
* @description 针对表【t_amz_transparency_authority】的数据库操作Service
* @createDate 2025-08-07 10:19:02
*/
public interface ITransparencyAuthorityService extends IService<TransparencyAuthority> {
    String sgtin(TransparencyAuthority authority,String gtin,Integer count);
    TransparencyAuthority saveAuthority(UserInfo userinfo,TransparencyAuthority authority);
    String job(TransparencyAuthority authority, String jobid);
}
