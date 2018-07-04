package cyf.gradle.newredis.intercept;

import cyf.gradle.newredis.annotation.LogConfig;
import cyf.gradle.newredis.en.OperaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Cheng Yufei
 * @create 2017-10-31 17:29
 **/
public class Interception implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        LogConfig annotation = handlerMethod.getMethodAnnotation(LogConfig.class);
        if (null != annotation) {
            OperaType opera = annotation.opera();
            request.setAttribute("OperaType-Name",opera.getName());
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
