package com.gomcarter.frameworks.base.common;


import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;
import java.util.Objects;

/**
 * 二维码工具类
 *
 * @author gomcarter 2017年12月3日 15:27:06
 */
public class QRCodeUtils {

    public static final String CHARSET = "UTF-8";

    private static QRCodeWriter writer = new QRCodeWriter();
    // private static QRCodeReader reader = new QRCodeReader();

    /**
     * @param content content
     * @param size    size
     * @return image
     * @throws Exception for failed
     */
    public static BufferedImage createImage(String content,
                                            Integer size) throws Exception {

        return createImage(content, size, 0, ErrorCorrectionLevel.L);
    }

    /**
     * @param content     content
     * @param size        size
     * @param level       level
     * @param extraHeight extraHeight for extra empty area under qrcode
     * @return image
     * @throws Exception for failed
     */
    public static BufferedImage createImage(String content,
                                            Integer size,
                                            Integer extraHeight,
                                            ErrorCorrectionLevel level) throws Exception {

        extraHeight = extraHeight == null ? 0 : extraHeight;
        extraHeight = extraHeight < 0 ? 0 : extraHeight;

        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, level);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height + extraHeight,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000
                        : 0xFFFFFFFF);
            }
        }
        return image;
    }

    /**
     * @param content content
     * @param size    size
     * @param level   level
     * @return image
     * @throws Exception for failed
     */
    public static BufferedImage createImage(String content,
                                            int size,
                                            ErrorCorrectionLevel level) throws Exception {
        return createImage(content, size, 0, level);
    }

    /**
     * 解析二维码
     *
     * @param file 二维码图片
     * @return qrcode content
     * @throws Exception Exception
     */
    public static String decode(File file) throws Exception {
        return Objects.requireNonNull(decodeCore(file)).getText();
    }

    /**
     * 二维码解析的核心类
     *
     * @param file 二维码图片
     * @return qrcode result
     * @throws Exception for read file
     */
    public static Result decodeCore(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        QrCodeLuminanceSource source = new QrCodeLuminanceSource(
                image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        result = new MultiFormatReader().decode(bitmap, hints);
        return result;
    }

    /**
     * 解析二维码
     *
     * @param path 二维码图片地址
     * @return qrcode content
     * @throws Exception if failed
     */
    public static String decode(String path) throws Exception {
        return QRCodeUtils.decode(new File(path));
    }
}

