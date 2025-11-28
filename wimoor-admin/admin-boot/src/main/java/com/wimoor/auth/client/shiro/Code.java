package com.wimoor.auth.client.shiro;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
 
public class Code {
    
    // 图片的宽度。
    private int width = 100;
    // 图片的高度。
    private int height = 36;
    // 验证码字符个数
    private int codeCount = 4;
    // 验证码干扰线数
    private int lineCount = 20;
    // 验证码
    private String code = null;
    // 验证码图片Buffer
    private BufferedImage buffImg = null;
    Random random = new Random();
     
    private boolean type = false;
 
    public Code() {
        
    }
 
    public Code(int width, int height) {
        this.width = width;
        this.height = height;
    }
 
    public Code(int width, int height, int codeCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
    }
 
    public Code(int width, int height, int codeCount, int lineCount) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
    }
     
    public void init(boolean type){
       this.type = type;
    }
 
    // 生成图片
    private void creatImage(boolean type) {
        int fontWidth = (width-20) / codeCount;// 字体的宽度
        int fontHeight = height - 5;// 字体的高度
        int codeY = height - 7;
 
        // 图像buffer
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = buffImg.getGraphics();
        //Graphics2D g = buffImg.createGraphics();
        // 设置背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
         
        // 设置字体
        Font font = null;
        if(!type) font = new Font("Fixedsys", Font.BOLD, fontHeight);
        else font = getFont(fontHeight);
        g.setFont(font);
 
        // 设置干扰线
        for (int i = 0; i < lineCount/2; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width);
            int ye = ys + random.nextInt(height);
            g.setColor(getRandColor(1, 255));
            if(!type) g.drawLine(xs, ys, xe, ye);
            else shear(g, width, height, getRandColor(1, 255)) ;
        }
 
        // 添加噪点
        float yawpRate = 0.01f;// 噪声率
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
 
            buffImg.setRGB(x, y, random.nextInt(255));
        }
 
        String str1 = randomStr(codeCount);// 得到随机字符
        this.code = str1;
        for (int i = 0; i < codeCount; i++) {
            String strRand = str1.substring(i, i + 1);
            g.setColor(getRandColor(1, 255));
            // g.drawString(a,x,y);
            // a为要画出来的东西，x和y表示要画的东西最左侧字符的基线位于此图形上下文坐标系的 (x, y) 位置处
             
            g.drawString(strRand, i*fontWidth+2, codeY);
        }
 
    }
    
   public List<String> list = new ArrayList<String>();
   public List<String> list2 = new ArrayList<String>();
   public void initList(){
    	list.add("m");
        list.add("w");
        list2.add("j");
        list2.add("l");
        list2.add("t");
    }
    
 // 生成图片
    private void createImage(boolean type, String str) {
//    	type = true;
    	initList();
    	this.code = str;
        int fontWidth = (width - 85) / str.length();// 字体的宽度
        int fontHeight = height - 70;// 字体的高度
        int codeY = height - 60;//Y坐标
 
        // 图像buffer
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = buffImg.getGraphics();
        //Graphics2D g = buffImg.createGraphics();
        // 设置背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
         
        // 设置字体
        Font font = null;
        if(!type) font = new Font("Antique Olive Compact", Font.BOLD, fontHeight);//Fixedsys
        else font = getFont(fontHeight);
        g.setFont(font);
 
        if (str.length()>0&&str.length()<2) {
        	//画出第一个字符
        	String strRand = str.substring(0, 1);
        	g.setColor(getRandColor(1, 255));
        	// g.drawString(a,x,y);
        	// a为要画出来的东西，x和y表示要画的东西最左侧字符的基线位于此图形上下文坐标系的 (x, y) 位置处
        	if (list.contains(strRand.toLowerCase())) {
        		g.drawString(strRand, 35, codeY);
        	} else if (list2.contains(strRand.toLowerCase())) {
        		g.drawString(strRand, 60, codeY);
        	} else if (strRand.equalsIgnoreCase("i")) {
        		g.drawString(strRand, 75, codeY);
        	} else {
        		g.drawString(strRand, 50, codeY);
        	}
		}
        if (str.length()>1&&str.length()<3) {
        	//画出第一个字符
        	String strRand = str.substring(0, 1);
        	g.setColor(getRandColor(1, 255));
        	// g.drawString(a,x,y);
        	// a为要画出来的东西，x和y表示要画的东西最左侧字符的基线位于此图形上下文坐标系的 (x, y) 位置处
        	if (list.contains(strRand.toLowerCase())) {
        		g.drawString(strRand, 15, codeY);
        	} else if (list2.contains(strRand.toLowerCase())) {
        		g.drawString(strRand, 27, codeY);
        	} else if (strRand.equalsIgnoreCase("i")) {
        		g.drawString(strRand, 40, codeY);
        	} else {
        		g.drawString(strRand, 20, codeY);
        	}
	        //画出第二个字符
	        String strRand2 = str.substring(1, 2);
	        g.setColor(getRandColor(1, 255));
	        // g.drawString(a,x,y);
	        // a为要画出来的东西，x和y表示要画的东西最左侧字符的基线位于此图形上下文坐标系的 (x, y) 位置处
	        if (list.contains(strRand2.toLowerCase())) {
	        	g.drawString(strRand2, fontWidth+12, codeY);
			} else if (list2.contains(strRand2.toLowerCase())) {
	        	g.drawString(strRand2, fontWidth+60, codeY);
			} else if (strRand2.equalsIgnoreCase("i")) {
				g.drawString(strRand2, fontWidth+65, codeY);
        	} else if (strRand2.equalsIgnoreCase("r")) {
				g.drawString(strRand2, fontWidth+50, codeY);
        	} else {
				g.drawString(strRand2, fontWidth+35, codeY);
			}
        }
 
    }
 
    // 得到随机字符
    private String randomStr(int n) {
        String str1 = "ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz1234567890";//I和l不要
        String str2 = "";
        int len = str1.length() - 1;
        double r;
        for (int i = 0; i < n; i++) {
            r = (Math.random()) * len;
            str2 = str2 + str1.charAt((int) r);
        }
        return str2;
    }
 
    // 得到随机颜色
    private Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
     
    /**
     * 产生随机字体
     */
    private Font getFont(int size) {
        Random random = new Random();
        Font font[] = new Font[5];
        font[0] = new Font("Ravie", Font.PLAIN, size);
        font[1] = new Font("Antique Olive Compact", Font.PLAIN, size);
        font[2] = new Font("Fixedsys", Font.PLAIN, size);
        font[3] = new Font("Wide Latin", Font.PLAIN, size);
        font[4] = new Font("Gill Sans Ultra Bold", Font.PLAIN, size);
        return font[random.nextInt(5)];
    }
     
    // 扭曲方法
    private void shear(Graphics g, int w1, int h1, Color color) {
        shearX(g, w1, h1, color);
        shearY(g, w1, h1, color);
    }
 
    private void shearX(Graphics g, int w1, int h1, Color color) {
 
        int period = random.nextInt(2);
 
        boolean borderGap = true;
        int frames = 1;
        int phase = random.nextInt(2);
 
        for (int i = 0; i < h1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                            + (6.2831853071795862D * (double) phase)
                            / (double) frames);
            g.copyArea(0, i, w1, 1, (int) d, 0);
            if (borderGap) {
                g.setColor(color);
                g.drawLine((int) d, i, 0, i);
                g.drawLine((int) d + w1, i, w1, i);
            }
        }
 
    }
 
    private void shearY(Graphics g, int w1, int h1, Color color) {
 
        int period = random.nextInt(40) + 10; // 50;
 
        boolean borderGap = true;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1)
                    * Math.sin((double) i / (double) period
                            + (6.2831853071795862D * (double) phase)
                            / (double) frames);
            g.copyArea(i, 0, 1, h1, 0, (int) d);
            if (borderGap) {
                g.setColor(color);
                g.drawLine(i, (int) d, i, 0);
                g.drawLine(i, (int) d + h1, i, h1);
            }
 
        }
 
    }
 
    public void write(OutputStream sos) throws IOException {
        if(buffImg == null) creatImage(type);
        ImageIO.write(buffImg, "png", sos);
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(sos);
//        encoder.encode(buffImg);
        sos.close();
    }
    
    public void write(OutputStream sos, String str) throws IOException {
        if(buffImg == null) createImage(type, str);
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }
 
    public BufferedImage getBuffImg() {
       if(buffImg == null) creatImage(type);
        return buffImg;
    }
 
    public String getCode() {
        return code.toLowerCase();
    }
     
    //使用方法
//    public void getCode3(HttpServletRequest req, HttpServletResponse response,HttpSession session) throws IOException{
//        // 设置响应的类型格式为图片格式
//            response.setContentType("image/jpeg");
//            //禁止图像缓存。
//            response.setHeader("Pragma", "no-cache");
//            response.setHeader("Cache-Control", "no-cache");
//            response.setDateHeader("Expires", 0);
//             
//             
//            Code vCode = new Code(100,30,5,10);
//            session.setAttribute("code", vCode.getCode());
//            vCode.write(response.getOutputStream());
//            response.flushBuffer();
//     }
 
}
