package cyf.gradle.interview.controller.time;

import cyf.gradle.interview.service.time_concurrent.stampedlock.StampedLockService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Cheng Yufei
 * @create 2019-06-17 16:29
 **/
@RestController
@RequestMapping("/stampedLock")
public class StampedLockController {

    @Resource
    private StampedLockService stampedLockService;

    @GetMapping("/read")
    public String limit() throws InterruptedException {
        stampedLockService.optimisticRead();
        return "success";
    }

    @PostMapping("/write/{newA}/{newB}")
    public String write(@PathVariable Integer newA, @PathVariable Integer newB) throws InterruptedException {
        stampedLockService.write(newA, newB);
        return "success";
    }
}
