package cyf.gradle.api.service;

import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2018-06-04 20:17
 **/
@Service
@Slf4j
public class GuavaCacheService {

    @Autowired
    private LoadingCache<String, String> expireLoadingCache;
    @Autowired
    private LoadingCache<String, String> refreshLoadingCache;
    @Autowired
    private LoadingCache<String, String> asyncRefreshLoadingCache;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private ApplicationContext applicationContext;


    //    @Transactional(rollbackFor = Exception.class)
    public String get(String key) throws ExecutionException, InterruptedException {

//        String expire = expireLoadingCache.get(v);

// 缓存显示清除       asyncRefreshLoadingCache.invalidate(key);

        /*String unchecked = expireLoadingCache.getUnchecked(v);
        log.info("unchecked:{}", unchecked);*/

        String refresh = expireLoadingCache.get(key);
//        expireLoadingCache.put();
        CacheStats stats = expireLoadingCache.stats();
        long hitCount = stats.hitCount();
        double loadTime = stats.averageLoadPenalty();
        log.info("缓存命中次数：{}，新缓存加载时间：{}", hitCount, loadTime);

        // jemter 测试线程池(ThreadPoolExecutor)
       /* Future<?> submitFuture = threadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(600);
                    log.info("线程：{}，执行完毕", Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });*/
        /*threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(600);
                    log.info("线程：{}，执行完毕", Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });*/
        /**
         * 异步线程池：ThreadPoolTaskExecutor
         *  1.如果本方法没有@Transactional, 用AopContext调用本类的@Async 异步方法时，类转换错误，因为获取的当前代理类是Controller的代理类转换不成Service类
         *  2.加上@Transactional后，Service 本类方法因为事务产生代理， AopContext 获取的当前的代理类是Service，可转换成service 执行@Async 异步方法
         *  3. 使用 ApplicationContext 获取当前类的代理类，可处理异步，但不能用@PostConstruct设置全局的代理，否则处理异步仍是请求的线程在处理
         */
//        ((GuavaCacheService) AopContext.currentProxy()).taskPool();
       /* GuavaCacheService proxy = applicationContext.getBean(GuavaCacheService.class);
        proxy.taskPool();*/
        return refresh;
    }

    public String futureCallback(String str) {

        //并发下可提供成功回调
        ListenableFuture<String> listenableFuture = MoreExecutors.listeningDecorator(threadPoolExecutor).submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread().getName());
                return str + "d";
            }
        });

        Futures.addCallback(listenableFuture, new FutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(Thread.currentThread().getName());
                System.out.println(result.toUpperCase());
            }

            @Override
            public void onFailure(Throwable t) {
                return;
            }
        });
        return str;

    }

    @Async
    public void taskPool() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(600);
        log.info("task线程：{}，执行完毕", Thread.currentThread().getName());
    }

}
