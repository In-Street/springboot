package cyf.gradle.interview.service.base.abstractandinterface;

import cyf.gradle.interview.service.base.innerclass.Robot;

/**
 * @author Cheng Yufei
 * @create 2019-02-02 17:49
 **/
public  class B implements RobotFactory {

    /**
     * 实现接口里所有的抽象方法，否则该类定义成抽象类
     * @return
     */
    @Override
    public Robot get() {
        return null;
    }

    @Override
    public void walk() {

    }

    public static void main(String[] args) {
        B b = new B();
        b.get();
        b.work();
        RobotFactory.run();
    }
}
