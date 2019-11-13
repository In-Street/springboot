package cyf.gradle.api.configuration;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cyf.gradle.api.service.EventListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * EventBus 注册器，将含有@Subscribe 的Bean注册
 *
 * @author Cheng Yufei
 * @create 2018-06-17 上午10:37
 **/
@Component
public class EventBusRegister implements BeanPostProcessor {

    @Autowired
    private EventBus eventBus;
    @Autowired
    private EventBus asyncEventBus;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        boolean isEventListener = bean instanceof EventListener;
        if (isEventListener) {
            Consumer<Annotation> consumerAnno = a -> {
                if (Objects.equals(a.annotationType(), Subscribe.class)) {
                    eventBus.register(bean);
                    //asyncEventBus.register(bean);
                }
            };
            Method[] methods = bean.getClass().getMethods();
            //获取方法的注解,若为 Subscribe ，注入事件总线
            Stream.of(methods).filter(method -> method.getAnnotations().length > 0)
                    .flatMap(method -> Stream.of(method.getAnnotations())).forEach(consumerAnno);
        }
        return bean;
    }

}
