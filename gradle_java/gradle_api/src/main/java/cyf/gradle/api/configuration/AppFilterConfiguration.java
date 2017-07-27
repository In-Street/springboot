package cyf.gradle.api.configuration;


import cyf.gradle.base.Constants;
import cyf.gradle.base.enums.RespStatusEnum;
import cyf.gradle.base.model.LocalData;
import cyf.gradle.base.model.Response;
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
import cyf.gradle.base.model.Header;

/**
 * 过滤器配置类
 * @since 1.0
 */
//@Configuration
@Slf4j
public class AppFilterConfiguration {

    //@Autowired
    private StringRedisTemplate redisTemplate;

   // @Bean
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
                    field.setAccessible(true);//设置允许访问
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
                       /* DsmUser dsmUser = FastJsonUtils.toBean(userJson, DsmUser.class);
                        header.setUid(dsmUser.getUid());*/
                    }
                }

                LocalData.HEADER.set(header);

                filterChain.doFilter(request,response);

                LocalData.USER_JSON.remove();
                LocalData.HEADER.remove();
            }
        };
        registration.addUrlPatterns("/v1/*","/app/*");
        registration.setFilter(filter);
        registration.setName("headerFilterRegistration");
        registration.setOrder(1);
        return registration;
    }


    /**
     * 必须登陆
     * @return FilterRegistrationBean
     */
  //  @Bean
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

        //点赞 评论 上传用户头像
        String[] urlPattern = new String[]{
                "/v1/user/publish/*",
                "/v1/comment/add/*",
                "/v1/comment/del/*",
                "/v1/user/uploadAvatar",
                "/v1/user/uploadVideo",
                "/v1/user/modify",
        };
        registration.setEnabled(true);
        registration.addUrlPatterns(urlPattern);
        registration.setOrder(110);
        registration.setName("apploginFilterRegistration");
        return registration;
    }





}
