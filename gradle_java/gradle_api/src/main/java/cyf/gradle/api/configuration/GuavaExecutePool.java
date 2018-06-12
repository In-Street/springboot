package cyf.gradle.api.configuration;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2018-06-08 10:58
 **/
@Configuration
@Slf4j
public class GuavaExecutePool   {

    @Autowired
    private AsyncPoolConfig asyncPoolConfig;


    /**
     * 缓存失效
     * @return
     */
    @Bean
    public LoadingCache<String, String> getLoadingCacheExpire() {

        log.info("<=================初始化 LoadingCache<String, String>=================>");
        LoadingCache<String, String>  cache_Expire = CacheBuilder.newBuilder()
                .maximumSize(100)
                //过期时间,当缓存失效时，多个线程用相同key获取缓存时，只有一个线程进入load,用于缓存生成（机制有效避免了缓存穿透），其他线程阻塞等待缓存生成，但此时某个缓存过期会导致大量请求线程阻塞，引入refreshAfterWrite刷新机制
                .expireAfterWrite(1, TimeUnit.MINUTES)
                //.expireAfterAccess() 在被访问多少秒后失效
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String value) throws Exception {
                        log.info("<================= 过期缓存 - load key: {}=================>",value);
                        return "Expire" + value;
                    }
                });
        return cache_Expire;
    }

    /**
     * 缓存定时刷新（有线程触发）
     * @return
     */
    @Bean
    public LoadingCache<String, String> getLoadingCacheRefresh() {

        log.info("<=================初始化 LoadingCache<String, String>=================>");
        LoadingCache<String, String>  cache_Refresh = CacheBuilder.newBuilder()
                .maximumSize(100)
                //为了避免很多线程阻塞等待缓存生成，此刷新会只有load一个线程阻塞用于生成缓存，其他线程不阻塞会给返回旧的缓存值，但是只有有线程触发时才会刷新
                //但多个线程获取多个key值的缓存时，因load还是会阻塞多个load线程，压力也会穿透到数据库层，此时也需要给这个线程返回旧值，更新操作交由线程池异步完成 -> 进阶
                .refreshAfterWrite(1,TimeUnit.MINUTES)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String value) throws Exception {
                        log.info("<================= Refresh缓存 - load :key:{}=================>",value);
                        return "Refresh" + value;
                    }
                });
        return cache_Refresh;
    }

    /**
     * java.util.concurrent ThreadPoolExecutor 线程池使用
     * @return
     */
    @Bean
    public ThreadPoolExecutor getThreadPoolExecutor() {

        log.info("<=================初始化 ThreadPoolExecutor=================>");

        /**
         * 不建议Executors.创建线程池：
         * newFixedThreadPool、newSingleThreadExecutor :
         * 堆积的请求处理队列会耗费大量内存，OOM
         * newScheduledThreadPool、newCachedThreadPool ：
         * 可创建线程数 Integer.MAX_VALUE ,会创建大量线程导致OOM
         *
         */
        ThreadFactory build = new ThreadFactoryBuilder().setNameFormat("demo_pool_%d").build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(asyncPoolConfig.getCorePoolSize(), asyncPoolConfig.getMaxPoolSize(),
                asyncPoolConfig.getKeepAliveSeconds(), TimeUnit.SECONDS,
                //LinkedBlockingQueue：链表存储数据，有两把锁，放数据锁与取数据锁，放入数据的线程和取出数据的线程可以同时操作；不指定容器大小时为Integer.MAX_VALUE
                //ArrayBlockingQueue：放数据线程与取数据线程是互斥的，需指定容器大小
                new LinkedBlockingQueue<>(1024), build, new ThreadPoolExecutor.AbortPolicy());

        return threadPoolExecutor;
    }


}
