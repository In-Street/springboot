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
        InputStream inputStream = request.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String s;
        while((s = bufferedReader.readLine()) != null) {
            System.out.println(">>>>>,controller中读取requestInputStream"+s);
        }

        inputService.originalRequest(request);
        return "success";

    }
}
