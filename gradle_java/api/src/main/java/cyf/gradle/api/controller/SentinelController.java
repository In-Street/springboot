package cyf.gradle.api.controller;

import cyf.gradle.api.service.SentinelService;
import cyf.gradle.base.model.Header;
import cyf.gradle.base.model.LocalData;
import cyf.gradle.base.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2019-04-09 16:18
 **/
@RestController
@RequestMapping("/sentinel")
public class SentinelController {


    @Autowired
    private SentinelService sentinelService;

    @PostMapping(value = "/byName")
    public Response select2(@RequestParam String name) {

        return new Response(sentinelService.selectByName(name));
    }

    @PostMapping(value = "/byName2")
    public Response select(@RequestParam String name) throws InterruptedException {

        return new Response(sentinelService.selectByName2(name));
    }
}
