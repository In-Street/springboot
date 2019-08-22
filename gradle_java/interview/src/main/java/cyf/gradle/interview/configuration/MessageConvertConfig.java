package cyf.gradle.interview.configuration;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Cheng Yufei
 * @create 2019-08-22 14:47
 **/
@Configuration
public class MessageConvertConfig {

    @Bean
    public HttpMessageConverters customConverters() {
        return new HttpMessageConverters(new FastJsonHttpMessageConverter());
    }
}
