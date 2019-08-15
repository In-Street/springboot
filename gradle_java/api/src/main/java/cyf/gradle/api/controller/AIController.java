package cyf.gradle.api.controller;

import com.google.common.collect.Lists;
import cyf.gradle.api.service.baiduai.HttpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Cheng Yufei
 * @create 2019-01-14 11:30
 **/
@RestController
@RequestMapping("/jodd")
@Slf4j
public class AIController {

    @Autowired
    private HttpService httpService;


    @GetMapping("/getAccessToken")
    public Object getAccessToken() {
       return  httpService.getAccessToken();
    }

    @GetMapping("/detect")
    public Map<String, Object> detect() {
        return httpService.detect();
    }

    @GetMapping("/faceAudit")
    public Map<String, Object> faceAudit(@RequestParam String url) {
        return httpService.faceAudit(url);
    }

    @GetMapping("/return")
    public List returnConfigTest()  {
        return Lists.newArrayList("A", "B");
    }
}
