package cyf.gradle.interview.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2018-06-01 下午9:23
 **/
@RestController
@RequestMapping("/concurrent")
public class ConcurrentController {

    @GetMapping
    @RequestMapping("/jdk")
    public String jdk(@RequestParam String msg) {
        return msg;
    }

}
