package cyf.gradle.interview.controller;

import cyf.gradle.interview.impl.proxy.Apple;
import cyf.gradle.interview.impl.proxy.Impl;
import cyf.gradle.interview.proxy.CGLibProxy;
import cyf.gradle.interview.proxy.JdkProxy;
import cyf.gradle.interview.service.proxy.HI;
import cyf.gradle.interview.service.proxy.Hello;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Proxy;

/**
 *
 *syn_4
 * @author Cheng Yufei
 * @create 2018-06-01 下午9:23
 **/
@RestController
@RequestMapping("/proxy")
public class ProxyController {

    @GetMapping
    @RequestMapping("/jdk")
    public String jdk(@RequestParam String msg) {


        Impl impl = new Impl();
        Class<?>[] interfaces = impl.getClass().getInterfaces();

        /**
         * 获取代理类的所有接口
         * 生成代理类类名
         * 根据接口信息，动态创建Proxy字节码
         * 将字节码转换对应的class对象
         * 创建InvocationHandler对象
         */
        Object proxy = Proxy.newProxyInstance(impl.getClass().getClassLoader(), interfaces, new JdkProxy(impl));

        Hello hello = (Hello) proxy;
        HI hi = (HI) proxy;
        /**
         * 方法调用时，直接调用 invoke 方法，根据代理类传递的method参数区分调用哪个
         */
        hello.sayHello(msg);
        hi.sayHi(msg);
        return msg;

    }

    @GetMapping
    @RequestMapping("/cglib")
    public String cglib(@RequestParam String msg) {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(new Apple().getClass());
        enhancer.setCallback(new CGLibProxy());

        //获取代理对象，所有非final方法调用转发到 intercept()
        Apple o = (Apple)enhancer.create();
        o.color(msg);
        o.getInt(12);
        return null;
    }
}
