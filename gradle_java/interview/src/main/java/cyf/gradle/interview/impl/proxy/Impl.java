package cyf.gradle.interview.impl.proxy;

import cyf.gradle.interview.service.proxy.HI;
import cyf.gradle.interview.service.proxy.Hello;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Cheng Yufei
 * @create 2018-06-01 下午9:03
 **/
@Slf4j
@NoArgsConstructor
public class Impl implements Hello,HI {

    @Override
    public String sayHi(String msg) {
        log.info("HiImpl:{}",msg);
        return msg;
    }

    @Override
    public String sayHello(String msg) {
        log.info("HelloImpl:{}",msg);
        return msg;
    }
}
