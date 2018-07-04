package cyf.gradle.api.service;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 事件监听，当EventBus 发布时，相应的监听器进行业务处理
 *
 * @author Cheng Yufei
 * @create 2018-06-17 上午10:33
 **/
@Component
@Slf4j
public class EventListener {


    @Subscribe
    //允许并发执行
    @AllowConcurrentEvents
    public String stringListener(String str) {

        String result = str.toUpperCase();
        log.info("String事件监听器处理：{} - {}", result, Thread.currentThread().getName());
        return result;
    }

    @Subscribe
    public Integer intListener(Integer i) {
        Integer integer = i + 100;
        log.info("Integer事件监听器处理：{} - {}", integer, Thread.currentThread().getName());
        return integer;
    }

}
