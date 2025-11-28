package com.wimoor.amazon.transparency.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wimoor.amazon.transparency.pojo.dto.TransparencyDTO;
import com.wimoor.amazon.transparency.pojo.entity.TransparencySkuinfo;
import com.wimoor.amazon.transparency.service.ITransparencySkuinfoService;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 订单抓取 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-05-14
 */
@Api(tags = "transparencySkuinfo接口")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/transparency/skuinfo")
public class TransparencySkuinfoController {
    final ITransparencySkuinfoService transparencySkuinfoService;

    @ApiOperation(value = "保存")
    @PostMapping("/save")
    public Result<?> saveAction(@RequestBody TransparencySkuinfo dto) {
        UserInfo userinfo = UserInfoContext.get();
        return Result.success(transparencySkuinfoService.saveSkuinfo(userinfo,dto));
    }

     @ApiOperation(value = "上传")
     @PostMapping(value = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     public Result<String> uploadExcelAction(@RequestParam("file") MultipartFile file
             , @RequestParam("groupid")String groupid
             , @RequestParam("authid")String authid){
        UserInfo user=UserInfoContext.get();
        if (file != null) {
            try {
                transparencySkuinfoService.uploadExcelAction(file,user,groupid,authid);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return Result.success();
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public Result<?> deleteAction(@RequestBody TransparencySkuinfo dto) {
        UserInfo userinfo = UserInfoContext.get();
        TransparencySkuinfo old = transparencySkuinfoService.getById(dto.getId());
        old.setOperator(userinfo.getId());
        old.setOpttime(new Date());
        old.setDisabled(true);
        return Result.success(transparencySkuinfoService.updateById(old));
    }


    @ApiOperation(value = "报表分类")
    @PostMapping("/list")
    public Result<?> listAction(@RequestBody TransparencyDTO dto) {
        UserInfo userinfo = UserInfoContext.get();
        dto.setAuthid(StrUtil.isBlankOrUndefined(dto.getAuthid())?null:dto.getAuthid());
        dto.setSku(StrUtil.isBlankOrUndefined(dto.getSku())?null:dto.getSku());
        dto.setGroupid(StrUtil.isBlankOrUndefined(dto.getGroupid())?null:dto.getGroupid());
        dto.setTcodelist(dto.getTcodelist()!=null&& !dto.getTcodelist().isEmpty() ?dto.getTcodelist():null);
        dto.setTaskid(StrUtil.isNullOrUndefined(dto.getTaskid())?null:dto.getTaskid());
        IPage<Map<String,Object>> result= transparencySkuinfoService.listSkuinfo(userinfo,dto);
        return Result.success(result);
    }
}

