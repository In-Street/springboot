package cyf.gradle.interview.service.review;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author Cheng Yufei
 * @create 2018-12-17 15:32
 **/
@Service
@Slf4j
public class AtomicStampedRefService {

    @Autowired
    private ThreadPoolExecutor threadPool;

    private static final AtomicStampedReference<Integer> stampReference = new AtomicStampedReference(0, 0);
    private static AtomicInteger ato = new AtomicInteger(0);

    /**
     * stamp : 版本控制，只充值一次
     *
     * @return
     */
    @SneakyThrows(InterruptedException.class)
    public int topUp() {
        Integer reference = stampReference.getReference();
        int stamp = stampReference.getStamp();
        for (int i = 0; i < 10; i++) {
            TimeUnit.MILLISECONDS.sleep(100);
            threadPool.submit(new Callable<Object>() {
                @Override
                public Object call() {
                    if (stampReference.compareAndSet(reference, reference + 10, stamp, stamp + 1)) {
                        log.debug("充值成功");
                        ato.getAndAdd(10);
                    }
                    return ato.get();
                }
            });
        }
        return ato.get();
    }

    /**
     * reference 避免超额消费
     * @return
     * @throws InterruptedException
     */
    public int consume() throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            Integer reference = stampReference.getReference();
            int stamp = stampReference.getStamp();
            TimeUnit.MILLISECONDS.sleep(100);
            if (reference > 3) {
                threadPool.submit(() -> {
                    log.info("Thread:{} ，reference:{}", Thread.currentThread().getName(), reference);
                    if (stampReference.compareAndSet(reference, reference - 3, stamp, stamp + 1)) {
                        ato.getAndAdd(-3);
                        log.debug("消费3元,余额：{}，版本：{}", ato.get(), stampReference.getStamp());
                    }
                    return ato.get();
                });
            } else {
                log.debug("余额不足：{}", ato.get());
            }
        }
        return ato.get();
    }

    public int init() {
        ato.set(0);
        stampReference.set(0, 0);
        return ato.get();
    }

}
