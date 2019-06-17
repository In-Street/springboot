package cyf.gradle.interview.service.time_concurrent.stampedlock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * 写锁、悲观读锁、乐观读
 *
 * @author Cheng Yufei
 * @create 2019-06-17 15:50
 **/
@Service
@Slf4j
public class StampedLockService {

    private final StampedLock stampedLock = new StampedLock();

    private int a = 5;

    private int b = 10;

    public void optimisticRead() throws InterruptedException {


        /**
         * 乐观读：没有锁 ，效率高于 悲观读锁readLock();
         */
        long stamp = stampedLock.tryOptimisticRead();
        int x, y;
        x = a;
        y = b;

        //sleep期间 执行write操作，看是否进入校验stamp重新获取值
        TimeUnit.SECONDS.sleep(3);

        /**
         * 在给定stamp后没有被独占过【没被写过】则返回true,否则false
         */
        if (!stampedLock.validate(stamp)) {
            stamp = stampedLock.readLock();
            try {
                //重新获取
                x = a;
                y = b;
                log.debug(">>>>>>>>>数据有变动，重新获取<<<<<<<<<");
                System.out.println(x + "----------" + y);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        System.out.println(x + "----------" + y);
    }

    public void write(int newA, int newB) {
        long stamp = stampedLock.writeLock();
        try {
            a = newA;
            b = newB;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }
}
