package cyf.gradle.newredis.intercept;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Cheng Yufei
 * @create 2017-10-31 18:57
 **/
@Configuration
public class InterceptConfig extends WebMvcConfigurerAdapter {

    /**
     * 启动时进行拦截器注册
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new Interception()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }


}
