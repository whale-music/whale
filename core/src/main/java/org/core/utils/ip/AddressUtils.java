package org.core.utils.ip;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;

/**
 * 获取地址类
 *
 * @author ruoyi
 */
public class AddressUtils {
    // IP地址查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";
    // 未知地址
    public static final String UNKNOWN = "XX XX";
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);
    
    private AddressUtils() {
    }
    
    public static String getRealAddressByIP(String ip) {
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        try {
            String rspStr = HttpUtil.get(IP_URL + "?ip=" + ip + "&json=true", CharsetUtil.CHARSET_GBK);
            if (StringUtils.isBlank(rspStr)) {
                log.error("获取地理位置异常 {}", ip);
                return UNKNOWN;
            }
            ObjectMapper objectMapper = new ObjectMapper(rspStr);
            BeanMap beanMap = BeanMap.create(objectMapper);
            Object region = beanMap.get("pro");
            Object city = beanMap.get("city");
            return String.format("%s %s", region, city);
        } catch (Exception e) {
            log.error("获取地理位置异常 {}", ip);
        }
        return UNKNOWN;
    }
}
