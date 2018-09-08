package cyf.gradle.newredis.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import cyf.gradle.newredis.delay.DelayConsumer;
import cyf.gradle.newredis.delay.DelayProducer;
import cyf.gradle.newredis.module.Kerr2;
import cyf.gradle.newredis.module.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2017-10-30 17:02
 **/
@Service
public class UserService {

    @Autowired
    private DelayProducer producer;
    @Autowired
    private DelayConsumer consumer;

    @Cacheable(value = "my_redis_cache",keyGenerator = "wiseKeyGenerator")
    public User getUserS(Integer id,String username,String pwd) {
        System.out.println("无缓存时候执行-getUserS-调用数据库时间："+ LocalDateTime.now());
        return new User(id,username,pwd);
    }


    @Cacheable(value = "my_redis_cache",keyGenerator = "wiseKeyGenerator")
    public List<Kerr2> getKerr2() {

        System.out.println("无缓存时候执行-getKerr2-调用数据库时间："+ LocalDateTime.now());
        Kerr2 k = Kerr2.builder().id(1).title("Swift").build();
        Kerr2 k2 = Kerr2.builder().id(2).title("Candice").build();

        return Lists.newArrayList(k, k2);
    }

    public void delay() {

        long now = System.currentTimeMillis();

        User u_1 = User.builder().id(1).username("AA").build();
        User u_2 = User.builder().id(2).username("BB").build();
        User u_3 = User.builder().id(3).username("CC").build();
        User u_4 = User.builder().id(4).username("DD").build();
        User u_5 = User.builder().id(5).username("EE").build();

        //生产延时任务， zset 会根据score进行升序排序
        producer.produce(JSONObject.toJSONString(u_1),now+ TimeUnit.SECONDS.toMillis(5));
        producer.produce(JSONObject.toJSONString(u_2),now+ TimeUnit.SECONDS.toMillis(10));
        producer.produce(JSONObject.toJSONString(u_3),now+ TimeUnit.SECONDS.toMillis(3));
        producer.produce(JSONObject.toJSONString(u_4),now+ TimeUnit.SECONDS.toMillis(25));
        producer.produce(JSONObject.toJSONString(u_5),now+ TimeUnit.SECONDS.toMillis(2));

        consumer.scan();

    }
}
