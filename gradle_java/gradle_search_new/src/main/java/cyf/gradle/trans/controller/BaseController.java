package cyf.gradle.trans.controller;

import cyf.gradle.base.model.Response;
import cyf.gradle.trans.service.SearchTransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Cheng Yufei
 * @create 2017-12-12 15:03
 **/
@RestController
@RequestMapping("/basesearch")
public class BaseController {

    @Autowired
    private SearchTransService transService;

    @RequestMapping(value = "/add")
    public Response add() throws IOException {
//        String node = transService.getNode();
        String node = "";
        return new Response(node);

    }

}
