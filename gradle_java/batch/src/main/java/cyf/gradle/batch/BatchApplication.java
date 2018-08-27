package cyf.gradle.batch;

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
        scanBasePackages = {"cyf.gradle.batch","cyf.gradle.dao"},exclude={MongoAutoConfiguration.class, MongoDataAutoConfiguration.class}
)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class BatchApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BatchApplication.class)
                //类名重复bean的处理
                .beanNameGenerator(new DefaultBeanNameGenerator())
                .run(args);
    }
}
