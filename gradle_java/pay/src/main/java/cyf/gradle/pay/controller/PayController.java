package cyf.gradle.pay.controller;

import cyf.gradle.pay.service.PayWxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2018-09-21 18:19
 **/
@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayWxService payWxService;

    @GetMapping("/wxUnifyOrder")
    public String unifyOrder() {
        payWxService.pay();
        return "";
    }
}
