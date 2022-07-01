package com.wimoor.util;

import java.awt.image.BufferedImage;

import jp.sourceforge.qrcode.data.QRCodeImage;

public class QRCodeImageImpl implements QRCodeImage {
    BufferedImage bufferedImage;//内存中的二维码
    public QRCodeImageImpl(BufferedImage bufferedImage){
        this.bufferedImage = bufferedImage;
    }

    @Override
    public int getWidth() {
        return bufferedImage.getHeight();
    }

    @Override
    public int getHeight() {
        return bufferedImage.getHeight();
    }

    //像素
    @Override
    public int getPixel(int x, int y) {
        return bufferedImage.getRGB(x, y);
    }
}