package cyf.gradle.interview.configuration;

import cyf.gradle.base.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 针对String类型在 AbstractMessageConverterMethodProcessor.writeWithMessageConverters() 处理返回值的时候会根据String类型选择StringMessageConverter，但已经转为Response，接受参数，Response强转String类型有
 * ClassCastException:
 * 两种解决方法：
 *  1. 如下 beforeBodyWrite 方法中处理。
 *  2. MessageConvertConfig 中自定义 HttpMessageConverters
 *
 *
 * @author Cheng Yufei
 * @create 2019-08-22 10:13
 **/
@RestControllerAdvice
@Slf4j
public class ReturnConfig implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof Response) {
            return body;
        }
        //String类型单独处理
       /* if (body instanceof String) {
            return JSON.toJSONString(new Response<>(body));
        }*/
        return new Response<>(body);
    }
}
