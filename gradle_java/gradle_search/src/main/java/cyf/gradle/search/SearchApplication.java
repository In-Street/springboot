package cyf.gradle.search;

import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * boot入口
 */
@SpringBootApplication(
        scanBasePackages = {"cyf.gradle.search"}
)
public class SearchApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SearchApplication.class)
                //类名重复bean的处理
                .beanNameGenerator(new DefaultBeanNameGenerator())
                .run(args);
    }



}
