package cyf.gradle.rabbitmq;

import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author Cheng Yufei
 * @create 2017-09-18 16:23
 **/
@SpringBootApplication
public class MqApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(MqApplication.class)
                //类名重复bean的处理
                .beanNameGenerator(new DefaultBeanNameGenerator())
                .run(args);
    }
}
