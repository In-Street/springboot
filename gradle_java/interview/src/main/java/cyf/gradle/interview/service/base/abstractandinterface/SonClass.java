package cyf.gradle.interview.service.base.abstractandinterface;

/**
 * @author Cheng Yufei
 * @create 2019-02-03 18:44
 **/
public class SonClass extends ParentClass {

    public SonClass() {
        System.out.println("子类构造器");
    }

    static {
        System.out.println("子类静态代码块");
    }

    {
        System.out.println("子类代码块");
    }


}
