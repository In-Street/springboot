package cyf.gradle.webflux;

import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import javax.servlet.MultipartConfigElement;

/**
 * boot入口
 *
 */
@SpringBootApplication(
        scanBasePackages = {"cyf.gradle.webflux", "cyf.gradle.dao"},exclude={MongoAutoConfiguration.class,MongoDataAutoConfiguration.class}
)
public class FluxApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(FluxApplication.class)
                //类名重复bean的处理
                .beanNameGenerator(new DefaultBeanNameGenerator())
                .run(args);
    }
}
