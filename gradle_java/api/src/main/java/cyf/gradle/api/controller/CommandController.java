package cyf.gradle.api.controller;

import cyf.gradle.api.service.CommandOrder;
import cyf.gradle.api.service.CommandUser;
import cyf.gradle.api.service.GuavaCacheService;
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
@RequestMapping("/command")
@Slf4j
public class CommandController {


    @GetMapping("/get")
    public String get() {
        CommandOrder order = new CommandOrder("Order");
        CommandUser user = new CommandUser("User");
        CommandUser user_2 = new CommandUser("Taylor");


        String orderExecute = order.execute();
        log.debug("order-execute {}", orderExecute);

        String userExecute = user.execute();
        log.debug("user-execute {}", userExecute);

        String user_2Execute = user_2.execute();
        log.debug("user-execute {}", user_2Execute);

        return "";
    }
}
