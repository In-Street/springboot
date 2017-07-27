package cyf.gradle.api.controller;

import cyf.gradle.api.service.UserService;
import cyf.gradle.base.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by cheng on 2017/7/10.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/save",method = {RequestMethod.POST,RequestMethod.GET})
    public Response save(){
        userService.save();
        return new Response();
    }
}
