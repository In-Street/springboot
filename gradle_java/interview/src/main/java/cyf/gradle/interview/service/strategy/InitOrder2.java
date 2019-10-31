package cyf.gradle.interview.service.strategy;

import cn.hutool.core.lang.ClassScanner;
import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * InitOrder1的优化版，只会处理一次
 *
 * @author Cheng Yufei
 * @create 2019-04-28 18:15
 **/
@Component
public class InitOrder2 implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        HashMap<OrderType, Class> map = Maps.newHashMapWithExpectedSize(3);
        ClassScanner.scanPackageByAnnotation("cyf.gradle.interview.service.strategy.impl", Type.class)
                .stream().forEach(cla -> {
            Type annotation = cla.getAnnotation(Type.class);
            map.put(annotation.value(), cla);
        });
        if (!map.isEmpty()) {
            HandleContext handleContext = new HandleContext(map);
            beanFactory.registerSingleton("orderHandlerContext", handleContext);
        }
    }
}
