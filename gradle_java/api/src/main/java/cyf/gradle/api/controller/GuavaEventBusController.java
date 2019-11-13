package cyf.gradle.api.controller;

import com.google.common.eventbus.EventBus;
import cyf.gradle.api.service.EventListener;
import cyf.gradle.api.service.EventService;
import cyf.gradle.dao.model.Kerr2;
import cyf.gradle.dao.model.Region;
import cyf.gradle.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Cheng Yufei
 * @create 2018-06-04 20:16
 **/
@RestController
@RequestMapping("/event")
@Slf4j
public class GuavaEventBusController {

    @Autowired
    private EventService eventService;
    private AtomicInteger id = new AtomicInteger(1);

    @Autowired
    private EventListener eventListener;

    @GetMapping("/post")
    public /*<T> T*/ Object post() {
        int incrementId = id.getAndIncrement();
        /*User user = User.builder().id(incrementId).build();
        log.debug("初始化User：{} - {}", user.toString(), Thread.currentThread().getName());*/
        /*Kerr2 kerr2 = Kerr2.builder().id(incrementId).build();
        log.debug("初始化Kerr2：{} - {}", kerr2.toString(), Thread.currentThread().getName());
        return (T) eventService.post(kerr2);*/
        //return eventService.post(new Region());
        EventBus eventBus = new EventBus((exception, context) -> {
            System.out.println(context.getSubscriber());
            log.error(exception.getMessage());
        });
        eventBus.register(new EventListener());
        Region region = new Region();
        eventBus.post(region);
        return region;


    }

}
