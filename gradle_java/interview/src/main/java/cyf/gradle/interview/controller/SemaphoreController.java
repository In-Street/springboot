package cyf.gradle.interview.controller;

import cyf.gradle.interview.service.time_concurrent.semaphore.LimitingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 信号量模式
 * @author Cheng Yufei
 * @create 2019-06-17 11:25
 **/
@RestController
@RequestMapping("/time")
public class SemaphoreController {

    @Resource
    private LimitingService limitingService;

    @GetMapping("/limit")
    public String limit() throws InterruptedException {
        limitingService.limit();
        return "success";
    }
}
