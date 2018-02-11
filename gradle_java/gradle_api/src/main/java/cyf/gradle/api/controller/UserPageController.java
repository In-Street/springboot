package cyf.gradle.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Cheng Yufei
 * @create 2018-01-04 下午11:38
 **/
/*@RestController
@RequestMapping("/userPage")
public class UserPageController {

    @Autowired
    private UserServiceForPage userServiceForPage;

    @GetMapping("/find")
    public Page findPage(Pageable pageable) {

        return userServiceForPage.findPage(pageable);

    }
}*/
