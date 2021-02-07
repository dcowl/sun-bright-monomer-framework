package org.sun.bright.framework.network;

import lombok.extern.slf4j.Slf4j;
import org.sun.bright.framework.network.useragent.UserAgentUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 获取MAC地址工具
 *
 * @author <a href="mailto:2867665887@qq.com">SunlightBright</a>
 * @version 1.0
 */
@Slf4j
public class MacUtils {

    private MacUtils() {
    }

    private static final String WINDOWS = "windows";

    private static final String LINUX = "linux";

    public static String getMacAddress() {
        String os = getOsName();
        String mac;
        if (os.startsWith(WINDOWS)) {
            mac = getWindowsMacAddress();
        } else if (os.startsWith(LINUX)) {
            mac = getLinuxMacAddress();
        } else {
            mac = getUnixMacAddress();
        }
        return mac == null ? "" : mac;
    }

    /**
     * 获取客户端 Mac 地址
     */
    public static String getMacAddressByClient(HttpServletRequest request) {
        String os = UserAgentUtils.getOs(request);
        String mac;
        if (os.startsWith(WINDOWS)) {
            mac = getWindowsMacAddress();
        } else if (os.startsWith(LINUX)) {
            mac = getLinuxMacAddress();
        } else {
            mac = getUnixMacAddress();
        }
        return mac == null ? "" : mac;
    }

    /**
     * 获取当前操作系统名称. return 操作系统名称 例如:windows,Linux,Unix等.
     */
    public static String getOsName() {
        return System.getProperty("os.name").toLowerCase();
    }

    /**
     * 获取Unix网卡的mac地址.
     *
     * @return mac地址
     */
    public static String getUnixMacAddress() {
        String mac = null;
        try (// Unix下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                     Runtime.getRuntime().exec("ifconfig eth0").getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // 寻找标示字符串[hwaddr]
                int index = line.toLowerCase().indexOf("hwaddr");
                // 找到了
                if (index != -1) {
                    // 取出mac地址并去除2边空格
                    mac = line.substring(index + "hwaddr".length() + 1).trim();
                    break;
                }
            }
        } catch (IOException e) {
            log.error("get unix mac address is error, error type: {}", e.getMessage(), e);
        }
        return mac;
    }

    /**
     * 获取Linux网卡的mac地址.
     *
     * @return mac地址
     */
    public static String getLinuxMacAddress() {
        String mac = "";
        try (// linux下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                     Runtime.getRuntime().exec("ifconfig eth0").getInputStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                int index = line.toLowerCase().indexOf("硬件地址");
                //  找到了
                if (index != -1) {
                    // 取出mac地址并去除2边空格
                    mac = line.substring(index + 4).trim();
                    break;
                }
            }
        } catch (IOException e) {
            log.error("get linux mac address is error, error type: {}", e.getMessage(), e);
        }
        // 取不到，试下Unix取发
        if ("".equals(mac)) {
            return getUnixMacAddress();
        }
        return mac;
    }

    /**
     * 获取 Windows 网卡的mac地址.
     *
     * @return mac地址
     */
    public static String getWindowsMacAddress() {
        String mac = null;
        try (// windows下的命令，显示信息中包含有mac地址信息
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                     Runtime.getRuntime().exec("ipconfig /all").getInputStream()))) {
            String line;
            int index;
            while ((line = bufferedReader.readLine()) != null) {
                // 寻找标示字符串[physical address 或  物理地址]
                if (line.split("-").length == 6) {
                    index = line.indexOf(":");
                    if (index != -1) {
                        // 取出mac地址并去除2边空格
                        mac = line.substring(index + 1).trim();
                    }
                    break;
                }
            }
        } catch (IOException e) {
            log.error("get windows mac address is error, error type: {}", e.getMessage(), e);
        }
        return mac;
    }

}
