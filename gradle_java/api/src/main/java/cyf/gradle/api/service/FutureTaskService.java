package cyf.gradle.api.service;

import com.google.common.base.Stopwatch;
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

/**
 * @author Cheng Yufei
 * @create 2018-09-17 15:47
 **/
@Service
@Slf4j
public class FutureTaskService {

    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    public void futureHandle() {
        Stopwatch stopwatch = Stopwatch.createStarted();
//        List<FutureTask> list = Lists.newArrayList();
        List<Future<Long>> futureList = Lists.newArrayList();
        final long[] sum = {0};
        for (int i = 0; i < 1000; i++) {
            futureList.add(threadPoolExecutor.submit(new MyCallable()));
            //FutureTask 为 Future接口的实现，创建对象后可以作为submit参数，也可取到执行的结果
           /* FutureTask futureTask = new FutureTask(new MyCallable());
            threadPoolExecutor.submit(futureTask);
            futureTask.get();    */
        }
        futureList.parallelStream().forEach(task -> {
            try {
                System.out.println(task.get());
                    sum[0] += task.get();
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

    /**
     * FutureTask 耗时：4 s，结果：49999995000000000
     * 2018-09-21 10:19:41.702  INFO 3996 --- [nio-8090-exec-3] cyf.gradle.api.configuration.AopHandler  : <<< 结束请求: /future/futureHandle,futureHandle(),耗时:4477ms with result =
     * 2018-09-21 10:19:50.529  INFO 3996 --- [nio-8090-exec-5] cyf.gradle.api.configuration.AopHandler  : >>> 开始请求: /future/commonHandle,commonHandle() with argument[s] = []
     * 2018-09-21 10:20:08.122  INFO 3996 --- [nio-8090-exec-5] c.gradle.api.service.FutureTaskService   : common 结果：49999995000000000
     * 2018-09-21 10:20:08.122  INFO 3996 --- [nio-8090-exec-5] c.gradle.api.service.FutureTaskService   : common 耗时：17 s
     * 2018-09-21 10:20:08.123  INFO 3996 --- [nio-8090-exec-5] cyf.gradle.api.configuration.AopHandler  : <<< 结束请求: /future/commonHandle,commonHandle(),耗时:17594ms with result =
     */


}
