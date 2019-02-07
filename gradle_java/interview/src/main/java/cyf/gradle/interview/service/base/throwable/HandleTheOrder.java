package cyf.gradle.interview.service.base.throwable;

/**
 * @author Cheng Yufei
 * @create 2019-02-04 11:21
 **/
public class HandleTheOrder {

    public int handle() {
        int a = 1;
        try {
            System.out.println("try");
//            throw new Exception();
            return a;
        } catch (Exception e) {
            System.out.println("cache");
            a = a + 2;
            return a;

        } finally {
            a = a + 1;
            System.out.println("finally:  " + a);
//            return a;

        }
//        return a;
    }


    public int handle2() {
        int a = 1;
        try {
            System.out.println("try");
            throw new Exception();
        } catch (Exception e) {
            System.out.println("cache");
            a = a + 2;
            try {
                throw new Exception();
            } catch (Exception e1) {
                a = a + 2;
                System.out.println("Inner catch");
            } finally {
                a = a + 2;
                System.out.println("Inner finally");
            }
        } finally {
            a = a + 1;
            System.out.println("finally:  " + a);

        }
        return a;
    }

    public static void main(String[] args) {
        HandleTheOrder handleTheOrder = new HandleTheOrder();
        /**
         * 结果：1
         *  finally 块中执行在 try/catch 中return 语句执行之后返回之前
         */
      /*  int handle = handleTheOrder.handle();
        System.out.println(handle);*/

        int handle2 = handleTheOrder.handle2();
        System.out.println(handle2);

    }
}
