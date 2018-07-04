package cyf.gradle.api.service;

import com.google.common.eventbus.EventBus;
import cyf.gradle.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Cheng Yufei
 * @create 2018-06-17 上午11:10
 **/
@Service
@Slf4j
public class EventService {


    @Autowired
    private EventBus stringEventBus;
    @Autowired
    private EventBus intAsyncEventBus;

    public  <K>  K genericity(K k) {

       return k;
    }

    public  void post(User user) {
        stringEventBus.post(user.getUsername());
        intAsyncEventBus.post(user.getId());
    }
}
