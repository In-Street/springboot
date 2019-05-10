package cyf.gradle.api.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 配置文件外部化，启动加载后，不会动态刷新
 * @author Cheng Yufei
 * @create 2019-05-10 14:33
 **/
@Component
@PropertySource(value = {"file:D:/external.properties"})
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class ExternalConfig {

    Integer core;
    Integer enableKey;
}
