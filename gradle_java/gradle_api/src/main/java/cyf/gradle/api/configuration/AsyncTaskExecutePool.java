package cyf.gradle.api.configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
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
     * @return
     */

    private static ThreadPoolTaskExecutor executor;

    public static ThreadPoolTaskExecutor getExecutor() {
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncPoolConfig.getCorePoolSize());
        executor.setKeepAliveSeconds(asyncPoolConfig.getKeepAliveSeconds());
        executor.setMaxPoolSize(asyncPoolConfig.getMaxPoolSize());
        executor.setQueueCapacity(asyncPoolConfig.getQueueCapacity());
        /**
         * 线程池对拒绝任务（无线程可用）的处理策略，目前只支持AbortPolicy、CallerRunsPolicy
         AbortPolicy:直接抛出java.util.concurrent.RejectedExecutionException异常 -->
         CallerRunsPolicy:主线程直接执行该任务，执行完之后尝试添加下一个任务到线程池中，可以有效降低向线程池内添加任务的速度 -->
         DiscardOldestPolicy:抛弃旧的任务、暂不支持；会导致被丢弃的任务无法再次被执行 -->
         DiscardPolicy:抛弃当前任务、暂不支持；会导致被丢弃的任务无法再次被执行 -->
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        AsyncTaskExecutePool.executor = executor;
        log.info("<=================初始化Executor=================>");

        Executors.newScheduledThreadPool()

        /**
         * 不建议Executors.创建线程池：
         * newFixedThreadPool、newSingleThreadExecutor
         *
         * newScheduledThreadPool、
         *
         */
        ThreadFactory build = new ThreadFactoryBuilder().setNameFormat("demo_pool_%d").build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(asyncPoolConfig.getCorePoolSize(), asyncPoolConfig.getMaxPoolSize(),
                asyncPoolConfig.getKeepAliveSeconds(), TimeUnit.SECONDS, new LinkedBlockingQueue<>(1024), build, new ThreadPoolExecutor.AbortPolicy());



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


