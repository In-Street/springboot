package cyf.gradle.interview;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * boot入口
 *
 */
@SpringBootApplication(
        scanBasePackages = {"cyf.gradle.interview","cyf.gradle.dao"},exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class}
)
//@EnableApolloConfig
@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
/**
 * 1. 此处添加MapperScan 主要用于注解型操作数据库时扫描@Mapper 使用，否则在Service中注入失败,【参考：cyf.gradle.dao.mapper.UserMapper】
 * 2. 不使用xml中的sql操作，不需要在 @Configuration 类中创建datasource 和 扫描mapper、xml路径
 */
@MapperScan(basePackages = "cyf.gradle.dao.mapper")
public class InterviewApplication {

    public static void main(String[] args) {
        //SpringApplication.run(InterviewApplication.class, args);

        new SpringApplicationBuilder(InterviewApplication.class)
                //类名重复bean的处理
                .beanNameGenerator(new DefaultBeanNameGenerator())
                .run(args);
    }

    /**
     * 重新定义 HttpMessageConverters ，避免在@RestControllerAdvice中定义统一返回格式时，处理String类型报 ClassCastException
     * @return
     */
    @Bean
    public HttpMessageConverters customConverters() {
        return new HttpMessageConverters(new FastJsonHttpMessageConverter());
    }
}
