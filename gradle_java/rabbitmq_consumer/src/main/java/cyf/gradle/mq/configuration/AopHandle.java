package cyf.gradle.mq.configuration;

import cyf.gradle.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.UUID;


/**
 * @author Cheng Yufei
 * @create 2018-01-02 17:00
 **/
@Aspect
@Component
@Slf4j
public class AopHandle {

    @Pointcut("execution(* cyf.gradle.mq.*.*.*(..))")
    public void point() {

    }

    @Before(value = "point()")
    public void handle() {
        String uuid = UUID.randomUUID().toString();
        LogUtil.uuid.set("[" + uuid + "]");
    }

}
