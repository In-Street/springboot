package cyf.gradle.api.controller;

import cyf.gradle.api.configuration.ExternalConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * @author Cheng Yufei
 * @create 2019-05-13 18:25
 *
 * 外部配置刷新API
 **/
@RestController
@RequestMapping("/config")
public class RefreshConfigController {

    @Autowired
    private AbstractEnvironment environment;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private ExternalConfig externalConfig;


    @GetMapping("/refresh")
    public String configRefresh() {
        PropertySource<?> target = null;
        //String configLocation = environment.getProperty("spring.config.location");
 /* if(StringUtils.isNotEmpty(configLocation)) {
         for(PropertySource<?> ps : environment.getPropertySources()) {
             if(ps.getName().startsWith("applicationConfig") && StringUtils.startsWithIgnoreCase(configLocation, "http")) {
                 target  = ps;
                 break;
             }
         }
     }*/

     /*   if(target ==null) {
         return "--spring.config.location没有配置，或者不是一个url地址，你可能没有使用配置中心，没有配置可以刷新";
     }*/

        for (PropertySource<?> ps : environment.getPropertySources()) {
            //if (ps.getName().startsWith("URL [file:D:/external.properties]")) {
            if (ps.getName().contains("external.properties")) {
                target = ps;
                break;
            }
        }

        Properties pros = new Properties();
        try {
            PropertiesLoaderUtils.fillProperties(pros, new UrlResource("file", "D:/YUFEI/work/private/external.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return "reload failed , " + e.getMessage();
        }

        // 更新到spring容器
        Map map = (Map) target.getSource();
        map.clear();
        map.putAll(pros);

        //更新所有bean @value注解的字段。
        //context.getBeanDefinitionNames() 得到所有spring容器中注册的bean
      /*  for (String beanName : context.getBeanDefinitionNames()) {
            Object bean = context.getBean(beanName);
            Class<?> beanClass = bean.getClass();
            if (!beanClass.equals(ExternalConfig.class)) {
                break;
            }
            for (Field f : beanClass.getDeclaredFields()) {
                Value valueAnno = f.getAnnotation(Value.class);
                if (valueAnno == null) {
                    continue;
                }
                String key = valueAnno.value();
                if (key == null) {
                    continue;
                }
                key = key.replace("${", "").replace("}", "");
                key = key.split(":")[0];
                if (map.containsKey(key)) {
                    f.setAccessible(true);
                    try {
                        f.set(bean, map.get(key));
                    } catch (Exception e) {
                        return e.getMessage();
                    }
                }
            }
        }*/

      //找到ExternalConfig注册bean，更新属性
        Object bean = context.getBean(ExternalConfig.class);
        for (Field f : bean.getClass().getDeclaredFields()) {
            Value valueAnno = f.getAnnotation(Value.class);
            if (valueAnno == null) {
                continue;
            }
            String key = valueAnno.value();
            if (key == null) {
                continue;
            }
            key = StringUtils.removeAll(key, "[${}]");
            if (map.containsKey(key)) {
                f.setAccessible(true);
                try {
                    f.set(bean, map.get(key));
                } catch (Exception e) {
                    return e.getMessage();
                }
            }
        }
        return "all @Value field update success";
    }


    /**
     * 外部配置文件动态刷新
     * @throws IOException
     */
    @GetMapping("/re")
    public void refresh() throws IOException {

       /* MutablePropertySources propertySources = environment.getPropertySources();

        Iterator<PropertySource<?>> iterator = propertySources.iterator();
        while (iterator.hasNext()) {
            PropertySource<?> propertySource = iterator.next();
            if (propertySource.getName().contains("external.properties")) {
                Map source =   (Map) propertySource.getSource();
            }
        }*/

        Properties properties = new Properties();
        PropertiesLoaderUtils.fillProperties(properties, new EncodedResource(new FileUrlResource("D:/YUFEI/work/private/external.properties"),"utf-8"));

        ExternalConfig bean = context.getBean(ExternalConfig.class);

        Stream.of(bean.getClass().getDeclaredFields()).forEach(field -> {

            String value = field.getDeclaredAnnotation(Value.class).value();
            String key = StringUtils.removeAll(value, "[${}]");
            Object newValue = properties.get(key);
            try {
                field.setAccessible(true);
                field.set(bean, newValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();

            }
        });
        System.out.println();
    }


    @GetMapping("/get")
    public String getConfig() {
        return externalConfig.getCore() + ">>>>>>>>>>>>>>>>>>" + externalConfig.getEnableKey();
    }
}
