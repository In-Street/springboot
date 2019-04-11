package cyf.gradle.api.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import cyf.gradle.api.configuration.SentinelBlockException;
import cyf.gradle.dao.mapper.UserMapper;
import cyf.gradle.dao.model.User;
import cyf.gradle.dao.model.UserExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2019-04-09 16:19
 **/
@Service
@Slf4j
public class SentinelService {

    @Autowired
    private UserMapper userMapper;


    @SentinelResource(value = "SentinelByName",blockHandlerClass ={SentinelBlockException.class},blockHandler = "blockHandle")
    public List<User> selectByName(String name) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andPwdEqualTo(name);
        List<User> list = userMapper.selectByExample(example);
        return list;
    }
}
