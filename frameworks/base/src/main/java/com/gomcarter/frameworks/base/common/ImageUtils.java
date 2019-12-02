package com.gomcarter.frameworks.base.common;


import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

/**
 * @author gomcarter 2017年12月3日 15:27:06
 */
public class ImageUtils {

    public static final String CHARSET = "UTF-8";
    public static final String FORMAT_NAME_JPG = "JPG";
    public static final String FORMAT_NAME_PNG = "PNG";

    public static final int THRESHOLD = 5;

    public static class Size {
        public int width;
        public int height;

        public Size(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    public static Size getSize(InputStream target) {
        try {
            return getSize(ImageIO.read(target));
        } catch (Exception e) {
            return new Size(0, 0);
        }
    }

    public static Size getSize(File target) {
        try {
            return getSize(ImageIO.read(target));
        } catch (Exception e) {
            return new Size(0, 0);
        }
    }

    public static Size getSize(BufferedImage target) {
        return new Size(target.getWidth(null), target.getHeight(null));
    }

    public static BufferedImage insertTitle(BufferedImage target, String title, Color color, Font font, int startX, int startY) {
        if (StringUtils.isBlank(title)) {
            return target;
        }

        Graphics2D g = target.createGraphics();
        if (color == null) {
            g.setColor(Color.black);
        }
        g.setColor(color);
//        g.setBackground(Color.white);

        AttributedString ats = new AttributedString(title);

        ats.addAttribute(TextAttribute.FONT, font, 0, title.length());
        AttributedCharacterIterator iter = ats.getIterator();

        g.drawString(iter, startX, startY);
        //添加水印的文字和设置水印文字出现的内容 ----位置
        g.dispose();//画笔结束
        return target;
    }

    public static BufferedImage insertTitle(BufferedImage target, String title, Color color, int startX, int startY) {
        return insertTitle(target, title, color, new Font("Serif", Font.BOLD, 13), startX, startY);
    }

    /**
     * 插入 图片
     *
     * @param target   源图片
     * @param inserted 插入图片
     * @param startX   开始X坐标
     * @param startY   开始Y坐标
     * @throws Exception
     */
    public static BufferedImage insertImage(BufferedImage target,
                                            File inserted,
                                            int startX,
                                            int startY) throws Exception {

        return insertImage(target, ImageIO.read(inserted), startX, startY);
    }

    public static BufferedImage insertImage(BufferedImage target,
                                            Image inserted,
                                            int startX,
                                            int startY) throws Exception {

        int width = inserted.getWidth(null),
                height = inserted.getHeight(null);
        Graphics2D graph = target.createGraphics();
        graph.drawImage(inserted, startX, startY, width, height, null);
//        Shape shape = new RoundRectangle2D.Float(startX, startY, width, height, 6, 6);
//        graph.setStroke(new BasicStroke(3f));
//        graph.draw(shape);
        graph.dispose();
        return target;
    }

    public static BufferedImage insertImage(BufferedImage target, Image inserted, double rate, float transparent) throws Exception {
        int targetWidth = target.getWidth(),
                targetHight = target.getHeight();

        if (rate > 0) {
            inserted = resizePng(inserted, (targetWidth * rate) / (double) inserted.getWidth(null));
        }
        int width = inserted.getWidth(null),
                height = inserted.getHeight(null);

        Graphics2D graph = target.createGraphics();
        if (transparent > 0 && transparent < 1) {
            graph.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, transparent));
        }
        graph.drawImage(inserted, (targetWidth - width) / 2, (targetHight - height) / 2, width, height, null);
        graph.dispose();
        return target;
    }

    public static BufferedImage insertImage(BufferedImage target, Image inserted, double rate) throws Exception {
        return insertImage(target, inserted, rate, 0);
    }

    public static BufferedImage insertImage(BufferedImage target, Image inserted) throws Exception {
        return insertImage(target, inserted, 0, 0);
    }

    /**
     * 按高度等比压缩
     *
     * @param target
     * @param height
     */
    public static BufferedImage compressImageByHeight(BufferedImage target,
                                                      int height) throws Exception {
        int w = target.getWidth(null),
                h = target.getHeight(null);
        // 为等比缩放计算输出的图片宽度及高度
        double rate = ((double) h) / (double) height + 0.1;
        // 根据缩放比率大的进行缩放控制
        int newWidth = (int) (((double) w) / rate),
                newHeight = (int) (((double) h) / rate);
        return compressImage(target, newWidth, newHeight);
    }

