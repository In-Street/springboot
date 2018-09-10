package cyf.gradle.interview.service.concurrent;

/**
 * 饿汉模式单例
 *
 * @author Cheng Yufei
 * @create 2018-09-10 下午8:57
 **/
public class TestUtil2 {

    private static TestUtil2 testUtil2 = new TestUtil2();

    public void send() {

    }

    private TestUtil2(){
        System.out.println("<===初始化===》");
    }

    /**
     * 单例获取
     * @return
     */
    public static TestUtil2 getInstance() {
        return testUtil2;
    }

}
