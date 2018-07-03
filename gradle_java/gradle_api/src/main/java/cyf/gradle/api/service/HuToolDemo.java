package cyf.gradle.api.service;

import cn.hutool.core.convert.NumberChineseFormater;

/**
 * @author Cheng Yufei
 * @create 2018-06-28 14:24
 **/
public class HuToolDemo {


    public static void main(String[] args) {
        NumberChineseFormater();
    }

    private static void NumberChineseFormater() {
        String format = NumberChineseFormater.format(230023.32, true, true);
        System.out.println(format);
    }
}
