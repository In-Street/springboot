package cyf.gradle.interview.service.concurrent;

import com.google.common.collect.Lists;
import cyf.gradle.interview.modle.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 利用 CompletableFuture 完成并行处理，或者采用CountDownLantch 、CyclicBarrier
 *
 * 对多组任务并行执行，依靠线程池，但有阻塞队列，存在竞争
 * @author Cheng Yufei
 * @create 2018-09-27 15:39
 **/
@Service
@Slf4j
public class CompletableFutureService {

    @Autowired
    private ThreadPoolExecutor executor;

    public void concurrent() throws ExecutionException, InterruptedException {

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
        log.info("User:{}",userBuilder.build());
    }

}
