package org.sun.bright.framework.network;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Ip Utils
 *
 * @author <a href="mailto:2867665887@qq.com">SunlightBright</a>
 * @version 1.0
 */
@Slf4j
public class IpUtils {

    private IpUtils() {
    }

    private static final String UNKNOWN = "unknown";

    private static final long LONG_MAX = 4294967295L;

    private static final long LONG_255L = 255L;

    private static final long LONG_16777215L = 16777215L;

    private static final long LONG_65535L = 65535L;

    /**
     * 获取访问者IP 在一般情况下使用Request.getRemoteAddress()即可，但是经过nginx等反向代理软件后，这个方法会失效。
     * <p>
     * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)， 如果还不存在则调用Request
     * .getRemoteAddress()。
     *
     * @param request HttpServletRequest
     * @return String
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip != null && !"".equals(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"".equals(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

    /**
     * 获取客户端IP地址
     *
     * @param request HttpServletRequest
     * @return ip地址
     */
    public static String getRemoteAddress(HttpServletRequest request) {
        if (request == null) {
            return UNKNOWN;
        }
        String ip = null;
        String xffName = "X-Forwarded-For";
        if (!StringUtils.isEmpty(xffName)) {
            ip = request.getHeader(xffName);
        }
        if (!StringUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.isEmpty(ip)) {
            ip = UNKNOWN;
        } else {
            String[] split = StringUtils.split(ip, ",");
            if (null != split) {
                ip = split[0];
            } else {
                ip = UNKNOWN;
            }
        }
        return ip;
    }

    /**
     * 是否是本地地址
     *
     * @param ip ip地址
     * @return 是否
     */
    public static boolean isLocalAddress(String ip) {
        return inString(ip, "127.0.0.1", "0:0:0:0:0:0:0:1");
    }

    /**
     * 判断IP地址为内网IP还是公网IP
     *
     * <pre>
     *     tcp/ip协议中, 专门保留了三个IP地址区域作为私有地址, 其地址范围如下:
     *   10.0.0.0/8：10.0.0.0        ～ 10.255.255.255
     *   172.16.0.0/12：172.16.0.0   ～ 172.31.255.255
     *   192.168.0.0/16：192.168.0.0 ～ 192.168.255.255
     * </pre>
     *
     * @param ip IP地址
     * @return {@link Boolean} 基本数据类型
     */
    public static boolean isInternal(String ip) {
        if (isLocalAddress(ip)) {
            return true;
        }
        byte[] address = textToNumericFormatV4(ip);
        if (address.length != 0) {
            final byte b0 = address[0];
            final byte b1 = address[1];
            // 10.x.x.x/8
            final byte section1 = 0x0A;
            // 172.16.x.x/12
            final byte section2 = (byte) 0xAC;
            final byte section3 = (byte) 0x10;
            final byte section4 = (byte) 0x1F;
            // 192.168.x.x/16
            final byte section5 = (byte) 0xC0;
            final byte section6 = (byte) 0xA8;
            switch (b0) {
                case section1:
                    return true;
                case section2:
                    return b1 >= section3 && b1 <= section4;
                case section5:
                    return b1 == section6;
                default:
                    return false;
            }
        }
        return false;
    }

    public static boolean isIpV4LiteralAddress(String paramString) {
        return textToNumericFormatV4(paramString).length != 0;
    }

    public static boolean isIpV6LiteralAddress(String paramString) {
        return textToNumericFormatV6(paramString).length != 0;
    }

