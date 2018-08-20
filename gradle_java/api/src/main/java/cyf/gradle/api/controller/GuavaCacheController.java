package cyf.gradle.api.controller;

import cyf.gradle.api.service.GuavaCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @author Cheng Yufei
 * @create 2018-06-04 20:16
 **/
@RestController
@RequestMapping("/cache")
@Slf4j
public class GuavaCacheController {

    @Autowired
    private GuavaCacheService cacheService;

    @GetMapping("/get")
    public String get(@RequestParam String key) throws ExecutionException, InterruptedException {

        return this.cacheService.get(key);
    }

    @GetMapping("/future")
    public String future(@RequestParam String value)  {
//        return cacheService.futureCallback(value);
        return "";
    }
}
