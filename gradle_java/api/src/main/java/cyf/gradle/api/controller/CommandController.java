package cyf.gradle.api.controller;

import cyf.gradle.api.service.CommandOrder;
import cyf.gradle.api.service.CommandUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

/**
 * @author Cheng Yufei
 * @create 2018-06-04 20:16
 **/
@RestController
@RequestMapping("/command")
@Slf4j
public class CommandController {


    @GetMapping("/get")
    public String get()  {
        CommandOrder order = new CommandOrder("Order");
        CommandOrder order_2 = new CommandOrder("Order_2");
        CommandUser user = new CommandUser("User");
        CommandUser user_2 = new CommandUser("Taylor");
        CommandUser user_3 = new CommandUser("Swift");

        /**
         * execute() 实现run方法同步
         * queue() 实现run 方法的异步执行
         */
        String orderExecute = order.execute();
        log.debug("order-execute {}", orderExecute);

        String userExecute = user.execute();
        log.debug("user-execute {}", userExecute);

        String user_2Execute = user_2.execute();
        log.debug("user_2-execute {}", user_2Execute);

        Future<String> queue = user_3.queue();

        String order_2Execute = order_2.execute();
        log.debug("order_2-execute {}", order_2Execute);
        return "";
    }
}