    private static boolean isIpV4MappedAddress(byte[] paramArrayOfByte) {
        if (paramArrayOfByte.length < 16) {
            return false;
        }
        return (paramArrayOfByte[0] == 0) && (paramArrayOfByte[1] == 0) && (paramArrayOfByte[2] == 0)
                && (paramArrayOfByte[3] == 0) && (paramArrayOfByte[4] == 0) && (paramArrayOfByte[5] == 0)
                && (paramArrayOfByte[6] == 0) && (paramArrayOfByte[7] == 0) && (paramArrayOfByte[8] == 0)
                && (paramArrayOfByte[9] == 0) && (paramArrayOfByte[10] == -1) && (paramArrayOfByte[11] == -1);
    }

    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("get host IP address is error, error type: {}", e.getMessage(), e);
            return UNKNOWN;
        }
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error("get host name is error, error type: {}", e.getMessage(), e);
            return "Unknown Host";
        }
    }

    /**
     * 是否包含字符串
     *
     * @param str     验证字符串
     * @param strings 字符串组
     * @return 包含返回true
     */
    private static boolean inString(String str, String... strings) {
        if (str != null && strings != null) {
            for (String s : strings) {
                if (str.equals(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 去空格
     */
    public static String trim(String str) {
        return (str == null ? "" : str.trim());
    }

    public static byte[] textToNumericFormatV4(String paramString) {
        if (paramString.length() == 0) {
            return new byte[0];
        }
        byte[] arrayOfByte = new byte[4];
        String[] arrayOfString = paramString.split("\\.", -1);
        try {
            long longMax;
            int i;
            switch (arrayOfString.length) {
                case 1:
                    arrayOfByte = formatV4Case1(arrayOfString);
                    break;
                case 2:
                    arrayOfByte = formatV4Case2(arrayOfString);
                    break;
                case 3:
                    arrayOfByte = formatV4Case3(arrayOfString);
                    break;
                case 4:
                    for (i = 0; i < 4; i++) {
                        longMax = Integer.parseInt(arrayOfString[i]);
                        if ((longMax < 0L) || (longMax > LONG_255L)) {
                            return new byte[0];
                        }
                        arrayOfByte[i] = ((byte) (int) (longMax & 0xFF));
                    }
                    break;
                default:
                    return new byte[0];
            }
        } catch (NumberFormatException localNumberFormatException) {
            log.error("--> textToNumericFormatV4 is error, error type: {}",
                    localNumberFormatException.getMessage(), localNumberFormatException);
            return new byte[0];
        }
        return arrayOfByte;
    }

    private static byte[] formatV4Case1(String[] arrayOfString) {
        byte[] arrayOfByte = new byte[4];
        long longMax = Long.parseLong(arrayOfString[0]);
        if ((longMax < 0L) || (longMax > LONG_MAX)) {
            return new byte[0];
        }
        arrayOfByte[0] = ((byte) (int) (longMax >> 24 & 0xFF));
        arrayOfByte[1] = ((byte) (int) ((longMax & 0xFFFFFF) >> 16 & 0xFF));
        arrayOfByte[2] = ((byte) (int) ((longMax & 0xFFFF) >> 8 & 0xFF));
        arrayOfByte[3] = ((byte) (int) (longMax & 0xFF));
        return arrayOfByte;
    }

    private static byte[] formatV4Case2(String[] arrayOfString) {
        byte[] arrayOfByte = new byte[4];
        long longMax = Integer.parseInt(arrayOfString[0]);
        if ((longMax < 0L) || (longMax > LONG_255L)) {
            return new byte[0];
        }
        arrayOfByte[0] = ((byte) (int) (longMax & 0xFF));
        longMax = Integer.parseInt(arrayOfString[1]);
        if ((longMax < 0L) || (longMax > LONG_16777215L)) {
            return new byte[0];
        }
        arrayOfByte[1] = ((byte) (int) (longMax >> 16 & 0xFF));
        arrayOfByte[2] = ((byte) (int) ((longMax & 0xFFFF) >> 8 & 0xFF));
        arrayOfByte[3] = ((byte) (int) (longMax & 0xFF));
        return arrayOfByte;
    }

    private static byte[] formatV4Case3(String[] arrayOfString) {
        byte[] arrayOfByte = new byte[4];
        for (int i = 0; i < 2; i++) {
            long longMax = Integer.parseInt(arrayOfString[i]);
            if ((longMax < 0L) || (longMax > LONG_255L)) {
                return new byte[0];
            }
            arrayOfByte[i] = ((byte) (int) (longMax & 0xFF));
        }
        long longMax = Integer.parseInt(arrayOfString[2]);
        if ((longMax < 0L) || (longMax > LONG_65535L)) {
            return new byte[0];
        }
        arrayOfByte[2] = ((byte) (int) (longMax >> 8 & 0xFF));
        arrayOfByte[3] = ((byte) (int) (longMax & 0xFF));
        return arrayOfByte;
    }

    public static byte[] textToNumericFormatV6(String paramString) {
        if (paramString.length() < 2) {
            return new byte[0];
        }
        char[] arrayOfChar = paramString.toCharArray();
        byte[] arrayOfByte1 = new byte[16];
        int m = arrayOfChar.length;
        int n = paramString.indexOf("%");
        if (n == m - 1) {
            return new byte[0];
        }
        if (n != -1) {
            m = n;
        }
        int i = -1;
        int i1 = 0;
        int i2 = 0;
        if ((arrayOfChar[i1] == ':') && (arrayOfChar[(++i1)] != ':')) {
            return new byte[0];
        }
        int i3 = i1;
        int j = 0;
        int k = 0;
        int i4;
        while (i1 < m) {
            char c = arrayOfChar[(i1++)];
            i4 = Character.digit(c, 16);
            if (i4 != -1) {
                k <<= 4;
                k |= i4;
                if (k > 65535) {
                    return new byte[0];
                }
                j = 1;
            } else if (c == ':') {
                i3 = i1;
                if (j == 0) {
                    if (i != -1) {
                        return new byte[0];
                    }
                    i = i2;
                } else {
                    if (i1 == m) {
                        return new byte[0];
                    }
                    if (i2 + 2 > 16) {
                        return new byte[0];
                    }
                    arrayOfByte1[(i2++)] = ((byte) (k >> 8 & 0xFF));
                    arrayOfByte1[(i2++)] = ((byte) (k & 0xFF));
                    j = 0;
                    k = 0;
                }
            } else if ((c == '.') && (i2 + 4 <= 16)) {
                String str = paramString.substring(i3, m);
                int i5 = 0;
                int i6 = 0;
                while ((i6 = str.indexOf('.', i6)) != -1) {
                    i5++;
                    i6++;
                }
                if (i5 != 3) {
                    return new byte[0];
                }
                byte[] arrayOfByte3 = textToNumericFormatV4(str);
                if (arrayOfByte3 == null) {
                    return new byte[0];
                }
                for (int i7 = 0; i7 < 4; i7++) {
                    arrayOfByte1[(i2++)] = arrayOfByte3[i7];
                }
                j = 0;
            } else {
                return new byte[0];
            }
        }
        if (j != 0) {
            if (i2 + 2 > 16) {
                return new byte[0];
            }
            arrayOfByte1[(i2++)] = ((byte) (k >> 8 & 0xFF));
            arrayOfByte1[(i2++)] = ((byte) (k & 0xFF));
        }
        if (i != -1) {
            i4 = i2 - i;
            if (i2 == 16) {
                return new byte[0];
            }
            for (i1 = 1; i1 <= i4; i1++) {
                arrayOfByte1[(16 - i1)] = arrayOfByte1[(i + i4 - i1)];
                arrayOfByte1[(i + i4 - i1)] = 0;
            }
            i2 = 16;
        }
        if (i2 != 16) {
            return new byte[0];
        }
        byte[] arrayOfByte2 = convertFromIpV4MappedAddress(arrayOfByte1);
        if (arrayOfByte2.length == 0) {
            return arrayOfByte2;
        }
        return arrayOfByte1;
    }

    public static byte[] convertFromIpV4MappedAddress(byte[] paramArrayOfByte) {
        if (isIpV4MappedAddress(paramArrayOfByte)) {
            byte[] arrayOfByte = new byte[4];
            System.arraycopy(paramArrayOfByte, 12, arrayOfByte, 0, 4);
            return arrayOfByte;
        }
        return new byte[0];
    }

}
