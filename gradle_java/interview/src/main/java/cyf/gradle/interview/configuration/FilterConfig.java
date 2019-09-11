package cyf.gradle.interview.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Cheng Yufei
 * @create 2019-09-08 1:52 下午
 **/
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean requestInputStream() {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        OncePerRequestFilter oncePerRequestFilter = new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

                //filter读取一次请求参数【地址参数在parameter中获取；body参数从流中获取】，用于参数校验等工作，但流读取一次后，在后续的controller或service中流无法再次读取，所以重新设置
            /*    Map<String, String[]> parameterMap = request.getParameterMap();
                parameterMap.forEach((k, v) -> System.out.println(k + "---filter---" + v));

                InputStream inputStream = request.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s;
                while ((s = bufferedReader.readLine()) != null) {
                    System.out.println(">>>>>filter中读取requestInputStream: " + s);
                }
                 filterChain.doFilter(request, response);*/

                //采用自定义
                MyRequestInputStream myRequestInputStream = new MyRequestInputStream(request);
                filterChain.doFilter(myRequestInputStream,response);
            }
        };

        registrationBean.setFilter(oncePerRequestFilter);
        registrationBean.addUrlPatterns(new String[]{"/input/*"});
        return registrationBean;
    }
}
