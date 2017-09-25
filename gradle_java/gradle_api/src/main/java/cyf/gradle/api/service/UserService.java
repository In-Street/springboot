package cyf.gradle.api.service;

import cyf.gradle.base.model.Response;
import cyf.gradle.dao.mapper.UserMapper;
import cyf.gradle.dao.model.User;
import cyf.gradle.dao.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by cheng on 2017/7/10.
 */
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;


    public Response save() {
        User user = new User();
        user.setUsername("taylor");
        user.setPwd("taylor");
        userMapper.insertSelective(user);
        return new Response();
    }

    public Response select() {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(4);
        List<User> list = userMapper.selectByExample(example);
        return new Response(list);
    }

    public Response select1() {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(2);
        List<User> list = userMapper.selectByExample(example);
        return new Response(list);
    }

    public Response update() {
        User user = new User();
        user.setId(4);
        user.setPwd("CANDICE");
        userMapper.updateByPrimaryKeySelective(user);
        return new Response();
    }

}
