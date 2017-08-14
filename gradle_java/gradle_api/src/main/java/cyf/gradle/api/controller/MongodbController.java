package cyf.gradle.api.controller;

import cyf.gradle.api.service.MongoService;
import cyf.gradle.base.model.Response;
import cyf.gradle.dao.mongodb.PrimaryMongoObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * mongo 测试
 *
 * @author Cheng Yufei
 * @create 2017-08-14 17:54
 **/
@RestController
@RequestMapping("/mongo")
public class MongodbController {

    @Autowired
    MongoService mongoService;


    @RequestMapping(value = "/mongodbSave", method = {RequestMethod.POST, RequestMethod.GET})
    public Response mongodbSave(@RequestParam String name) {
        System.out.println();
        // this.primaryRepository.save(new PrimaryMongoObject(null,"测试2save()"));
//        mongoTemplate.insert(new PrimaryMongoObject(null,"测试2save()"));
        mongoService.insert(name);
        return new Response();
    }

    @RequestMapping(value = "/mongodbFind", method = {RequestMethod.POST, RequestMethod.GET})
    public Response mongodbFind() {
//        List<PrimaryMongoObject> all = primaryRepository.findAll();
        List<PrimaryMongoObject> list = mongoService.findAll();
        return new Response(list);
    }

    @RequestMapping(value = "/mongodbDelAll", method = {RequestMethod.POST, RequestMethod.GET})
    public Response mongodbDelAll() {
        System.out.println();
        mongoService.del();
        System.out.println();
        return new Response();
    }

    @RequestMapping(value = "/getCollections")
    public Response getCollections() {
        List<String> list = mongoService.getCollections();
        for (String s : list) {
            System.out.println(s);
        }
        return new Response(list);
    }

    @RequestMapping(value = "/findByCondition")
    public Response findByCondition() {
        List<PrimaryMongoObject> list = mongoService.findByCondition();
        for (PrimaryMongoObject s : list) {
            System.out.println(s);
        }
        return new Response(list);
    }
}
