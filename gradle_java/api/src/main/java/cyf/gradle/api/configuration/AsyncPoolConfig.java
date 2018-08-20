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
}
