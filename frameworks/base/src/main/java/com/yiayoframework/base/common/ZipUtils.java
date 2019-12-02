package com.yiayoframework.base.common;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ZipUtils {


    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();

        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream("D://c.zip"));
             /*BufferedOutputStream bo = new BufferedOutputStream(out)*/) {
            File f = new File("d://b");
//            zip(out, f, f.getName(), bo);
            for (int i = 0; i < 100; ++i) {
                System.out.println("第：" + i + "个了");

                BufferedImage qrcode = QRCodeUtils.createImage("https://g.yiayo.com/c/ouiqwoujklfasjkfsajasd", 460);

                BufferedImage template = ImageIO.read(new File("d://style4.jpg"));
//
                BufferedImage target = ImageUtils.insertImage(template, qrcode, 123, 97);
//
//            BufferedImage source = ImageUtils.compressImageByHeight(target, 1400);

                out.putNextEntry(new ZipEntry("二维码/" + i + ".jpg"));

//            ImageIO.write(target, ImageUtils.FORMAT_NAME_JPG, new File("d://out/" + i + ".jpg"));
                ImageIO.write(target, ImageUtils.FORMAT_NAME_JPG, out);
            }
        }

//        File p = new File("d://" + UUID.randomUUID().toString() + ".jpg");
//        ImageIO.write(source, ImageUtils.FORMAT_NAME_JPG, p);
//
//        zip("d://c.zip", p);

//        ZipOutputStream out = new ZipOutputStream(new FileOutputStream("d://c.zip"));
//        BufferedOutputStream bo = new BufferedOutputStream(out);
//
//
//        out.putNextEntry(new ZipEntry(base)); // 创建zip压缩进入点base
//        System.out.println(base);
//        FileInputStream in = new FileInputStream(f);
//        BufferedInputStream bi = new BufferedInputStream(in);
//        int b;
//        while ((b = bi.read()) != -1) {
//            bo.write(b); // 将字节流写入当前zip目录
//        }
//        bi.close();
//        in.close(); // 输入流关闭
//
//
//        bo.close();
//        out.close();
        long end = System.currentTimeMillis();
        System.out.println("压缩完毕，耗时：" + (end - start) / 1000 + "s");
    }

    private static void zip(String zipFileName, File inputFile) throws Exception {
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
             BufferedOutputStream bo = new BufferedOutputStream(out)) {
            zip(out, inputFile, inputFile.getName(), bo);
        }
    }

    private static void zip(ZipOutputStream out, File inputFile) throws Exception {
        try (BufferedOutputStream bo = new BufferedOutputStream(out)) {
            zip(out, inputFile, inputFile.getName(), bo);
        }
    }

    private static void zip(ZipOutputStream out, File fff, String base,
                            BufferedOutputStream bo) throws Exception { // 方法重载
        if (fff.isDirectory()) {
            File[] fl = fff.listFiles();
            if (fl.length == 0) {
                // 创建zip压缩进入点base
                out.putNextEntry(new ZipEntry(base + "/"));
            }
            for (int i = 0; i < fl.length; i++) {
                // 递归遍历子文件夹
                zip(out, fl[i], base + "/" + fl[i].getName(), bo);
            }
        } else {
            // 创建zip压缩进入点base
            out.putNextEntry(new ZipEntry(base));
            try (FileInputStream in = new FileInputStream(fff);
                 BufferedInputStream bi = new BufferedInputStream(in)) {
                int b;
                while ((b = bi.read()) != -1) {
                    // 将字节流写入当前zip目录
                    bo.write(b);
                }
            }
        }
    }
}
