package org.sun.bright.framework.network.useragent;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * User Agent Utils
 *
 * @author <a href="mailto:2867665887@qq.com">dcowl</a>
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public class UserAgentUtils {

    private UserAgentUtils() {
    }

    /**
     * 根据http获取userAgent信息
     *
     * @param request HttpServletRequest
     * @return String
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    /**
     * 根据request获取userAgent，然后解析出osVersion
     *
     * @param request HttpServletRequest
     * @return os
     */
    public static String getOsVersion(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getOsVersion(userAgent);
    }

    /**
     * 根据userAgent解析出osVersion
     *
     * @param userAgent String
     * @return os
     */
    public static String getOsVersion(String userAgent) {
        String osVersion = "";
        if (!StringUtils.isEmpty(userAgent)) {
            return osVersion;
        }
        String[] strArr = userAgent.substring(userAgent.indexOf('(') + 1, userAgent.indexOf(')')).split(";");
        if (strArr.length == 0) {
            return osVersion;
        }
        osVersion = strArr[1];
        log.info("osVersion is:{}", osVersion);
        return osVersion;
    }

    /**
     * 获取操作系统对象
     *
     * @param userAgent String
     * @return OperatingSystem
     */
    private static OperatingSystem getOperatingSystem(String userAgent) {
        UserAgent agent = UserAgent.parseUserAgentString(userAgent);
        return agent.getOperatingSystem();
    }

    /**
     * 获取os：Windows/ios/Android
     */
    public static String getOs(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getOs(userAgent);
    }

    /**
     * 获取os：Windows/ios/Android
     */
    public static String getOs(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        String os = operatingSystem.getGroup().getName();
        log.info("os is:{}", os);
        return os;
    }

    /**
     * 获取deviceType
     */
    public static String getDeviceType(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getDeviceType(userAgent);
    }

    /**
     * 获取deviceType
     */
    public static String getDeviceType(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        String deviceType = operatingSystem.getDeviceType().toString();
        log.info("deviceType is:{}", deviceType);
        return deviceType;
    }

    /**
     * 获取操作系统的名字
     *
     * @param request HttpServletRequest
     * @return String
     */
    public static String getOsName(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getOsName(userAgent);
    }

    /**
     * 获取操作系统的名字
     *
     * @param userAgent String
     * @return String
     */
    public static String getOsName(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        String osName = operatingSystem.getName();
        log.info("osName is:{}", osName);
        return osName;
    }

    /**
     * 获取device的生产厂家
     *
     * @param request HttpServletRequest
     */
    public static String getDeviceManufacturer(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getDeviceManufacturer(userAgent);
    }

    /**
     * 获取device的生产厂家
     *
     * @param userAgent String
     */
    public static String getDeviceManufacturer(String userAgent) {
        OperatingSystem operatingSystem = getOperatingSystem(userAgent);
        String deviceManufacturer = operatingSystem.getManufacturer().toString();
        log.info("deviceManufacturer is:{}", deviceManufacturer);
        return deviceManufacturer;
    }

    /**
     * 获取浏览器对象
     */
    public static Browser getBrowser(String agent) {
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        return userAgent.getBrowser();
    }

    /**
     * 获取browser name
     */
    public static String getBorderName(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderName(userAgent);
    }

    /**
     * 获取browser name
     */
    public static String getBorderName(String userAgent) {
        Browser browser = getBrowser(userAgent);
        String borderName = browser.getName();
        log.info("borderName is:{}", borderName);
        return borderName;
    }

    /**
     * 获取浏览器的类型
     */
    public static String getBorderType(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderType(userAgent);
    }

    /**
     * 获取浏览器的类型
     */
    public static String getBorderType(String userAgent) {
        Browser browser = getBrowser(userAgent);
        String borderType = browser.getBrowserType().getName();
        log.info("borderType is:{}", borderType);
        return borderType;
    }

    /**
     * 获取浏览器组： CHROME、IE
     */
    public static String getBorderGroup(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderGroup(userAgent);
    }

    /**
     * 获取浏览器组： CHROME、IE
     */
    public static String getBorderGroup(String userAgent) {
        Browser browser = getBrowser(userAgent);
        String browerGroup = browser.getGroup().getName();
        log.info("browerGroup is:{}", browerGroup);
        return browerGroup;
    }

    /**
     * 获取浏览器的生产厂商
     */
    public static String getBrowserManufacturer(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBrowserManufacturer(userAgent);
    }

    /**
     * 获取浏览器的生产厂商
     */
    public static String getBrowserManufacturer(String userAgent) {
        Browser browser = getBrowser(userAgent);
        String browserManufacturer = browser.getManufacturer().getName();
        log.info("browserManufacturer is:{}", browserManufacturer);
        return browserManufacturer;
    }

    /**
     * 获取浏览器使用的渲染引擎
     */
    public static String getBorderRenderingEngine(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBorderRenderingEngine(userAgent);
    }

    /**
     * 获取浏览器使用的渲染引擎
     */
    public static String getBorderRenderingEngine(String userAgent) {
        Browser browser = getBrowser(userAgent);
        String renderingEngine = browser.getRenderingEngine().name();
        log.info("renderingEngine is:{}", renderingEngine);
        return renderingEngine;
    }

    /**
     * 获取浏览器版本
     */
    public static String getBrowserVersion(HttpServletRequest request) {
        String userAgent = getUserAgent(request);
        return getBrowserVersion(userAgent);
    }

    /**
     * 获取浏览器版本
     */
    public static String getBrowserVersion(String userAgent) {
        Browser browser = getBrowser(userAgent);
        return browser.getVersion(userAgent).toString();
    }

}