    /**
     * 按宽度等比压缩
     *
     * @param target
     * @param width
     */
    public static BufferedImage compressImageByWidth(BufferedImage target,
                                                     int width) throws Exception {
        int w = target.getWidth(null),
                h = target.getHeight(null);
        // 为等比缩放计算输出的图片宽度及高度
        double rate = ((double) w) / (double) width + 0.1;
        // 根据缩放比率大的进行缩放控制
        int newWidth = (int) (((double) w) / rate),
                newHeight = (int) (((double) h) / rate);
        return compressImage(target, newWidth, newHeight);
    }

    public static BufferedImage compressImage(BufferedImage target,
                                              int width,
                                              int height) throws Exception {
        int originWidth = target.getWidth(null),
                originHeight = target.getHeight(null);
        if (originHeight == height && width == originWidth) {
            return target;
        }

        Image image = target.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return tag;
    }

    public static BufferedImage resizePng(Image from, int size) {
        // 判断是否是等比缩放
        int width = from.getWidth(null),
                height = from.getHeight(null);
        // 为等比缩放计算输出的图片宽度及高度
        double rate1 = (double) size / (double) width + 0.1;
        double rate2 = (double) size / (double) height + 0.1;
        // 根据缩放比率大的进行缩放控制
        double rate = rate1 < rate2 ? rate1 : rate2;
        return resizePng(from, rate);
    }

    public static BufferedImage resizePng(Image from, double rate) {
        return resizePng(from, (int) ((double) from.getWidth(null) * rate), (int) ((double) from.getHeight(null) * rate));
    }

    public static BufferedImage resizePng(Image from, int outputWidth, int outputHeight) {
        BufferedImage to = new BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = to.createGraphics();
        to = g2d.getDeviceConfiguration().createCompatibleImage(outputWidth, outputHeight, Transparency.TRANSLUCENT);
        g2d.dispose();
        g2d = to.createGraphics();
        g2d.drawImage(from.getScaledInstance(outputWidth, outputHeight, BufferedImage.SCALE_AREA_AVERAGING), 0, 0, null);
        g2d.dispose();
        return to;
    }

    public static InputStream base64ToInputStream(String base64) throws Exception {
        BASE64Decoder decoder = new BASE64Decoder();
        // Base64解码
        byte[] b = decoder.decodeBuffer(StringUtils.replaceEach(base64,
                new String[]{
                        "data:image/png;base64,",
                        "data:image/jpeg;base64,",
                        "data:image/svg+xml;base64,",
                },
                new String[]{"", "", ""}));
        for (int i = 0; i < b.length; ++i) {
            // 调整异常数据
            if (b[i] < 0) {
                b[i] += 256;
            }
        }

        return new ByteArrayInputStream(b);
    }

    public static File writeToFile(BufferedImage image, String path) throws IOException {
        File file = new File(path);
        ImageIO.write(image, ImageUtils.FORMAT_NAME_PNG, file);
        return file;
    }

    public static void main(String[] args) throws Exception {
//        BufferedImage to = resizePng(ImageIO.read(new File("D://1.png")), 200);
//        BufferedImage source = QRCodeUtils.createImage("https://g.xx.com/c/ouiqwoujklfasjkfsajasd", 290, ErrorCorrectionLevel.L);
//        BufferedImage target = insertImage(ImageIO.read(new File("d://style4.png")), source, 75, 50);
//        boolean a = ImageIO.write(to, ImageUtils.FORMAT_NAME_PNG, new File("D://t.png"));
//
//        System.out.println(a);

//        BufferedImage source = ImageIO.read(new File("/template/a.pdf"));
//        BufferedImage mark = ImageIO.read(new File("D://mark.png"));
//        BufferedImage out = insertImage(source, mark, 0.666667D, 0.5f),
//        source = insertTitle(source, "中国商品诚信数据库", new Color(63,59,58), new Font("Serif",Font.BOLD, 12),  50, 60);
//        ImageIO.write(source, ImageUtils.FORMAT_NAME_PNG, new File("D://out.png"));

        File f = new File("D://mark.png");
        FileInputStream inputStream = new FileInputStream(f);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, outputStream);
        byte[] buffer = outputStream.toByteArray();

        ImageIO.write(ImageIO.read(new ByteArrayInputStream(buffer)), ImageUtils.FORMAT_NAME_PNG, new File("D://out1.png"));

        ImageIO.write(ImageIO.read(new ByteArrayInputStream(buffer)), ImageUtils.FORMAT_NAME_PNG, new File("D://out2.png"));


    }
}
