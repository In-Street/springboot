package cyf.gradle.interview.configuration;

import com.google.common.base.Stopwatch;
import cyf.gradle.util.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * AOP
 */
@Aspect
@Component
@Slf4j
public class AopHandler {


    /**
     * 打印Controller层日志
     *
     * @param pjp            切点
     * @param requestMapping 注解类型
     * @return Object
     * @throws Throwable Throwable
     */
    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)||@annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "||@annotation(org.springframework.web.bind.annotation.PostMapping) ")
    public Object printMethodsExecutionTime(ProceedingJoinPoint pjp/*, RequestMapping requestMapping*/) throws Throwable {

        Stopwatch watch = Stopwatch.createStarted();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String requestURI = request.getRequestURI();

        String key = "requestId";
        MDC.put(key, UUID.randomUUID().toString());
        log.info(">>> 开始请求: {},{}() with argument[s] = {}", requestURI, pjp.getSignature().getName(), Arrays.toString(pjp.getArgs()));

        Object result = pjp.proceed();

        String json = "";
        if (result != null) {
            json = FastJsonUtils.toJSONString(result);
        }
        log.info("<<< 结束请求: {},{}(),耗时:{}ms with result = {}", requestURI, pjp.getSignature().getName(), watch.elapsed(TimeUnit.MILLISECONDS), json);
        MDC.remove(key);

        return result;
    }

    /**
     *为了在 service 中自调用方法可通过AopContext获取当前Service的代理，否则获取的是Controller代理对象，有类型转换错误
     * @param pjp
     * @return
     * @throws Throwable
     */
    /*@Around("service()")
     public Object serviceProxy(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
     }


    @Pointcut("execution(* cyf.gradle.api.service.*Service.*(..)) ")
    public void service() {}*/

}
