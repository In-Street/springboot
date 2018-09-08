package cyf.gradle.newredis.delay;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 延时任务生产者
 *
 * @author Cheng Yufei
 * @create 2018-09-07 下午5:59
 **/
@Service
@Slf4j
public class DelayProducer {

    @Autowired
    private RedisTemplate redisTemplate;

    private BoundZSetOperations zSet = null;

    @PostConstruct
    public void init() {
        zSet = redisTemplate.boundZSetOps("delay_task");
    }

    public void produce(String json, double score) {
        //只有第一次添加成功为true
        Boolean add = zSet.add(json, score);
        log.info("添加任务- {}，{}，{}", json, Thread.currentThread().getName(), add);
    }
}
