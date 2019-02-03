package cyf.gradle.interview.service.base.innerclass;

/**
 * @author Cheng Yufei
 * @create 2019-01-28 21:21
 **/

import cyf.gradle.interview.service.base.abstractandinterface.AbstractClass;
import cyf.gradle.interview.service.base.abstractandinterface.RobotFactory;

/**
 * 1. 通过内部类方式实现多继承
 *
 * 3. 内部类通过 “外部类类名.this” 返回外部类引用 ;  外部类通过直接new Inner 获取内部类引用
 *
 * 4. 普通内部类：不能定义静态成员变量，可访问外部类的成员变量、静态成员变量、普通方法、静态方法；
 *
 *      静态内部类：可定义普通、静态成员变量，只可访问外部类的静态成员变量、静态方法；
 *
 *      外部类： new Inner 获取内部类引用后，可访问普通内部类的成员变量及方法，静态内部类的非静态方法、非静态成员变量；
 */
public class Robot extends Guide {

    private static String country;
    private String city;

    /**
     * 匿名内部类:
     *  通过接口、抽象类创建
     */
    public static RobotFactory robotFactory = new RobotFactory() {
        @Override
        public Robot get() {
            return null;
        }

        @Override
        public void walk() {

        }
    };

    AbstractClass abstractClass = new AbstractClass(city) {
        @Override
        public Robot get() {
            return null;
        }

        @Override
        public void walk() {

        }
    };

    public void guide(String name) {
        doGuide(name);
    }

    public void repair(String name) {
        new RobotRepair().innerRepair(name);

    }

    private static void walk() {
        // 只能访问静态内部类的非静态成员变量\非静态方法
        System.out.println(new InnerRobot());
    }


    /**
     * 普通内部类:
     *
     * 只能定义普通的 成员变量/方法
     *
     * 可访问外部类的普通/静态的成员变量和方法
     */
    public class RobotRepair extends Repair {

        private String body;

        public void innerRepair(String name) {
            // 外部类引用
            Robot.this.guide(name);
            //外部类私有成员变量
            System.out.println(country + "" + city);
            //方法
            guide(name);
            walk();

            doRepair(name);
        }

    }

    /**
     * 静态内部类:
     *
     * 可定义普通、静态的 成员变量/方法；
     *
     * 只能访问外部类的静态成员变量和方法
     *
     *
     */
    public static class InnerRobot {
        public String name;
        private static String heigh;


        public static void run() {
            walk();
        }

        public void a() {
            System.out.println(country);
        }

    }

    public static void main(String[] args) {
        RobotRepair repair = new Robot().new RobotRepair();
        InnerRobot innerRobot = new InnerRobot();

        Robot robot = Robot.robotFactory.get();


    }
}
