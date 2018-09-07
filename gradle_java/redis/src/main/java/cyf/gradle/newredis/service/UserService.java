package cyf.gradle.newredis.service;

import com.google.common.collect.Lists;
import cyf.gradle.newredis.module.Kerr2;
import cyf.gradle.newredis.module.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2017-10-30 17:02
 **/
@Service
public class UserService {



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
}
