package cyf.gradle.api.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2018-01-08 下午9:42
 **/
@Service
@Slf4j
public class HandleAsyncService {


    @Async
    public void insert() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(200);

        log.debug("自调用异步方法：{}", Thread.currentThread().getName());
    }
}
