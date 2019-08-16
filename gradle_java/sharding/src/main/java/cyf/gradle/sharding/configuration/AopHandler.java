package cyf.gradle.sharding.configuration;

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

/**
 * AOP
 */
@Aspect
@Component
@Slf4j
public class AopHandler {


    /**
     * syn_3
     * 打印Controller层日志
     *
     * @param pjp            切点
     * @return Object
     * @throws Throwable Throwable
     */
    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)||@annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "||@annotation(org.springframework.web.bind.annotation.PostMapping) ")
    public Object printMethodsExecutionTime(ProceedingJoinPoint pjp/*, RequestMapping requestMapping*/) throws Throwable {
        long start = System.currentTimeMillis();

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
        long usedTime = System.currentTimeMillis() - start;
        log.info("<<< 结束请求: {},{}(),耗时:{}ms with result = {}", requestURI, pjp.getSignature().getName(), usedTime, json);
        MDC.remove(key);
        String pcallback = request.getParameter("pcallback");
        if (StringUtils.isNoneBlank(pcallback)) {
            response.addHeader("Content-Type", "application/json;charset=UTF-8");
            try {
                response.getWriter().write(pcallback + "(");
                response.getWriter().write(json);
                response.getWriter().write(")");
                response.getWriter().close();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     *为了在 service 中自调用方法可通过AopContext获取当前Service的代理，否则获取的是Controller代理对象，有类型转换错误
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("service()")
     public Object serviceProxy(ProceedingJoinPoint pjp) throws Throwable {
        return pjp.proceed();
     }


    @Pointcut("execution(* cyf.gradle.sharding.service.*Service.*(..)) ")
    public void service() {}

}
