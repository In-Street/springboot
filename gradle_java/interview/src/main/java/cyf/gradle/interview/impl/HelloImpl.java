package cyf.gradle.interview.impl;

import cyf.gradle.interview.service.Hello;

/**
 * @author Cheng Yufei
 * @create 2018-06-01 下午9:03
 **/
public class HelloImpl implements Hello {

    @Override
    public String say(String msg) {
        System.out.println();
        return msg;
    }
}
