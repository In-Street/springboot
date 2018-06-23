package cyf.gradle.interview.service.concurrent;

/**
 * @author Cheng Yufei
 * @create 2018-06-23 11:12
 **/
public class SingletonDemo {

   private static class SingletonHolder{
        private static final SingletonDemo singletonDemo = new SingletonDemo();
    }

    private SingletonDemo() {
    }

    public static final SingletonDemo getInstance() {
        return SingletonHolder.singletonDemo;
    }
}

/**
 * 采用静态内部类方式实现单例模式：
 *
 * 只有调用getInstance() 时才会创建单例
 *
 * 第一次调用静态字段时，触发类加载器（同一个类只加载一次），静态内部类同理；
 *
 * 利用类加载器创建对象时，jvm会加锁，同步多个线程对同一个类的初始化，进而保证单例对象的唯一性（由类加载器负责加锁，保证线程安全性）
 *
 */
