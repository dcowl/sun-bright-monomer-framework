package org.sun.bright.framework.core.longs;


import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Number 工具类
 *
 * @author <a href="mailto:2867665887@qq.com">SunlightBright</a>
 * @version 1.0
 */
@Slf4j
public class NumbersUtils extends org.apache.commons.lang3.math.NumberUtils {

    private NumbersUtils() {
        super();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param minuend 被减数
     * @param subtrahend 减数
     * @return 两个参数的差
     */
    public static double subtract(double minuend, double subtrahend) {
        return BigDecimal.valueOf(minuend).subtract(BigDecimal.valueOf(subtrahend)).doubleValue();
    }

    /**
     * 提供精确的加法运算。
     *
     * @param summand 被加数
     * @param addend 加数
     * @return 两个参数的和
     */
    public static double add(double summand, double addend) {
        return BigDecimal.valueOf(summand).add(BigDecimal.valueOf(addend)).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double multiply(double v1, double v2) {
        return BigDecimal.valueOf(v1).multiply(BigDecimal.valueOf(v2)).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2) {
        return divide(v1, v2, 10);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double divide(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = BigDecimal.valueOf(v1);
        BigDecimal b2 = BigDecimal.valueOf(v2);
        if (b1.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.doubleValue();
        }
        if (b2.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("除数不能为0！");
        }
        return b2.divide(b2, scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = BigDecimal.valueOf(v);
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 格式化双精度，保留两个小数
     *
     * @return String
     */
    public static String formatDouble(Double b) {
        BigDecimal bg = BigDecimal.valueOf(b);
        return bg.setScale(2, RoundingMode.HALF_UP).toString();
    }

    /**
     * 格式化双精度，保留两个小数
     *
     * @return String
     */
    public static String formatDouble(Double b, int scale) {
        BigDecimal bg = BigDecimal.valueOf(b);
        return bg.setScale(scale, RoundingMode.HALF_UP).toString();
    }

    /**
     * 百分比计算
     *
     * @return String
     */
    public static String formatScale(double one, long total) {
        BigDecimal bg = BigDecimal.valueOf(one * 100 / total);
        return bg.setScale(0, RoundingMode.HALF_UP).toString();
    }

    /**
     * 格式化数值类型
     *
     * @param data    Object
     * @param pattern pattern
     */
    public static String formatNumber(Object data, String pattern) {
        if (pattern == null) {
            return new DecimalFormat().format(data);
        } else {
            return new DecimalFormat(pattern).format(data);
        }
    }

    /**
     * 生成String类型数据的随机数
     *
     * @param length 所要生成随机数的长度
     * @return 随机数的字符串
     */
    public static String getRandomString(int length) {
        try {
            StringBuilder val = new StringBuilder();
            Random random = SecureRandom.getInstanceStrong();
            for (int i = 0; i < length; i++) {
                val.append(random.nextInt(10));
            }
            return val.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("get random String is error, error type: {}", e.getMessage(), e);
            return "";
        }
    }

    /**
     * 生成int类型数据的随机数
     *
     * @param length 所要生成的随机数长度位数
     * @return 整型随机数
     */
    public static Integer getRandomInt(int length) {
        try {
            StringBuilder val = new StringBuilder();
            Random random = SecureRandom.getInstanceStrong();
            for (int i = 0; i < length; i++) {
                val.append(random.nextInt(10));
            }
            return Integer.parseInt(val.toString());
        } catch (NoSuchAlgorithmException e) {
            log.error("get random Int is error, error type: {}", e.getMessage(), e);
            return null;
        }
    }

}
