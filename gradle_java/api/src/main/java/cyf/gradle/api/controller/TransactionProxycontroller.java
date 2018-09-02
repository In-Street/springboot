package cyf.gradle.api.controller;

import cyf.gradle.api.service.TransactionProxyService;
import cyf.gradle.api.service.TransactionProxyService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2018-01-13 下午8:49
 **/
@RestController
@RequestMapping("/transaction")
public class TransactionProxycontroller {

    @Autowired
    private TransactionProxyService proxyService;

    @Autowired
    private TransactionProxyService1 proxyService1;

    @GetMapping("/test")
    public void test() throws Exception {

//        proxyService.test5();
//        proxyService1.test1();
//        proxyService1.insert();
        proxyService1.insertTwo();
    }
}
