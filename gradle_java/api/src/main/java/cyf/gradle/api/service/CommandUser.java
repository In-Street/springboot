package cyf.gradle.api.service;

import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * HystrixCommand 实现线程池的隔离使用，多个线程池针对多个业务，避免一个业务占满线程池导致其他业务无法实施
 *
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
                //线程池隔离配置（CommandOrder业务、CommandUser业务进行线程池隔离，避免一个业务把线程池沾满）
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                        //设置超时时间
                        .withExecutionTimeoutInMilliseconds(500)));
        this.userName = userName;
    }

    @Override
    protected String run() throws Exception {
        TimeUnit.MILLISECONDS.sleep(400);
//        log.debug(Thread.currentThread().getName());
        System.out.println("username: " + userName);
        return "username: " + userName;
    }

    @Override
    protected String getFallback() {
        return "---------用户模块降级处理：" + userName + "---------";
    }
}
