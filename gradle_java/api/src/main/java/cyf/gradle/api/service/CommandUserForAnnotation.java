package cyf.gradle.api.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2018-08-04 上午9:54
 **/
@Service
@Slf4j
public class CommandUserForAnnotation {


    /**
     * 注解形式 利用 HystrixCommand 进行熔断降级,指定降级方法，降级的与主方法的返回值与参数需保持一致
     * <p>
     * 需将 HystrixCommandAspect 类已bean形式注入spring ： 参考 HystrixConfiguration 类
     *
     * @param userName
     * @return
     * @throws Exception
     */
    @HystrixCommand(fallbackMethod = "getFallback",
            commandProperties = {
                    /**
                     * 线程池隔离：业务请求线程和依赖服务的执行线程是俩个线程
                     * 信号量隔离：（业务请求线程和依赖服务的执行线程是同一个线程）
                     *      如果不依赖网络访问的服务，或者依赖服务是极低延迟的，比如访问内存缓存，使用信号量隔离会更为轻量和开销小
                     *
                     */
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
            })
    public String run(String userName, ThreadLocal threadLocal) throws Exception {
//        TimeUnit.MILLISECONDS.sleep(600);
        log.debug("username: {} ---Thread:{}", userName, Thread.currentThread().getName());
//        System.out.println("username: " + userName + "---" + Thread.currentThread().getName() + "ThreadLocal:" + threadLocal.get());
        //抛出异常走降级方法
//        throw new Exception("");
        //信号量隔离服务执行线程和业务请求线程是同一个线程，可传递ThreadLocal
        return "run - username: " + userName + "---------ThreadLocal:" + threadLocal.get();
    }


    public String getFallback(String userName, ThreadLocal threadLocal) {
        return "---------用户模块降级处理：" + userName + " ---------";
    }

    /**
     * 虽然属性中设置了隔离策略，但执行时仍按同步执行
     *
     * @param orderName
     * @return
     * @throws InterruptedException
     */
    @HystrixCommand(commandProperties = {
            //name值从 HystrixPropertiesManager 类获取
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000")
    })
    public String sync(String orderName, ThreadLocal threadLocal) throws InterruptedException {
        /**
         * 在不设定超时属性时，sleep时间过长会报TimeOutException
         */
//        TimeUnit.MILLISECONDS.sleep(1500);
        System.out.println("同步：" + orderName + "---" + Thread.currentThread().getName() + "----" + "ThreadLocal:" + threadLocal.get());
        return "同步：" + orderName;
    }

    /**
     * 异步执行：需返回 AsyncResult 实列
     *
     * @param orderName
     * @return
     * @throws InterruptedException
     */
    @HystrixCommand
    public Future<String> async(String orderName, ThreadLocal threadLocal) throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(500);
        return new AsyncResult<String>() {
            @Override
            public String invoke() {

                System.out.println("异步：" + orderName + "---" + Thread.currentThread().getName() + "----" + "ThreadLocal:" + threadLocal.get());
                return "异步：" + orderName;
            }
        };
    }

    @HystrixCommand(fallbackMethod = "fallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
            },
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "5"),
                    @HystrixProperty(name = "maxQueueSize", value = "7"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "5"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "50")
            })
    public String handle(String userName, ThreadLocal threadLocal) throws Exception {
        log.debug("username: {} ---Thread:{} --- ThreadLocal：{}", userName, Thread.currentThread().getName(), threadLocal.get());
        //抛出异常走降级方法
//        throw new Exception("");
        return "run - username: " + userName;
    }

    public String fallback(String userName, ThreadLocal threadLocal) {
        return "---------用户模块降级处理：" + userName + " ---------";
    }

    @HystrixCommand
    public Future<String> async(String userName){

        return new AsyncResult<String>() {
            @Override
            public String invoke() {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("异步:{}, --- Thread:{}", userName, Thread.currentThread().getName());
                return "异步：" + userName;
            }
        };
    }
}
