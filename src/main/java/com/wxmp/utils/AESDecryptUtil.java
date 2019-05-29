package com.wxmp.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;

/**
 * @author TWS
 * @title: AESDecryptUtil
 * @projectName smartwx
 * @description: TODO
 * @date 2019/5/28 21:55
 */
@Slf4j
public class AESDecryptUtil {

    // 算法名称
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 加解密算法/模式/填充方式
     */
    private static final String algorithmStr = "AES/CBC/PKCS7Padding";

    private static Key key;
    private static Cipher cipher;

    /**
     * 加密
     * @param originalContent
     * @param encryptKey
     * @param ivByte
     * @return
     */
    public static String encrypt(byte[] originalContent, byte[] encryptKey, byte[] ivByte) {
        try {
            encryptKey = Base64.decodeBase64(encryptKey);
            ivByte = Base64.decodeBase64(ivByte);

            init(encryptKey);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(ivByte));
            byte[] encrypted = cipher.doFinal(originalContent);
            return new String(Base64.encodeBase64(encrypted), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void init(byte[] keyBytes) {

        // 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
        int base = 16;
        if (keyBytes.length % base != 0) {
            int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
            keyBytes = temp;
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        try {
            // 初始化cipher
            cipher = Cipher.getInstance(algorithmStr);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.error("AES加密算法出错",e);
        }
    }

    /**
     * 解密方法
     * @param encryptedDataStr
     * @param keyBytesStr
     * @param ivStr
     * @return
     */
    public static String decrypt(String encryptedDataStr, String keyBytesStr, String ivStr) {
        byte[] encryptedText = null;
        byte[] encryptedData = null;
        byte[] sessionkey = null;
        byte[] iv = null;
        try {
            sessionkey = Base64.decodeBase64(keyBytesStr);
            encryptedData = Base64.decodeBase64(encryptedDataStr);
            iv = Base64.decodeBase64(ivStr);

            init(sessionkey);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            encryptedText = cipher.doFinal(encryptedData);
        } catch (Exception e) {
            log.error("AES解密算法出错",e);
        }
        assert encryptedText != null;
        return new String(encryptedText,StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {

        String content = "{\"phoneNumber\":\"18789536667\"}";
        String key = "9QJPlP2TyuDFH1A73pnGqg==";
        String iv = "SABdIDCax6u7H0f6OILGzw==";

        String encrptContent = AESDecryptUtil.encrypt(content.getBytes(),key.getBytes(),iv.getBytes());
        System.out.println(encrptContent);

        System.out.println(AESDecryptUtil.decrypt(encrptContent, key, iv));
    }
}
