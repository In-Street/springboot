package cyf.gradle.api.configuration;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
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
 *
 * EventBus 注册器，将含有@Subscribe 的Bean注册
 * @author Cheng Yufei
 * @create 2018-06-17 上午10:37
 **/
@Component
public class EventBusRegister implements BeanPostProcessor {

    @Autowired
    private EventBus stringEventBus;
    @Autowired
    private EventBus intAsyncEventBus;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {


        Method[] methods = bean.getClass().getMethods();

        Consumer<Annotation> consumerAnno = a->{
            if (Objects.equals(a.annotationType(),Subscribe.class)) {
                stringEventBus.register(bean);
                intAsyncEventBus.register(bean);
            }
        };

        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            Stream.of(annotations).forEach(consumerAnno);
        }

        return bean;
    }

}
