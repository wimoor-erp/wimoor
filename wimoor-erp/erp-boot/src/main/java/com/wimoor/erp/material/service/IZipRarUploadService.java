package com.wimoor.erp.material.service;


import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.wimoor.common.user.UserInfo;


public interface IZipRarUploadService {
	public List<String> uploadZipOrRar(UserInfo user, MultipartFile file, String ftype, String name);
	
	public String copyFolder(String oldPath, String newPath, String newFileName);
}
