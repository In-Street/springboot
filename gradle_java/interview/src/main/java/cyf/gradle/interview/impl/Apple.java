package cyf.gradle.interview.impl;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Cheng Yufei
 * @create 2018-06-18 上午11:29
 **/
@Slf4j
public class Apple {

    public String color(String color) {
        log.info("Apple - color:{}", color);
        return color;
    }

    public int getInt(int i) {
        log.info("Apple - getInt:{}", i);
        return i;
    }
}
