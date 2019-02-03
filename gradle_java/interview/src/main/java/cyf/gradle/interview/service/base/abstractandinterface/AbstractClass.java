package cyf.gradle.interview.service.base.abstractandinterface;

import cyf.gradle.interview.service.base.innerclass.Robot;

/**
 * @author Cheng Yufei
 * @create 2019-02-02 11:17
 **/
public abstract class AbstractClass {

    private String name;
    private static Integer age;

    public AbstractClass(){}
    /**
     * 有构造器，但不能通过new 创建
     */
   public AbstractClass(String name) {
       this.name = name;
       System.out.println("AbstractClass - init-"+name);
    }

    /**
     * 可包含内部类、初始方法块
     */
    class B{
    }

    /**
     *
     * @return
     */
    public   abstract Robot get();

    public   abstract void walk();

    public static void run() {

    }

    public void work() {

    }
}
