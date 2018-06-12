package cyf.gradle.api.configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步线程池
 * 启动类：@EnableAsync 开启，使用 ThreadPoolTaskExecutor ，同属 org.springframework ，需要异步时添加 @Async
 *
 * @author Cheng Yufei
 * @create 2018-01-08 下午10:46
 **/
@Configuration
@Slf4j
public class AsyncTaskExecutePool implements AsyncConfigurer {


    @Autowired
    private AsyncPoolConfig asyncPoolConfig;

    /**
     * 不设置执行异步任务时 提示 no TaskExecutor
     *
     *  杜绝显示创建线程：避免创建、销毁的时间及资源的开销，可能会创建出大量同类线程，消耗内存或者过度切换；因使用线程池
     *  new Thread(()->{
     *             System.out.println();
     *         }).start();
     */

    @Override
    public Executor getAsyncExecutor() {

        log.info("<=================初始化 Async - ThreadPoolTaskExecutor =================>");
        /**
         * ThreadPoolTaskExecutor :
         * 1.属于org.springframework.scheduling.concurrent ，在初始化 initializeExecutor中仍是 ThreadPoolExecutor (java.util.concurrent)
         * 2. implements AsyncTaskExecutor --> TaskExecutor --> Executor
         *               /ə'zɪŋk/                              /ɪgˈzekjətə(r)/
         */
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncPoolConfig.getCorePoolSize());
        executor.setKeepAliveSeconds(asyncPoolConfig.getKeepAliveSeconds());
        executor.setMaxPoolSize(asyncPoolConfig.getMaxPoolSize());
        executor.setQueueCapacity(asyncPoolConfig.getQueueCapacity());
        /**
         * 线程数已最大，队列已满，此时新来的任务处理方式
         * 线程池对拒绝任务（无线程可用）的处理策略，目前只支持AbortPolicy、CallerRunsPolicy
         AbortPolicy:直接抛出java.util.concurrent.RejectedExecutionException异常 -->
         CallerRunsPolicy:主线程直接执行该任务，执行完之后尝试添加下一个任务到线程池中，可以有效降低向线程池内添加任务的速度 -->
         DiscardOldestPolicy:抛弃旧的任务、暂不支持；会导致被丢弃的任务无法再次被执行 -->
         DiscardPolicy:抛弃当前任务、暂不支持；会导致被丢弃的任务无法再次被执行 -->
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //ThreadFactory 生产线程
        executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("demo_pool_%d").build());
//        executor.setThreadNamePrefix();
        //初始化
        executor.initialize();

        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler() {
            @Override
            public void handleUncaughtException(Throwable arg0, Method arg1, Object... arg2) {
                log.error("==========================" + arg0.getMessage() + "=======================", arg0);
                log.error("exception method:" + arg1.getName());
            }
        };
    }

}


