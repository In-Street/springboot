package cyf.gradle.interview.service.retry;

import com.alibaba.fastjson.JSONObject;
import com.github.rholder.retry.RetryException;
import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Cheng Yufei
 * @create 2019-09-03 11:43
 **/
@Service
@Slf4j
public class RetryService {

    private AtomicInteger times = new AtomicInteger(0);

    //》》》》》》》》》》》》》》》》》》》》》》》》》》》 spring-retry 》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》
    /**
     * value: 只针对特定异常进行重试
     * maxAttempts：最大重试次数
     * backoff: 指定重试间隔（delay），重试延迟倍数(multiplier)
     *
     * @return
     * @throws Exception
     */
    @Retryable(value = {RuntimeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    public JSONObject retryTest() throws Exception {
        int time = times.incrementAndGet();
        if (time % 4 == 0) {
            return new JSONObject(ImmutableMap.of("reMsg", "success"));
        }
        log.info("发生异常，重试 time:{}", LocalTime.now());
        throw new RuntimeException("异常");
    }

    /**
     * 针对 @Retryable 达到最大重试后进行处理，两个方法返回值需一致
     *
     * @return
     */
    @Recover
    public JSONObject recover() {
        log.info("已达最大重试次数");
        return new JSONObject(ImmutableMap.of("reMsg", "fail"));
    }


    //》》》》》》》》》》》》》》》》》》》》》》》》》》》 guava-retry 》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》》

    public JSONObject retryTest2() throws Exception {
        int time = times.incrementAndGet();
        if (time % 3 == 0) {
            return new JSONObject(ImmutableMap.of("reMsg", "success"));
        }
        /*log.info("发生异常，重试 time:{}", LocalTime.now());
        throw new RuntimeException("异常");*/
        return new JSONObject(ImmutableMap.of("reMsg", "fail"));
    }

    public JSONObject guavaRetry() {
        JSONObject call = new JSONObject();
        Retryer<Object> retryer = RetryerBuilder.newBuilder()
                //根据异常重试
                //.retryIfExceptionOfType(RuntimeException.class)
                //根据返回结果时候符合要求进行重试
                .retryIfResult(j -> {
                            String reMsg = ((JSONObject) j).getString("reMsg");
                            log.info("reMsg:{}", reMsg);
                            return !reMsg.equals("success");
                        }
                )
                //间隔3秒
                .withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
                //重试4次停止
                .withStopStrategy(StopStrategies.stopAfterAttempt(4))
                .build();
        try {
            call = (JSONObject) retryer.call(() -> retryTest2());
            return call;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (RetryException e) {
            e.printStackTrace();
        }
        return call;
    }
}
