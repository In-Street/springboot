package cyf.gradle.interview.controller;

import cyf.gradle.interview.impl.proxy.Apple;
import cyf.gradle.interview.impl.proxy.Impl;
import cyf.gradle.interview.proxy.CGLibProxy;
import cyf.gradle.interview.proxy.JdkProxy;
import cyf.gradle.interview.service.proxy.HI;
import cyf.gradle.interview.service.proxy.Hello;
import cyf.gradle.interview.service.strategy.OrderService;
import cyf.gradle.interview.service.strategy.OrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Proxy;

/**
 * syn_4
 *
 * @author Cheng Yufei
 * @create 2018-06-01 下午9:23
 **/
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    @RequestMapping("/handle/{type}")
    public String handle(@PathVariable Integer type) {

        OrderType orderType = null;
        switch (type) {
            case 1:
                orderType = OrderType.COMMON;
                break;
            case 2:
                orderType = OrderType.SALES_PROMOTION;
                break;
            case 3:
                orderType = OrderType.GROUP_BUY;
                break;
            default:
                break;
        }

        return orderService.orderHandler(orderType);
    }
}
