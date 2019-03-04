package cyf.gradle.interview.service.concurrent;

import com.google.common.collect.Lists;
import cyf.gradle.interview.modle.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 利用 CompletableFuture 完成并行处理，或者采用CountDownLatch 、CyclicBarrier
 * <p>
 * 对多组任务并行执行，依靠线程池，但有阻塞队列，存在竞争；
 * 未指定自定义线程池时，使用 ForkJoinPool.commonPool-worker-1 线程池；
 *
 * @author Cheng Yufei
 * @create 2018-09-27 15:39
 **/
@Service
@Slf4j
public class ParallelService {

    @Autowired
    private ThreadPoolExecutor threadPool;

    private CountDownLatch countDownLatch = new CountDownLatch(7);

    public void completableFutureConcurrent() throws ExecutionException, InterruptedException {

        User.UserBuilder userBuilder = User.builder();
        //各个计算结果集合
        List<CompletableFuture<Void>> result = Lists.newArrayList();

        CompletableFuture<Void> resultId = CompletableFuture.runAsync(() -> {
            log.info("id 处理：{}", Thread.currentThread().getName());
            userBuilder.id(100);
        }, threadPool);
        result.add(resultId);

        //添加回调
//        resultId.whenComplete()


        CompletableFuture<Void> nameResult = CompletableFuture.runAsync(() -> {
            log.info("name 处理：{}", Thread.currentThread().getName());
            userBuilder.name("Taylor Swift");
        }, threadPool);
        result.add(nameResult);

        //汇聚任务结果
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(result.toArray(new CompletableFuture[result.size()]));
        //get()阻塞等待汇聚的所有任务完成
        allDoneFuture.get();

        //只等待Id处理完成，不管name处理情况
//        resultId.get();
        log.info("User:{}", userBuilder.build());
    }

    public void completableFutureConcurrent2() {
        User.UserBuilder userBuilder = User.builder();
        //各个计算结果集合
        List<CompletableFuture<User.UserBuilder>> result = Lists.newArrayList();

        CompletableFuture<User.UserBuilder> resultId = CompletableFuture.supplyAsync(
                () -> {
                    log.info("id 处理：{}, User:{}", Thread.currentThread().getName(), userBuilder.build());
                    return userBuilder.id(200);
                }, threadPool);

        CompletableFuture<User.UserBuilder> resultName = CompletableFuture.supplyAsync(
                () -> {
                    log.info("name 处理：{} ，User:{}", Thread.currentThread().getName(), userBuilder.build());
                    return userBuilder.name("Candice");
                }, threadPool);

        //thenAccept:请求线程处理;  thenAcceptAsync: 异步处理，不指定线程池时默认使用 ForkJoinPool.commonPool
        resultId.thenAcceptAsync(b -> {
            b.city("北京");
            log.info("resultId -- thenAcceptAsync 处理：{}，User:{}", Thread.currentThread().getName(), userBuilder.build());
        }, threadPool);

//        TimeUnit.NANOSECONDS.sleep(200);
        log.info("User:{}", userBuilder.build());
    }

    @SneakyThrows({InterruptedException.class, ExecutionException.class})
    public void countdownLatch() {

        for (int i = 0; i < 7; i++) {
            int finalI = i;
            Future<Integer> future = threadPool.submit(() -> {
                log.info("Thread ：{} 完成任务：{}", Thread.currentThread().getName(), finalI);
                return finalI;
            });
            if (future.get() >= 0) {
                countDownLatch.countDown();
            }
        }
        //请求线程阻塞，异步线程完成后，结束请求
        countDownLatch.await();
        log.info("全部任务完成");
    }

    public void cyclicBarrier()  {
        /**
         * 请求线程不阻塞，结束请求；
         * 异步线程业务处理后，到达await 阻塞，等待其他线程到达屏障
         * 所有线程都到达后执行 CyclicBarrier 里的任务。
         *
         * 注意：
         *      线程池核心数量5 ，队列：100
         *      7个任务，核心线程执行5个，await()后核心线程阻塞，
         *      2个任务进队列，队列未满，不会创建新的线程执行，等待核心线程执行，但核心线程一直阻塞，无限期等待
         */
        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
            log.info("分批任务已全部完成");
        });

        for (int i = 0; i < 7; i++) {
            int finalI = i;
//            log.info("阻塞等待线程数：{}",cyclicBarrier.getNumberWaiting());
            threadPool.execute(() -> {
                log.info("Thread ：{} ，完成任务：{}", Thread.currentThread().getName(),finalI);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException |BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
    }


}
