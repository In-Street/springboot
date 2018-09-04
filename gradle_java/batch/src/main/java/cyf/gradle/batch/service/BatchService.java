package cyf.gradle.batch.service;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2018-08-27 下午10:00
 **/
@Service
@Slf4j
public class BatchService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job jobOne;


//    @Scheduled(cron = "0 0/1 * * * ?")
    public void launch() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        log.info("<====batch任务开始====>");
        /**
         * JobParameters 相同的任务只能成功执行一次；
         * 若要周期执行，需保证周期内参数唯一，否则任务只会执行一次；
         */
        JobParameters parameters = new JobParametersBuilder().addDate("time", new Date())
                .toJobParameters();




        jobLauncher.run(jobOne, parameters);

        log.info("<====batch任务结束====>");
    }

}
