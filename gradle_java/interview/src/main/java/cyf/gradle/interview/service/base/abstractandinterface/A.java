package cyf.gradle.interview.service.base.abstractandinterface;

import cyf.gradle.interview.service.base.innerclass.Robot;

/**
 * @author Cheng Yufei
 * @create 2019-02-02 11:23
 **/
public class A extends AbstractClass {

    public A(String name) {
        /**
         * 显示调用父类有参构造器
         */
        super(name);
        System.out.println("A -- init");
    }

    /**
     * 继承抽象类后，实现抽象类里所有的抽象方法，否则该类定义成抽象类
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
        A a = new A("Taylor");
    }
}
