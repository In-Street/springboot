package cyf.gradle.interview.service.base.abstractandinterface;

import cyf.gradle.interview.service.base.innerclass.Robot;

public interface RobotFactory {

    /**
     * 默认成员变量修饰：public static final
     */
    public static final String name = "";


    /**
     *  默认方法修饰：public abstract
     *
     *  静态方法有方法体
     *
     *  default 修饰的方法有方法体
     */
    public   abstract Robot get();

    void walk();

    public static void run() {

    }

    default  void work() {}

}
