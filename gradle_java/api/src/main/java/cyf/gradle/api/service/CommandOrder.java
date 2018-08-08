package cyf.gradle.api.service;

import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * HystrixCommand 实现线程池的隔离使用，多个线程池针对多个业务，避免一个业务占满线程池导致其他业务无法实施
 *
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
        //隔离配置
        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
        );

        this.orderName = orderName;
    }

    @Override
    protected String run() throws Exception {
        TimeUnit.MILLISECONDS.sleep(100);
//        log.debug(Thread.currentThread().getName());
//        throw new Exception("");
        return "order: " + orderName;
    }

    /**
     *
     * 熔断降级方法：
     *
     * 熔断：默认情况下，如果run方法在运行期间，10秒总请求数超过20个，且有50%以上的请求发生异常，Hystrix内部会自动发生熔断，并且执行getFallback方法。
     *
     * 恢复：默认情况下，如果发生了熔断，Hystrix内部每隔5s进行一次试探，即放过一个正常请求到后端服务，如果这个请求成功了，就算后端服务恢复了，Hystrix内部会自动关闭熔断。
     *
     * @return
     */
    @Override
    protected String getFallback() {
        return "---------订单模块降级处理：" + orderName + "---------";
    }
}
