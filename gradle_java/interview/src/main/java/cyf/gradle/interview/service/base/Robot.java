package cyf.gradle.interview.service.base;

/**
 * @author Cheng Yufei
 * @create 2019-01-28 21:21
 **/

/**
 *  1. 通过内部类方式实现多继承
 *
 *  2. 内部类可随意访问外部类的任一修饰符修饰的成员变量及方法。
 *
 *  3. 内部类通过 “外部类类名.this” 返回外部类引用 ;  外部类通过直接new Inner 获取内部类引用
 *
 *  4. 普通内部类：不能定义静态成员变量，可访问外部类的成员变量、静态成员变量、普通方法、静态方法；
 *
 *      静态内部类：可定义普通、静态成员变量，只可访问外部类的静态成员变量、静态方法；
 *
 *      外部类： new Inner 获取内部类引用后，可访问普通内部类的成员变量及方法，静态内部类的非静态方法、非静态成员变量；
 *
 *   5.
 *
 *
 */
public class Robot  extends Guide{

    private  static String country;
    private  String city;


    public void guide(String name) {
        doGuide(name);
    }

    public void repair(String name) {
        new RobotRepair().innerRepair(name);

    }

    private static void walk() {
    }


    /**
     * 普通内部类
     */
    public class RobotRepair extends Repair{

        private  String body;

        public  void innerRepair(String name) {
            // 外部类引用
            Robot.this.guide(name);
            //外部类私有成员变量
            System.out.println(country+""+city);
            //方法
            guide(name);
            walk();

            doRepair(name);
        }
    }

    /**
     * 静态内部类
     */
    public static class InnerRobot {
        public String name;
        private  static String heigh;

        public  void run() {
            walk();
        }

    }
    public static void main(String[] args) {
        RobotRepair repair = new Robot().new RobotRepair();
    }
}