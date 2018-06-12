package cyf.gradle.api.service;

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
    private LoadingCache<String, String> getLoadingCacheRefresh;

    public String get(String v) throws ExecutionException {

        String s = expireLoadingCache.get(v);
//        getLoadingCacheExpire.invalidate();
        return s;

    }


}
