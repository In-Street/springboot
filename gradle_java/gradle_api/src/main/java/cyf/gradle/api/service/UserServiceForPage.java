package cyf.gradle.api.service;

import cyf.gradle.api.service.interfaces.UserForPageRepository;
import cyf.gradle.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Cheng Yufei
 * @create 2018-01-04 下午11:24
 **/
@Service
@Slf4j
public class UserServiceForPage {

    /*@Autowired
    private UserForPageRepository userForPageRepository;

    public Page findPage(Pageable pageable) {

        log.debug("---------boot 自带分页测试---------");

        Page<User> userPage = userForPageRepository.findAll(pageable);

        int total = userPage.getTotalPages();

        return userPage;
    }*/


}
