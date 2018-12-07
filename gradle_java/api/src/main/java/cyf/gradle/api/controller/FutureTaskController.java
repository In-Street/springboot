package cyf.gradle.api.controller;

import cyf.gradle.api.service.FutureTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2018-01-08 下午9:52
 **/
@RestController
@RequestMapping("/future")
@Slf4j
public class FutureTaskController {


    @Autowired
    private FutureTaskService futureTaskService;

    /**
     */
    @GetMapping("/futureHandle")
    public void futureHandle() {
        futureTaskService.futureHandle();
    }

    @GetMapping("/commonHandle")
    public void commonHandle() {
        futureTaskService.commonHandle();
    }
}
