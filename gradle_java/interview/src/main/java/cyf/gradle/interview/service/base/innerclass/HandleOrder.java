package cyf.gradle.interview.service.base.innerclass;

/**
 * @author Cheng Yufei
 * @create 2019-03-18 17:43
 **/
public class HandleOrder {

    public static void main(String[] args) {
        // A 操作
        handle();

        //B 操作
//        HandleOrder handleOrder = new HandleOrder();
    }

    static HandleOrder handleOrder = new HandleOrder();

    static {
        System.out.println("1");
    }

    HandleOrder() {
        System.out.println("3");
        System.out.println("a= " + a + ", b=" + b);
    }

    {
        System.out.println("2");
    }


    public static void handle() {
        System.out.println("4");
    }

    int a = 10;
    static int b = 100;
}
/**
 * 类初始化流程：
 *      父类静态变量赋值、静态代码块 -> 子类静态变量赋值、静态代码块
 *
 * 对象实例化流程：
 *      父类成员变量赋值、普通代码块 -> 父类构造器 -> 子类成员变量、普通代码块 -> 子类构造器
 *
 * 实例化时需要类的初始化，但不是一定要初始化完才能实例化。 初始化过程中遇到实例化，先执行实例化过程，然后继续初始化过程。
 */


/**
 * A 操作执行结果：
 *
 * 2
 * 3
 * a= 10, b=0
 * 1
 * 4
 * 类初始化：
 *  加载第一个静态变量，遇到对象实例化，走实例化流程：初始化成员变量及普通代码块 -> 构造器 -> 然后继续初始化，静态代码块、静态成员变量 -> main
 *
 *B 操作，将实例化放入main方法中：
 * 1
 * 4
 * 2
 * 3
 * a= 10, b=100
 */
