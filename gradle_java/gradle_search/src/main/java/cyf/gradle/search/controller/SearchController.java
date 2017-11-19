package cyf.gradle.search.controller;

import cyf.gradle.base.model.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2017-11-19 下午7:08
 **/
@RestController
@RequestMapping("/search")
public class SearchController {

     public Response search() {


         return new Response();
     }
}
