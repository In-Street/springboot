package cyf.gradle.api.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Cheng Yufei
 * @create 2019-03-18 16:58
 **/

/**
 * ApplicationRunner：在容器启动完成之后执行，用于必要的操作
 * args： 获取启动参数设定，在Edit Configuration 中启动类，program arguments: --type=456
 */
@Component
@Slf4j
@Order(1)
public class Init implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.containsOption("type") && args.getOptionValues("type").contains("456")) {
            log.info("init >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> runner");
        }
    }
}
