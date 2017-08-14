package cyf.gradle.api.controller;

import cyf.gradle.api.service.MongoService;
import cyf.gradle.api.service.UserService;
import cyf.gradle.base.model.Response;
import cyf.gradle.dao.mongodb.PrimaryMongoObject;
import cyf.gradle.dao.mongodb.PrimaryMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by cheng on 2017/7/10.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    PrimaryMongoRepository primaryRepository;

    @Autowired
    MongoTemplate mongoTemplate;



    @RequestMapping(value = "/save", method = {RequestMethod.POST, RequestMethod.GET})
    public Response save() {
        //   userService.save();

        // 获取当前方法名
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println(methodName);

        //字符串转Date
        try {
            Date date = DateFormat.getDateInstance().parse("2017-07-29");
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //ArrayUtils.toMap();
        return new Response();
    }


    // 会先到MyBatisRedis - redis 中，getObject() 查看是否有缓存，如果有不执行数据库操作 否则执行sql语句并且 putObject() 加入redis缓存
    @RequestMapping(value = "/select", method = {RequestMethod.POST, RequestMethod.GET})
    public Response select() {



        return  userService.select();
    }

    //先执行 clear() ，然后执行sql 操作数据库
    @RequestMapping(value = "/update", method = {RequestMethod.POST, RequestMethod.GET})
    public Response update() {

        userService.update();
        System.out.println();
        return new Response();
    }

    //查询时条件不一样 也会 进行缓存添加
    @RequestMapping(value = "/select1", method = {RequestMethod.POST, RequestMethod.GET})
    public Response select1() {

        return  userService.select1();
    }


}
