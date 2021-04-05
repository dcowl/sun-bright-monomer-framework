package org.sun.bright.framework.codec;

import org.apache.commons.codec.binary.Base64;
import org.sun.bright.handler.exception.codec.CodecException;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Rsa Util
 *
 * @author <a href="mailto:2867665887@qq.com">dcowl</a>
 * @version 1.0
 */
public class RsaUtils {

    private RsaUtils() {
    }

    /**
     * 加密方式 {@value}
     */
    private static final String RSA_ALGORITHM = "RSA/None/OAEPWITHSHA-256ANDMGF1PADDING";

    /**
     * 加密错误描述 {@value}
     */
    private static final String ERROR_INFO = "]时遇到异常";

    /**
     * 生产key
     */
    public static Map<String, String> createKeys(int keySize) {
        // 为RSA算法创建一个KeyPairGenerator对象
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            // 初始化KeyPairGenerator对象,密钥长度
            kpg.initialize(keySize);
            // 生成密匙对
            KeyPair keyPair = kpg.generateKeyPair();
            // 得到公钥
            Key publicKey = keyPair.getPublic();
            String publicKeyVar = Base64.encodeBase64URLSafeString(publicKey.getEncoded());
            // 得到私钥
            Key privateKey = keyPair.getPrivate();
            String privateKeyVar = Base64.encodeBase64URLSafeString(privateKey.getEncoded());
            ConcurrentMap<String, String> keyPairMap = new ConcurrentHashMap<>(4);
            keyPairMap.put("publicKey", publicKeyVar);
            keyPairMap.put("privateKey", privateKeyVar);
            return keyPairMap;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
        }
    }

    /**
     * 得到公钥
     *
     * @param publicKey 密钥字符串（经过base64编码）
     */
    public static RSAPublicKey getPublicKey(String publicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
        return (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
    }

    /**
     * 得到私钥
     *
     * @param privateKey 密钥字符串（经过base64编码）
     */
    public static RSAPrivateKey getPrivateKey(String privateKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /**
     * 公钥加密
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE,
                    data.getBytes(StandardCharsets.UTF_8), publicKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new CodecException("加密字符串[" + data + ERROR_INFO, e);
        }
    }

    /**
     * 私钥解密
     */
    public static String privateDecrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data),
                    privateKey.getModulus().bitLength()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CodecException("解密字符串[" + data + ERROR_INFO, e);
        }
    }

    /**
     * 私钥加密
     */

    public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE,
                    data.getBytes(StandardCharsets.UTF_8), privateKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new CodecException("加密字符串[" + data + ERROR_INFO, e);
        }
    }

    /**
     * 公钥解密
     */
    public static String publicDecrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data),
                    publicKey.getModulus().bitLength()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CodecException("解密字符串[" + data + ERROR_INFO, e);
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opcode, byte[] dataArrays, int keySize) {
        int maxBlock;
        if (opcode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            while (dataArrays.length > offSet) {
                if (dataArrays.length - offSet > maxBlock) {
                    buff = cipher.doFinal(dataArrays, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(dataArrays, offSet, dataArrays.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
            return out.toByteArray();
        } catch (Exception e) {
            throw new CodecException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
    }

}

