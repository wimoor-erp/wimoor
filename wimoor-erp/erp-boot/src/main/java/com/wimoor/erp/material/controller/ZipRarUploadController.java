package com.wimoor.erp.material.controller;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.material.service.IZipRarUploadService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;



@Api(tags = "zip上传图片接口")
@RestController
@RequestMapping("/api/v1/material/zip")
@RequiredArgsConstructor
public class ZipRarUploadController {

	@Resource
	IZipRarUploadService zipRarUploadService;
	
	@PostMapping(value="/uploadFile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> uploadFileAction(@RequestParam("file")MultipartFile file) {
		UserInfo user = UserInfoContext.get();
		if(file != null) {
			try {
				zipRarUploadService.uploadZipOrRar(user, file, null, "zip");
				return Result.success("ok");
			}catch(Exception e) {
				e.printStackTrace();
				throw new BizException(e.getMessage());
			}
		}
		return Result.success("ok");
	}
 

}
