package cyf.gradle.api.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2018-08-04 上午9:54
 **/
@Service
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
    @HystrixCommand(fallbackMethod = "getFallback")
    public String run(String userName) throws Exception {
//        TimeUnit.MILLISECONDS.sleep(600);
        System.out.println("username: " + userName + "---" + Thread.currentThread().getName());
        //抛出异常走降级方法
//        throw new Exception("");
        return "username: " + userName;
    }


    public String getFallback(String userName) {
        return "---------用户模块降级处理：" + userName + " ---------";
    }

    /**
     * 虽然属性中设置了隔离策略，但执行时仍按同步执行
     * @param orderName
     * @return
     * @throws InterruptedException
     */
    @HystrixCommand(commandProperties = {
            //name值从 HystrixPropertiesManager 类获取
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
           @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "2000")
    })
    public String sync(String orderName) throws InterruptedException {
        /**
         * 在不设定超时属性时，sleep时间过长会报TimeOutException
         */
        TimeUnit.MILLISECONDS.sleep(1500);
        System.out.println("同步：" + orderName + "---" + Thread.currentThread().getName());
        return "同步：" + orderName;
    }

    /**
     * 异步执行：需返回 AsyncResult 实列
     * @param orderName
     * @return
     * @throws InterruptedException
     */
    @HystrixCommand
    public Future<String> async(String orderName) throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(500);
        return new AsyncResult<String>(){
            @Override
            public String invoke() {
                System.out.println("异步：" + orderName + "---" + Thread.currentThread().getName());
                return "异步：" + orderName;
            }
        };
    }
}
