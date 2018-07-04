package cyf.gradle.schedule.configuration;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 *
 * @author Cheng Yufei
 * @create 2017-08-04 17:52
 **/
@Component
public class ScheduleTask {

    private int count = 0;

//    @Scheduled(fixedRate = 5000)  每5 秒钟执行

    //每天的 18:07 执行
    @Scheduled(cron = "0 07 18 ? * *")
    private void process() {
        System.out.println("running:  "+ (count++));
    }


}
