package cyf.gradle.api.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 线程池参数设置
 * @author Cheng Yufei
 * @create 2018-01-08 下午10:37
 **/
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "async.pool")
public class AsyncPoolConfig {

    /**
     * 核心线程数
     * Runtime.getRuntime().availableProcessors():获取CPU核数进心参数设定
     */
    private int corePoolSize ;
    /**
     *最大线程数
     */
    private int maxPoolSize ;
    /**
     *线程池维护线程所允许的空闲时间(最大不活动时间，否则清除此线程)
     */
    private int keepAliveSeconds ;
    /**
     *队列长度，存放提交的任务
     */
    private int queueCapacity ;


    /**
     * 需要根据几个值来决定
     * tasks ：每秒的任务数，假设为500~1000
     * taskcost：每个任务花费时间，假设为0.1s
     * responsetime：系统允许容忍的最大响应时间，假设为1s
     *
     * 做几个计算
     * corePoolSize = 每秒需要多少个线程处理？
     * threadcount = tasks/(1/taskcost) =tasks*taskcout =  (500~1000)*0.1 = 50~100 个线程。corePoolSize设置应该大于50
     * 根据8020原则，如果80%的每秒任务数小于800，那么corePoolSize设置为80即可
     *
     * queueCapacity = (coreSizePool/taskcost)*responsetime
     * 计算可得 queueCapacity = 80/0.1*1 = 80。意思是队列里的线程可以等待1s，超过了的需要新开线程来执行
     * 切记不能设置为Integer.MAX_VALUE，这样队列会很大，线程数只会保持在corePoolSize大小，当任务陡增时，不能新开线程来执行，响应时间会随之陡增。
     *
     *
     * maxPoolSize = (max(tasks)- queueCapacity)/(1/taskcost)
     * 计算可得 maxPoolSize = (1000-80)/10 = 92
     * （最大任务数-队列容量）/每个线程每秒处理能力 = 最大线程数
     *
     * rejectedExecutionHandler：根据具体情况来决定，任务不重要可丢弃，任务重要则要利用一些缓冲机制来处理
     * keepAliveTime和allowCoreThreadTimeout采用默认通常能满足
     */

}
