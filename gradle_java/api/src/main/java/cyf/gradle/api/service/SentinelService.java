package cyf.gradle.api.service;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import cyf.gradle.api.configuration.SentinelBlockException;
import cyf.gradle.dao.mapper.UserMapper;
import cyf.gradle.dao.model.User;
import cyf.gradle.dao.model.UserExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Cheng Yufei
 * @create 2019-04-09 16:19
 **/
@Service
@Slf4j
public class SentinelService {

    @Autowired
    private UserMapper userMapper;

    private AtomicInteger atomicInteger = new AtomicInteger();

    @SentinelResource(value = "SentinelByName", blockHandlerClass = {SentinelBlockException.class}, blockHandler = "blockHandle")
    public List<User> selectByName(String name) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andPwdEqualTo(name);
        List<User> list = userMapper.selectByExample(example);
        return list;
    }

    @SentinelResource(value = "SentinelByName2", fallback = "fallback")
    public List<User> selectByName2(String name) throws InterruptedException {
        if (atomicInteger.incrementAndGet() < 7) {
            Thread.sleep(10);
        }
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andPwdEqualTo(name);
        List<User> list = userMapper.selectByExample(example);
        return list;
    }

    public List<User> fallback(String name) {
        log.error(">>>>>>>>fallback: 降级");
        return Collections.EMPTY_LIST;
    }

    public List<User> selectByName3(String name, String origin) {
        String resource = "SentinelByName3";
        ContextUtil.enter(resource, origin);
        List<User> list = null;
        Entry entry = null;
        try {
            entry = SphU.entry(resource);
            System.out.println(String.format("Passed for resource %s, origin is %s", resource, origin));
            UserExample example = new UserExample();
            UserExample.Criteria criteria = example.createCriteria();
            criteria.andPwdEqualTo(name);
            list = userMapper.selectByExample(example);
        } catch (BlockException e) {
            System.err.println(String.format("Blocked for resource %s, origin is %s", resource, origin));
        } finally {
            if (entry != null) {
                entry.exit();
            }
            ContextUtil.exit();
        }
        return list;


    }
}
