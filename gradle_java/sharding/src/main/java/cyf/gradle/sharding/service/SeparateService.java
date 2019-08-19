package cyf.gradle.sharding.service;

import cyf.gradle.dao.mapper.RecordMapper;
import cyf.gradle.dao.mapper.UserMapper;
import cyf.gradle.dao.model.Record;
import cyf.gradle.dao.model.RecordExample;
import cyf.gradle.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2019-08-16 11:08
 **/
@Service
@Slf4j
public class SeparateService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RecordMapper recordMapper;

    public void insert(User user) {
        userMapper.insertSelective(user);
    }

    public User getUserById(Integer id) {

        //强制走主库查询
        //HintManager.getInstance().setMasterRouteOnly();

        return userMapper.selectByPrimaryKey(id);
    }

    public void insertRecord(Record record) {
        recordMapper.insertSelective(record);
    }

    public Record getRecordById(Long id) {

        return recordMapper.selectByPrimaryKey(id);
    }

    public List<Record> getRecordList(String likeName) {
        RecordExample recordExample = new RecordExample();
        recordExample.createCriteria().andNameLike(likeName);
        return recordMapper.selectByExample(recordExample);
    }
 }
