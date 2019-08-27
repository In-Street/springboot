package cyf.gradle.sharding;

import org.mybatis.spring.annotation.MapperScan;
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
        scanBasePackages = {"cyf.gradle.sharding","cyf.gradle.dao"},exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class}
)
@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
@MapperScan(basePackages = "cyf.gradle.dao.mapper")
public class ShardingApplication {

    public static void main(String[] args) {

        new SpringApplicationBuilder(ShardingApplication.class)
                //类名重复bean的处理
                .beanNameGenerator(new DefaultBeanNameGenerator())
                .run(args);
    }
}
