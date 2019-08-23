package cyf.gradle.interview.service.annotationdao;

import cyf.gradle.dao.mapper.RecordMapper;
import cyf.gradle.dao.mapper.UserMapper;
import cyf.gradle.dao.model.Record;
import cyf.gradle.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private RecordMapper recordMapper;

    public List<User> getList() {
        return userMapper.getList();

    }

    @Transactional(rollbackFor = Exception.class)
    public void insertUser(User user) {
        userMapper.insertSelective(user);
        int i = 1 / 0;
    }

    public List<Record> getRecoordList() {
        return recordMapper.getRecordList();

    }

    @Transactional(rollbackFor = Exception.class)
    public void insertRecord(Record record) {
        recordMapper.insertSelective(record);
        //int i = 1 / 0;
    }
}
