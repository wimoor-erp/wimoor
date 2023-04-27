package com.wimoor.erp.material.service.impl;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.impl.PictureServiceImpl;
import com.wimoor.common.user.UserInfo;
import com.wimoor.erp.common.pojo.entity.ERPBizException;
import com.wimoor.erp.material.mapper.MaterialMapper;
import com.wimoor.erp.material.pojo.entity.Material;
import com.wimoor.erp.material.service.IZipRarUploadService;
import com.wimoor.erp.util.ZipRarUploader;
import lombok.RequiredArgsConstructor;


@Service("zipRarUploadService")
@RequiredArgsConstructor
public class ZipRarUploadServiceImpl implements IZipRarUploadService {

	final MaterialMapper materialMapper;
	 
	final IPictureService pictureService;
	 

	public static String zipImgPath = "C:\\Web\\photo\\ZipRarUpload\\imageZ\\";

	public List<String> uploadZipOrRar(UserInfo user, MultipartFile file, String ftype, String name) {
		String shopid = user.getCompanyid();
		// ftp服务器路径
		String ftpPath = PictureServiceImpl.materialImgPath + user.getCompanyid() + "/";
		// 将压缩包保存在指定路径
		String packFilePath = zipImgPath + shopid + "\\" + file.getOriginalFilename();
		// 将解压图片保存在指定路径
		String imageFilePath = zipImgPath + shopid + "\\image";
		List<String> errorList = new ArrayList<String>();
		File pathfile = new File(packFilePath);
		if (!pathfile.exists()) {
			pathfile.mkdirs();
		}
		try {
			file.transferTo(pathfile);
		} catch (IOException e) {
			throw new ERPBizException("上传解压文件失败！");
		}
		if (GeneralUtil.isNotEmpty(name)) {
			if ("zip".equals(name)) {
				ZipRarUploader.unPackZip(pathfile, null, imageFilePath);
				Map<String, File> map = getFileName(imageFilePath);
				if (map == null || map.size() == 0) {
					throw new ERPBizException("压缩文件内容为空！");
				} else {
					for (String sku : map.keySet()) {
						QueryWrapper<Material> queryWrapper = new QueryWrapper<Material>();
						queryWrapper.eq("sku", sku);
						queryWrapper.eq("shopid", shopid);
						queryWrapper.eq("isDelete", 0);
						List<Material> materialList = materialMapper.selectList(queryWrapper);
						if (materialList != null && materialList.size() > 0) {
							Material material = materialList.get(0);
							String id = material.getImage();
							String path = map.get(sku).getAbsolutePath();
							try {
								Picture picture = pictureService.downloadPictureMaterial(id, path, ftpPath);
								if (picture != null && GeneralUtil.isEmpty(id)) {
									material.setImage(picture.getId());
									materialMapper.updateById(material);
								}
								if (picture == null) {
									errorList.add(sku);
								}
								map.get(sku).delete();
							} catch (Exception e) {
								e.printStackTrace();
								throw new ERPBizException("上传FTP服务器失败！");
							}
						} else {
							map.get(sku).delete();
						}
					}
					pathfile.delete();
				}
			} else if ("rar".equals(name)) {
				// ZipRarUploader.unPackRar(pathfile, packFilePath);
			}
		}
		if (errorList.size() > 0) {
	 
		} else {
 
		}
		return errorList;
	}

	public Map<String, File> getFileName(String pathfile) {
		Map<String, File> map = new HashMap<String, File>();
		File file = new File(pathfile);
		// 获得该文件夹内的所有文件
		File[] arrayFiles = null;
		while (file.isDirectory()) {
			arrayFiles = file.listFiles();
			file = arrayFiles[0];
		}
		for (int i = 0; i < arrayFiles.length; i++) {
			File image = arrayFiles[i];
			if (image.exists() && isImage(image)) {// 如果是文件,并且是图片
				String imageName = image.getName();
				String imageNames = imageName.substring(0, imageName.lastIndexOf("."));
				map.put(imageNames, image);
			}
		}
		return map;
	}

	/**
	 * 通过读取文件并获取其width及height的方式，来判断判断当前文件是否图片。
	 * 
	 * @param imageFile
	 * @return
	 */
	public static boolean isImage(File imageFile) {
		if (!imageFile.exists()) {
			return false;
		}
		Image img = null;
		try {
			img = ImageIO.read(imageFile);
			if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			img = null;
		}
	}

	public String copyFolder(String oldPath, String newPath, String newFileName) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		String filePath = null;
		try {
			File mFile = new File(newPath);
	        if(!mFile .exists()){
	        	(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
	        }
	        String[] types = oldPath.split("\\.");
	        filePath = newPath + "/" + new Date().getTime() + newFileName + "." + types[types.length - 1];
	        fis = new FileInputStream(oldPath);
	        fos = new FileOutputStream(filePath);
	        int len = 0;
	        byte[] b = new byte[fis.available()];
	        while ((len = fis.read(b)) != -1) {
	            fos.write(b, 0, len);
	        }
		} catch (Exception e) {
			filePath = null;
			e.printStackTrace();
		}finally {
			try {
				if(fos != null) {
					fos.close();
				} 
				if(fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filePath;
	}

}
