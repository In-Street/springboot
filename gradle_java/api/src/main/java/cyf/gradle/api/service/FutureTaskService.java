package cyf.gradle.api.service;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void futureHandle() throws ExecutionException, InterruptedException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Future<?> future = null;
        FutureTask task = new FutureTask(new MyCallable());
        for (int i = 0; i < 1000; i++) {
            future = threadPoolExecutor.submit(task);
        }
        log.info("future 结果：{}", task.get());
        log.info("FutureTask 耗时：{} s", stopwatch.elapsed(TimeUnit.SECONDS));
    }

    private static class MyCallable implements Callable {
        int sum = 0;
        @Override
        public Object call() throws Exception {
            for (int i = 0; i < 10000000; i++) {
                sum += i;
            }
            return sum;
        }
    }


    public void commonHandle() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        int sum = 0;
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 10000000; j++) {
                sum += j;
            }
        }
        log.info("common 结果：{}", sum);
        log.info("common 耗时：{} s", stopwatch.elapsed(TimeUnit.SECONDS));
    }

}
