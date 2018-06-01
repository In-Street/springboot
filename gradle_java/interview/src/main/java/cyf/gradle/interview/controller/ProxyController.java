package cyf.gradle.interview.controller;

import cyf.gradle.interview.impl.HelloImpl;
import cyf.gradle.interview.proxy.JdkProxy;
import cyf.gradle.interview.service.Hello;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Proxy;

/**
 * @author Cheng Yufei
 * @create 2018-06-01 下午9:23
 **/
@RestController
@RequestMapping("/proxy")
public class ProxyController {

    @GetMapping
    @RequestMapping("/jdk")
    public String proxy(@RequestParam String msg) {


        Hello hello = new HelloImpl();

        Hello he = (Hello) Proxy.newProxyInstance(hello.getClass().getClassLoader(), new Class[]{Hello.class}, new JdkProxy(hello));

        return he.say(msg);

    }

}
