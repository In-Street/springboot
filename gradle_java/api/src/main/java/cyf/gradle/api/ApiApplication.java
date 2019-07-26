package cyf.gradle.api;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowSlot;
import com.cxytiandi.encrypt.springboot.annotation.EnableEncrypt;
import com.google.common.collect.Lists;
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
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.servlet.MultipartConfigElement;
import java.util.ArrayList;

/**
 * boot入口sys
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
/**
 * 指定外部配置位置git
 */
//@PropertySource()
public class ApiApplication {

    public static void main(String[] args) {
        flowRule();
        degradeRule2();
        authorityRule3();
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
     * 流控限制：
     * resource：规则的资源名
     * grade：限流阈值类型，qps 或线程数
     * count：限流的阈值
     * limitApp：流控针对的调用来源，若为 default 则不区分调用来源,多个 , 分割
     * strategy：基于调用关系的限流策略
     * controlBehavior：流量控制效果（直接拒绝、Warm Up、匀速排队）
     *                 1. 直接拒绝：超过阀值新请求拒绝，抛出FlowException,【通过压测，已知系统确切处理能力的情况下】
     *                 2. warm up: 慢启动预热，系统通过流量缓慢增加，在一定时间内达到阀值 【系统长期处于低水位，流量突然增加，瞬间压垮】
     *                 3. 匀速排队： 请求匀速通过
     *
     * 可通过：http://localhost:8719/cnode?id=资源名 查看通过线程及阻塞线程个数
     * 实时查询【相当于文件夹里 log/csp 里日志记录】：http://localhost:8719/metric?identity=资源名&startTime=开始时间戳&endTime=结束时间戳&maxLines=最大行数
     *
     * 参考：https://github.com/all4you/sentinel-tutorial/blob/master/sentinel-practice/sentinel-flow-control/sentinel-flow-control.md
     *      https://github.com/alibaba/Sentinel/wiki/%E6%B5%81%E9%87%8F%E6%8E%A7%E5%88%B6
     */
    private static void flowRule() {
        ArrayList<FlowRule> flowRules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("SentinelByName");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(10);
        rule.setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_WARM_UP_RATE_LIMITER);
        //匀速通过时设置最大排队时间，超过时长请求拒绝
        rule.setMaxQueueingTimeMs(1000);
        rule.setWarmUpPeriodSec(15);

        flowRules.add(rule);
        FlowRuleManager.loadRules(flowRules);
    }

    /**
     * 熔断降级：
     *      【调用链路某个资源不稳定（调用超时或异常比例升高），导致请求堆积。对资源限制，让快速失败，避免影响其他资源导致级联错误】
     *    降级策略：
     *          1. 平均响应时间：(RT)
     *              count(ms) 设置阀值，超过阀值进入降级状态。 QPS>5 内的响应时间都超过阀值，则在 timeWindow(s)时间内的请求都会自动熔断，抛出DegradeException
     *              注意： Sentinel 默认统计的 RT 上限是 4900 ms，超出此阈值的都会算作 4900 ms，若需要变更此上限可以通过启动配置项 -Dcsp.sentinel.statistic.max.rt=xxx 来配置。
     *
     *          2. 错误比例：
     *               资源每秒内的 异常数/通过量 的比值超过 count ,在timeWindow时间内的请求会自动返回。
     *
     *          3. 异常数：
     *               近1分钟内的异常数超过count, 自动熔断
     */

    private static void degradeRule2() {
        DegradeRule degradeRule = new DegradeRule();
        degradeRule.setResource("SentinelByName2");
        degradeRule.setCount(5);
        //(0: 平均响应时间, 1: 错误比率).
        degradeRule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        //降级发生时，恢复正常的时间（s）
        degradeRule.setTimeWindow(10);
        DegradeRuleManager.loadRules(Lists.newArrayList(degradeRule));
    }

    /**
     * 黑白名单
     */
    private static  void authorityRule3() {

        AuthorityRule blackRule = new AuthorityRule();
        blackRule.setResource("SentinelByName3");
        blackRule.setStrategy(RuleConstant.AUTHORITY_BLACK);
        blackRule.setLimitApp("blackApp");
        AuthorityRuleManager.loadRules(Lists.newArrayList(blackRule));
    }

    /**
     * sentinel 动态规则扩展：
     *              使用API （loadRules）设定规则；
     *              使用数据源[使用推模式，不采用拉模式]： zookeeper  nacos  apollo 三种规则
     *
     * 集群流控？？？
     */
}
