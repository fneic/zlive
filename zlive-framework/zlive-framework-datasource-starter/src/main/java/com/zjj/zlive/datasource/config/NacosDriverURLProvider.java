package com.zjj.zlive.datasource.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import groovy.util.logging.Slf4j;
import org.apache.shardingsphere.driver.jdbc.core.driver.ShardingSphereDriverURLProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName NacosDriverURLProvider
 * @Description
 * @Author Zhou JunJie
 * @Date 2024/3/9 0:46
 **/

public class NacosDriverURLProvider implements ShardingSphereDriverURLProvider {

    private static Logger logger = LoggerFactory.getLogger(NacosDriverURLProvider.class);
    private final String NACOS_TYPE = "nacos:";
    private final String DEFAULT_GROUP = "DEFAULT_GROUP";

    @Override
    public boolean accept(String url) {
        return url.contains(NACOS_TYPE);
    }

    @Override
    public byte[] getContent(String url) {
        String substring = url.substring(url.indexOf(NACOS_TYPE) + NACOS_TYPE.length());
        String[] split = substring.split("\\?");
        String[] nacos = split[0].split(":");
        String[] args = split[1].split("&&");
        Map<String,String> propertyMap = new HashMap<>();
        for (String property : args) {
            String[] values = property.split("=");
            propertyMap.put(values[0],values[1]);
        }
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR,split[0]);
        properties.setProperty(PropertyKeyConst.PASSWORD, propertyMap.get("password"));
        properties.setProperty(PropertyKeyConst.USERNAME, propertyMap.get("username"));
        properties.setProperty(PropertyKeyConst.NAMESPACE, propertyMap.get("namespace"));
        String dataId = propertyMap.get("fileName").substring(0,propertyMap.get("fileName").indexOf("."));
        try {
            ConfigService configService = NacosFactory.createConfigService(properties);
            String content = configService.getConfig(dataId, DEFAULT_GROUP, 6000);
            logger.info(content);
            return content.getBytes();
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

}
