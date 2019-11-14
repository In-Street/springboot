package cyf.gradle.interview.controller;

import cyf.gradle.interview.service.request_inputinstream.InputService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Cheng Yufei
 * @create 2019-09-08 12:46 下午
 **/
@RestController
@RequestMapping("/input")
public class RequestInputStreamController {

    @Resource
    private InputService inputService;

    @GetMapping("/original")
    public String setInput(HttpServletRequest request) throws IOException {

        Map<String, String[]> parameterMap = request.getParameterMap();
        System.out.println("------controller: parameter start");
        parameterMap.forEach((k, v) -> {
            System.out.println(k);
            Stream.of(v).forEach(v2 -> System.out.println(v2));
        });
        System.out.println("------controller: parameter end");



        InputStream inputStream = request.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String s;
        System.out.println("------controller: InputStream start");
        while((s = bufferedReader.readLine()) != null) {
            System.out.println(s);
        }
        System.out.println("------controller: InputStream end");
        inputService.originalRequest(request);
        return "success";

    }
}
