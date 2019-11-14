package cyf.gradle.api.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 配置文件外部化，启动加载后，不会动态刷新
 * @author Cheng Yufei
 * @create 2019-05-10 14:33
 **/
@Component
//@PropertySource(value = {"file:D:/YUFEI/work/private/external.properties"})
@PropertySource(value = {"file:/data/project/external.properties"})
@Getter
@Setter
public class ExternalConfig {

    @Value("${appp.core}")
    Object core;
    @Value("${appp.enableKey}")
    Object enableKey;
}
