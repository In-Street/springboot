package cyf.gradle.interview.service.time_concurrent.deadlock;

import java.util.ArrayList;
import java.util.List;

/**
 * 转账账户资源统一分配，一次性申请所有锁，破坏死锁的占有等待条件
 * <p>
 * 占有等待：一个线程持有一个资源A，申请不下B资源时，等待同时不释放A
 *
 * 资源发放着
 * @author Cheng Yufei
 * @create 2019-05-31 15:50
 **/
public class Allocator {

    private List<Account> list = new ArrayList<>();

    public Allocator() {
        System.out.println(">>>>Allocator构造");
    }
    /**
     * 一次性资源申请
     * @param from
     * @param to
     * @throws InterruptedException
     */
    synchronized void apply(Account from, Account to) throws InterruptedException {

        /**
         * 1> 包含任意一个说明资源已经分配过，申请者需等待；
         * 2> 使用while 而不是if：notifyAll被唤醒后，若是if 则直接执行wait后面的代码，若是while则会再次进行条件判断，
         *      因为此刻的不一定满足分配锁的条件，只能说明notifyAll那个时刻的条件时满足的
         */
        while (list.contains(from) || list.contains(to)) {
            System.out.println(">>>>>>>锁申请中<<<<<<<<");
            wait();
        }

        list.add(from);
        list.add(to);
        System.out.println(">>>>>>>锁申请完成<<<<<<<<");
    }

    /**
     * 资源释放
     */
    synchronized void release(Account from, Account to) {

        list.remove(from);
        list.remove(to);
        System.out.println(">>>>>>>锁释放<<<<<<<<");
        notifyAll();
    }

}
