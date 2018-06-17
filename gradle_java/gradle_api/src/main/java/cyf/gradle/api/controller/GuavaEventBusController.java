package cyf.gradle.api.controller;

import cyf.gradle.api.service.EventService;
import cyf.gradle.api.service.GuavaCacheService;
import cyf.gradle.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

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

    @GetMapping("/post")
    public String post(){
        User user = new User(1, "swift");
         eventService.post(user);
        System.out.println(eventService.genericity("gen"));
        return "";
    }

}
