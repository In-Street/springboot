package cyf.gradle.interview.controller;

import cyf.gradle.interview.service.customstarter.CustomStarterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Cheng Yufei
 * @create 2019-08-21 17:59
 **/
@RestController
@RequestMapping("/starter")
public class CustomStarterController {

    @Resource
    private CustomStarterService customStarterService;

    @GetMapping("/sender")
    public String sender()  {
        return customStarterService.send();
    }
}
