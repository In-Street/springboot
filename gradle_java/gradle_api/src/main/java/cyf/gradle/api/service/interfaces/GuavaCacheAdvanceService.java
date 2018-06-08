package cyf.gradle.api.service.interfaces;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ListenableFuture;
import cyf.gradle.api.configuration.AsyncTaskExecutePool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

/**
 * @author Cheng Yufei
 * @create 2018-06-05 19:35
 **/
@Service
@Slf4j
public class GuavaCacheAdvanceService {

    static {
        CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {

            @Override
            public String load(String key) throws Exception {
                log.info("<====================load====================>");
                return "load_" + key;
            }

            @Override
            public ListenableFuture<Object> reload(String key, String oldValue) throws Exception {
               return (ListenableFuture<Object>)AsyncTaskExecutePool.getExecutor().submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        log.info("<==================== reload call ====================>");
                        return "load_" + key;
                    }
                });
//                return super.reload(key, oldValue);

            }
        });
    }
}
