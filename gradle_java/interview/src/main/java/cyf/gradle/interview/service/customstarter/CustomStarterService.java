package cyf.gradle.interview.service.customstarter;

import cn.anony.boot.core.SmsSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Cheng Yufei
 * @create 2019-09-02 14:50
 **/
@Service
@Slf4j
public class CustomStarterService {

    @Resource
    private SmsSender smsSender;

    public String send() {
        return smsSender.send();
    }
}
