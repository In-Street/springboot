package cyf.gradle.interview.service.time_concurrent.deadlock;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 避免死锁
 *
 * @author Cheng Yufei
 * @create 2019-05-31 15:23
 **/
@Slf4j
@Service
public class AvoidDeadlockService {

    private static Map<String, Account> accountMap;

    static {
        accountMap = Maps.newHashMapWithExpectedSize(2);
        accountMap.put("A", new Account(1,"A", 500));
        accountMap.put("B", new Account(2,"B", 500));
        accountMap.put("C", new Account(3,"C", 600));
    }

    /**
     * 转账：所需所有资源统一申请发放,破坏死锁占有等待条件
     */
    public void transfer(String fromName, String toName, int money) throws InterruptedException {

        //获取单例资源分配器
        Allocator allocator = Instance.ALLOCATOR.allocator;

        Account fromAccount = accountMap.get(fromName);
        Account toAccount = accountMap.get(toName);

        //申请锁
        allocator.apply(fromAccount, toAccount);
        TimeUnit.MILLISECONDS.sleep(200);
        synchronized (fromAccount) {
            synchronized (toAccount) {
                fromAccount.setBalance(fromAccount.getBalance() - money);
                toAccount.setBalance(toAccount.getBalance() + money);
            }
        }
        log.info("{}余额：{},{}余额：{}", fromAccount.getName(), fromAccount.getBalance(), toAccount.getName(), toAccount.getBalance());
        //释放锁
        allocator.release(fromAccount, toAccount);

      /*
       A转B , B转A 会发生死锁 两种情况各用10个线程并发
      TimeUnit.MILLISECONDS.sleep(200);
        synchronized (fromAccount) {
            synchronized (toAccount) {
                fromAccount.setBalance(fromAccount.getBalance() - money);
                toAccount.setBalance(toAccount.getBalance() + money);
            }
        }*/

        log.info("{}余额：{},{}余额：{}", fromAccount.getName(), fromAccount.getBalance(), toAccount.getName(), toAccount.getBalance());
    }

    /**
     * 转账：资源排序申请，从小到大申请，破坏死锁循环等待条件
     */
    public void transferByResourceOrder(String fromName, String toName, int money) throws InterruptedException {
        Account fromAccount = accountMap.get(fromName);
        Account toAccount = accountMap.get(toName);

        Account small = fromAccount;
        Account big = toAccount;

        if (fromAccount.getResourceId() > toAccount.getResourceId()) {
            small = toAccount;
            big = fromAccount;
        }
        TimeUnit.MILLISECONDS.sleep(200);
        synchronized (small){
            synchronized (big){
                fromAccount.setBalance(fromAccount.getBalance() - money);
                toAccount.setBalance(toAccount.getBalance() + money);
            }
        }
        log.info("{}余额：{},{}余额：{}", fromAccount.getName(), fromAccount.getBalance(), toAccount.getName(), toAccount.getBalance());
    }

    @Getter
    private enum Instance {

        //
        ALLOCATOR;

        private Allocator allocator;

        Instance() {
            log.info(">>>>>>>>>>枚举<<<<<");
            allocator = new Allocator();
        }
    }

    static class insclass {

        private static Allocator ins = new Allocator();

        private insclass() {
        }

        public static Allocator getIns() {
            return insclass.ins;
        }
    }
}
