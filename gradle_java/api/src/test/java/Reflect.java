import cyf.gradle.api.Enums.PushType;
import cyf.gradle.api.controller.C;
import cyf.gradle.api.controller.D;
import cyf.gradle.api.controller.Einterface;
import cyf.gradle.dao.model.User;
import org.junit.Test;

import java.lang.reflect.*;

/**
 * @author Cheng Yufei
 * @create 2019-03-03 15:26
 **/
public class Reflect {

    @Test
    public void Class() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        /**
         *  newInstance: 没有直接生成目标对象实例
         */
        Class<?> user = Class.forName("cyf.gradle.dao.model.User");
        Object obj = user.newInstance();

        //////////////////////////////////////////////

        Class<User> userClass = User.class;
        User user1 = userClass.newInstance();

        //////////////////////////////////////////////

        /**
         * Number及其子类
         */
        Class<? extends Number> obj2 = int.class;
        obj2 = double.class;
        obj = long.class;
        obj2 = Number.class;

        //////////////////////////////////////////////

        /**
         * 创建对象：
         */
        Class<String> stringClass = String.class;
        String s = stringClass.newInstance();

        Constructor<String> constructor = stringClass.getConstructor(String.class);
        String abc = constructor.newInstance("ABC");


        //////////////////////////////////////////////

        /**
         * dClass 获取父类时，不能直接指定 Class<C>, 需写成 Class<? super D>
         *     D 及其父类
         */
//        public class C extends D{}
        Class<D> dClass = D.class;
        Class<? super D> superclass = dClass.getSuperclass();

        superclass = D.class;
        superclass = C.class;

        //////////////////////////////////////////////

        Class<Einterface> einterfaceClass = Einterface.class;

        Class<Integer> integerClass = Integer.class;
        Class<Integer> type = Integer.TYPE;

    }


    @Test
    public void getMethods() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {

        // D extends C

        /**
         * 创建对象
         */
        Class<D> dClass = D.class;

        D d = dClass.newInstance();

        Constructor<D> constructor = dClass.getConstructor(String.class);
        D a = constructor.newInstance("A");


        /**
         * 获取所有，包括继承类的所有public方法
         */
        Method[] methods = dClass.getMethods();

        /**
         * 获取类里所有方法： public \ protected \ 空 \ private 方法 ， 不包括继承类里方法
         */
        Method[] declaredMethods = dClass.getDeclaredMethods();

        for (Method method : declaredMethods) {

            String s = Modifier.toString(method.getModifiers());
            System.out.println("Modifiers:  " + s);

            Modifier.isStatic(method.getModifiers());

            String simpleName = method.getReturnType().getSimpleName();
            System.out.println("returnType:  " + simpleName);

            Class<?>[] parameterTypes = method.getParameterTypes();

            Parameter[] parameters = method.getParameters();

            for (Class<?> parameterType : parameterTypes) {
                String simpleName1 = parameterType.getSimpleName();
                System.out.println("parameterType： " + simpleName1);
            }

        }


    }
}
