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

}
