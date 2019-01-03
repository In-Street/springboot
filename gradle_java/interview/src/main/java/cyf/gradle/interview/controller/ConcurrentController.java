package cyf.gradle.interview.controller;

import cyf.gradle.interview.modle.User;
import cyf.gradle.interview.service.concurrent.ParallelService;
import cyf.gradle.interview.service.concurrent.ForkJoinTaskService;
import cyf.gradle.interview.service.concurrent.SingleEnumDemo;
import cyf.gradle.interview.service.concurrent.SynChronizedService;
import cyf.gradle.interview.service.concurrent.TestUtil2;
import cyf.gradle.interview.service.review.AtomicStampedRefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ExecutionException;

/**
 * @author Cheng Yufei
 * @create 2018-06-01 下午9:23
 **/
@RestController
@RequestMapping("/concurrent")
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConcurrentController {

    @Autowired
    private SynChronizedService synChronizedService;
    @Autowired
    private ParallelService parallelService;
    @Autowired
    private ForkJoinTaskService forkJoinTaskService;
    @Autowired
    private AtomicStampedRefService stampedRefService;


    @GetMapping
    @RequestMapping("/synTest")
    public void synTest() {

        /**
         *
         */
        synChronizedService.hand();

        List list = SingleEnumDemo.INSTANCE.getList();
        System.out.println(list);
    }

    @GetMapping("/singleton")
    public void singleton() {
//      TestUtil.getInstance().send();

        TestUtil2 util2 = TestUtil2.getInstance();
        System.out.println(util2);
    }

    @GetMapping("/completableFuture/{method}")
    public void completableFuture(@PathVariable Integer method) throws ExecutionException, InterruptedException, BrokenBarrierException {
        switch (method) {
            case 1:
                parallelService.completableFutureConcurrent();
                break;
            case 2:
                parallelService.completableFutureConcurrent2();
                break;
            case 3:
                parallelService.countdownLatch();
                break;
            case 4:
                parallelService.cyclicBarrier();
                break;
            default:
                break;
        }
    }

    @GetMapping("/forkJoinTask")
    public User forkJoinTask() throws ExecutionException, InterruptedException {
        return forkJoinTaskService.taskCompute();
    }

    ///////////////////////////////////////------------- review -----------------///////////////////////////////////////

    @GetMapping("/AtomicStampedReference/{method}")
    public Integer AtomicStampedReference(@PathVariable Integer method) throws InterruptedException {

        switch (method) {
            case 1:
                return stampedRefService.topUp();
            case 2:
                return stampedRefService.consume();
            default:
                return stampedRefService.init();

        }
    }

}
