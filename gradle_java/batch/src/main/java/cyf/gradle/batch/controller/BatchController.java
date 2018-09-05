package cyf.gradle.batch.controller;

import cyf.gradle.batch.service.BatchService;
import cyf.gradle.batch.service.UserDailyHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2018-08-27 下午10:06
 **/
@RestController
@RequestMapping("/batch")
@Slf4j
public class BatchController {

    @Autowired
    private BatchService batchService;
    @Autowired
    private UserDailyHistoryService userDailyHistoryService;

    @GetMapping("/execute")
    public String execute() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        batchService.launch();
        return "success";
    }

    @GetMapping("/commExecute")
    public String v()  {
        userDailyHistoryService.copyToHistoryAndUpdate();
        return "success";
    }
}
