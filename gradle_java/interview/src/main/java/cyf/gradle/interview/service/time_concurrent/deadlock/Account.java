package cyf.gradle.interview.service.time_concurrent.deadlock;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * 账户
 *
 * @author Cheng Yufei
 * @create 2019-05-31 15:55
 **/
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class Account {

    /**
     * 资源ID,用于获取锁时排序
     */
    Integer resourceId;

    String name;
    /**
     * 余额
     */
    Integer balance;

    String password;


    public Account(Integer resourceId,String name, Integer balance) {
        this.resourceId = resourceId;
        this.name = name;
        this.balance = balance;
    }
}
