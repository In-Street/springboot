package cyf.gradle.api.controller;

import cyf.gradle.api.service.EventService;
import cyf.gradle.dao.model.Kerr2;
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

    @GetMapping("/post")
    public <T> T post() {
        int incrementId = id.getAndIncrement();
        /*User user = User.builder().id(incrementId).build();
        log.debug("初始化User：{} - {}", user.toString(), Thread.currentThread().getName());*/
        Kerr2 kerr2 = Kerr2.builder().id(incrementId).build();
        log.debug("初始化Kerr2：{} - {}", kerr2.toString(), Thread.currentThread().getName());
        return (T) eventService.post(kerr2);
    }

}
