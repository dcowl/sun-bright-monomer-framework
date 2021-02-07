package org.sun.bright.framework.core.longs;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类, 继承 {@link StringUtils} 类
 *
 * @author <a href="mailto:2867665887@qq.com">SunlightBright</a>
 * @version 1.0
 */
public class CharsetsUtils extends StringUtils {

    private CharsetsUtils() {
    }

    private static final char SEPARATOR = '_';

    /**
     * 转换为字节数组
     *
     * @param str String
     * @return byte[]
     */
    public static byte[] getBytes(String str) {
        if (null != str) {
            return str.getBytes(StandardCharsets.UTF_8);
        } else {
            return new byte[0];
        }
    }

    /**
     * 转换为字节数组
     *
     * @param bytes byte[]
     * @return String
     */
    public static String toString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 是否包含字符串
     *
     * @param str     验证字符串
     * @param strings 字符串组
     * @return 包含返回true
     */
    public static boolean inString(String str, String... strings) {
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
     * 是否包含字符串
     *
     * @param str     验证字符串
     * @param strings 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strings) {
        if (str != null && strings != null) {
            for (String s : strings) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 去除左右空格（包含中文空格）
     *
     * @param str String
     */
    public static String trim2(final String str) {
        return str == null ? null : str.replaceAll("^[\\s|　| ]*|[\\s|　| ]*$", "");
    }

    /**
     * 替换掉HTML标签方法
     */
    public static String stripHtml(String html) {
        if (isBlank(html)) {
            return "";
        }
        html = html.replaceAll("\\&[a-zA-Z]{0,9};", "").replaceAll("<[^>]*>", "");
        String regEx = "<.+?>";
        return Pattern.compile(regEx).matcher(html).replaceAll("");
    }

    /**
     * 替换为手机识别的HTML，去掉样式及属性，保留回车。
     *
     * @param html String
     * @return String
     */
    public static String toMobileHtml(String html) {
        if (html == null) {
            return "";
        }
        return html.replaceAll("<([a-z]+?)\\s+?.*?>", "<$1>");
    }

    /**
     * 缩略字符串替换Html正则表达式预编译
     */
    private static final Pattern pattern = Pattern.compile("<([a-zA-Z]+)[^<>]*>");

    /**
     * 缩略字符串（适应于与HTML标签的）
     *
     * @param param  目标字符串
     * @param length 截取长度
     * @return String
     */
    public static String htmlAbbr(String param, int length) {
        if (param == null) {
            return "";
        }
        AtomicReference<StringBuffer> result = new AtomicReference<>(new StringBuffer());
        int n = 0;
        char temp;
        // 是不是HTML代码
        boolean isCode = false;
        // 是不是 HTML 特殊字符,如 <code>&nbsp</code>;
        boolean isHtml = false;
        for (int i = 0; i < param.length(); i++) {
            temp = param.charAt(i);
            if (temp == '<') {
                isCode = true;
            } else if (temp == '&') {
                isHtml = true;
            } else if (temp == '>' && isCode) {
                n = n - 1;
                isCode = false;
            } else if (temp == ';' && isHtml) {
                isHtml = false;
            }
            try {
                if (!isCode && !isHtml) {
                    n += String.valueOf(temp).getBytes("GBK").length;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (n <= length - 3) {
                result.get().append(temp);
            } else {
                result.get().append("...");
                break;
            }
        }
        // 取出截取字符串中的HTML标记
        String tempResult = result.toString().replaceAll("(>)[^<>]*(<?)", "$1$2");
        // 去掉不需要结素标记的HTML标记
        tempResult = tempResult.replaceAll("</?(AREA|BASE|BASEFONT|BODY|BR|COL|COLGROUP|DD|DT|FRAME|HEAD|HR|"
                + "HTML|IMG|INPUT|ISINDEX|LI|LINK|META|OPTION|P|PARAM|TBODY|TD|TFOOT|TH|THEAD|TR|area|base|"
                + "basefont|body|br|col|colgroup|dd|dt|frame|head|hr|html|img|input|isindex|li|link|meta|"
                + "option|p|param|tbody|td|tfoot|th|thead|tr)[^<>]*/?>", "");
        // 去掉成对的HTML标记
        tempResult = tempResult.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
        // 用正则表达式取出标记
        Matcher m = pattern.matcher(tempResult);
        List<String> endHtml = new ArrayList<>();
        while (m.find()) {
            endHtml.add(m.group(1));
        }
        // 补全不成对的HTML标记
        for (int i = endHtml.size() - 1; i >= 0; i--) {
            result.get().append("</");
            result.get().append(endHtml.get(i));
            result.get().append(">");
        }
        return result.toString();
    }

    /**
     * 首字母大写
     */
    public static String cap(String str) {
        return capitalize(str);
    }

    /**
     * 首字母小写
     */
    public static String uncap(String str) {
        return uncapitalize(str);
    }

    /**
     * 驼峰命名法工具
     *
     * @return camelCase(" hello_world ") == "helloWorld"
     * capCamelCase("hello_world") == "HelloWorld"
     * unCamelCase("helloWorld") = "hello_world"
     */
    public static String camelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == SEPARATOR) {
                upperCase = i != 1; // 不允许第二个字符是大写
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return camelCase(" hello_world ") == "helloWorld"
     * capCamelCase("hello_world") == "HelloWorld"
     * unCamelCase("helloWorld") = "hello_world"
     */
    public static String capCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = camelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return camelCase(" hello_world ") == "helloWorld"
     * capCamelCase("hello_world") == "HelloWorld"
     * unCamelCase("helloWorld") = "hello_world"
     */
    public static String unCamelCase(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean nextUpperCase = true;
            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }
            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * 转换为JS获取对象值，生成三目运算返回结果
     *
     * @param objectString 对象串
     *                     例如：row.user.id
     *                     返回：!row?'':!row.user?'':!row.user.id?'':row.user.id
     */
    public static String jsGetVal(String objectString) {
        StringBuilder result = new StringBuilder();
        StringBuilder val = new StringBuilder();
        String[] values = split(objectString, ".");
        for (String s : values) {
            val.append(".").append(s);
            result.append("!").append(val.substring(1)).append("?'':");
        }
        result.append(val.substring(1));
        return result.toString();
    }

    /**
     * 获取随机字符串
     *
     * @param count int 个数
     * @return getRandomStr
     */
    public static String getRandomStr(int count) {
        try {
            char[] codeSeq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                    'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                    'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < count; i++) {
                String r = String.valueOf(codeSeq[random.nextInt(codeSeq.length)]);
                s.append(r);
            }
            return s.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * 获取随机数字
     *
     * @param count int 个数
     * @return String
     */
    public static String getRandomNum(int count) {
        try {
            char[] codeSeq = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < count; i++) {
                String r = String.valueOf(codeSeq[random.nextInt(codeSeq.length)]);
                s.append(r);
            }
            return s.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * 获取树节点名字
     *
     * @param isShowCode 是否显示编码<br>
     *                   true or 1：显示在左侧：(code)name<br>
     *                   2：显示在右侧：name(code)<br>
     *                   false or null：不显示编码：name
     * @param code       编码
     * @param name       名称
     * @return String
     */
    public static String getTreeNodeName(String isShowCode, String code, String name) {
        if ("true".equals(isShowCode) || "1".equals(isShowCode)) {
            return "(" + code + ") " + replace(name, " ", "");
        } else if ("2".equals(isShowCode)) {
            return replace(name, " ", "") + " (" + code + ")";
        } else {
            return replace(name, " ", "");
        }
    }

    /**
     * 空字符串
     */
    private static final String NULL_STRING = "";

    /**
     * 获取参数不为空值
     *
     * @param value defaultValue 要判断的value
     * @return value 返回值
     */
    public static <T> T nvl(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * * 判断一个Collection是否为空， 包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * * 判断一个Collection是否非空，包含List，Set，Queue
     *
     * @param coll 要判断的Collection
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

    /**
     * * 判断一个对象数组是否为空
     *
     * @param objects 要判断的对象数组
     *                * @return true：为空 false：非空
     */
    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) || (objects.length == 0);
    }

    /**
     * * 判断一个对象数组是否非空
     *
     * @param objects 要判断的对象数组
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }

    /**
     * * 判断一个Map是否为空
     *
     * @param map 要判断的Map
     * @return true：非空 false：空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * * 判断一个字符串是否为空串
     *
     * @param str String
     * @return true：为空 false：非空
     */
    public static boolean isEmpty(String str) {
        return isNull(str) || NULL_STRING.equals(str.trim());
    }

    /**
     * * 判断一个字符串是否为非空串
     *
     * @param str String
     * @return true：非空串 false：空串
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object) {
        return object == null;
    }

    /**
     * * 判断一个对象是否非空
     *
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    /**
     * * 判断一个对象是否是数组类型（Java基本型别的数组）
     *
     * @param object 对象
     * @return true：是数组 false：不是数组
     */
    public static boolean isArray(Object object) {
        return isNotNull(object) && object.getClass().isArray();
    }

    /**
     * 去空格
     */
    public static String trim(@NonNull String str) {
        return str.trim();
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @return 结果
     */
    public static String substring(final String str, int start) {
        if (str == null) {
            return NULL_STRING;
        }

        if (start < 0) {
            start = str.length() + start;
        }

        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return NULL_STRING;
        }
        return str.substring(start);
    }

    /**
     * 截取字符串
     *
     * @param str   字符串
     * @param start 开始
     * @param end   结束
     * @return 结果
     */
    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return NULL_STRING;
        }
        if (end < 0) {
            end = str.length() + end;
        }
        if (start < 0) {
            start = str.length() + start;
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return NULL_STRING;
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }

        return str.substring(start, end);
    }

    /**
     * 字符串转set
     *
     * @param str 字符串
     * @param sep 分隔符
     * @return set集合
     */
    public static Set<String> str2Set(String str, String sep) {
        return new HashSet<>(str2List(str, sep, true, false));
    }

    /**
     * 字符串转list
     *
     * @param str         字符串
     * @param sep         分隔符
     * @param filterBlank 过滤纯空白
     * @param trim        去掉首尾空白
     * @return list集合
     */
    public static List<String> str2List(String str, String sep, boolean filterBlank, boolean trim) {
        List<String> list = new ArrayList<>();
        if (CharsetsUtils.isEmpty(str)) {
            return list;
        }
        // 过滤空白字符串
        if (filterBlank && isBlank(str)) {
            return list;
        }
        String[] split = str.split(sep);
        for (String string : split) {
            if (filterBlank && isBlank(string)) {
                continue;
            }
            if (trim) {
                string = string.trim();
            }
            list.add(string);
        }
        return list;
    }

    /**
     * 下划线转驼峰命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase;
        // 当前字符是否大写
        boolean curCharIsUpperCase;
        // 下一字符是否大写
        boolean nextCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }

            curCharIsUpperCase = Character.isUpperCase(c);

            if (i < (str.length() - 1)) {
                nextCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }
            if (preCharIsUpperCase && curCharIsUpperCase && !nextCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            boolean flag = (i != 0 && !preCharIsUpperCase) && curCharIsUpperCase;
            if (flag) {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    /**
     * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。 例如：HELLO_WORLD->HelloWorld
     *
     * @param name 转换前的下划线大写方式命名的字符串
     * @return 转换后的驼峰式命名的字符串
     */
    public static String convertToCamelCase(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母大写
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String[] camels = name.split("_");
        for (String camel : camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 首字母大写
            result.append(camel.substring(0, 1).toUpperCase());
            result.append(camel.substring(1).toLowerCase());
        }
        return result.toString();
    }

    /**
     * 驼峰式命名法 例如：user_name->userName
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = s.toLowerCase();
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
