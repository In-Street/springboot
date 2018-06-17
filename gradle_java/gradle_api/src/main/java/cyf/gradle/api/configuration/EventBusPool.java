package cyf.gradle.api.configuration;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 事件总线pool
 *
 * @author Cheng Yufei
 * @create 2018-06-17 上午10:28
 **/
@Configuration
@Slf4j
public class EventBusPool {

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * String 处理事件总线
     * @return
     */
    @Bean
    public EventBus stringEventBus() {
        return new EventBus();
    }

    /**
     * Integer 异步事件处理总线
     * @return
     */
    @Bean
    public AsyncEventBus intAsyncEventBus() {
        return new AsyncEventBus(threadPoolExecutor);
    }
}
