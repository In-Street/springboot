package cyf.gradle.api.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Cheng Yufei
 * @create 2019-03-18 16:59
 **/
@Component
@Slf4j
@Order(2)
public class Info implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Info >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>runner");
    }
}
