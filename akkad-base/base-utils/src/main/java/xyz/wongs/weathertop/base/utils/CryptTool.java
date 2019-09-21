package xyz.wongs.weathertop.base.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 文 件 名: CryptTool.java 功能描述: 加密工具类 .
 *
 * @author g.fanggq
 * @version Revision 1.0.0
 */
public class CryptTool {
    /**
     * 定义加密算法,可用
     */
    private static String          algorithm      = "DESede";
    /**
     * DES,DESede,Blowfish
     */
    private static String          key            = "LIYWER52KH63JGFHLN456XWO";
    private static String          encCharsetName = "UTF-8";
    private static String          iv             = "12345678";
    private static SecretKeySpec   keySpec        = null;
    private static IvParameterSpec ivSpec         = null;

    public static SecretKey genDESKey(final byte[] key_byte) throws Exception {
        final SecretKey key = new SecretKeySpec(key_byte,
                CryptTool.algorithm);
        return key;
    }

    public static byte[] md5Digest(final byte[] src) throws Exception {
        final java.security.MessageDigest alg = java.security.MessageDigest
                .getInstance("MD5");
        return alg.digest(src);
    }

    public static String md5Digest(final String src) throws Exception {
        return new String(CryptTool.md5Digest(src.getBytes()));
    }

    public static String base64Encode(final String src) {
        final sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        return encoder.encode(src.getBytes());
    }

    public static String base64Encode(final byte[] src) {
        final sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        return encoder.encode(src);
    }

    public static String base64Decode(final String src) {
        final sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try {
            return new String(decoder.decodeBuffer(src));
        } catch (final Exception ex) {
            return null;
        }
    }

    public static byte[] base64DecodeToBytes(final String src) {
        final sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try {
            return decoder.decodeBuffer(src);
        } catch (final Exception ex) {
            return null;
        }

    }

    public static String urlEncode(String src) {
        try {
            src = java.net.URLEncoder.encode(src, CryptTool.encCharsetName);
            return src;
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return src;
    }

    public static String urlDecode(final String value) {
        try {
            return java.net.URLDecoder.decode(value, CryptTool.encCharsetName);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return value;
    }

    public static byte[] desEncrypt(final SecretKey deskey, final byte[] src) {
        try {
            final Cipher cipher = Cipher.getInstance(CryptTool.algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, deskey);
            return cipher.doFinal(src);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte[] desDecrypt(final SecretKey deskey, final byte[] src) {
        try {
            final Cipher cipher = Cipher.getInstance(CryptTool.algorithm);
            cipher.init(Cipher.DECRYPT_MODE, deskey);
            return cipher.doFinal(src);
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void genAESKey() throws Exception {
        final byte[] keyBytes = new byte[16];
        final byte[] ivBytes = new byte[16];
        final byte[] kbyte = CryptTool.key.getBytes(CryptTool.encCharsetName);
        final byte[] vbyte = CryptTool.iv.getBytes(CryptTool.encCharsetName);
        int klen = kbyte.length;
        int vlen = vbyte.length;
        if (klen > keyBytes.length) {
            klen = keyBytes.length;
        }
        if (vlen > ivBytes.length) {
            vlen = ivBytes.length;
        }
        System.arraycopy(kbyte, 0, keyBytes, 0, klen);
        System.arraycopy(vbyte, 0, ivBytes, 0, vlen);
        CryptTool.keySpec = new SecretKeySpec(keyBytes, "AES");
        CryptTool.ivSpec = new IvParameterSpec(ivBytes);
    }

    public static byte[] encrypt(final byte[] text) throws Exception {
        if (CryptTool.keySpec == null || CryptTool.ivSpec == null) {
            CryptTool.genAESKey();
        }
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, CryptTool.keySpec, CryptTool.ivSpec);
        final byte[] results = cipher.doFinal(text);
        return results;
    }

    public static byte[] decrypt(final byte[] text) throws Exception {
        if (CryptTool.keySpec == null || CryptTool.ivSpec == null) {
            CryptTool.genAESKey();
        }
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, CryptTool.keySpec, CryptTool.ivSpec);
        final byte[] results = cipher.doFinal(text);
        return results;
    }

    public static void main(final String[] args) throws Exception {
        System.out
                .println("加密前:"
                        + "account:12,passwd:12,newPasswd:12;fztest@fzlanliang5571681liang5571681");
        System.out.println("加密后:");
        System.out
                .println(CryptTool
                        .urlEncode(CryptTool
                                .base64Encode(CryptTool
                                        .encrypt("account:16,passwd:12,newPasswd:12;453277705@fzadslliang5571681liang5571681"
                                                .getBytes("utf-8")))));
    }

    public static void help() {
        System.out.println("Usage:");
        System.out
                .println("  java com.ffcs.commons.CryptTool Account [Password] [NewPassword]");
        System.out.println("    for this message.");
    }

    public void setAlgorithm(final String algorithm) {
        CryptTool.algorithm = algorithm;
    }

    public void setKey(final String key) {
        CryptTool.key = key;
    }

    public void setEncCharsetName(final String encCharsetName) {
        CryptTool.encCharsetName = encCharsetName;
    }

    public void setIv(final String iv) {
        CryptTool.iv = iv;
    }

}
