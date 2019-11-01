package cyf.gradle.interview.service.strategy;

import cn.hutool.core.lang.ClassScanner;
import com.google.common.collect.Maps;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Objects;

/**
 * 三类订单处理类的自定义注解（Type）的处理，找到三类订单处理类及其对应类型，放入HandleContext，并将HandleContext注册进spring容器；
 * 此种方式不方便，包下有三个类型的处理类，扫描到每个类时候都会进行HandleContext的注册，需做特殊处理，使用InitOrder2
 *
 * @author Cheng Yufei
 * @create 2019-04-28 16:08
 **/
//@Component
public class InitOrder implements BeanPostProcessor {

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;
    private HashMap<OrderType, Class> map = Maps.newHashMapWithExpectedSize(3);
    private OrderType[] orderTypes = OrderType.values();


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        //目标包下有三个类，扫描到每个类的时候都会进行注册操作，报错：注册的bean重复,所以只有第一次扫描到时候已经把所有类型全部注册进去了
        if (map.size() == orderTypes.length) {
            return bean;
        }
        ClassScanner.scanPackageByAnnotation("cyf.gradle.interview.service.strategy.impl", Type.class)
                .stream().forEach(cla -> {
            Type annotation = cla.getAnnotation(Type.class);
            map.put(annotation.value(), cla);
        });
        if (!map.isEmpty()) {
            HandleContext handleContext = new HandleContext(map);
            beanFactory.registerSingleton("orderHandlerContext", handleContext);
        }
        return bean;

      /*
        //适用只有一个类有注解时,否则注册HandleContext时报错
       HashMap<OrderType, Class> map = Maps.newHashMapWithExpectedSize(3);
        Class<?> beanClass = bean.getClass();
        String name = beanClass.getPackage().getName();
        if ("cyf.gradle.interview.service.strategy.impl".equals(name)) {
            Type annotation = beanClass.getAnnotation(Type.class);
            if (Objects.nonNull(annotation)) {
                map.put(annotation.value(), beanClass);
            }
        }
        if (!map.isEmpty()) {
            HandleContext handleContext = new HandleContext(map);
            beanFactory.registerSingleton("orderHandlerContext", handleContext);
        }
        return bean;*/

    }
}
