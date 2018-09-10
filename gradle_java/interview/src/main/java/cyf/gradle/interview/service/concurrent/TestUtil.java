package cyf.gradle.interview.service.concurrent;

import cyf.gradle.interview.modle.User;

/**
 * @author Cheng Yufei
 * @create 2018-09-10 下午8:57
 **/
public class TestUtil {

    public void send() {
        User user = Singleton.instance.user;
        System.out.println(user);

        User user_1 = Singleton.instance.user;
        System.out.println(user_1);

        System.out.println(user==user_1);

    }

    private TestUtil(){}

    /**
     * 单例获取
     * @return
     */
    public static TestUtil getInstance() {
        System.out.println(Singleton.instance.testUtil);
        return Singleton.instance.testUtil;
    }


    /**
     * 枚举初始化数据
     */
    private  enum Singleton{
        //
        instance;

        private User user;
        private TestUtil testUtil;

         Singleton(){
             System.out.println("<===TestUtil 枚举初始化===>");
            testUtil = new TestUtil();
             user = new User(1,"Tay");
        }

    }
}
