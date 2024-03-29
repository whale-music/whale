package org.web.neteasecloudmusic;

import org.api.config.ApplicationStartup;
import org.api.config.EnableApiServer;
import org.core.factory.YamlPropertySourceFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableApiServer
// 开启安全校验
@EnableWebSecurity
@EnableMethodSecurity
@PropertySource(value = "classpath:application-neteasecloudmusic.yml", factory = YamlPropertySourceFactory.class)
@PropertySource("classpath:application-neteasecloudmusic.properties")
@SpringBootApplication
public class NeteaseCloudMusicSpringBootApplication implements ApplicationStartup {
}
