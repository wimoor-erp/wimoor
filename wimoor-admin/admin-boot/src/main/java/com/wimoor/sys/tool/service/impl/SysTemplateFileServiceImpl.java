package com.wimoor.sys.tool.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wimoor.common.mvc.FileUpload;
import com.wimoor.common.service.IPictureService;
import com.wimoor.common.service.ObjectHandler;
import com.wimoor.common.service.impl.StorageLargeService;
import com.wimoor.common.service.impl.StorageService;
import com.wimoor.common.user.UserInfo;
import com.wimoor.sys.tool.mapper.SysTemplateFileMapper;
import com.wimoor.sys.tool.pojo.entity.SysTemplateFile;
import com.wimoor.sys.tool.service.ISysTemplateFileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author liufei
* @description 针对表【t_sys_tool_large_file(用于存放Image)】的数据库操作Service实现
* @createDate 2025-10-11 11:42:11
*/
@Service
@RequiredArgsConstructor
public class SysTemplateFileServiceImpl extends ServiceImpl<SysTemplateFileMapper, SysTemplateFile> implements ISysTemplateFileService {

    final StorageLargeService storageLargeService;
    final IPictureService pictureService;
    final StorageService storageService;
    final FileUpload fileUpload;

    @Override
    public List<Map<String, Object>> getRecord(String shopid) {
        List<Map<String,Object>> list = this.baseMapper.selectRecordByShop(shopid);
        for(Map<String,Object> map : list){
                map.put("filepath",fileUpload.getPictureImage(map.get("filepath")));
        }
        return list;
    }


