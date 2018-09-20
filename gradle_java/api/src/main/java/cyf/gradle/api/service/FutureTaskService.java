package cyf.gradle.api.service;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Cheng Yufei
 * @create 2018-09-17 15:47
 **/
@Service
@Slf4j
public class FutureTaskService {

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;
//    private static AtomicInteger sumAtomic = new AtomicInteger(0);

    public void futureHandle() throws ExecutionException, InterruptedException {
        Stopwatch stopwatch = Stopwatch.createStarted();
//        List<FutureTask> list = Lists.newArrayList();
        List<Future<Long>> futureList = Lists.newArrayList();
        final long[] sum = {0};
        for (int i = 0; i < 1000; i++) {
//            FutureTask task = new FutureTask(new MyCallable());
            futureList.add(threadPoolExecutor.submit(new MyCallable()));
        }
        futureList.parallelStream().forEach(task -> {
            try {
//                if (task.isDone()) {
                    sum[0] += task.get();
//                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        log.info("FutureTask 耗时：{} s，结果：{}", stopwatch.elapsed(TimeUnit.SECONDS), sum[0]);
    }

    private static class MyCallable implements Callable<Long> {
        @Override
        public Long call() throws Exception {
            long sum = 0;
//
            for (long i = 0; i < 10000000; i++) {
                sum += i;
            }
//            log.info("子线程：{}，sum:{}", Thread.currentThread().getName(), sum);
            return sum;
        }
    }


    public void commonHandle() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        long sum = 0;
        for (int i = 0; i < 1000; i++) {
            for (long j = 0; j < 10000000; j++) {
                sum += j;
            }
//            System.out.println(sum);
        }
        log.info("common 结果：{}", sum);
        log.info("common 耗时：{} s", stopwatch.elapsed(TimeUnit.SECONDS));
    }

}
