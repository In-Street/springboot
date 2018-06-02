package cyf.gradle.interview.proxy;


import cyf.gradle.interview.service.Hello;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author Cheng Yufei
 * @create 2018-06-01 下午9:04
 **/
@AllArgsConstructor
@NoArgsConstructor
public class JdkProxy implements InvocationHandler {

    private Hello hello;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Objects.equals(method.getName(),"say")) {
            System.out.println("代理：say");
        }
        return method.invoke(hello, args);
    }
}
