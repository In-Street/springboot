package cyf.gradle.interview;

import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
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
public class InterviewApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(InterviewApplication.class)
                //类名重复bean的处理
                .beanNameGenerator(new DefaultBeanNameGenerator())
                .run(args);
    }
}
