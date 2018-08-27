package cyf.gradle.api.configuration;

import cyf.gradle.base.model.Response;
import cyf.gradle.util.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ControllerAdvice  控制层的切面 捕获异常
 *
 */
@ControllerAdvice
@Slf4j
public class GlobalDefaultExceptionHandler extends ResponseEntityExceptionHandler {

   /* @ExceptionHandler(value = Exception.class)
    public void defaultErrorHandler(HttpServletRequest req, HttpServletResponse response , Exception e)  {
        log.error("异常信息", e);
        HttpStatus status = getStatus(req);
        String s = FastJsonUtils.toJSONString(new Response(status));
        log.info("数据返回，{}",s);
        try {
            response.getWriter().write(s);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }*/

   @ExceptionHandler(value = Exception.class)
   public ResponseEntity handleConflict(RuntimeException ex, WebRequest request) {
       String body = "test exception";
       return handleExceptionInternal(ex,body,new HttpHeaders(),HttpStatus.CONFLICT, request);
   }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        log.info("异常捕获，{}", statusCode);
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

}