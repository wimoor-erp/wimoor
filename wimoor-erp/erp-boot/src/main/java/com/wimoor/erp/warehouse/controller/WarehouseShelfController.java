package com.wimoor.erp.warehouse.controller;



import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.result.Result;
import com.wimoor.common.service.impl.SystemControllerLog;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.erp.warehouse.pojo.entity.ErpWarehouseAddress;
import com.wimoor.erp.warehouse.pojo.entity.WarehouseShelf;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfTreeVo;
import com.wimoor.erp.warehouse.pojo.vo.WarehouseShelfVo;
import com.wimoor.erp.warehouse.service.IErpWarehouseAddressService;
import com.wimoor.erp.warehouse.service.IWarehouseShelfService;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 仓库货柜 前端控制器
 * </p>
 *
 * @author wimoor team
 * @since 2022-03-19
 */
@Api(tags = "库位接口")
@SystemControllerLog("库位模块")
@RestController
@RequestMapping("/api/v1/warehouse/shelf")
@RequiredArgsConstructor
public class WarehouseShelfController {
 
	final IWarehouseShelfService iWarehouseShelfService;
	final IErpWarehouseAddressService iErpWarehouseAddressService;
	@Resource
    QrConfig qrconfig;
    @ApiOperation("仓库库位树")
    @GetMapping("/list")
    public Result<List<WarehouseShelfTreeVo>> list(String warehouseid) {
    	UserInfo user=UserInfoContext.get();
        return Result.success(iWarehouseShelfService.getAllTree(user,warehouseid));
    }
    
	
    @ApiOperation(value = "新增仓库库位")
    @SystemControllerLog("新增仓库库位")
    @PostMapping("/add")
    public Result<Boolean> add(@RequestBody List<WarehouseShelf> shelflist) {
     	if(shelflist==null||shelflist.size()==0) {
    		return Result.failed("仓位数量不能为0");
    	}
        boolean status = iWarehouseShelfService.save(shelflist);
        return Result.judge(status);
    }
    

    @ApiOperation("查询当前库位详细信息，包含所有子库位信息")
    @GetMapping("/{id}/detail")
    public Result<List<WarehouseShelfVo>> detail(@ApiParam("查询当前库位中的所有子库位") @PathVariable("id") String id) {
    	UserInfo user=UserInfoContext.get();
        return Result.success(iWarehouseShelfService.detail(user,id));
    }
    
    @ApiOperation("查询当前库位详细信息，包含所有子库位信息")
    @GetMapping("/{id}/shelfdetail")
    public Result<WarehouseShelf> shelfdetail(@ApiParam("查询库位") @PathVariable("id") String id) {
        return Result.success(iWarehouseShelfService.getById(id));
    }
    
    
    @ApiOperation("查询当前库位中的基础信息")
    @GetMapping("/getShelfInfo")
    public Result<WarehouseShelfVo> getShelfInfoAction(@ApiParam("仓位ID")@RequestParam String shelfid,@ApiParam("仓库地址Number")@RequestParam String addressnum,@ApiParam("库位treepath")@RequestParam String shelftreepath){
    	UserInfo user=UserInfoContext.get();
    	ErpWarehouseAddress address = iErpWarehouseAddressService.getByNumber(user.getCompanyid(),addressnum);
    	if(!StrUtil.isEmpty(shelfid)) {
    		WarehouseShelfVo res =iWarehouseShelfService.getShelfInfo(user, shelfid);
    		return Result.success(res);
    	}else {
    		if(address==null) {
        		throw new BizException("无法找到地址信息，请确保您的账号下存在此地址编码"+addressnum);
        	}
    		WarehouseShelfVo res = iWarehouseShelfService.getShelfInfo(user,address,shelftreepath);
        	return Result.success(res);
    	}
    }
    
    @ApiOperation("查询当前仓位中的所有子库位")
    @GetMapping("/{warehouseid}/detailwarehouse")
    public Result<List<WarehouseShelfVo>> detailWarehouse(@ApiParam("查询当前仓位中的所有子库位") @PathVariable("warehouseid") String warehouseid) {
    	UserInfo user=UserInfoContext.get();
        return Result.success(iWarehouseShelfService.detailWarehouse(user,warehouseid));
    }
    @ApiOperation("查询")
    @GetMapping("/{warehouseid}/warehousesum")
    public Result<WarehouseShelfVo> warehouseSum(@ApiParam("查询当前仓位中的所有子库位") @PathVariable("warehouseid") String warehouseid) {
    	UserInfo user=UserInfoContext.get();
        return Result.success(iWarehouseShelfService.detailWarehouseSum(user,warehouseid));
    }
    
    @ApiOperation("修改库位")
    @SystemControllerLog("修改库位")
    @PutMapping("/modify")
    public Result<Object> modify(@ApiParam("修改库位") @Validated @RequestBody WarehouseShelf shelf) {
        if (shelf.getId() == null) {
            return Result.failed("库位ID不能为空");
        }
    	UserInfo user=UserInfoContext.get();
        shelf.setOperator(new BigInteger(user.getId()));
        iWarehouseShelfService.updateById(shelf);
        return Result.success();
    }
    
