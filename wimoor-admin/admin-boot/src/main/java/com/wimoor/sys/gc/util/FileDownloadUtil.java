package com.wimoor.sys.gc.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;


/**
 * 文件下载工具类
 */
@SuppressWarnings("all")
@Slf4j
public class FileDownloadUtil {


    /**
     * 下载指定文件
     *
     * @param file     下载文件
     * @param fileName 下载后的文件名
     * @param response
     * @return void
     */
    public static void download(File file, String fileName, HttpServletResponse response) {
        // 下载指定文件
        try {
            //文件流
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream");
            response.addHeader("Content-Length", file.length() + ""); //文件长度
            BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            // 放入outputStream流
            // byte[] buffer = new byte[in.available()];
            byte[] buffer = new byte[1024];
            int len = 0;
            int i = 0;
            // in.read(buffer);
            while ((len = in.read(buffer)) != -1) {
                i = i + len;
                outputStream.write(buffer, 0, len);
            }
            response.addHeader("Content-Length", i + "");
            //最后的内容
            outputStream.write(buffer);
            //响应返回字节长度-无效:
            // response.addHeader("Content-Length", "" + i);//  log.error(i);
            outputStream.flush();
            outputStream.close();
            in.close();
        } catch (IOException e) {
            log.error(e.toString());
        }
    }


    /**
     * url可访问的单文件下载
     * @author wangsong
     * @param response
     * @param filePath 可访问的url
     * @param fileName 下载的文件名
     * @return void
     */
    public static void download(String filePaths, HttpServletResponse response) {
        // 获取文件名称
        String fileName = filePaths.substring(filePaths.lastIndexOf("/") + 1, filePaths.length());
        try {
            // 处理文件名编码问题
            int index = filePaths.lastIndexOf("/");
            String newFilePath = filePaths.substring(0, index + 1);
            String newFileName = filePaths.substring(index + 1);
            newFilePath = newFilePath + URLEncoder.encode(fileName, "utf-8");
            // 下载网络文件
            URL url = new URL(newFilePath);
            //文件流
            URLConnection conn = url.openConnection();
            InputStream in = conn.getInputStream();
            // 设置response的Header
            //response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream");
            response.addHeader("Content-Length", conn.getContentLength() + "");//文件长度
            BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            // 放入outputStream流
            //byte[] buffer = new byte[in.available()];
            byte[] buffer = new byte[1024];
            int len = 0;
            int i = 0;
            //in.read(buffer);
            while ((len = in.read(buffer)) != -1) {
                i = i + len;
                outputStream.write(buffer, 0, len);
            }
            response.addHeader("Content-Length", i + "");
            //最后的内容
            outputStream.write(buffer);
            //响应返回字节长度-无效:response.addHeader("Content-Length", "" + i);//  log.error(i);
            outputStream.flush();
            outputStream.close();
            in.close();
        } catch (IOException e) {
            log.error(e.toString());
        }
    }


    /**
     * Zip 压缩包（url可访问的文件打包下载）
     * @param res         HttpServletResponse
     * @param filePaths  下载路径集, 可访问的url
     * @param zipName     压缩包名 + .zip
     * @return void
     */
    public static void downloadZip(List<String> filePaths, String zipName, HttpServletResponse res) { //String zipName = zipName;       // 压缩包名字
        //项目跟目录
        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!path.exists()) {
                path = new File("");
            }
            //压缩包根目录
            File upload = new File(path.getAbsolutePath(), "static/images/uploaZip");
            if (!upload.exists()) {
                upload.mkdirs();
            }
            //拼接目录
            String zipFilePath = upload + File.separator + zipName + ".zip";
            //创建zip文件输出流 == 压缩文件========================================================
            File zip = new File(zipFilePath);
            if (!zip.exists()) {
                zip.createNewFile();
            }
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
            // 打压缩包
            FileDownloadUtil.zipFile(filePaths, zos);
            zos.close();
            // 设置下载的压缩文件名称 --> java.net.URLEncoder.encode(zipName, "UTF-8")
            res.setContentType("text/html; charset=UTF-8");        // 设置编码字符
            res.setContentType("application/octet-stream");        // 设置内容类型为下载类型
            OutputStream out = res.getOutputStream();              // 创建页面返回方式为输出流，会自动弹出下载框 new String(zipName.getBytes("utf-8"),"ISO8859-1")
            // res.setHeader("Content-disposition", "attachment;filename=123" + zipName);
            res.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(zipName + ".zip", "UTF-8"));
            //创建zip文件输出流==========================================================
            //将打包后的文件写到客户端，输出的方法同上，使用缓冲流输出
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipFilePath));
            byte[] buff = new byte[bis.available()];
            bis.read(buff);
            bis.close();
            out.write(buff);//输出数据文件
            out.flush();//释放缓存
            out.close();//关闭输出流
        } catch (IOException e) {
            log.error(e.toString());
        }
    }


    /**
     * 压缩文件
     *
     * @param filePaths   需要压缩的文件路径集合
     * @throws IOException
     */
    private static String zipFile(List<String> filePaths, ZipOutputStream zos) {
        //循环读取文件路径集合，获取每一个文件的路径
        for (String filePath : filePaths) {
            if (filePath == null || filePath == "") {
                continue;
            }
            // 获取文件名前的路径
            int index = filePath.lastIndexOf("/");
            String newFilePath = filePath.substring(0, index + 1);
            String fileName = filePath.substring(index + 1);

            // 根据文件路径创建文件, 替换文件名称，先获取后缀名
            File inputFile = new File(filePath);  //根据文件路径创建文件
            // String suffixName = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
            // File inputFile = new File(newFilePath + fileName + "1." + suffixName);
            try {
                newFilePath = newFilePath + URLEncoder.encode(fileName, "utf-8");
                URL url = new URL(newFilePath);
                InputStream inputStream = url.openStream();
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                //将文件写入zip内，即将文件进行打包
                zos.putNextEntry(new ZipEntry(inputFile.getName()));
                //写入文件的方法，同上
                int size = 0;
                byte[] buffer = new byte[1024];  //设置读取数据缓存大小
                while ((size = bis.read(buffer)) > 0) {
                    zos.write(buffer, 0, size);
                }
                //关闭输入输出流
                zos.closeEntry();
                bis.close();
            } catch (ZipException | UnsupportedEncodingException | MalformedURLException e) {
                if (e.getMessage().indexOf("duplicate entry:") != -1) {
                    log.error("文件重复：" + filePath);
                } else {
                    log.error("文件下载失败：" + filePath);
                    log.error(e.toString());
                }
            } catch (IOException e) {
                log.error(e.toString());
            }
        }
        return null;
    }
}
