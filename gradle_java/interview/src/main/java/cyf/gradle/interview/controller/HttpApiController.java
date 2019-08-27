package cyf.gradle.interview.controller;

import com.alibaba.fastjson.JSONObject;
import cyf.gradle.dao.model.Record;
import cyf.gradle.dao.model.User;
import cyf.gradle.interview.service.annotationdao.AnnDaoService;
import cyf.gradle.interview.service.http.api.HttpApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2019-08-21 17:59
 **/
@RestController
@RequestMapping("/httpapai")
public class HttpApiController {

    @Resource
    private HttpApiService httpApiService;

    @GetMapping("/getList")
    public JSONObject getList(@RequestParam Long id) throws IOException {
        return httpApiService.get(id);
    }
}
