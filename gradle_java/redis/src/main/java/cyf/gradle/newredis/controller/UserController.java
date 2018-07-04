package cyf.gradle.newredis.controller;

import cyf.gradle.newredis.annotation.LogConfig;
import cyf.gradle.newredis.en.OperaType;
import cyf.gradle.newredis.module.User;
import cyf.gradle.newredis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2017-10-30 17:09
 **/
@RestController
@RequestMapping("/newredis/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/getUser/{id}/{username}/{pwd}")
    @LogConfig(opera = OperaType.online)
    public User getUser(@PathVariable Integer id,@PathVariable String username,@PathVariable String pwd) {
        return userService.getUserS(id,username,pwd);
    }
}
