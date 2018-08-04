package cyf.gradle.api.service;

import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2018-08-02 17:11
 **/
@Slf4j
public class CommandOrder extends HystrixCommand<String> {

    private String orderName;

    public CommandOrder(String orderName) {
        super(Setter
                //服务分组
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("order"))

                //线程组
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("order_thread_pool"))

                //线程池配置
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(5)
                        .withMaxQueueSize(6)
                        .withKeepAliveTimeMinutes(5)
                        .withQueueSizeRejectionThreshold(50))

        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
        );

        this.orderName = orderName;
    }

    @Override
    protected String run() throws Exception {
        TimeUnit.MILLISECONDS.sleep(100);
        log.debug(Thread.currentThread().getName());
        return "order: " + orderName;
    }
}
