package cyf.gradle.interview.controller.time;

import cyf.gradle.interview.service.time_concurrent.cyclicandlatch.CyclicBarrierService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Cheng Yufei
 * @create 2019-06-19 11:30
 **/
@RestController
@RequestMapping("/cl")
public class CyclicLatchController {

    @Resource
    private CyclicBarrierService cyclicBarrierService;

    @RequestMapping("/handler")
    public String cl() {
        cyclicBarrierService.premiseHandler();
        return "successful";
    }
}
