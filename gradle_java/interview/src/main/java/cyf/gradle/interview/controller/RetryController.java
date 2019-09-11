package cyf.gradle.interview.controller;

import com.alibaba.fastjson.JSONObject;
import cyf.gradle.interview.service.retry.RetryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Cheng Yufei
 * @create 2019-08-21 17:59
 **/
@RestController
@RequestMapping("/retry")
public class RetryController {

    @Resource
    private RetryService retryService;

    @GetMapping("/retry")
    public JSONObject retry() throws Exception {
        return retryService.guavaRetry();
    }
}
