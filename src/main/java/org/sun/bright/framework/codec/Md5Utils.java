package org.sun.bright.framework.codec;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.util.encoders.Hex;

/**
 * Md5 Utils
 *
 * @author <a href="mailto:2867665887@qq.com">dcowl</a>
 * @version 1.0
 */
@Slf4j
public class Md5Utils {

    private Md5Utils() {
    }

    /**
     * <pre>
     *     bouncy castle 组件提供的加密方式
     *     MD2 {@link org.bouncycastle.crypto.digests.MD2Digest}
     *     MD4 {@link org.bouncycastle.crypto.digests.MD4Digest}
     * </pre>
     *
     * @param var 所要加密的字符串
     * @return 加密后的 Hash 值
     */
    public static String bcToHash(String var) {
        Digest digest = new MD5Digest();
        digest.update(var.getBytes(), 0, var.getBytes().length);
        byte[] md5Bytes = new byte[digest.getDigestSize()];
        digest.doFinal(md5Bytes, 0);
        return Hex.toHexString(md5Bytes);
    }

    /**
     * commons-codec工具加密类
     * <pre>
     *     commons-codec中提供MD2和MD5加密运算
     *     commons-codec中没有MD4，CC可以简化操作
     *      <code>
     *          DigestUtils.md2Hex(var.getBytes())
     *      </code>
     * </pre>
     *
     * @param var 所要加密的字符串
     * @return 加密后的 Hash 值
     */
    public static String ccToHash(String var) {
        return DigestUtils.md5Hex(var.getBytes());
    }

}
