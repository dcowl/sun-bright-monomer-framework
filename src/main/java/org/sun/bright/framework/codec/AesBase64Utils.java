package org.sun.bright.framework.codec;

import org.sun.bright.exception.codec.CodecException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Aes Base64 Utils
 *
 * @author <a href="mailto:2867665887@qq.com">dcowl</a>
 * @version 1.0
 */
public class AesBase64Utils {

    private AesBase64Utils() {
    }

    private static final String KEY_ALGORITHM = "AES";

    /*** 默认的加密算法 */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * @param algorithm 加密算法
     * @param mode      密码器模式
     * @param key       加密秘钥
     * @return Cipher
     */
    public static Cipher initCipher(String algorithm, int mode, Key key) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(algorithm);
            // 初始化为某种模式的密码器ENCRYPT_MODE/DECRYPT_MODE/WRAP_MODE/UNWRAP_MODE/PUBLIC_KEY/PRIVATE_KEY
            cipher.init(mode, key);
            return cipher;
        } catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
            throw new CodecException("加密初始化失败", e);
        }
    }

    public static byte[] doFinal(Cipher cipher, byte[] content) {
        try {
            return cipher.doFinal(content);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new CodecException("加密初始化失败", e);
        }
    }

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {

            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            //初始化密码器
            Cipher cipher = initCipher(DEFAULT_CIPHER_ALGORITHM, Cipher.ENCRYPT_MODE, getSecretKey(password));
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            //通过Base64转码返回
            return Base64.encode(result);
        } catch (Exception ex) {
            Logger.getLogger(AesBase64Utils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * AES 解密操作
     *
     * @param content  待解密内容
     * @param password 解密密码
     * @return 返回 Base64 转码后的解密数据
     */
    public static String decrypt(String content, String password) {
        try {
            //初始化密码器
            Cipher cipher = initCipher(DEFAULT_CIPHER_ALGORITHM, Cipher.DECRYPT_MODE, getSecretKey(password));
            // 加密
            byte[] result = cipher.doFinal(Base64.decode(content));
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            Logger.getLogger(AesBase64Utils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * 生成加密秘钥
     *
     * @return SecretKeySpec
     */
    private static SecretKeySpec getSecretKey(final String password) {
        try {
            // 返回生成指定算法密钥生成器的 KeyGenerator 对象
            KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            // AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(password.getBytes()));
            // 生成一个密钥
            SecretKey secretKey = kg.generateKey();
            // 转换为AES专用密钥
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AesBase64Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }


}
