package org.sun.bright.framework.network;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取系统的信息
 *
 * @author <a href="mailto:2867665887@qq.com">SunlightBright</a>
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public class SystemUtils {

    private SystemUtils() {
    }

    /**
     * 获取来访者的浏览器版本
     */
    public static String getRequestBrowserInfo(HttpServletRequest request) {
        String browserVersion = "";
        String header = request.getHeader("user-agent");
        if (header == null || "".equals(header)) {
            return "unknown Browser";
        }
        if (header.contains("MSIE")) {
            browserVersion = "IE";
        } else if (header.contains("Firefox")) {
            browserVersion = "Firefox";
        } else if (header.contains("Chrome")) {
            browserVersion = "Chrome";
        } else if (header.contains("Safari")) {
            browserVersion = "Safari";
        } else if (header.contains("Camino")) {
            browserVersion = "Camino";
        } else if (header.contains("Konqueror")) {
            browserVersion = "Konqueror";
        }
        return browserVersion;
    }

    /**
     * 获取系统版本信息
     */
    public static String getRequestSystemInfo(HttpServletRequest request) {
        String header = request.getHeader("user-agent");
        if (StringUtils.isEmpty(header)) {
            return "";
        }
        switch (header) {
            case "NT 6.0":
                return "Windows Vista/Server 2008";
            case "NT 5.2":
                return "Windows Server 2003";
            case "NT 5.1":
                return "Windows XP";
            case "NT 6.1":
                return "Windows 7";
            case "NT 6.2":
                return "Windows Slate";
            case "NT 6.3":
                return "Windows 9";
            case "NT 5":
                return "Windows 2000";
            case "NT 4":
                return "Windows NT4";
            case "Me":
                return "Windows Me";
            case "98":
                return "Windows 98";
            case "95":
                return "Windows 95";
            case "Mac":
                return "Mac";
            case "Unix":
                return "UNIX";
            case "Linux":
                return "Linux";
            case "SunOS":
                return "SunOS";
            default:
                return "";
        }
    }

    /**
     * 获取来访者的主机名称
     */
    public static String getHostName(String ip) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            return inetAddress.getHostName();
        } catch (UnknownHostException e) {
            log.error("get hast name is error, error type: {}", e.getMessage(), e);
            return e.getMessage();
        }
    }

    /**
     * 获取MAC地址
     *
     * @return 返回MAC地址
     */
    public static String getMacAddress(String ip) {
        String macAddress = getMacInWindows(ip).trim();
        if ("".equals(macAddress)) {
            macAddress = getMacInLinux(ip).trim();
        }
        return macAddress;
    }

    /**
     * @param ip 目标ip
     * @return Mac Address
     */
    private static String getMacInWindows(final String ip) {
        String[] cmd = {"cmd", "/c", "ping " + ip};
        String[] another = {"cmd", "/c", "arp -a"};
        String cmdResult = callCmd(cmd, another);
        return filterMacAddress(ip, cmdResult, "-");
    }

    /**
     * @param ip 目标ip
     * @return Mac Address
     */
    private static String getMacInLinux(final String ip) {
        String[] cmd = {"/bin/sh", "-c", "ping " + ip + " -c 2 && arp -a"};
        String cmdResult = callCmd(cmd);
        return filterMacAddress(ip, cmdResult, ":");
    }

    /**
     * 命令获取mac地址
     */
    private static String callCmd(String[] cmd) {
        StringBuilder result = new StringBuilder();
        try (InputStreamReader is = new InputStreamReader(
                Runtime.getRuntime().exec(cmd).getInputStream());
             BufferedReader br = new BufferedReader(is)) {
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            log.error("call cmd is error, error type: {}", e.getMessage(), e);
            return "";
        }
    }

    /**
     * @param cmd     第一个命令
     * @param another 第二个命令
     * @return 第二个命令的执行结果
     */
    private static String callCmd(String[] cmd, String[] another) {
        StringBuilder result = new StringBuilder();
        String line;
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            proc.waitFor(); // 已经执行完第一个命令，准备执行第二个命令
            proc = rt.exec(another);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error("call cmd is error, error type: {}", e.getMessage(), e);
        }
        return result.toString();
    }

    /**
     * @param ip           目标ip,一般在局域网内
     * @param sourceString 命令处理的结果字符串
     * @param macSeparator mac分隔符号
     * @return mac地址，用上面的分隔符号表示
     */
    private static String filterMacAddress(final String ip, final String sourceString, final String macSeparator) {
        String regExp = "((([0-9,A-Fa-f]{1,2}" + macSeparator + "){1,5})[0-9,A-Fa-f]{1,2})";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(sourceString);
        while (matcher.find()) {
            if (sourceString.indexOf(ip) <= sourceString.lastIndexOf(matcher.group(1))) {
                // 如果有多个IP,只匹配本IP对应的Mac.
                return matcher.group(1);
            }
        }
        return "";
    }

}
