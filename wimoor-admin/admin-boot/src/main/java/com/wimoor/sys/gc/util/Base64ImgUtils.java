package com.wimoor.sys.gc.util;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 图片/文件 待定保留
 *
 * @author wangsong
 */
@Slf4j
public class Base64ImgUtils {


    /**
     * file 转base64
     *
     * @param file
     * @return java.lang.String
     * @author wangsong
     * @date 2021/3/3 0003 15:00
     * @version 1.0.1
     */
    public static String file2Base64(File file) {
        if (file == null) {
            return null;
        }
        String base64 = null;
        try (FileInputStream is = new FileInputStream(file)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = is.read(b, 0, b.length)) != -1) {
                baos.write(b, 0, len);
            }
            byte[] buffer = baos.toByteArray();
            base64 = Base64.encode(buffer);
        } catch (IOException e) {
            log.error(e.toString());
        }
        return base64;
    }


    /**
     * base 64 转 file
     *
     * @param base64
     * @return java.io.File
     * @author wangsong
     * @date 2021/3/3 0003 15:00
     * @version 1.0.1
     */
    public static File base64ToFile(String base64) {
        if (base64 == null || "" .equals(base64)) {
            return null;
        }
        byte[] buff = Base64.decode(base64);
        File file = null;
        try {
            file = File.createTempFile("tmp", null);
        } catch (IOException e) {
            log.error(e.toString());
        }
        try (FileOutputStream fout = new FileOutputStream(file)) {
            fout.write(buff);
        } catch (IOException e) {
            log.error(e.toString());
        }
        return file;
    }
}
