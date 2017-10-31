package cyf.gradle.newredis;

import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * boot入口
 */
@SpringBootApplication
public class NewRedisApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(NewRedisApplication.class)
                //类名重复bean的处理
                .beanNameGenerator(new DefaultBeanNameGenerator())
                .run(args);
    }



}
