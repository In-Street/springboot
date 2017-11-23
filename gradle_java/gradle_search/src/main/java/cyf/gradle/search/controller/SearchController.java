package cyf.gradle.search.controller;

import cyf.gradle.base.model.Response;
import cyf.gradle.search.enums.IndexType;
import cyf.gradle.search.pojo.KerrVo;
import cyf.gradle.search.pojo.Tag;
import cyf.gradle.search.service.BaseSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;

/**
 * @author Cheng Yufei
 * @create 2017-11-19 下午7:08
 **/
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private BaseSearch baseSearch;

    @RequestMapping(value = "/add")
     public Response add() throws IOException {

        KerrVo kerrVo = new KerrVo();
        kerrVo.setId(4);
        kerrVo.setTitle("《路易·德拉克斯的第九条命》：这个地球上最黑暗的地方，就是");
        kerrVo.setPublishTime(new Date());
        Tag tag = new Tag();
        tag.setId(4);
        tag.setName("悬疑");
        tag.setWeight(1);
        kerrVo.setTags(tag);
        baseSearch.add(kerrVo, kerrVo.getId().toString(), IndexType.kerr.getIndex(), IndexType.kerr.getType());
         return new Response();

     }
}
