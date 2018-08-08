package cyf.gradle.api.configuration;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Cheng Yufei
 * @create 2018-08-08 18:11
 **/
@Configuration
public class HystrixConfiguration {

    @Bean
    public HystrixCommandAspect getHystrixCommandAspect() {
        return new HystrixCommandAspect();
    }
}
