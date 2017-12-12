package cyf.gradle.searchdata;

import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * boot入口
 */
@SpringBootApplication(
        scanBasePackages = {"cyf.gradle.searchdata"}
)
public class SearchDataApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SearchDataApplication.class)
                //类名重复bean的处理
                .beanNameGenerator(new DefaultBeanNameGenerator())
                .run(args);
    }



}
