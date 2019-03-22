package cyf.gradle.api.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Cheng Yufei
 * @create 2019-03-18 16:57
 **/
@Component
@Slf4j
@Order(100)
public class Close implements ApplicationRunner {

  /*  @Autowired
    ApplicationContext application;*/
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("close >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> runner");
//        System.exit(SpringApplication.exit(application));
//        System.exit(1);
    }
}
