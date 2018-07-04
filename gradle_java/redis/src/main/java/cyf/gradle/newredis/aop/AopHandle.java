package cyf.gradle.newredis.aop;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author Cheng Yufei
 * @create 2017-10-31 15:02
 **/
@Aspect
@Component
@Slf4j
public class AopHandle {


    /**
     * 对所有的 @RequestMapping 进行拦截处理
     *
     * @param requestMapping
     */
   /* @Around("@annotation(requestMapping)")
    public void log(RequestMapping requestMapping) {

*Controller.*(..)
    }*/
    @Pointcut("execution(* cyf.gradle.newredis.service.*Service.*(..))")
    public void service() {
    }

    //自定义 pointcut 路径
    @Around("service()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String requestURI = request.getRequestURI();

        log.info(">>>>>> 开始请求: {},{}() with argument[s] = {}", requestURI, joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));

        Object result = joinPoint.proceed();

        String json = "";
        if (result != null) {
            json = JSONObject.toJSONString(result);
        }
        long usedTime = System.currentTimeMillis() - start;
        log.info("<<<<<< 结束请求: {},{}(),耗时:{}ms with result = {}", requestURI, joinPoint.getSignature().getName(), usedTime, json);


        String attribute = (String) request.getAttribute("OperaType-Name");
        log.info("-------自定义注解值获取:{}-------", attribute);
        return result;
    }

}
