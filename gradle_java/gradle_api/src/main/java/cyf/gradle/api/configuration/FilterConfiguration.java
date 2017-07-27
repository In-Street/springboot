package cyf.gradle.api.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 过滤器配置类
 * @author zhuruisong on 2017/3/31
 * @since 1.0
 */
@Configuration
@Slf4j
public class FilterConfiguration {

    /**
     * 通用设置
     *  跨域 编码 header参数
     * @return commentFilterRegistration
     */
    @Bean
    public FilterRegistrationBean commentFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

                request.setCharacterEncoding("utf-8");
                response.setCharacterEncoding("utf-8");
                response.setContentType("application/json");

                if (log.isDebugEnabled()) {
                    Enumeration<String> headerNames = request.getHeaderNames();
                    while (headerNames.hasMoreElements()) {
                        String s = headerNames.nextElement();
                        log.debug("hear参，{}={}", s, request.getHeader(s));
                    }
                    String method = request.getMethod();
                    log.debug("method方式，{}", method);

                    Enumeration<String> parameterNames = request.getParameterNames();
                    while (parameterNames.hasMoreElements()) {
                        String s = parameterNames.nextElement();
                        log.debug("入参，{}={}", s, request.getParameter(s));
                    }
                }

                if (response.getHeader("Access-Control-Allow-Methods") == null) {
                    String allowOrigin = "http://m.daishumovie.com";
                    String referer = request.getHeader("referer");
                    if (referer != null) {
                    	if (referer.contains("http://m.lanhaimian.com")) {
                            allowOrigin = "http://m.lanhaimian.com";
                    	} else if(referer.contains("http://m.pre.lanhaimian.com")){
                    		allowOrigin = "http://m.pre.lanhaimian.com";
                    	} else if(referer.contains("https://m.lanhaimian.com")){
                    		allowOrigin = "https://m.lanhaimian.com";
                    	} else if(referer.contains("http://mpre.lanhaimian.com")){
                    		allowOrigin = "http://mpre.lanhaimian.com";
                    	} else if (referer.contains("http://m.daishumovie.com")) {
                            allowOrigin = "http://m.daishumovie.com";
                    	} else if (referer.contains("http://m.daishumovie.cn")) {
                            allowOrigin = "http://m.daishumovie.cn";
                    	} else if (referer.contains("http://m.movie.com")) {
                            allowOrigin = "http://m.movie.com";
                    	} else if (referer.contains("http://daishumovie.f3322.net")) {
                            allowOrigin = "http://daishumovie.f3322.net";
                    	} else if (referer.contains("localhost")) {
                            allowOrigin = "http://localhost:8080";
                        } else if (referer.contains("192.168")) {
                            allowOrigin = "http://192.168.1.200";
                        } else if (referer.contains("http://101.200.127.174:8083")) {
                            allowOrigin = "http://101.200.127.174:8083";
                        } else if (referer.contains("http://m1.movie.com:8080")) {
                            allowOrigin = "http://m1.movie.com:8080";
                        } else if (referer.contains("http://m2.movie.com:8080")) {
                            allowOrigin = "http://m2.movie.com:8080";
                        } else if (referer.contains("http://47.93.113.202:8080")) {
                            allowOrigin = "http://47.93.113.202:8080";
                        }
                    }
                    response.addHeader("Access-Control-Allow-Origin", allowOrigin);
                    response.addHeader("Access-Control-Allow-Credentials", "true");
                    response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                    response.addHeader("Access-Control-Allow-Headers",
                            "Origin, No-Cache, X-Requested-With, If-Modified-Since, sessionid, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With, wechatCode, openId, LoadwechatCode");
                    response.addHeader("Access-Control-Max-Age", "1000");
                }

                filterChain.doFilter(request,response);

            }
        });

        registration.addUrlPatterns("/*");

        registration.setOrder(1);
        registration.setName("commentFilterRegistration");
        return registration;
    }




}
