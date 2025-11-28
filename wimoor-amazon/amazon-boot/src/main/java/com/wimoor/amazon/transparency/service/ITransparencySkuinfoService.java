package com.wimoor.amazon.transparency.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.transparency.pojo.dto.TransparencyDTO;
import com.wimoor.amazon.transparency.pojo.entity.TransparencySkuinfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.user.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
* @author liufei
* @description 针对表【t_amz_transparency_skuinfo】的数据库操作Service
* @createDate 2025-08-08 11:31:18
*/
public interface ITransparencySkuinfoService extends IService<TransparencySkuinfo> {

    TransparencySkuinfo saveSkuinfo(UserInfo userinfo, TransparencySkuinfo dto);

    IPage<Map<String, Object>> listSkuinfo(UserInfo userinfo, TransparencyDTO dto);

    void uploadExcelAction(MultipartFile file, UserInfo user, String groupid, String authid);
}
