package cyf.gradle.api.controller;

import cyf.gradle.api.service.AsyncService;
import cyf.gradle.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

/**
 * @author Cheng Yufei
 * @create 2018-01-08 下午9:52
 **/
@RestController
@RequestMapping("/async")
@Slf4j
public class AsyncController {


    @Autowired
    private AsyncService asyncService;

    @Autowired
    private UserService userService;

    @GetMapping("/async")
    public void async() throws InterruptedException {
        long start = System.currentTimeMillis();

        Future<String> task1 = asyncService.doTask1();
        Future<String> task2 = asyncService.doTask2();
        Future<String> task3 = asyncService.doTask3();

       /* asyncService.doTask1();
         asyncService.doTask2();
        asyncService.doTask3();*/

        while (true) {
            if (task1.isDone() && task2.isDone() && task3.isDone()) {
                break;
            }

        }
        Thread.sleep(1000);
        long end = System.currentTimeMillis();
        log.debug("----------异步任务全部执行完成耗时：{}----------", (end - start) + "ms");
    }

    /**
     *  同步(4012ms) 、异步(2050ms)耗时比较
     * @throws InterruptedException
     */
//    @GetMapping("/sync")
    @RequestMapping("/sync")
    public void synchronization() throws InterruptedException {
        long start = System.currentTimeMillis();
        userService.doTask1();
        userService.doTask2();
        userService.doTask3();
        Thread.sleep(1000);
        long end = System.currentTimeMillis();
        log.debug("----------同步任务全部执行完成耗时：{}----------", (end - start) + "ms");
    }

    @GetMapping("/handle")
    public void handle() throws InterruptedException {
        asyncService.handle();
//        asyncService.insert();
    }
}
