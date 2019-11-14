package cyf.gradle.interview.service.request_inputinstream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Cheng Yufei
 * @create 2019-09-08 12:45 下午
 **/
@Service
@Slf4j
public class InputService {

    public void originalRequest(HttpServletRequest request) throws IOException {

        //url参数可以多次读取
        Map<String, String[]> parameterMap = request.getParameterMap();
        System.out.println("------service: parameter start");
        parameterMap.forEach((k, v) -> {
            System.out.println(k);
            Stream.of(v).forEach(v2 -> System.out.println(v2));
        });
        System.out.println("------service: parameter end");

        //不重写requestInputStream时，使用一次request.getInputStream()后 【filter 或者controller 】，无法再次使用，读取不到body 里的参数
        InputStream inputStream = request.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String s;
        System.out.println("------service: InputStream start");
        while((s = bufferedReader.readLine()) != null) {
            System.out.println(s);
        }
        System.out.println("------service: InputStream start");
    }
}
