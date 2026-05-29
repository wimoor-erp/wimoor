package com.wimoor.erp.material.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.material.mapper.ErpMediaMapper.ErpMediaWithRef;
import com.wimoor.erp.material.pojo.entity.ErpMedia;
import com.wimoor.erp.material.pojo.entity.ErpMediaRef;
import com.wimoor.erp.material.service.IErpMediaRefService;
import com.wimoor.erp.material.service.IErpMediaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

/**
 * 商品媒体资源管理接口。
 *
 * @author wimoor
 */
@Api(tags = "商品媒体管理")
@SystemControllerLog("商品媒体模块")
@RestController
@RequestMapping("/api/v1/material/media")
@RequiredArgsConstructor
public class MaterialMediaController {

    private final IErpMediaService mediaService;
    private final IErpMediaRefService refService;

    @ApiOperation("上传单个媒体文件（图片/视频）")
    @SystemControllerLog("上传媒体")
    @PostMapping("/upload")
    public Result<ErpMedia> upload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value = "mediaType", required = false) Integer mediaType,
                                   @RequestParam(value = "usageType", required = false) Integer usageType,
                                   @RequestParam(value = "source", required = false) Integer source,
                                   @RequestParam(value = "materialid", required = false) String materialId,
                                   @RequestParam(value = "refType", required = false) Integer refType) {
        UserInfo user = UserInfoContext.get();
        ErpMedia media = mediaService.upload(file, mediaType, usageType, source, user);
        // 上传后自动关联到商品（前端传了 materialid 时）
        if (materialId != null && !materialId.isEmpty() && media != null) {
            int rt = (refType != null) ? refType : 0;
            refService.assign(media.getId(), materialId, rt, null, null, null, null, user);
        }
        return Result.success(media);
    }

    @ApiOperation("批量上传 ZIP 内的图片")
    @SystemControllerLog("批量上传媒体")
    @PostMapping("/uploadBatch")
    public Result<List<ErpMedia>> uploadBatch(@RequestParam("file") MultipartFile zipFile,
                                              @RequestParam(value = "usageType", required = false) Integer usageType,
                                              @RequestParam(value = "source", required = false) Integer source) {
        UserInfo user = UserInfoContext.get();
        return Result.success(mediaService.uploadBatch(zipFile, usageType, source, user));
    }

    @ApiOperation("查询 SKU 展示图列表")
    @GetMapping("/list")
    public Result<List<ErpMediaWithRef>> list(@RequestParam("materialid") String materialId) {
        return Result.success(refService.list(materialId));
    }

    @ApiOperation("查询 SPU 图片池")
    @GetMapping("/pool")
    public Result<List<ErpMediaWithRef>> pool(@RequestParam("materialid") String materialId) {
        return Result.success(refService.pool(materialId));
    }

    @ApiOperation("分配媒体到商品（SPU池或SKU展示）")
    @SystemControllerLog("分配媒体到商品")
    @PostMapping("/assign")
    public Result<ErpMediaRef> assign(@RequestBody Map<String, Object> body) {
        UserInfo user = UserInfoContext.get();
        String mediaId = (String) body.get("mediaId");
        String materialId = (String) body.get("materialId");
        Integer refType = body.get("refType") == null ? 1 : ((Number) body.get("refType")).intValue();
        Integer picClass = body.get("picClass") == null ? null : ((Number) body.get("picClass")).intValue();
        String platform = (String) body.get("platform");
        String marketplaceId = (String) body.get("marketplaceId");
        String slot = (String) body.get("slot");
        return Result.success(refService.assign(mediaId, materialId, refType, picClass,
                platform, marketplaceId, slot, user));
    }

    @ApiOperation("批量分配 SPU 池图片到 SKU")
    @SystemControllerLog("批量分配媒体")
    @PostMapping("/batchAssign")
    public Result<List<ErpMediaRef>> batchAssign(@RequestBody Map<String, Object> body) {
        UserInfo user = UserInfoContext.get();
        @SuppressWarnings("unchecked")
        List<String> mediaIds = (List<String>) body.get("mediaIds");
        String materialId = (String) body.get("materialId");
        Integer refType = body.get("refType") == null ? 1 : ((Number) body.get("refType")).intValue();
        Integer picClass = body.get("picClass") == null ? null : ((Number) body.get("picClass")).intValue();
        String platform = (String) body.get("platform");
        String marketplaceId = (String) body.get("marketplaceId");
        return Result.success(refService.batchAssign(mediaIds, materialId, refType, picClass,
                platform, marketplaceId, user));
    }

    @ApiOperation("取消分配")
    @SystemControllerLog("取消媒体分配")
    @DeleteMapping("/unassign/{refId}")
    public Result<Boolean> unassign(@PathVariable("refId") String refId) {
        return Result.judge(refService.unassign(refId, UserInfoContext.get()));
    }

    @ApiOperation("设置主图")
    @SystemControllerLog("设置主图")
    @PutMapping("/setMain/{refId}")
    public Result<Boolean> setMain(@PathVariable("refId") String refId) {
        return Result.judge(refService.setMain(refId, UserInfoContext.get()));
    }

    @ApiOperation("排序")
    @SystemControllerLog("媒体排序")
    @PostMapping("/sort")
    public Result<Boolean> sort(@RequestBody List<String> refIds) {
        return Result.judge(refService.sort(refIds, UserInfoContext.get()));
    }

    @ApiOperation("更新用途分类")
    @SystemControllerLog("更新媒体用途")
    @PutMapping("/usage/{refId}")
    public Result<Boolean> updateUsage(@PathVariable("refId") String refId,
                                       @RequestParam("picClass") Integer picClass) {
        return Result.judge(refService.updateUsage(refId, picClass, UserInfoContext.get()));
    }

    @ApiOperation("删除媒体资源")
    @SystemControllerLog("删除媒体")
    @DeleteMapping("/delete/{mediaId}")
    public Result<Boolean> delete(@PathVariable("mediaId") String mediaId,
                                  @RequestParam(value = "force", defaultValue = "false") boolean force) {
        return Result.judge(mediaService.delete(mediaId, force, UserInfoContext.get()));
    }

    @ApiOperation("从 Amazon 同步媒体")
    @SystemControllerLog("Amazon媒体同步")
    @PostMapping("/syncFromAmazon")
    public Result<Integer> syncFromAmazon(@RequestBody Map<String, String> body) {
        UserInfo user = UserInfoContext.get();
        String materialId = body.get("materialId");
        String sku = body.get("sku");
        String authorityId = body.get("authorityId");
        String marketplaceId = body.get("marketplaceId");
        return Result.success(refService.syncFromAmazon(materialId, sku, authorityId, marketplaceId, user));
    }

    @ApiOperation("AI 处理回调（预留）")
    @PostMapping("/processCallback")
    public Result<Boolean> processCallback(@RequestBody Map<String, Object> body) {
        // 预留：解析 mediaId/taskId/status/result 等字段并更新 ErpMedia.processStatus
        return Result.success(true);
    }
}
