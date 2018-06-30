package cyf.gradle.interview.controller;

import cyf.gradle.interview.service.concurrent.SynChronizedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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



    @GetMapping
    @RequestMapping("/synTest")
    public void synTest() {

        /**
         *
         */
        synChronizedService.hand();
    }



}
