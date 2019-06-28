package cyf.gradle.interview.controller;

import cyf.gradle.interview.service.transactional.TransactionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *syn_4
 * @author Cheng Yufei
 * @create 2018-06-01 下午9:23
 **/
@RestController
@RequestMapping("/transactional")
public class TransactionalController {

    @Autowired
    private TransactionalService service;

    @GetMapping
    @RequestMapping("/required")
    public String required() throws Exception {

        service.addUser();

        return "successful";
    }
}
