package cyf.gradle.api.Enums;

import java.util.List;

/**
 * @author Cheng Yufei
 * @create 2018-03-30 11:27
 **/
public class TestMain {

    public static void main(String[] args) {
        List<Integer> list = InitEnum.INSTANCE.getList();
        System.out.println(list);
    }
}
