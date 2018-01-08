package cyf.gradle.api.service;

import cyf.gradle.base.model.Response;
import cyf.gradle.dao.mapper.UserMapper;
import cyf.gradle.dao.model.User;
import cyf.gradle.dao.model.UserExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by cheng on 2017/7/10.
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    UserMapper userMapper;


    public Response save(User user) {
        userMapper.insertSelective(user);
        return new Response();
    }


    public Response select(Integer uid,String username) {
        UserExample example = new UserExample();
        example.createCriteria()
        .andIdEqualTo(uid);
        example.or().andUsernameLike("%"+username+"%");
        List<User> list = userMapper.selectByExample(example);
        return new Response(list);
    }

    public  List<User> select1(int id) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(id);
        List<User> list = userMapper.selectByExample(example);
        return list;
    }
    public  List<User> select2(String name) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
//        criteria.andUsernameEqualTo(name);
//        criteria.andUsernameLike("%" + name + "%");
        criteria.andPwdEqualTo(name);
        List<User> list = userMapper.selectByExample(example);
        return list;
    }


    public Response update(int id,String pwd) {
        User user = new User();
        user.setId(id);
        user.setPwd(pwd);
        userMapper.updateByPrimaryKeySelective(user);
        return new Response();
    }


    /**
     * 同步方法与异步耗时测试
     */
    public void doTask1() {
        long start = System.currentTimeMillis();
        log.debug("同步任务一开始执行：{}", FastDateFormat.getInstance("HH:mm:ss").format(new Date()));

        try {
            Thread.sleep(1000);
            long end = System.currentTimeMillis();
            log.debug("同步任务一执行结束耗时：{}", (end - start) + "ms");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doTask2() {
        long start = System.currentTimeMillis();
        log.debug("同步任务二开始执行：{}", FastDateFormat.getInstance("HH:mm:ss").format(new Date()));

        try {
            Thread.sleep(1000);
            long end = System.currentTimeMillis();
            log.debug("同步任务二执行结束耗时：{}", (end - start) + "ms");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doTask3() {
        long start = System.currentTimeMillis();
        log.debug("同步任务三开始执行：{}", FastDateFormat.getInstance("HH:mm:ss").format(new Date()));

        try {
            Thread.sleep(1000);
            long end = System.currentTimeMillis();
            log.debug("同步任务三执行结束耗时：{}", (end - start) + "ms");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
