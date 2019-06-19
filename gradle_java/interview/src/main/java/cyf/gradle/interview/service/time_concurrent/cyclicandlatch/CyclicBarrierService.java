package cyf.gradle.interview.service.time_concurrent.cyclicandlatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2019-06-19 11:09
 **/
@Service
@Slf4j
public class CyclicBarrierService {

    @Resource
    private ThreadPoolExecutor threadPool;

    private static final CopyOnWriteArrayList<String> q1 = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<String> q2 = new CopyOnWriteArrayList<>();

    private final CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {
        threadPool.execute(() -> theLastHandler());
    });


    private void theLastHandler() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String q1Remove = q1.remove(0);
        String q2Remove = q2.remove(0);
        log.debug(">>>>>>>>处理：{} - {}<<<<<<", q1Remove, q2Remove);
    }


    public void premiseHandler() {

        threadPool.execute(() -> {

            for (int i = 0; i < 10; i++) {
                q1.add(String.valueOf(i));
                try {
                    log.info(">>>>>>>>q1 第{}次添加<<<<<<", i);
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });

        threadPool.execute(() -> {
            for (int i = 0; i < 10; i++) {
                q2.add(String.valueOf(i));
                try {
                    log.info(">>>>>>>>q2 第{}次添加<<<<<<", i);
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 场景：订单、派送单 ，两者未对账时生成差异单;
     *
     * 传统： 单线程处理，查询满足条件的订单,然后查询满足条件的派送单，然后处理两种单生成差异单;
     *
     * 优化1： T1 处理订单，T2 处理派送单， T1.join  T2.join ，然后处理两种单生成差异单;
     *
     * 优化2： 使用CountDownLatch、线程池， 订单查询完成后latch.countDown() ，派送单完成后latch.countDown()，然后处理两种单;
     *          虽然查询两种订单是并行执行，但随后生成差异单和前面是同步的，处理完后才会继续下次查询；
     *
     *优化3：使用CyclicBarrier、线程池；查询订单、查询派送单、处理两种单都并行，前两者徐步调一致，一一对应，生成一对后执行处理；处理期间继续下次查询操作，然后入队；
     *      CyclicBarrier循环使用。
     *
     *
     *2019-06-19 14:46:59.882 INFO  - [] - >>>>>>>>q1 第0次添加<<<<<<
     * 2019-06-19 14:46:59.882 INFO  - [] - >>>>>>>>q2 第0次添加<<<<<<
     * 2019-06-19 14:46:59.882 INFO  - [] - >>>>>>>>q2 第1次添加<<<<<<
     * 2019-06-19 14:46:59.882 INFO  - [] - >>>>>>>>q1 第1次添加<<<<<<
     * 2019-06-19 14:46:59.882 INFO  - [] - >>>>>>>>q1 第2次添加<<<<<<
     * 2019-06-19 14:46:59.882 INFO  - [] - >>>>>>>>q2 第2次添加<<<<<<
     * 2019-06-19 14:46:59.883 INFO  - [] - >>>>>>>>q1 第3次添加<<<<<<
     * 2019-06-19 14:46:59.883 INFO  - [] - >>>>>>>>q2 第3次添加<<<<<<
     * 2019-06-19 14:46:59.883 INFO  - [] - >>>>>>>>q2 第4次添加<<<<<<
     * 2019-06-19 14:46:59.883 INFO  - [] - >>>>>>>>q1 第4次添加<<<<<<
     * 2019-06-19 14:46:59.883 INFO  - [] - >>>>>>>>q2 第5次添加<<<<<<
     * 2019-06-19 14:46:59.883 INFO  - [] - >>>>>>>>q1 第5次添加<<<<<<
     * 2019-06-19 14:47:01.883 DEBUG - [] - >>>>>>>>处理：0 - 0<<<<<<
     * 2019-06-19 14:47:01.883 DEBUG - [] - >>>>>>>>处理：1 - 1<<<<<<
     * 2019-06-19 14:47:01.884 DEBUG - [] - >>>>>>>>处理：3 - 3<<<<<<
     * 2019-06-19 14:47:01.884 DEBUG - [] - >>>>>>>>处理：4 - 4<<<<<<
     * 2019-06-19 14:47:01.884 DEBUG - [] - >>>>>>>>处理：5 - 5<<<<<<
     * 2019-06-19 14:47:01.884 DEBUG - [] - >>>>>>>>处理：2 - 2<<<<<<
     *
     */
}