    public Map<String,Object> uploadFile(UserInfo user,String ftype, InputStream inputStream, String filename) {
        Map<String,Object> map=new HashedMap<String, Object>();
        String shopid=user.getCompanyid();
        try {
            SysTemplateFile shipcustoms = this.baseMapper.findByNameAndShopid(shopid,filename);
            String filePath = "/amz/photos/"+ftype+"/" + shopid;
            String iamge=null;
            if(shipcustoms!=null && shipcustoms.getFilepath()!=null) {
                iamge=shipcustoms.getFilepath();
            }
                if(shipcustoms!=null) {
                    filePath=upload(inputStream, shipcustoms.getFilepath(), filePath, filename, iamge);
                    shipcustoms.setFtype(ftype);
                    shipcustoms.setFilepath(filePath);
                    shipcustoms.setFilename(filename);
                    shipcustoms.setOperator(new BigInteger(user.getId()));
                    shipcustoms.setOpttime(new Date());
                    shipcustoms.setUserid(new BigInteger(user.getId()));
                    this.baseMapper.updateById(shipcustoms);
                }else {
                    filePath= upload(inputStream, null, filePath, filename, iamge);
                    shipcustoms = new SysTemplateFile();
                    shipcustoms.setFtype(ftype);
                    shipcustoms.setCreatedate(new Date());
                    shipcustoms.setCreator(new BigInteger(user.getId()));
                    shipcustoms.setFilepath(filePath);
                    shipcustoms.setFilename(filename);
                    shipcustoms.setOperator(new BigInteger(user.getId()));
                    shipcustoms.setOpttime(new Date());
                    shipcustoms.setShopid(new BigInteger(shopid));
                    shipcustoms.setUserid(new BigInteger(user.getId()));
                    this.baseMapper.insert(shipcustoms);
                }
                map.put("msg", "文件上传成功！");
                map.put("code", "ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    String destinationPathBucketName(String destinationPath){
        if(!destinationPath.contains(storageService.getBucketName())) {
            if(destinationPath.substring(0,1).equals("/")) {
                destinationPath=storageService.getBucketName()+destinationPath;
            }else {
                destinationPath=storageService.getBucketName()+"/"+destinationPath;
            }
        }
        return destinationPath;
    }
    public String upload(InputStream dataInputStream, String oldpath, String destinationPath, String imageName, String pictureid) throws IOException {
        destinationPath=destinationPathBucketName(destinationPath);
        Boolean flag = false;
        String path = "";
        if (destinationPath.indexOf((storageService.getBucketName()+"/")) == 0) {
            path = destinationPath.substring((storageService.getBucketName()+"/").length(), destinationPath.length());
            if (path.charAt(path.length() - 1) == '/') {
                path = path.substring(0, path.length() - 1);
            }
        }
        if (destinationPath.charAt(destinationPath.length() - 1) != '/') {
            destinationPath = destinationPath + "/";
        }
        flag=storageService.putObject(storageService.getBucketName(), path+"/"+imageName, dataInputStream);

        // 清理没有用的图片
        if (oldpath != null && !oldpath.equals(path+"/"+imageName) ) {
            try {
                if(destinationPath.indexOf((storageService.getBucketName()+"/")) == 0) {
                    storageService.removeObject(storageService.getBucketName(), oldpath);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return storageService.getBucketName()+"/"+path+"/"+imageName;
    }
    @Override
    public void downloadFile(ServletOutputStream fOut, UserInfo userinfo,  String templateid,Map<String,Object> params) {
            // TODO Auto-generated method stub
            List<File> srcFiles=new ArrayList<File>();
            if(StrUtil.isEmpty(templateid)) {
                return;
            }
            SysTemplateFile shipfile = this.baseMapper.selectById(templateid);
            if(shipfile!=null && shipfile.getFilepath()!=null) {
                String location =  shipfile.getFilepath().replace("wimoor-file/","");
                storageService.getObject(storageService.getBucketName(), location, new ObjectHandler() {
                    @Override
                    public void treatReader(InputStream is, Map<String, Object> param)   {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        Workbook workbook = null;
                        try {
                            workbook = WorkbookFactory.create(is);
                            CellStyle style=workbook.createCellStyle();
                            style.setAlignment(HorizontalAlignment.LEFT);
                            org.apache.poi.ss.usermodel.Font font = workbook.createFont();
                            font.setFontHeightInPoints((short)10);
                            style.setFont(font);
                            style.setWrapText(true);
                            treatTemplate( workbook,  param);
                            workbook.write(fOut);
                            workbook.close();
                        } catch ( IOException e) {
                            throw new RuntimeException(e);
                        }
                        // 创建新的Excel工作薄

                    }
                },params);

        }

    }
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    @Override
    public Map<String, Object> deleteFile(String uploadid) {
        Map<String, Object> maps=new HashedMap<String, Object>();
        SysTemplateFile shipfile = this.baseMapper.selectById(uploadid);
        if(shipfile!=null && StrUtil.isNotEmpty(shipfile.getFilepath())) {
                    storageService.removeObject(storageService.getBucketName(),shipfile.getFilepath());
                    this.baseMapper.deleteById(shipfile);
                    maps.put("code", "ok");
                    maps.put("msg", "删除成功！");
        }else {
            maps.put("code", "fail");
            maps.put("msg", "删除失败！");
        }
        return maps;
    }




    private void treatTemplate(Workbook workbook, Map<String,Object> param)  {
        Map<String,ByteArrayOutputStream> bufferMap=new HashMap<String,ByteArrayOutputStream>();

        for(int sheetindex=0;sheetindex<workbook.getNumberOfSheets();sheetindex++) {
            Sheet sheet = workbook.getSheetAt(sheetindex);
            Row oldsheetRow=null;
            //第一步：列表填充，将列表行模板复制
            for (int i=0;i<=sheet.getLastRowNum();i++){
                Row sheetRow = sheet.getRow(i);
                if (sheetRow != null) {
                    for (int cellnum = 0; cellnum <= sheetRow.getLastCellNum(); cellnum++) {
                        Cell cell = sheetRow.getCell(cellnum);
                        if(cell==null)continue;
                        String cellValue =null;
                        try{
                            cellValue = cell.getStringCellValue().trim();
                        }catch(Exception e){
                            cellValue= String.valueOf(cell.getNumericCellValue());
                        }
                        if(!cellValue.contains("列表")){
                            continue;
                        }
                        String  key=cellValue.split("\\.")[0];
                        key=key+"}";
                        if(  param.containsKey(key)){
                            List<Map<String,Object>>  list= (List<Map<String, Object>>) param.get(key);
                            if(list.size()<=1){
                                continue;
                            }
                            oldsheetRow=sheetRow;
                            sheet.shiftRows(i, sheet.getLastRowNum(), list.size()-1,true,false);
                            for(int rowaddnum=0;rowaddnum<list.size()-1;rowaddnum++) {
                                Row row = sheet.createRow(i+rowaddnum);
                                row.setRowStyle(oldsheetRow.getRowStyle());
                                for (int mcellnum = 0; mcellnum <= oldsheetRow.getLastCellNum(); mcellnum++) {
                                    Cell oldcell = oldsheetRow.getCell(mcellnum);
                                    Cell mycell = row.createCell(mcellnum);
                                    if(oldcell!=null) {
                                        try{
                                            mycell.setCellValue(oldcell.getRichStringCellValue());
                                            mycell.setCellStyle(oldcell.getCellStyle());
                                        }catch(Exception e){
                                            try{
                                                mycell.setCellValue(oldcell.getNumericCellValue());
                                                mycell.setCellStyle(oldcell.getCellStyle());
                                            }catch(Exception e1){
                                                mycell.setCellValue("");
                                                e1.printStackTrace();
                                            }
                                        }
                                    } else {
                                        mycell.setCellValue("");
                                    }
                                }
                            }
                            i=i+list.size()-1;
                            break;
                        }
                    }
                }

            }

            //第二步：字段填充
            int listindex=0;
            for (int i=0;i<=sheet.getLastRowNum();i++){
                Row sheetRow = sheet.getRow(i);
                if (sheetRow == null) {
                    continue;
                }
                boolean needStepList=false;
                for (int cellnum = 0; cellnum <= sheetRow.getLastCellNum(); cellnum++) {
                    Cell cell = sheetRow.getCell(cellnum);
                    if (cell == null) {
                        continue;
                    }
                    String cellValue =null;
                    try{
                        cellValue = cell.getStringCellValue().trim();
                    }catch(Exception e){
                        cellValue= String.valueOf(cell.getNumericCellValue());
                    }
                    if(StrUtil.isNotEmpty(cellValue)) {
                        if(param.get(cellValue)!=null) {
                            cell.setCellValue(param.get(cellValue).toString());
                        }else {
                            if(cellValue.contains("列表")){
                                String key=cellValue.split("\\.")[0];
                                key=key+"}";
                                if(param.containsKey(key)){
                                    List<Map<String, Object>> list = (List<Map<String, Object>>) param.get(key);
                                    if(listindex>=list.size()) {
                                        listindex=0;
                                    }
                                    Map<String, Object> pmap = list.get(listindex);
                                    if(setCellValue(workbook,sheet,cell,pmap,cellValue,bufferMap)) {
                                        needStepList=true;
                                    }
                                }
                            } else if(cellValue.contains("{")) {
                                needStepList=false;
                                setCellValue(workbook,sheet,cell,param,cellValue,bufferMap);
                            }

                        }

                    }

                }
                if(needStepList) {
                    listindex++;
                }
                for(Map.Entry<String,ByteArrayOutputStream> entry:bufferMap.entrySet()) {
                    ByteArrayOutputStream value = entry.getValue();
                    if(value!=null){
                        try {
                            value.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                bufferMap.clear();
            }
        }
    }

    boolean setCellValue(Workbook workbook,Sheet sheet,Cell cell,Map<String, Object> map,String cellValue,Map<String,ByteArrayOutputStream> bufferMap){
        Object value =map.get(cellValue);
        if(value!=null) {
            if(cellValue.contains("图片")) {
                Row imgrow = cell.getRow();
                float nums=50;
                imgrow.setHeightInPoints(nums);
                String image =value!=null?fileUpload.getPictureImage(value):"";
                Drawing<?> draw = sheet.createDrawingPatriarch();
                ClientAnchor anchor = draw.createAnchor(0, 0, 0, 0, cell.getColumnIndex(), cell.getRowIndex(), cell.getColumnIndex()+1, cell.getRowIndex()+1);
                ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
                BufferedImage bufferImg;
                if(bufferMap.containsKey(image)) {
                    byteArrayOut = bufferMap.get(image);
                }else{
                    try {
                        URLConnection conn = new URL(image).openConnection();
                        conn.setConnectTimeout(50000); // 5秒连接超时
                        conn.setReadTimeout(100000);    // 10秒读取超时
                        int retry = 3;
                        while (retry-- > 0) {
                            try {
                                bufferImg = ImageIO.read(conn.getInputStream());
                                if(image.toLowerCase().contains(".jpg")||image.toLowerCase().contains(".jpeg")) {
                                    ImageIO.write(bufferImg, "JPEG", byteArrayOut);
                                    if(byteArrayOut.size()==0){
                                        ImageIO.write(bufferImg, "PNG", byteArrayOut);
                                    }
                                }else{
                                    ImageIO.write(bufferImg, "PNG", byteArrayOut);
                                    if(byteArrayOut.size()==0){
                                        ImageIO.write(bufferImg, "JPEG", byteArrayOut);
                                    }
                                }
                                byteArrayOut.flush();
                                break;
                            } catch (IOException e) {
                                if (retry == 0) throw e;
                                Thread.sleep(1000);
                            }
                        }
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException | InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if(byteArrayOut.size()==0){
                    cell.setCellValue(image);
                }else{
                    org.apache.poi.ss.usermodel.Picture picexcel = draw.createPicture(anchor, workbook.addPicture(byteArrayOut.toByteArray(), Workbook.PICTURE_TYPE_JPEG));
                    picexcel.setLineStyleColor(1, 1, 1);
                }



            }else {
                cell.setCellValue(value.toString());
            }
            return true;
        }else {
            List<String> cellvalueArr = splitValue(cellValue);
            boolean hasdata=false;
            for(int k=0;k<cellvalueArr.size();k++) {
                String paramkey = cellvalueArr.get(k);
                if(StrUtil.isNotEmpty(paramkey)) {
                    if(map.get(paramkey)!=null) {
                        String paramvalue = map.get(paramkey).toString();
                        cellValue=cellValue.replace( paramkey , paramvalue);
                        cell.setCellValue(cellValue);
                        hasdata=true;
                    }else {
                        String paramvalue = "";
                        cellValue=cellValue.replace( paramkey , paramvalue);
                        cell.setCellValue(cellValue);
                    }
                }
            }
            return hasdata;
        }

    }

    private List<String> splitValue(String value) {
        List<String> ls=new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\{([^}]*)\\}");
        Matcher matcher = pattern.matcher(value);
        while(matcher.find()) {
            ls.add(matcher.group());
        }
        return ls;
    }
}




