package org.sun.bright.framework.network;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.sun.bright.framework.core.http.HttpUtils;

import java.nio.charset.StandardCharsets;

/**
 * 获取地址类
 *
 * @author <a href="mailto:2867665887@qq.com">SunlightBright</a>
 * @version 1.0
 */
@Slf4j
public class AddressUtils {

    private AddressUtils() {
    }

    public static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php";

    /**
     * 根据IP地址获取IP地址所属地址
     *
     * @param ip IP地址
     * @return 地址信息
     */
    public static String getRealAddressByIp(String ip) {
        try {
            // 内网不查询
            if (IpUtils.isInternal(ip)) {
                return "内网IP";
            }
            String rspStr = HttpUtils.httpGet(IP_URL + "?ip=" + ip, StandardCharsets.UTF_8.name());
            if (StringUtils.isEmpty(rspStr)) {
                log.warn("获取地理位置异常 {}", ip);
                return "XX XX";
            }
            JSONObject obj = JSON.parseObject(rspStr);
            JSONObject data = obj.getObject("data", JSONObject.class);
            String region = data.getString("region");
            String city = data.getString("city");
            return region + " " + city;
        } catch (Exception e) {
            log.error("----> get user address error, error type: {}", e.getMessage(), e);
            return "XX XX";
        }
    }
}
