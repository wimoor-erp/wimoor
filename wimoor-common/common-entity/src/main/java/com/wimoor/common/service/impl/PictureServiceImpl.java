package com.wimoor.common.service.impl;


import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.GeneralUtil;
import com.wimoor.common.mapper.PictureMapper;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.pojo.entity.Picture;
import com.wimoor.common.service.IPictureService;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
 

@Service("pictureService")
@RequiredArgsConstructor
public class PictureServiceImpl extends ServiceImpl<PictureMapper,Picture> implements  IPictureService {
 
    final OSSApiService ossApiService;
    final FileUpload fileUpload;
	public static String userImgPath = "/sys/photos/userImg/";
	public static String shopPhotoPath = "/sys/photos/shopImg/";
	public static String materialImgPath = "/erp/photos/materialImg/";
	public static String productImgPath = "/amz/photos/productImg/";
	public static String logoImgPath = "/amz/photos/logoImg/";
	public static String storeImgPath = "/amz/photos/storeImg/";
	public static String reviewPath = "/amz/photos/reviews/";
	public static String customsImgPath = "/amz/photos/customs/";
	public static String transFilePath = "/erp/file/trans/";
	
	
	String destinationPathBucketName(String destinationPath){
		if(!destinationPath.contains(ossApiService.getBucketName())) {
			 if(destinationPath.substring(0,1).equals("/")) {
				 destinationPath=ossApiService.getBucketName()+destinationPath;
			 }else {
				 destinationPath=ossApiService.getBucketName()+"/"+destinationPath;
			 }
		}
		return destinationPath;
	}
	public Picture uploadPicture(String imgsrc, String destinationPath, String pictureid) throws IOException {
		String ftppath = "";
		Picture picture = null;
		String oldimageName = null;
		String oldlocation=null;
		destinationPath=destinationPathBucketName(destinationPath);
		if (!StrUtil.isEmpty(pictureid)) {
			picture = this.getById(pictureid);
			if (picture != null && picture.getLocation() != null) {
				String[] imageNames = picture.getLocation().split("/");
				if (imageNames.length > 0) {
					oldlocation= picture.getLocation();
					oldimageName = imageNames[imageNames.length - 1];
				}
			}
		}
		if (destinationPath.indexOf((ossApiService.getBucketName()+"/")) == 0) {
			ftppath = destinationPath.substring((ossApiService.getBucketName()+"/").length(), destinationPath.length());
			if (ftppath.charAt(ftppath.length() - 1) == '/') {
				ftppath = ftppath.substring(0, ftppath.length() - 1);
			}
		}
		if (destinationPath.charAt(destinationPath.length() - 1) != '/') {
			destinationPath = destinationPath + "/";
		}
		if (imgsrc.contains("data:image/") && imgsrc.contains("base64,")) {
			String ext = imgsrc.substring(imgsrc.indexOf("data:image/") + "data:image/".length(), imgsrc.indexOf(";"));// 图片后缀
			String base64ImgData = imgsrc.substring(imgsrc.indexOf("base64,") + "base64,".length());// 图片数据
			byte[] bs = GeneralUtil.decryptBASE64(base64ImgData);
			long date = new Date().getTime();
			String imageName = date + "_after." + ext;
			String filePath2 = System.getProperty("java.io.tmpdir") + "/" + imageName;
			try {
				Thumbnails.of(new ByteArrayInputStream(bs)).scale(1f).outputQuality(0.3f).toFile(filePath2);
			} catch (Exception e) {
				ossApiService.putObject(ossApiService.getBucketName(), ftppath+"/"+imageName, new ByteArrayInputStream(bs));
			}
			try {
				ossApiService.putObject(ossApiService.getBucketName(), ftppath+"/"+imageName, new FileInputStream(filePath2));
				if (picture != null) {
					picture.setLocation(destinationPath + imageName);
					this.baseMapper.updateById(picture);
				} else {
					picture = new Picture();
					picture.setLocation(destinationPath + imageName);
					this.baseMapper.insert(picture);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			// 清理没有用的图片
			if (oldimageName != null && !oldimageName.equals(imageName)) {
				List<Picture> list = this.selectByImageName(oldlocation);
				if (list == null || list.size() == 0) {
					try {
						ossApiService.removeObject(ossApiService.getBucketName(), ftppath+oldimageName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			return this.getById(pictureid);
		}
		return picture;
	}

	public Picture uploadPicture(InputStream dataInputStream, String sourcePath, String destinationPath, String imageName, String pictureid) throws IOException {
		Picture picture = null;
		if (!GeneralUtil.isEmpty(pictureid)) {
			picture = this.getById(pictureid);
		} 
		destinationPath=destinationPathBucketName(destinationPath);
		Boolean flag = false;
		String oldimageName = null;
		if (picture != null && picture.getLocation() != null) {
			String[] imageNames = picture.getLocation().split("/");
			if (imageNames.length > 0) {
				oldimageName = imageNames[imageNames.length - 1];
			}
		}
		String path = "";
		if (destinationPath.indexOf((ossApiService.getBucketName()+"/")) == 0) {
			path = destinationPath.substring((ossApiService.getBucketName()+"/").length(), destinationPath.length());
			if (path.charAt(path.length() - 1) == '/') {
				path = path.substring(0, path.length() - 1);
			}
		}
		if (destinationPath.charAt(destinationPath.length() - 1) != '/') {
			destinationPath = destinationPath + "/";
		}
		flag=ossApiService.putObject(ossApiService.getBucketName(), path+"/"+imageName, dataInputStream);
		if (flag) {
			if (picture != null) {
				if (!GeneralUtil.isEmpty(sourcePath)) {
					picture.setUrl(sourcePath);
				}
				picture.setLocation(destinationPath + imageName);
				this.baseMapper.updateById(picture);
			} else {
				picture = new Picture();
				if (!GeneralUtil.isEmpty(sourcePath)) {
					picture.setUrl(sourcePath);
				}
				picture.setLocation(destinationPath + imageName);
				this.baseMapper.insert(picture);
			}
		} else {
			if (picture != null) {
				if (GeneralUtil.isNotEmpty(sourcePath)) {
					picture.setUrl(sourcePath);
				}
				picture.setLocation(null);
				this.baseMapper.updateById(picture);
			} else {
				picture = new Picture();
				if (GeneralUtil.isNotEmpty(sourcePath)) {
					picture.setUrl(sourcePath);
				}
				this.baseMapper.insert(picture);
			}
		}
		// 清理没有用的图片
		if (oldimageName != null && !oldimageName.equals(imageName) && !oldimageName.contains("no-img-sm")) {
			//List<Picture> list = this.selectByImageName(oldlocation);
				try {
					ossApiService.removeObject(ossApiService.getBucketName(), path+"/"+oldimageName);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return picture;
	}

	private List<Picture> selectByImageName(String oldlocation) {
		return this.baseMapper.selectByImageName(oldlocation);
	}

	public Picture downloadPicture(String resourceUrl, String destinationPath, String pictureid) {
		if (resourceUrl == null || destinationPath == null)
			return null;
		Picture picture = null;
		InputStream openStream =null;
		destinationPath=destinationPathBucketName(destinationPath);
		try {
			  openStream = new URL(resourceUrl).openStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			    e.printStackTrace();
				picture = this.getById(pictureid);
			if(picture!=null) {
			    picture.setUrl(resourceUrl);
			    this.baseMapper.updateById(picture);
			} else {
				picture = new Picture();
				picture.setUrl(resourceUrl);
				this.baseMapper.insert(picture);
			}
			return picture;
		}
		String imageName = null;
		if (GeneralUtil.isNotEmpty(resourceUrl)) {
			String[] imageNames = resourceUrl.split("/");
			if (imageNames.length > 0) {
				imageName = imageNames[imageNames.length - 1];
				imageName= imageName.split("\\.")[1];
				Double rand =   (Math.random()*1000);
				imageName=(new Date().getTime()+""+rand.intValue())+"."+imageName;
			}
		}
		try {
			picture = uploadPicture(openStream, resourceUrl, destinationPath, imageName, pictureid);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
			if(openStream!=null) {
				openStream.close(); 
			}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return picture;
	}

	public void downloadPictureResponse(String pictureid, String defaultImage, OutputStream os)   {
		String filePath = null;
		Picture picture = this.getById(pictureid);// id为从页面获取photo id

		if (GeneralUtil.isEmpty(defaultImage)) {
			defaultImage = "default.png";
		}
		if (picture == null || picture.getLocation() == null) {
			filePath = PictureServiceImpl.userImgPath + defaultImage;
		} else {
			filePath = picture.getLocation();
		}
		try {
			URL url = new URL(fileUpload.getPictureImage(filePath));
			// 打开链接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置请求方式为"GET"
			conn.setRequestMethod("GET");
			// 超时响应时间为5秒
			conn.setConnectTimeout(5 * 1000);
			// 通过输入流获取图片数据
			InputStream inStream = conn.getInputStream();
			// 得到图片的二进制数据，以二进制封装得到数据，具有通用性
			byte[] buffer = new byte[1024];
			// 每次读取的字符串长度，如果为-1，代表全部读取完毕
			int len = 0;
			// 使用一个输入流从buffer里把数据读取出来
			while ((len = inStream.read(buffer)) != -1) {
				// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
				os.write(buffer, 0, len);
			}
			// 关闭输入流
			inStream.close();
			// 把outStream里的数据写入内存
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public Picture downloadPicturelocal(String pictureid, String sourcePath, String destinationPath) throws Exception {
		Picture picture = null;
		String uri = null;
		String imageName = null;
		destinationPath=destinationPathBucketName(destinationPath);
		if (GeneralUtil.isNotEmpty(pictureid)) {
			picture = this.getById(pictureid);
			if (picture != null && GeneralUtil.isNotEmpty(picture.getLocation())) {
				uri = picture.getLocation();
			} else {
				uri = picture.getUrl();
			}
			String[] imageNames = uri.split("/");
			if (imageNames.length > 0)
				imageName = imageNames[imageNames.length - 1];
		} else {
			picture = new Picture();
			String[] imageNames = sourcePath.split("/");
			if (imageNames.length > 0)
				imageName = imageNames[imageNames.length - 1];
		}
		picture.setLocation(destinationPath + imageName);
	    destinationPath=buildDestinationPath(destinationPath);
		try {
			ossApiService.putObject(ossApiService.getBucketName(),destinationPath+ "/"+imageName, new URL(sourcePath).openStream());
		} catch (Exception e) {
			return null;
		}
		if (GeneralUtil.isEmpty(pictureid)) {
			this.baseMapper.insert(picture);
		} else {
			this.baseMapper.updateById(picture);
		}
		return picture;
	}
    public String getSourceFileName(String sourcePath) {
    	String[] imageNames = sourcePath.split("\\\\");
	    return  imageNames[imageNames.length - 1];
    }
    public String createRandFileName(String imageName) {
    	imageName= imageName.split("\\.")[1];
		Double rand =   (Math.random()*1000);
		imageName=(new Date().getTime()+""+rand.intValue())+"."+imageName;
		return imageName;
    }
    public String buildDestinationPath(String destinationPath) {
    	 if (destinationPath.indexOf((ossApiService.getBucketName()+"/")) == 0) {
				destinationPath = destinationPath.substring((ossApiService.getBucketName()+"/").length(), destinationPath.length());
				if (destinationPath.charAt(destinationPath.length() - 1) == '/') {
					destinationPath = destinationPath.substring(0, destinationPath.length() - 1);
				}
	       }
    	 return destinationPath;
    }
	public Picture downloadPictureMaterial(String pictureid, String sourcePath, String destinationPath) throws Exception {
		Picture picture = null;
		String uri = null;
		String imageName = null;
		destinationPath=destinationPathBucketName(destinationPath);
		String oldname=null;
		if (GeneralUtil.isNotEmpty(pictureid)) {
			picture = this.getById(pictureid);
			if (picture != null) {
				if(GeneralUtil.isNotEmpty(picture.getLocation())) {
					uri = picture.getLocation();
				}else {
					uri = picture.getUrl();
				}
				String[] imageNames = uri.split("/");
				imageName = imageNames[imageNames.length - 1];
				oldname=imageName;
			} else {
				picture = new Picture();
				imageName =getSourceFileName(sourcePath);
			}
		}else {
			picture = new Picture();
			imageName =getSourceFileName(sourcePath);
		}
			imageName=createRandFileName(imageName);
		    picture.setLocation(destinationPath + imageName);
		    destinationPath=buildDestinationPath(destinationPath);
		try {
			if(StrUtil.isNotEmpty(oldname)) {
				ossApiService.removeObject(ossApiService.getBucketName(), destinationPath+"/"+oldname);
			}
			ossApiService.putObject(ossApiService.getBucketName(),destinationPath+"/"+imageName, new URL("file:\\"+sourcePath).openStream());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (GeneralUtil.isEmpty(pictureid)) {
			this.baseMapper.insert(picture);
		} else {
			this.baseMapper.updateById(picture);
		}
		return picture;
	}
	
}