    @ApiOperation("获取库位二维码")
    @PostMapping("/getQRCode/{shelfid}")
    public  void getQRCode(@ApiParam("库位ID") @PathVariable("shelfid") String shelfid,HttpServletResponse resp) {
		WarehouseShelf shelf = iWarehouseShelfService.getById(shelfid);
		ErpWarehouseAddress address = iErpWarehouseAddressService.getById(shelf.getAddressid());
    	 String content = "https://www.wimoor.com/ws/"+address.getNumber()+"/"+shelf.getTreepath();//内容信息
         ServletOutputStream os = null;
		try {
			resp.reset();
			resp.setHeader("Content-Disposition","attachment;fileName=qrcode.jpg");
			resp.setHeader("Access-Control-Allow-Origin", "*");
			resp.setHeader("Access-Control-Expose-Headers", "filename");
			resp.setContentType("octet-stream");// image/jpeg
			resp.setHeader("filename","二维码.jpg");
			resp.setContentType("application/force-download");// 设置强制下载不打开
			os = resp.getOutputStream();
			qrconfig.setWidth(170);
			qrconfig.setHeight(170);
			BufferedImage imagebuffer =	QrCodeUtil.generate(content, qrconfig);
		    ImageIO.write(imagebuffer, "png", os);
		    os.flush();
		    os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("文件读取错误");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("文件读取错误");
		}
    }
    
    @ApiOperation("获取库位二维码PDF")
    @GetMapping("/getQRCodePdf")
    public  void getQRCodePdf(@ApiParam("库位ID") @RequestParam String shelfid,
    		                  @ApiParam("父库位ID") @RequestParam String parentshelfid,
    		                  HttpServletResponse resp) {
		try {
			resp.addHeader("Content-Disposition", "attachment;fileName=QRCode" + System.currentTimeMillis() + ".pdf");// 设
			resp.setHeader("Access-Control-Allow-Origin", "*");
			resp.setHeader("Access-Control-Expose-Headers", "filename");
			resp.setHeader("filename","二维码.pdf");
			resp.setContentType("application/force-download");// 设置强制下载不打开
			Document document = new Document(PageSize.LETTER);
			PdfWriter.getInstance(document, resp.getOutputStream());
			document.open();
			document.addTitle("ID.NET");
			document.addAuthor("dotuian");
			document.addSubject("This is the subject of the PDF file.");
			document.addKeywords("This is the keyword of the PDF file.");
			if(StrUtil.isNotEmpty(shelfid)) {
					//document.newPage();
					WarehouseShelf shelf = iWarehouseShelfService.getById(shelfid);
					ErpWarehouseAddress address = iErpWarehouseAddressService.getById(shelf.getAddressid());
					String showname = shelf.getNumber()+"-"+shelf.getName();
					String path = new ClassPathResource("font/SIMYOU.TTF").getPath();
					BaseFont baseFont = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
					com.itextpdf.text.Font font = new com.itextpdf.text.Font(baseFont);
					font.setSize(50);
					Paragraph p = new Paragraph(showname,font);// 设置字体，解决中文显示不了的问题
					p.setAlignment(Element.ALIGN_CENTER);
					p.setSpacingAfter(10);
					document.add(p);
					String content = "https://www.wimoor.com/ws/"+address.getNumber()+"/"+shelf.getTreepath();//内容信息
					qrconfig.setWidth(300);
					qrconfig.setHeight(300);
					BufferedImage imagebuffer =	QrCodeUtil.generate(content, qrconfig);
		            Image image = Image.getInstance(imagebuffer , null); 
		            image.setAlignment(Element.ALIGN_CENTER);
				    document.add(image);
			}else if(StrUtil.isNotEmpty(parentshelfid)) {
				    UserInfo user=UserInfoContext.get();
					LambdaQueryWrapper<WarehouseShelf> wrapperShelf=new LambdaQueryWrapper<WarehouseShelf>();
	        		wrapperShelf.eq(WarehouseShelf::getShopid, user.getCompanyid());
	        		wrapperShelf.eq(WarehouseShelf::getParentid,new BigInteger(parentshelfid));
	        		wrapperShelf.eq(WarehouseShelf::getIsdelete, false);
					List<WarehouseShelf> shelflist = iWarehouseShelfService.list(wrapperShelf);
					for(WarehouseShelf shelf:shelflist) {
	                        if(shelf==null)continue;
	                        ErpWarehouseAddress address = iErpWarehouseAddressService.getById(shelf.getAddressid());
	    					String showname = shelf.getNumber()+"-"+shelf.getName();
	    					String path = new ClassPathResource("font/SIMYOU.TTF").getPath();
	    					BaseFont baseFont = BaseFont.createFont(path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
	    					com.itextpdf.text.Font font = new com.itextpdf.text.Font(baseFont);
	    					font.setSize(50);
	    					Paragraph p = new Paragraph(showname,font);// 设置字体，解决中文显示不了的问题
	    					p.setAlignment(Element.ALIGN_CENTER);
	    					p.setSpacingAfter(10);
	    					document.add(p);
	    					String content = "https://www.wimoor.com/ws/"+address.getNumber()+"/"+shelf.getTreepath();//内容信息
	    					qrconfig.setWidth(300);
	    					qrconfig.setHeight(300);
	    					BufferedImage imagebuffer =	QrCodeUtil.generate(content, qrconfig);
	    		            Image image = Image.getInstance(imagebuffer , null); 
	    		            image.setAlignment(Element.ALIGN_CENTER);
	    				    document.add(image);
						    document.newPage();
					}
				}
				document.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("文件读取错误");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BizException("文件读取错误");
		}
	 
    }
	
    /**
     * 删除库位
     *
     * @return
     */
    @ApiOperation("删除库位")
    @SystemControllerLog("删除库位")
    @DeleteMapping("/del")
    public Result<Object> del(@ApiParam("库位ID") @RequestParam("ids") String ids) {
        iWarehouseShelfService.logicDeleteById(ids);
        
        return Result.success();
    }
    
}

