package cyf.gradle.interview.controller.time;

import cyf.gradle.interview.service.time_concurrent.deadlock.AvoidDeadlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2019-05-31 下午4:18
 **/
@RestController
@RequestMapping("/avoidDeadlock")
public class AvoidDeadlockController {

    @Autowired
    private AvoidDeadlockService avoidDeadlockService;

    @GetMapping
    @RequestMapping("/transfer")
    public String transfer(@RequestParam String from, @RequestParam String to, @RequestParam int money) throws InterruptedException {
        //avoidDeadlockService.transfer(from, to, money);
        avoidDeadlockService.transferByResourceOrder(from, to, money);
        return "success";
    }
}
