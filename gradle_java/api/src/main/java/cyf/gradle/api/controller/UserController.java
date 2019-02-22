package cyf.gradle.api.controller;

import cyf.gradle.api.service.UserService;
import cyf.gradle.base.Constants;
import cyf.gradle.base.model.Header;
import cyf.gradle.base.model.LocalData;
import cyf.gradle.base.model.Response;
import cyf.gradle.dao.model.User;
import cyf.gradle.util.FastJsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by cheng on 2017/7/10.
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(value = "用户模块")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/save", method = {RequestMethod.POST, RequestMethod.GET})
    public Response save() {

        // 获取当前方法名
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println(methodName);

        //字符串转Date
        try {
            Date date = DateFormat.getDateInstance().parse("2017-07-29");
            String format = FastDateFormat.getInstance().format(new Date());

            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //ArrayUtils.toMap();
        return new Response();
    }

    @ApiOperation(value = "存储用户")
    @RequestMapping(value = "/saveUser",method = RequestMethod.POST)
    @ApiImplicitParam(name = "user",value = "用户实体",paramType = "body",dataType = "User",required = true)
    public Response saveUser(@RequestBody User user) {

        userService.save(user);

        return new Response();
    }


    // 会先到MyBatisRedis - redis 中，getObject() 查看是否有缓存，如果有不执行数据库操作 否则执行sql语句并且 putObject() 加入redis缓存
    @ApiOperation(value="根据UID获取用户", notes="根据url的id来获取信息")
    @RequestMapping(value = "/select", method = { RequestMethod.GET})
//    @ApiImplicitParam(name = "uid",value = "用户ID",paramType = "path",dataType = "int",required = true)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid",value = "用户ID",paramType = "path",dataType = "int",required = true ),
            @ApiImplicitParam(name = "appId",value = "appId",paramType = "header",dataType = "string",required = false),
            @ApiImplicitParam(name = "username",value = "用户名",paramType = "query",dataType = "string",required = true),
    })
    public Response select(@RequestParam  Integer uid,@RequestHeader(required = false) String appId,@RequestParam String username) {

        log.debug(String.format("参数 appId :【%s】",appId));
        return userService.select(uid,username);
    }

    //先执行 clear() ，然后执行sql 操作数据库
    @RequestMapping(value = "/update/{id}/{pwd}", method = {RequestMethod.POST, RequestMethod.GET})
    public Response update(@PathVariable int id,@PathVariable String pwd) {

        userService.update(id,pwd);
        System.out.println();
        return new Response();
    }

    //查询时条件不一样 也会 进行缓存添加
    @GetMapping(value = "/byId")
    public Response select1(@RequestParam int id) {

        Header header = LocalData.HEADER.get();
        Integer uid = header.getUid();
        return new Response(userService.select1(id));
    }

    //查询时条件不一样 也会 进行缓存添加

    @PostMapping(value = "/byName")
    public Response select2(@RequestParam String name) {

        Header header = LocalData.HEADER.get();
        Integer uid = header.getUid();
        return new Response(userService.select2(name));
    }

    @RequestMapping(value = "/save1/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    public Response save1(@PathVariable int id) {
        User user = userService.select1(id).get(0);
        redisTemplate.opsForValue().set(Constants.USER_LOGIN_KEY + "451DAE598CB14AB4B21BB19F113F9293",FastJsonUtils.toJSONString(user));
        return new Response();
    }


}
