package cyf.gradle.api.Enums;

import com.google.common.collect.Lists;

import java.util.List;

/**

 利用枚举实现单例模式，jvm保证enum不被反射，且构造器执行一次，只能通过枚举的valueOf找对应的值，
 避免了反射破坏单例

 * @author Cheng Yufei
 * @create 2018-03-30 11:11
 **/
public enum InitEnum {


    INSTANCE;

    /**
     * 可用于一些类的static对数据的初始化，或者链接数据库的一些操作
     */
    private List<Integer> list = null;

    private InitEnum(){
        initData();
    }

    private void initData() {
        list = Lists.newArrayList();
        list.add(1);
    }
    public List<Integer> getList() {
        return list;
    }
}
