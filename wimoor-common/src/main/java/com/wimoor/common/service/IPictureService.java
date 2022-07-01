package com.wimoor.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wimoor.common.pojo.entity.Picture;

public interface IPictureService extends IService<Picture>  {
	/**
	 * 上传图片到本地
	 * @param src image，base64
	 * @param filePath 保存到本地路径
	 * @return 
	 * @throws IOException 
	 */
	Picture uploadPicture(String src, String filePath,String pictureid) throws IOException;
	public Picture downloadPicture(String urlstr,String path,String pictureid) throws Exception;
	public void downloadPictureResponse(String pictureid,String defaultImage,OutputStream os ) throws IOException ;
	public Picture downloadPicturelocal(String pictureid, String filePath, String ftpPath) throws Exception ;
	public Picture downloadPictureMaterial(String pictureid, String filePath, String ftpPath) throws Exception;
	public Picture uploadPicture(InputStream dataInputStream, String sourcePath, String destinationPath, String imageName, String pictureid) throws IOException;
	
}
