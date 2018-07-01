package cyf.gradle.interview.service.concurrent;

import com.google.common.primitives.Ints;

import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2018-07-01 上午10:27
 **/
public enum SingleEnumDemo {

    INSTANCE;

    private List integers;

    SingleEnumDemo() {
        System.out.println("单例 - 枚举 构造器加载");
        init();
    }
    private List init() {

         integers = Ints.asList(1, 2);
        return integers;
    }
    public List getList() {
        return integers;
    }

}
