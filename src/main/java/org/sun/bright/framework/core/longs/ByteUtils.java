package org.sun.bright.framework.core.longs;

/**
 * 字节转换工具
 *
 * @author <a href="mailto:2867665887@qq.com">SunlightBright</a>
 * @version 1.0
 */
public class ByteUtils {

    private ByteUtils() {
    }

    /**
     * 指定unit大小
     */
    private static final int UNIT = 1024;

    private static final int ONE_THOUSAND = 1000;

    private static final int HUNDRED = 100;

    private static final int TEN = 10;

    private static final String B = "B";

    private static final String KB = "KB";

    private static final String MB = "MB";

    private static final String GB = "GB";

    private static final String TB = "TB";

    private static final String PB = "PB";

    /**
     * @param byteSize 字节
     * @return String
     */
    public static String formatByteSize(long byteSize) {
        if (byteSize <= -1) {
            return String.valueOf(byteSize);
        }
        double size = 1.0 * byteSize;
        //不足1KB
        if ((int) Math.floor(size / UNIT) <= 0) {
            return format(size, B);
        }
        size = size / UNIT;
        //不足1MB
        if ((int) Math.floor(size / UNIT) <= 0) {
            return format(size, KB);
        }
        size = size / UNIT;
        //不足1GB
        if ((int) Math.floor(size / UNIT) <= 0) {
            return format(size, MB);
        }
        size = size / UNIT;
        //不足1TB
        if ((int) Math.floor(size / UNIT) <= 0) {
            return format(size, GB);
        }
        size = size / UNIT;
        //不足1PB
        if ((int) Math.floor(size / UNIT) <= 0) {
            return format(size, TB);
        }
        size = size / UNIT;
        if ((int) Math.floor(size / UNIT) <= 0) {
            return format(size, PB);
        }
        return ">PB";
    }

    private static String format(double size, String type) {
        int precision = 0;
        if (size * ONE_THOUSAND % TEN > 0) {
            precision = 3;
        } else if (size * HUNDRED % TEN > 0) {
            precision = 2;
        } else if (size * TEN % TEN > 0) {
            precision = 1;
        }
        String formatStr = "%." + precision + "f";
        if (KB.equals(type)) {
            return String.format(formatStr, (size)) + "KB";
        } else if (MB.equals(type)) {
            return String.format(formatStr, (size)) + "MB";
        } else if (GB.equals(type)) {
            return String.format(formatStr, (size)) + "GB";
        } else if (TB.equals(type)) {
            return String.format(formatStr, (size)) + "TB";
        } else if (PB.equals(type)) {
            return String.format(formatStr, (size)) + "PB";
        }
        return String.format(formatStr, (size)) + "B";
    }

}
