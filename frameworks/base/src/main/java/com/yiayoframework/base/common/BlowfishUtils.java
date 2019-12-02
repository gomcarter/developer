package com.yiayoframework.base.common;


import org.apache.shiro.crypto.BlowfishCipherService;
import org.apache.shiro.util.ByteSource;

/**
 * @authher 李银 2017年12月2日 08:10:35
 */
public class BlowfishUtils {

    public static String encrypt(String text, String key) throws Exception {
        BlowfishCipherService service = new BlowfishCipherService();
        ByteSource bs = service.encrypt(text.getBytes("UTF-8"),
                key.getBytes("UTF-8"));

        return EncodeUtils.encodeUrlSafeBase64(bs.getBytes());
    }

    public static String decrypt(String encrypt, String key) throws Exception {
        BlowfishCipherService service = new BlowfishCipherService();
        ByteSource gc = service.decrypt(EncodeUtils.decodeBase64(encrypt),
                key.getBytes("UTF-8"));

        return new String(gc.getBytes(), "UTF-8");
    }

    public static void main(String[] args) throws Exception {
        String key = "-";
        String text = "++++++";
        long start = System.currentTimeMillis();
        String a = encrypt(text, key);
        System.out.println(encrypt(text, key));
        System.out.println(encrypt(text, key));
        System.out.println(encrypt(text, key));
        System.out.println(encrypt(text, key));
        System.out.println(encrypt(text, key));
        System.out.println(encrypt(text, key));
        System.out.println(encrypt(text, key));
        System.out.println(encrypt(text, key));

        System.out.println(System.currentTimeMillis() - start);
        System.out.println(a);

        String v = decrypt(a, key);

        System.out.println(v);
    }

}
