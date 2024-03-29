package org.core.oss.factory;

import cn.hutool.core.map.CaseInsensitiveMap;
import lombok.extern.slf4j.Slf4j;
import org.core.common.exception.BaseException;
import org.core.common.properties.SaveConfig;
import org.core.common.result.ResultCode;
import org.core.oss.service.OSSService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class OSSFactory {
    
    /**
     * 存储OSS，Map key是忽略大小写的
     */
    private final Map<String, OSSService> ossMap;
    
    private final SaveConfig saveConfig;
    
    public OSSFactory(Map<String, OSSService> ossMap, SaveConfig saveConfig) {
        this.ossMap = new CaseInsensitiveMap<>(ossMap);
        this.saveConfig = saveConfig;
    }
    
    /**
     * 获取音乐处理工厂
     * {@link  org.springframework.context.annotation.Bean} 必须命名为ossService, 否则注入Bean时需要手动指定Bean名
     * 使用{@link Scope}指定每次使用时，重新获取一次
     */
    @Bean("ossService")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public OSSService ossFactory() {
        OSSService oss = ossMap.get(String.valueOf(saveConfig.getSaveMode()));
        if (oss == null) {
            throw new BaseException(ResultCode.SAVE_NAME_INVALID);
        }
        oss.isConnected();
        return oss;
    }
}
