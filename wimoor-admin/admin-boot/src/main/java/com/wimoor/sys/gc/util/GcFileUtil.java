package com.wimoor.sys.gc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.wimoor.sys.gc.config.GcConfig;
import com.wimoor.sys.gc.config.model.GcFilePath;
import com.wimoor.sys.gc.constant.BooleanConst;
import com.wimoor.sys.gc.constant.TpParamConstant;

import lombok.extern.slf4j.Slf4j;

/**
 * 代码生成文件处理相关
 */
@Slf4j
public class GcFileUtil {

    /**
     * 获得BufferedReader（根据url读取模版文档），BufferedWriter （写入文件流），path生成的件路径
     *
     * @param gcConfig = 代码生成的相关配置信息
     */
    public static Map<String, Object> getBrBwPath(GcConfig gcConfig, String key) {
        // 获取指定对象的配置
        GcFilePath gcFilePath = gcConfig.getTemplatePathMap().get(key);
        //
        String path = gcFilePath.getPath();
        String templatePath = gcFilePath.getTemplatePath();
        String fileName = gcFilePath.getName();
        // 获取路径并创建目录
        String pathFile = path.substring(0, path.lastIndexOf("/"));
        GcFileUtil.mkdirFile(pathFile);
        Map<String, Object> brBw = new HashMap<>(2);
        try {
            // 获取代码模版文件
            BufferedReader br = GcFileUtil.getUrlDetail(templatePath);
            // 生成保存生成后的代码文件
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            brBw.put("br", br);
            brBw.put("bw", bw);
            // 保存生成后文件访问地址
            gcConfig.addVisitPath(fileName, path);
        } catch (Exception e) {
            log.error(e.toString());
        }
        return brBw;
    }


    /**
     * 读取文件 br 输入内容到新的文件 bw中
     *
     * @param gcConfig = 代码生成的相关配置信息
     * @param brBwPath = 模板文件流 br， 生成后的新文件对象 bw
     */
    public static void replacBrBwWritee(GcConfig gcConfig, Map<String, Object> brBwPath) {
        // 获取到getBrBwPath 方法拼装的数据
        BufferedReader br = (BufferedReader) brBwPath.get("br");
        BufferedWriter bw = (BufferedWriter) brBwPath.get("bw");
        try {
            String newLine = "";
            String line = null;
            // 碰到第一个 filterCrud  到下一个 filterCrud 之间的数据进行过滤,不生成到文件中
            boolean filterCrud = false;
            // 获取是否生成crud 方法配置
            String filterCrudConfig = gcConfig.getDefaultTemplateParam(TpParamConstant.FILTER_CRUD);
            while ((line = br.readLine()) != null) {
                // 过滤写入的文件内容
                if (("{" + TpParamConstant.FILTER_CRUD + "}").equals(line)) {
                    if (BooleanConst.TRUE_STE.equals(filterCrudConfig)) {
                        filterCrud = !filterCrud;
                    }
                    bw.write("");
                    bw.newLine();
                }

                // 只生成不属于过滤行的模板数据
                if (!("{" + TpParamConstant.FILTER_CRUD + "}").equals(line) && !filterCrud) {
                    // 内容替换并写入
                    newLine = GcReplacUtil.replaceParams(gcConfig.getDefaultTemplateParam(), gcConfig.getTemplateParam(), line);
                    bw.write(newLine);
                    bw.newLine();
                }
                bw.flush();
            }
            bw.close();
        } catch (IOException e) {
            log.error(e.toString());
        }
    }


    /**
     * 通过url 获取文件流 获取链接地址的字符数据，wichSep是否换行标记
     *
     * @param urlStr
     * @return java.lang.String
     */
    private static BufferedReader getUrlDetail(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.connect();
        InputStream cin = httpConn.getInputStream();
        return new BufferedReader(new InputStreamReader(cin, StandardCharsets.UTF_8));
    }


    /**
     * 判断文件路径是否存在，不存在创建
     *
     * @param path
     * @return void
     */
    public static void mkdirFile(String path) {
        // 不存在创建文件夹
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 判断文件路径是否存在，存在删除
     *
     * @param path
     * @return void
     */
    public static void delFile(String path) {
        // 不存在创建文件夹
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }
}
