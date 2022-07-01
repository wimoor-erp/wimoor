package com.wimoor.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import com.swetake.util.Qrcode;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;
 
public class QRCodeUtil {
	//加密：文字信息 -> 二维码
	public static BufferedImage encoderQRCode(String content, int size) throws Exception{
	    //RenderedImage是一个接口，因此要找到它的实现类 BufferedImage
	    //RenderedImage bufferedImage = null;
	    //代表内存中的一张图片
	    BufferedImage bufferedImage = generateQRCodeCommon(content, size);
	    //设置图片格式，与输出的路径
	    return bufferedImage;
	}

	//产生一个二维码的BufferedImage
	public  static BufferedImage generateQRCodeCommon(String content, int size) throws Exception{
	    //QRCode对象：字符串转为boolean[][]
	    Qrcode qrcode = new Qrcode();
	    //设置二维码的排错率
	    /**
	     * 纠错等级分为
	     * level L : 最大 7% 的错误能够被纠正；
	     * level M : 最大 15% 的错误能够被纠正；
	     * level Q : 最大 25% 的错误能够被纠正；
	     * level H : 最大 30% 的错误能够被纠正；
	     */
	    qrcode.setQrcodeErrorCorrect('M');
	    qrcode.setQrcodeEncodeMode('B');//注意版本信息 N代表数字 、A代表 a-z,A-Z、B代表 其他)
	    qrcode.setQrcodeVersion(size);//尺寸  1-40
	    boolean[][] codeOuts = qrcode.calQrcode(content.getBytes("gbk"));

	    int imgSize = 67 + 12 * (size - 1);

	    BufferedImage bufferedImage = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
	    //生成一个画板
	    Graphics2D graphics = bufferedImage.createGraphics();
	   //graphics.setBackground(Color.WHITE);//将画板的背景色设置为白色
	    graphics.clearRect(0, 0, imgSize, imgSize);//初始化
	   // graphics.setColor(Color.BLACK);//设置画板上图像的颜色

	    int pixoff = 2;
	    for(int j = 0;j<codeOuts.length;j++){
	        for(int i = 0;i<codeOuts.length;i++){
	            if (codeOuts[j][i]) {
	                graphics.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
	            }
	        }
	    }
	    
	    //增加logo
	    InputStream is = new ClassPathResource("image/icon.png").getInputStream();
	    Image logo = ImageIO.read(is);
	    int maxWidth = bufferedImage.getWidth();
	    int maxHeight = bufferedImage.getHeight();
	    graphics.drawImage(logo, imgSize/5*2, imgSize/5*2, maxWidth/5, maxHeight/5, null);
	    graphics.dispose();//释放空间
	    return bufferedImage;
	}
	
	//解密
	public static String decoderQRCode(BufferedImage bufferedImage) throws IOException {
	    //解密
	    QRCodeDecoder qrCodeDecoder = new QRCodeDecoder();
	    QRCodeImage qrCodeImage = new QRCodeImageImpl(bufferedImage);
	    byte[] decode = qrCodeDecoder.decode(qrCodeImage);
	    bufferedImage.flush();
	    return new String(decode, "gbk");
	}
}
