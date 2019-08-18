package cyf.gradle.sharding.service;

import cyf.gradle.dao.mapper.UserMapper;
import cyf.gradle.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Cheng Yufei
 * @create 2019-08-16 11:08
 **/
@Service
@Slf4j
public class SeparateService {

    @Autowired
    private UserMapper userMapper;

    public void insert(User user) {
        userMapper.insert(user);
    }

    public User getUserById(Integer id) {

        //强制走主库查询
        //HintManager.getInstance().setMasterRouteOnly();

        return userMapper.selectByPrimaryKey(id);
    }
}
