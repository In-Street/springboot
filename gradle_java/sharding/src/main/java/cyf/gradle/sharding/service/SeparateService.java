package cyf.gradle.sharding.service;

import cyf.gradle.dao.mapper.UserMapper;
import cyf.gradle.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.api.hint.HintManager;
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

        HintManager.getInstance().setMasterRouteOnly();

        return userMapper.selectByPrimaryKey(id);
    }
}
