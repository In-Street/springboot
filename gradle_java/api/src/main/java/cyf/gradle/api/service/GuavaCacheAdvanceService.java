package cyf.gradle.api.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            public ListenableFuture<String> reload(String key, String oldValue) throws Exception {


                return null;
            }
        });
    }
}
