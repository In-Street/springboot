package cyf.gradle.batch.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

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


    @Scheduled(cron = "0 11 22 ? * *")
    public void launch() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        log.info("<====batch任务开始====>");
        JobParameters parameters = new JobParametersBuilder().addDate("time", new Date())
                .toJobParameters();

        jobLauncher.run(jobOne, parameters);

        log.info("<====batch任务结束====>");
    }

}
