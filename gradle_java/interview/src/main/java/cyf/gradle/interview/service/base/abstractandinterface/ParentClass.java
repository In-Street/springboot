package cyf.gradle.interview.service.base.abstractandinterface;

/**
 * @author Cheng Yufei
 * @create 2019-02-03 18:42
 **/
public class ParentClass {

    public ParentClass() {
        System.out.println("父类构造器");
    }

    static {
        System.out.println("父类静态代码块");
    }

    {
        System.out.println("父类代码块");
    }


}
