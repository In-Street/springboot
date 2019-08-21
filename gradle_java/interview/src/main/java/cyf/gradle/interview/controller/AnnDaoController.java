package cyf.gradle.interview.controller;

import cyf.gradle.dao.model.Region;
import cyf.gradle.dao.model.User;
import cyf.gradle.interview.service.annotationdao.AnnDaoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2019-08-21 17:59
 **/
@RestController
@RequestMapping("/anndao")
public class AnnDaoController {

    @Resource
    private AnnDaoService annDaoService;

    @GetMapping("/getList")
    public List<User> getList() {
        return annDaoService.getList();
    }
}
