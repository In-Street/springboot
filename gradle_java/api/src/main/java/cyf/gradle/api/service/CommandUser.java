package cyf.gradle.api.service;

import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2018-08-04 上午9:54
 **/
@Slf4j
public class CommandUser extends HystrixCommand<String> {

    private String userName;

    public CommandUser(String userName) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("user"))

                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("user_thread_pool"))

                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(5)
                        .withMaxQueueSize(6)
                        .withKeepAliveTimeMinutes(5)
                        .withQueueSizeRejectionThreshold(50))

                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)));
        this.userName = userName;
    }

    @Override
    protected String run() throws Exception {
        TimeUnit.MILLISECONDS.sleep(100);
        log.debug(Thread.currentThread().getName());
        return "username: " + userName;
    }
}
