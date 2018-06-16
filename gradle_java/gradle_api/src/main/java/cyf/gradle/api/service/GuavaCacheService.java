package cyf.gradle.api.service;

import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import cyf.gradle.api.configuration.GuavaExecutePool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

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

    public String get(String v) throws ExecutionException {

//        String expire = expireLoadingCache.get(v);

// 缓存显示清除       asyncRefreshLoadingCache.invalidate(key);

        /*String unchecked = expireLoadingCache.getUnchecked(v);
        log.info("unchecked:{}", unchecked);*/

        String refresh = expireLoadingCache.get(v);
        CacheStats stats = expireLoadingCache.stats();
        long hitCount = stats.hitCount();
        double loadTime = stats.averageLoadPenalty();
        log.info("缓存命中次数：{}，新缓存加载时间：{}", hitCount, loadTime);
        return refresh;

    }

    public String futureCallback(String str){

        //并发下可提供成功回调
        ListenableFuture<String> listenableFuture = MoreExecutors.listeningDecorator(threadPoolExecutor).submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread().getName());
                return str +"d";
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

}
