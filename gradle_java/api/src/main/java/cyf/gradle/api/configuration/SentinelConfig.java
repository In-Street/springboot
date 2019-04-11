package cyf.gradle.api.configuration;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Cheng Yufei
 * @create 2019-03-22 16:38
 *
 * sentinel-annotation-aspectj 方式需添加 ； spring-cloud-starter-alibaba-sentinel 方式无需添加
 * 注解限流配置
 **/
@Configuration
public class SentinelConfig {

    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }
}
