package cyf.gradle.newredis.delay;

import com.alibaba.fastjson.JSONObject;
import cyf.gradle.newredis.module.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 延时任务消费者
 *
 * @author Cheng Yufei
 * @create 2018-09-07 下午5:59
 * <p>
 * 1. Sorted Set ，我们可以把任务的描述序列化成字符串，放在Sorted Set的value中，然后把任务的执行时间戳作为score，利用Sorted Set天然的排序特性，执行时刻越早的会排在越前面
 * <p>
 * 2.只要开一个或多个定时线程，每隔一段时间去查一下这个Sorted Set中score小于或等于当前时间戳的元素（这可以通过zrangebyscore命令实现），然后再执行元素对应的任务即可
 * <p>
 * 3.执行完任务后，还要将元素从Sorted Set中删除，避免任务重复执行
 **/
@Slf4j
@Service
public class DelayConsumer {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ScheduledThreadPoolExecutor scheduleThreadPool;

    private BoundZSetOperations zSet = null;

    @PostConstruct
    public void init() {
        zSet = redisTemplate.boundZSetOps("delay_task");
    }

    public void consumer() {

        //拉取 小于 当前时间 score的 任务
        Set<String> task = zSet.rangeByScore(0, System.currentTimeMillis());

        if (task.isEmpty()) {
            log.info("{} - 检测没有任务", Thread.currentThread().getName());
            return;
        }
        Consumer<String> consumer = s -> {
            //任务移除
            Long remove = zSet.remove(s);
            if (remove == 1) {
                User user = JSONObject.parseObject(s, User.class);
                log.info("{} - 任务执行完成，{}", Thread.currentThread().getName(), user.toString());
            }
        };
        task.stream().forEach(consumer);
        log.info("完成：{} 个任务", task.size());
    }

    public void scan() {
        //每个5秒检测一次
        scheduleThreadPool.scheduleAtFixedRate(() -> consumer(), 0, 5, TimeUnit.SECONDS);

    }
}
