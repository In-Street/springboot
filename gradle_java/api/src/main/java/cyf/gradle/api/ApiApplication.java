package cyf.gradle.api;

import com.cxytiandi.encrypt.springboot.annotation.EnableEncrypt;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.servlet.MultipartConfigElement;

/**
 * boot入口
 */
//@EnableCaching
@SpringBootApplication(
        scanBasePackages = {"cyf.gradle.api", "cyf.gradle.dao"}, exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class}
)

//排除mongo自动配置 或者在 @SpringBootApplication 中排除，否则即使 把dao层的mongo配置注释也会自动加载 localhost：27017的配置
//@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class,MongoDataAutoConfiguration.class})

//开启异步使用
@EnableAsync
/**
 * 1.通过AopContext上下文获取代理对象,用于 数据库事务操作 或者 通过ApplicationContext上下文 详见 TransactionProxyService （test5()）、TransactionProxyService1（test1()）
 * 2. exposeProxy = true: 通过Aop框架暴露该代理对象，AopContext能够访问
 * 3. ApplicationContext 获取代理对象时无需开启暴露
 * 4. proxyTargetClass = true ：参数设为true时,表示使用CGLIB来为目标对象创建代理子类实现AOP，否则使用jdk基于接口的代理；
 *                              与在application.yml 中标注：spring.aop.proxy-target-class: true 效果一样
 */
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
/**
 * 使用filter形式无需添加此注解
 * 配置文件形式、
 */
@EnableEncrypt
/**
 * 监控
 */
/*@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector*/
public class ApiApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApiApplication.class)
                //类名重复bean的处理
                .beanNameGenerator(new DefaultBeanNameGenerator())
                .run(args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("-1");
        factory.setMaxRequestSize("-1");
        //设置上传文件时临时路径
//        factory.setLocation();
        return factory.createMultipartConfig();
    }

    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}")String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }
}
