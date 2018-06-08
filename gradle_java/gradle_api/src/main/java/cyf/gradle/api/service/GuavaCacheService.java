package cyf.gradle.api.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2018-06-04 20:17
 **/
@Service
public class GuavaCacheService {

    private static LoadingCache<String, String> build_2 = null;

    //当缓存失效时，多个线程用相同key获取缓存时，只有一个线程进入load（机制有效避免了缓存穿透），其他线程阻塞等待缓存生成
    static {
        build_2 = CacheBuilder.newBuilder()
                .maximumSize(100)
                //过期时间
                .expireAfterWrite(20, TimeUnit.SECONDS)
                //为了避免很多线程阻塞等待缓存生成，此刷新会只有load一个线程阻塞用于生成缓存，其他线程不阻塞会给返回就的缓存值，但是只有有线程触发时才会刷新
                //但多个线程获取多个key值的缓存时，因load还是会阻塞多个load线程，此时也需要给这个线程返回旧值，更新操作交由线程池异步完成
                .refreshAfterWrite(10,TimeUnit.SECONDS)
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String value) throws Exception {
                        System.out.println("重新load");
                        return "Hi" + value;
                    }
                });
    }


    public String get(String v) throws ExecutionException {

        String c = build_2.get(v);
        return c + " <-------> " ;
    }


}
