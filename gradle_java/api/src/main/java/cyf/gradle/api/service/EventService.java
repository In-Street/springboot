package cyf.gradle.api.service;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Cheng Yufei
 * @create 2018-06-17 上午11:10
 **/
@Service
@Slf4j
public class EventService<T> {


    @Autowired
    private EventBus eventBus;
    @Autowired
    private AsyncEventBus asyncEventBus;

    public <K> K genericity(K k) {

        return k;
    }

    public T post(T t) {
        eventBus.post(t);
        //asyncEventBus.post(t);
        return t;
    }
}
