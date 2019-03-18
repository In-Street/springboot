package cyf.gradle.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * boot入口
 */
@SpringBootApplication(
        scanBasePackages = {"cyf.gradle.admin"}, exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class}
)

//排除mongo自动配置 或者在 @SpringBootApplication 中排除，否则即使 把dao层的mongo配置注释也会自动加载 localhost：27017的配置
//@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class,MongoDataAutoConfiguration.class})

@EnableAsync
/**
 * 1.通过AopContext上下文获取代理对象,用于 数据库事务操作 或者 通过ApplicationContext上下文 详见 TransactionProxyService （test5()）、TransactionProxyService1（test1()）
 * 2. exposeProxy = true: 通过Aop框架暴露该代理对象，AopContext能够访问
 * 3. ApplicationContext 获取代理对象时无需开启暴露
 * 4. proxyTargetClass = true ：参数设为true时,表示使用CGLIB来为目标对象创建代理子类实现AOP，否则使用jdk基于接口的代理；
 *                              与在application.yml 中标注：spring.aop.proxy-target-class: true 效果一样
 */
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableAdminServer
public class MonitorApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MonitorApplication.class)
                //类名重复bean的处理
                .beanNameGenerator(new DefaultBeanNameGenerator())
                .run(args);
    }
}
