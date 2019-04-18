package cyf.gradle.api;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowSlot;
import com.cxytiandi.encrypt.springboot.annotation.EnableEncrypt;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.servlet.MultipartConfigElement;
import java.util.ArrayList;

/**
 * boot入口
 */
//@EnableCaching
@SpringBootApplication(
        scanBasePackages = {"cyf.gradle.api", "cyf.gradle.dao"}, exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class}
)

//排除mongo自动配置 或者在 @SpringBootApplication 中排除，否则即使 把dao层的mongo配置注释也会自动加载 localhost：27017的配置
//@EnableAutoConfiguration(exclude={MongoAutoConfiguration.class,MongoDataAutoConfiguration.class})

//开启异步使用
@EnableAsync
/**
 * 1.通过AopContext上下文获取代理对象,用于 数据库事务操作 或者 通过ApplicationContext上下文 详见 TransactionProxyService （test5()）、TransactionProxyService1（test1()）
 * 2. exposeProxy = true: 通过Aop框架暴露该代理对象，AopContext能够访问
 * 3. ApplicationContext 获取代理对象时无需开启暴露
 * 4. proxyTargetClass = true ：参数设为true时,表示使用CGLIB来为目标对象创建代理子类实现AOP，否则使用jdk基于接口的代理；
 *                              与在application.yml 中标注：spring.aop.proxy-target-class: true 效果一样
 */
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
/**
 * 使用filter形式无需添加此注解
 * 配置文件形式、  java -javaagent:/usr/local/soft/prometheus/jmx_prometheus_javaagent-0.11.0.jar=3010:/usr/local/soft/prometheus/jmx_exporter.yml -jar yourJar.jar
 */
@EnableEncrypt
public class ApiApplication {

    public static void main(String[] args) {
        initSentinelRule();
        new SpringApplicationBuilder(ApiApplication.class)
                //类名重复bean的处理
                .beanNameGenerator(new DefaultBeanNameGenerator())
                .run(args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("-1");
        factory.setMaxRequestSize("-1");
        //设置上传文件时临时路径
//        factory.setLocation();
        return factory.createMultipartConfig();
    }

    /**
     * prometheus 监控
     * @param applicationName
     * @return
     */
    @Bean
    MeterRegistryCustomizer<MeterRegistry> configurer(@Value("${spring.application.name}")String applicationName) {
        return registry -> registry.config().commonTags("application", applicationName);
    }

    /**
     * sentinel 限流
     *
     * 一条FlowRule有以下几个重要的属性组成：
     *
     * resource：规则的资源名
     * grade：限流阈值类型，qps 或线程数
     * count：限流的阈值
     * limitApp：流控针对的调用来源，若为 default 则不区分调用来源,多个 , 分割
     * strategy：基于调用关系的限流策略
     * controlBehavior：流量控制效果（直接拒绝、Warm Up、匀速排队）
     *                 1. 直接拒绝：超过阀值新请求拒绝，抛出FlowException,【通过压测，已知系统确切处理能力的情况下】
     *                 2. warm up: 冷启动，系统通过流量缓慢增加，在一定时间内达到阀值 【系统长期处于低水位，流量突然增加，瞬间压垮】
     *                 3. 匀速排队： 请求匀速通过
     *
     * 可通过：http://localhost:8719/cnode?id=资源名 查看通过线程及阻塞线程个数
     *
     * 参考：https://github.com/all4you/sentinel-tutorial/blob/master/sentinel-practice/sentinel-flow-control/sentinel-flow-control.md
     *      https://github.com/alibaba/Sentinel/wiki/%E6%B5%81%E9%87%8F%E6%8E%A7%E5%88%B6
     */
    private static void initSentinelRule() {
        ArrayList<FlowRule> flowRules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("SentinelByName");
        //此处添加流控未生效
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(3);
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER);

        flowRules.add(rule);
        FlowRuleManager.loadRules(flowRules);
    }
}
