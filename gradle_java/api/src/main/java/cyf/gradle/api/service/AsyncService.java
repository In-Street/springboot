package cyf.gradle.api.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2018-01-08 下午9:42
 **/
@Service
@Slf4j
public class AsyncService {

    @Autowired
    private HandleAsyncService handleAsyncService;

    @Autowired
    private ApplicationContext context;


    private AsyncService proxy;

    /*@PostConstruct
    public void init() {
        proxy = ( (AsyncService)AopContext.currentProxy()); // 此写法 Cannot find current proxy
        proxy = context.getBean(AsyncService.class); //此写法还是请求线程调用，无法异步
    }*/

    /**
     * 异步 方法如果不设置 返回值 ，在Controller中调用会出现：没有任何任务相关的输出；有部分任务相关的输出；乱序的任务相关的输出；
     * 主程序并不会理会这三个函数是否执行完成了，由于没有其他需要执行的内容，所以程序就自动结束了，导致了不完整或是没有输出任务相关内容的情况。
     * <p>
     * 使用Future<T>来返回异步调用的结果 （task1.isDone()）
     * <p>
     * <p>
     * 1.异步方法失效情况：方法使用 static修饰 ；本类中调用 ；与同步相比节省大量时间
     * 2.如果在本类中使用@Async 需用代理调用，否则异步不生效;
     * 3.多个Service 存在循环 @Autowired 时，加载A 时，有注入B ，加载B时，发现有A的注入，A中有@Async 需要代理，此时A已经被Spring代理，报错：
     * <p>
     * Bean with name ‘xxxService’ has been injected into other beans [xxxService] in its raw version as part of a circular reference, but has eventually been wrapped. This means that said other beans do not use the final version of the bean. This is often the result of over-eager type matching – consider using ‘getBeanNamesOfType’ with the ‘allowEagerInit’ flag turned off, for example.
     * <p>
     * 解决：在某一个重复调用的地方加:  @Autowired 两者共同使用，采用懒加载
     *
     * @return
     * @Lazy
     */
    @Async
    public Future<String> doTask1() {
        long start = System.currentTimeMillis();
        log.debug("异步任务一开始执行：{}  - {}", FastDateFormat.getInstance("HH:mm:ss").format(new Date()), Thread.currentThread().getName());

        try {
            Thread.sleep(1000);
            long end = System.currentTimeMillis();
            log.debug("异步任务一执行结束耗时：{}", (end - start) + "ms");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>("任务一结束");
    }

    @Async
    public Future<String> doTask2() {
        long start = System.currentTimeMillis();
        log.debug("异步任务二开始执行：{} - {}", FastDateFormat.getInstance("HH:mm:ss").format(new Date()), Thread.currentThread().getName());

        try {
            Thread.sleep(1000);
            long end = System.currentTimeMillis();
            log.debug("异步任务二执行结束耗时：{}", (end - start) + "ms");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>("任务二结束");
    }

    @Async
    public Future<String> doTask3() {
        long start = System.currentTimeMillis();
        log.debug("异步任务三开始执行：{} - {}", FastDateFormat.getInstance("HH:mm:ss").format(new Date()), Thread.currentThread().getName());

        try {
            Thread.sleep(1000);
            long end = System.currentTimeMillis();
            log.debug("异步任务三执行结束耗时：{}", (end - start) + "ms");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>("任务三结束");
    }

    public void handle() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(500);
        log.info("<===正常业务执行===>");

        //没有ThreadPoolTaskExecutor线程池情况下，默认使用 SimpleAsyncTaskExecutor,每次都新创建没有复用。如果全局获取@PostConstruct则无法实现异步

//        context.getBean(AsyncService.class).insert();
//        ((AsyncService)AopContext.currentProxy()).insert();
//        proxy.insert();
        handleAsyncService.insert();
    }

    @Async
    public void insert() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(200);

        log.debug("自调用异步方法：{}", Thread.currentThread().getName());
    }
}
