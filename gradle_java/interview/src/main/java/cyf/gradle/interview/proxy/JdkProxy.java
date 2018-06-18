package cyf.gradle.interview.proxy;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * JDK代理必须有接口
 * @author Cheng Yufei
 * @create 2018-06-01 下午9:04
 **/
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class JdkProxy implements InvocationHandler {

    /**
     * 需代理的类/接口
     */
    private Object object;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        log.info("JDK代理：{} - {}", method.getName(), Arrays.toString(args));

        return method.invoke(object, args);
    }
}
