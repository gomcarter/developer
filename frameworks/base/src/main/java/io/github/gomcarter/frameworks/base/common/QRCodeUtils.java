package io.github.gomcarter.frameworks.base.common;


import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

/**
 * 二维码工具类
 *
 * @author gomcarter 2017年12月3日 15:27:06
 */
public class QRCodeUtils {

    public static final String CHARSET = "UTF-8";

    private static QRCodeWriter writer = new QRCodeWriter();
    private static QRCodeReader reader = new QRCodeReader();

    public static BufferedImage createImage(String content,
                                            Integer qrcodeSize) throws Exception {

        return createImage(content, qrcodeSize, 0, ErrorCorrectionLevel.L);
    }

    /**
     * @param content
     * @param qrcodeSize
     * @return
     * @throws Exception
     */
    public static BufferedImage createImage(String content,
                                            Integer qrcodeSize,
                                            Integer extraHeight,
                                            ErrorCorrectionLevel level) throws Exception {

        extraHeight = extraHeight == null ? 0 : extraHeight;
        extraHeight = extraHeight < 0 ? 0 : extraHeight;

        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, level);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, qrcodeSize, qrcodeSize, hints);
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

    public static BufferedImage createImage(String content,
                                            int qrcodeSize,
                                            ErrorCorrectionLevel level) throws Exception {
        return createImage(content, qrcodeSize, 0, level);
    }

    /**
     * 解析二维码
     *
     * @param file 二维码图片
     * @return
     * @throws Exception
     */
    public static String decode(File file) throws Exception {

        return decodeCore(file).getText();
    }

    /**
     * 二维码解析的核心类
     *
     * @param file 二维码图片
     * @return
     * @throws Exception
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
     * @return
     * @throws Exception
     */
    public static String decode(String path) throws Exception {
        return QRCodeUtils.decode(new File(path));
    }

    public static void main(String[] args) throws Exception {
//        BufferedImage image = QRCodeUtils.createImage("XM_kjsdjkasdgjlkasgdlkj", 280);
//        BufferedImage image = QRCodeUtils.createImage("http://dec.lh-xm.com/e/1", 280);
//        BufferedImage image1 = QRCodeUtils.createImage("http://g.xx.com/c/ycxWcMi3qSY1WMxe2rn2M4", 280);
//        BufferedImage image2 = QRCodeUtils.createImage("http://g.xx.com/c/ycxWcMi3qSY1WMxe2rn2M4", 280);
//        BufferedImage image = QRCodeUtils.createImage("http://g.xx.com/c/ycxWcMi3qSY1WMxe2rn2M4", 280);

//        File file = new File("/Users/liyin/Downloads/aa.jpg");
//        boolean a = ImageIO.write(image, ImageUtils.FORMAT_NAME_JPG, file);
//        System.out.println(a);
//
//
//        System.out.println(new Md5Hash("https://g.xx.com/c/1HFNhzuNNi4v-NqF8lEREQ").toHex());
//        ImageIO.write(QRCodeUtils.createImage("https://g.xx.com/c/1HFNhzuNNi4v-NqF8lEREQ", 112), ImageUtils.FORMAT_NAME_JPG,
//          new File("D://b.png"));
//        ImageIO.write(QRCodeUtils.createImage(new Md5Hash("https://g.xx.com/c/1HFNhzuNNi4v-NqF8lEREQ").toHex(), 280),
//          ImageUtils.FORMAT_NAME_JPG, new File("D://b.png"));
//
//        InputStream in = new QRCodeUtils().getClass().getResourceAsStream("/template/letter.pdf");
//        ImageIO.read(in);
//        BufferedImage image = QRCodeUtils.createImage("http://g.xx.com/c/ycxWcMi3qSY1WMxe2rn2M4", 280);
//        ImageUtils.writeToFile(image, "/Users/liyin/Downloads/3.png");

        System.out.println(decode(new File("d://1234.png")));

        System.out.println(decode("/Users/liyin/Downloads/aa.jpg"));
//        System.out.println(decode("/Users/liyin/Downloads/1.png"));
        System.out.println(decode("/Users/liyin/Downloads/3.jpg"));

    }
}

