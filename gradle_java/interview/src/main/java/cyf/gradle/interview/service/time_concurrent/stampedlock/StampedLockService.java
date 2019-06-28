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
         * 乐观读：
         * 1.没有锁 ，效率高于 悲观读锁readLock();
         * 2.多线程读的时候，允许一个线程进行写; ReentrantReadWriteLock 多线程读时，所有写阻塞
         */
        long stamp = stampedLock.tryOptimisticRead();
        int x, y;
        x = a;
        y = b;

        //sleep期间 执行write操作，看是否进入校验stamp重新获取值
        TimeUnit.MILLISECONDS.sleep(100);

        /**
         * 在给定stamp后没有被独占过【没被写过】则返回true,否则false
         */
        if (!stampedLock.validate(stamp)) {

            //升级为 悲观读锁
            stamp = stampedLock.readLock();
            try {
                //重新获取
                x = a;
                y = b;
                log.debug(">>>>>>>>>数据有变动，重新获取<<<<<<<<<");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
        System.out.println(x + "----------" + y);
    }

    public void write(int newA, int newB) throws InterruptedException {
        //为了体现一部分线程获取旧值、一部分线程获取新值
        TimeUnit.MILLISECONDS.sleep(120);
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

    /**
     * 2019-06-17 17:36:04.145 INFO  - [3a2f433d-7cc4-404a-ada4-a8ce3679077f] - >>> 开始请求: /stampedLock/read,limit() with argument[s] = []
     * 110----------210
     * 2019-06-17 17:36:04.246 INFO  - [3a2f433d-7cc4-404a-ada4-a8ce3679077f] - <<< 结束请求: /stampedLock/read,limit(),耗时:100ms with result = "success"
     * 2019-06-17 17:36:04.402 INFO  - [574a6cf0-33e1-45c1-ae93-24b84ac7aa56] - >>> 开始请求: /stampedLock/read,limit() with argument[s] = []
     * 110----------210
     * 2019-06-17 17:36:04.502 INFO  - [574a6cf0-33e1-45c1-ae93-24b84ac7aa56] - <<< 结束请求: /stampedLock/read,limit(),耗时:99ms with result = "success"
     * 2019-06-17 17:36:04.542 INFO  - [5413ac74-1fc2-4168-98b8-8845681dd90d] - >>> 开始请求: /stampedLock/read,limit() with argument[s] = []
     * 110----------210
     * 2019-06-17 17:36:04.642 INFO  - [5413ac74-1fc2-4168-98b8-8845681dd90d] - <<< 结束请求: /stampedLock/read,limit(),耗时:100ms with result = "success"
     * 2019-06-17 17:36:04.747 INFO  - [c5f8db9a-cc51-402e-aa53-4f40ecc09399] - >>> 开始请求: /stampedLock/read,limit() with argument[s] = []
     * 110----------210
     * 2019-06-17 17:36:04.848 INFO  - [c5f8db9a-cc51-402e-aa53-4f40ecc09399] - <<< 结束请求: /stampedLock/read,limit(),耗时:100ms with result = "success"
     * 2019-06-17 17:36:04.866 INFO  - [7538a870-4863-4051-8457-1eef696631bf] - >>> 开始请求: /stampedLock/write/111/211,write() with argument[s] = [111, 211]
     * 2019-06-17 17:36:04.946 INFO  - [5d28f8e8-c04b-4ccb-874f-454f6ffd610b] - >>> 开始请求: /stampedLock/read,limit() with argument[s] = []
     * 2019-06-17 17:36:04.987 INFO  - [7538a870-4863-4051-8457-1eef696631bf] - <<< 结束请求: /stampedLock/write/111/211,write(),耗时:121ms with result = "success"
     * 2019-06-17 17:36:05.047 DEBUG - [5d28f8e8-c04b-4ccb-874f-454f6ffd610b] - >>>>>>>>>数据有变动，重新获取<<<<<<<<<
     * 111----------211
     * 2019-06-17 17:36:05.047 INFO  - [5d28f8e8-c04b-4ccb-874f-454f6ffd610b] - <<< 结束请求: /stampedLock/read,limit(),耗时:100ms with result = "success"
     * 2019-06-17 17:36:05.366 INFO  - [55103cf1-4c55-4e8e-95af-5f354372ddc9] - >>> 开始请求: /stampedLock/write/111/211,write() with argument[s] = [111, 211]
     * 2019-06-17 17:36:05.486 INFO  - [55103cf1-4c55-4e8e-95af-5f354372ddc9] - <<< 结束请求: /stampedLock/write/111/211,write(),耗时:120ms with result = "success"
     */
}
