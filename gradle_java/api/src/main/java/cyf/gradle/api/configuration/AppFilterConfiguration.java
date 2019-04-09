package cyf.gradle.api.configuration;


import cyf.gradle.base.Constants;
import cyf.gradle.base.enums.RespStatusEnum;
import cyf.gradle.base.model.Header;
import cyf.gradle.base.model.LocalData;
import cyf.gradle.base.model.Response;
import cyf.gradle.dao.model.User;
import cyf.gradle.util.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;

/**
 * 过滤器配置类
 * @since 1.0
 */
@Configuration
@Slf4j
public class AppFilterConfiguration {

    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 通用设置
     *  跨域 编码 header参数
     * @return commentFilterRegistration
     */
  /*  @Bean
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
                        if (referer.contains("http://39.106.118.71")) {
                            allowOrigin = "http://39.106.118.71";
                        }else if (referer.contains("localhost")) {
                            allowOrigin = "http://localhost:8090";
                        }else if (referer.contains("127.0.0.1")) {
                            allowOrigin = "http://127.0.0.1:8090";
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
    }*/

    /**
     * 刚开始启动项目不运行 doFilterInternal() 方法，直接到 registration.addUrlPatterns ，访问controller 时 执行
     *
     * 仅执行 doFilterInternal()
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean headerFilterRegistration(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter filter = new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                //反射映射header
                Header header = new Header();
                Field[] fields = header.getClass().getDeclaredFields();
                for (Field field : fields) {
                    String name = field.getName();
                    field.setAccessible(true);
                    try {
                        field.set(header, request.getHeader(name));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                String sessionId = header.getSessionId();
                if(StringUtils.isNotBlank(sessionId)){
                    String userJson = redisTemplate.opsForValue().get(Constants.USER_LOGIN_KEY + sessionId);
                    if(StringUtils.isNotBlank(userJson)){
                        LocalData.USER_JSON.set(userJson);
                        log.info("用户信息已设置,{}", userJson);
                        User dsmUser = FastJsonUtils.toBean(userJson, User.class);
                        header.setUid(dsmUser.getId());
                    }
                }

                LocalData.HEADER.set(header);

                filterChain.doFilter(request,response);

                LocalData.USER_JSON.remove();
                LocalData.HEADER.remove();
                System.out.println();
            }
        };
//        registration.addUrlPatterns("/v1/*","/app/*");
        registration.addUrlPatterns("/user/*");
        registration.setFilter(filter);
        registration.setName("headerFilterRegistration");
        registration.setOrder(1);
        return registration;
    }


    /**
     * 必须登陆
     * @return FilterRegistrationBean
     */
  @Bean
    public FilterRegistrationBean apploginFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter filter = new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

                String sessionId = LocalData.HEADER.get().getSessionId();
                log.info("登陆过滤器 sessionId={}",sessionId);

                if(sessionId == null || redisTemplate.opsForValue().get(Constants.USER_LOGIN_KEY + sessionId) == null){
                    log.info("用户未登录");
                    String s = FastJsonUtils.toJSONString(new Response<>(RespStatusEnum.USER_NOT_LOGIN));
                    PrintWriter writer = response.getWriter();
                    writer.print(s);
                    writer.close();
                    return;
                }

                filterChain.doFilter(request,response);
            }
        };

        registration.setFilter(filter);


        String[] urlPattern = new String[]{
                "/v1/user/publish/*",
                "/v1/comment/add/*",
                "/v1/comment/del/*",
                "/v1/user/uploadAvatar",
                "/v1/user/uploadVideo",
//                "/user/select/*",
        };
        registration.setEnabled(true);
        registration.addUrlPatterns(urlPattern);
        registration.setOrder(110);
        registration.setName("apploginFilterRegistration");
        return registration;
    }

    /**
     *  Filter 模式实现数据加密
     *  不支持 * 符号，所以请求参数不能是 @PathVariable形式
     * @return
     */
   /* @Bean
    public FilterRegistrationBean encryptFilter() {

        EncryptionConfig config = new EncryptionConfig();
        config.setKey("abcdef0123456789");

        config.setResponseEncryptUriList(Lists.newArrayList(
//                "/user/select",
                "/user/byId","/user/byName"
        ));
        //config.setRequestDecyptUriList();
        config.setUrlPatterns(new String[]{"/*"});
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new EncryptionFilter(config));
        registrationBean.setEnabled(true);
        registrationBean.addUrlPatterns(new String[]{"/*"});
        registrationBean.setOrder(1);
        return registrationBean;
    }*/





}
