package cyf.gradle.interview.controller;

import cyf.gradle.interview.service.concurrent.SingleEnumDemo;
import cyf.gradle.interview.service.concurrent.SynChronizedService;
import cyf.gradle.interview.service.concurrent.TestUtil;
import cyf.gradle.interview.service.concurrent.TestUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

        List list = SingleEnumDemo.INSTANCE.getList();
        System.out.println(list);
    }

    @GetMapping("/singleton")
    public void singleton() {
//      TestUtil.getInstance().send();

        TestUtil2 util2 = TestUtil2.getInstance();
        System.out.println(util2);


    }

}
