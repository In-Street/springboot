package cyf.gradle.pay;

import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * boot入口
 *
 */
@SpringBootApplication(
        scanBasePackages = {"cyf.gradle.pay"}
)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class PayApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(PayApplication.class)
                //类名重复bean的处理
                .beanNameGenerator(new DefaultBeanNameGenerator())
                .run(args);
    }
}
