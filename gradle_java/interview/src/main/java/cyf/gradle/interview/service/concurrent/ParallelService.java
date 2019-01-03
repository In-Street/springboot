package cyf.gradle.interview.service.concurrent;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import cyf.gradle.interview.modle.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private ThreadPoolExecutor executor;

    private CountDownLatch countDownLatch = new CountDownLatch(7);

    public void completableFutureConcurrent() throws ExecutionException, InterruptedException {

        User.UserBuilder userBuilder = User.builder();
        //各个计算结果集合
        List<CompletableFuture<Void>> result = Lists.newArrayList();

        CompletableFuture<Void> resultId = CompletableFuture.runAsync(() -> {
            log.info("id 处理：{}", Thread.currentThread().getName());
            userBuilder.id(100);
        }, executor);
        result.add(resultId);

        CompletableFuture<Void> nameResult = CompletableFuture.runAsync(() -> {
            log.info("name 处理：{}", Thread.currentThread().getName());
            userBuilder.name("Taylor Swift");
        }, executor);
        result.add(nameResult);

        //汇聚任务结果
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(result.toArray(new CompletableFuture[result.size()]));
        //get()阻塞等待汇聚的所有任务完成
        allDoneFuture.get();

        //只等待Id处理完成，不管name处理情况
//        resultId.get();
        log.info("User:{}", userBuilder.build());
    }

    public void completableFutureConcurrent2() throws InterruptedException {
        User.UserBuilder userBuilder = User.builder();
        //各个计算结果集合
        List<CompletableFuture<User.UserBuilder>> result = Lists.newArrayList();

        CompletableFuture<User.UserBuilder> resultId = CompletableFuture.supplyAsync(
                () -> {
                    log.info("id 处理：{}, User:{}", Thread.currentThread().getName(), userBuilder.build());
                    return userBuilder.id(200);
                }, executor);

        CompletableFuture<User.UserBuilder> resultName = CompletableFuture.supplyAsync(
                () -> {
                    log.info("name 处理：{} ，User:{}", Thread.currentThread().getName(), userBuilder.build());
                    return userBuilder.name("Candice");
                }, executor);

        //thenAccept:请求线程处理;  thenAcceptAsync: 异步处理，不指定线程池时默认使用 ForkJoinPool.commonPool
        resultId.thenAcceptAsync(b -> {
            b.city("北京");
            log.info("resultId -- thenAcceptAsync 处理：{}，User:{}", Thread.currentThread().getName(), userBuilder.build());
        }, executor);

//        TimeUnit.NANOSECONDS.sleep(200);
        log.info("User:{}", userBuilder.build());
    }

    @SneakyThrows({InterruptedException.class, ExecutionException.class})
    public void countdownLatch() {

        for (int i = 0; i < 7; i++) {
            int finalI = i;
            Future<Integer> future = executor.submit(() -> {
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

    public void cyclicBarrier() throws ExecutionException, InterruptedException, BrokenBarrierException {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
            log.info("分批任务已全部完成");
        });

        for (int i = 0; i < 7; i++) {
            int finalI = i;
            executor.execute(() -> {
                log.info("Thread ：{} ", Thread.currentThread().getName());
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException |BrokenBarrierException e) {
                    e.printStackTrace();
                }
                log.info("完成任务：{}", finalI);
            });

        }


        /*for (int i = 0; i < 7; i++) {
            int finalI = i;
            Future<Integer> future = executor.submit(() -> {
                log.info("Thread ：{} ", Thread.currentThread().getName());
                cyclicBarrier.await();
                return finalI;
            });
            if (future.get()>=0) {
            log.info("完成任务：{}", finalI);
            }
        }*/

       /* for (int i = 1; i <= 7; i++) {
            int finalI = i;
            new Thread(() -> {
                System.out.println("第" + finalI + "位法师已到来");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            },"Thread_" + i).start();
        }*/

    }


}
