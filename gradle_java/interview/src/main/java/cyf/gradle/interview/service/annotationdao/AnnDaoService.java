package cyf.gradle.interview.service.annotationdao;

import cyf.gradle.dao.mapper.RegionMapper;
import cyf.gradle.dao.mapper.UserMapper;
import cyf.gradle.dao.model.Region;
import cyf.gradle.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2019-08-21 17:49
 **/
@Service
@Slf4j
public class AnnDaoService {


    @Resource
    private UserMapper userMapper;

    public List<User> getList() {
        return userMapper.getList();

    }
}
