package cyf.gradle.newredis.service;

import cyf.gradle.newredis.module.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Cheng Yufei
 * @create 2017-10-30 17:02
 **/
@Service
public class UserService {



    @Cacheable(value = "userCache",keyGenerator = "wiseKeyGenerator")
    public User getUserS(Integer id,String username,String pwd) {
        System.out.println("无缓存时候执行-调用数据库时间："+ LocalDateTime.now());
        return new User(id,username,pwd);
    }
}
