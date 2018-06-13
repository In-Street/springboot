package cyf.gradle.api.service;

import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import cyf.gradle.api.configuration.GuavaExecutePool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

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

    public String get(String v) throws ExecutionException {

//        String expire = expireLoadingCache.get(v);

// 缓存显示清除       asyncRefreshLoadingCache.invalidate(key);
//        expireLoadingCache.getUnchecked()


        String refresh = expireLoadingCache.get(v);
        CacheStats stats = expireLoadingCache.stats();
        long hitCount = stats.hitCount();
        double loadTime = stats.averageLoadPenalty();
        log.info("缓存命中次数：{}，新缓存加载时间：{}", hitCount, loadTime);
        return refresh;

    }


}
