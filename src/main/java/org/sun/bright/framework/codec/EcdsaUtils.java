package org.sun.bright.framework.codec;

import org.sun.bright.exception.codec.CodecException;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * <pre>
 *     JDK 实现加密方式
 *     JDKSignatureEcdsaUtils.java
 * </pre>
 *
 * @author <a href="mailto:2867665887@qq.com">dcowl</a>
 * @version 1.0
 */
public class EcdsaUtils {

    private EcdsaUtils() {
    }

    private static final String ALGORITHM = "EC";

    private static final String CODEC_ERROR = "加密错误";

    private static final String SIGN_ALGORITHM = "SHA256withECDSA";

    public static void main(String[] args) {
        String str = "java小工匠";
        byte[] data = str.getBytes();
        // 初始化密钥度
        KeyPair keyPair = initKey();
        byte[] publicKey = getPublicKey(keyPair);
        byte[] privateKey = getPrivateKey(keyPair);
        // 签名
        byte[] sign = sign(str.getBytes(), privateKey, SIGN_ALGORITHM);
        // 验证
        boolean b = verify(data, publicKey, sign, SIGN_ALGORITHM);
        System.out.println(b);
    }

    /**
     * 初始化密钥对
     */
    public static KeyPair initKey() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
            ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec("secp256k1");
            generator.initialize(ecGenParameterSpec, new SecureRandom());
            generator.initialize(256);
            return generator.generateKeyPair();
        } catch (Exception e) {
            throw new CodecException(CODEC_ERROR, e);
        }
    }

    /**
     * 获取公钥
     *
     * @param keyPair 密钥对
     * @return 公钥
     */
    public static byte[] getPublicKey(KeyPair keyPair) {
        return keyPair.getPublic().getEncoded();
    }

    /**
     * 获取字符串公钥
     *
     * @param keyPair 密钥对
     * @return 公钥
     */
    public static String getPublicKeyStr(KeyPair keyPair) {
        byte[] bytes = keyPair.getPublic().getEncoded();
        return encodeHex(bytes);
    }

    /**
     * 获取私钥
     *
     * @param keyPair 密钥对
     * @return 私钥
     */
    public static byte[] getPrivateKey(KeyPair keyPair) {
        return keyPair.getPrivate().getEncoded();
    }

    /**
     * 获取私钥
     *
     * @param keyPair 密钥对
     * @return 私钥
     */
    public static String getPrivateKeyStr(KeyPair keyPair) {
        byte[] bytes = keyPair.getPrivate().getEncoded();
        return encodeHex(bytes);
    }

    /**
     * 签名
     *
     * @param data          加密数据
     * @param privateKey    私钥
     * @param signAlgorithm 加密类型
     */
    public static byte[] sign(byte[] data, byte[] privateKey, String signAlgorithm) {
        try {
            // 还原使用
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            // 1、实例化Signature
            Signature signature = Signature.getInstance(signAlgorithm);
            // 2、初始化Signature
            signature.initSign(priKey);
            // 3、更新数据
            signature.update(data);
            // 4、签名
            return signature.sign();
        } catch (Exception e) {
            throw new CodecException(CODEC_ERROR, e);
        }
    }

    /**
     * 验证
     */
    public static boolean verify(byte[] data, byte[] publicKey, byte[] sign, String signAlgorithm) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            // 1、实例化Signature
            Signature signature = Signature.getInstance(signAlgorithm);
            // 2、初始化Signature
            signature.initVerify(pubKey);
            // 3、更新数据
            signature.update(data);
            // 4、签名
            return signature.verify(sign);
        } catch (Exception e) {
            throw new CodecException(CODEC_ERROR, e);
        }
    }

    /**
     * 数据准16进制编码
     */
    public static String encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }

    /**
     * 数据转16进制编码
     */
    public static String encodeHex(final byte[] data, final boolean toLowerCase) {
        final char[] digitsLower = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        final char[] digitsUpper = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        final char[] toDigits = toLowerCase ? digitsLower : digitsUpper;
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return new String(out);
    }
}
