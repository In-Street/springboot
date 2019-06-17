package cyf.gradle.interview.service.time_concurrent.semaphore;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 信号量限流
 *
 * @author Cheng Yufei
 * @create 2019-06-17 10:48
 **/
@Service
@Slf4j
public class LimitingService {


    private Semaphore semaphore;
    private CopyOnWriteArrayList<String> list;

    @PostConstruct
    private void init() {
        //初识容量5
        semaphore = new Semaphore(5);
        list = new CopyOnWriteArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
        list.add("G");
        list.add("H");
        list.add("I");
        list.add("J");
    }

    /**
     * 初识值：5
     * down: -1，小于0 阻塞
     * up: +1 ，小于等于0 ，唤醒等待队列中的线程
     * @throws InterruptedException
     */
    public void limit() throws InterruptedException {
        String res = null;
        try {
            log.debug("Thread:{}，等待线程数：{}", Thread.currentThread().getId(), semaphore.getQueueLength());
            semaphore.acquire();
            String remove = list.remove(0);
            res = StringUtils.lowerCase(remove);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            TimeUnit.SECONDS.sleep(5);
            log.debug("Thread:{} 释放", Thread.currentThread().getId());
            list.add(res);
            System.out.println(list);
            semaphore.release();
        }
    }

}
