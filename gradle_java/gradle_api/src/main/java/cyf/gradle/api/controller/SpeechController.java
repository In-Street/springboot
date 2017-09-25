package cyf.gradle.api.controller;

import cyf.gradle.api.service.SpeechService;
import cyf.gradle.api.service.UserService;
import cyf.gradle.base.model.Response;
import cyf.gradle.dao.mongodb.PrimaryMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by cheng on 2017/7/10.
 */
@RestController
@RequestMapping("/speech")
public class SpeechController {

    @Autowired
    SpeechService speechService;




    @RequestMapping(value = "/save", method = {RequestMethod.POST, RequestMethod.GET})
    public Response save() {
        System.out.println();
        speechService.test_2();
        return new Response();

        // 8618235011372_bf2d66@kindle.cn
    }




}
