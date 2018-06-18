package cyf.gradle.interview.proxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 通过类的继承实现代理： 生成Proxy对象的父类就是被代理的类
 *
 *类的 hashCode()、equals()、toString()会代理，
 * getClass()、wait()等方法不会，因为它是final方法
 *
 * @author Cheng Yufei
 * @create 2018-06-18 上午11:21
 **/
@Slf4j
public class CGLibProxy implements MethodInterceptor {

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

        /**
         * 可加入日志功能，安全检查等
         */
        log.info("CGLib 代理：{}，{}",method.getName(), Arrays.toString(objects));

        //转发给原始对象
        return methodProxy.invokeSuper(o, objects);
    }
}
