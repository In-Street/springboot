package cyf.gradle.interview.service.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 原子更新带有版本号的引用类型
 * 无锁状态利用CAS保证数据的一致性
 *
 * @author Cheng Yufei
 * @create 2018-06-26 15:51
 **/
public class AtomicStampedReferenceDemo {

    public static AtomicStampedReference<Integer> atomic = new AtomicStampedReference(18, 0);

    public static AtomicInteger integer = new AtomicInteger(1);

    public static void main(String[] args) {

        //利用版本控制数据只被充值一次
        int stamp = atomic.getStamp();
        Integer reference = atomic.getReference();
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            //多线程充值,版本控制只有一次充值成功
            new Thread(() -> {
                while (true) {
//                    Integer reference = atomic.getReference();
                    System.out.println(finalI);
                    if (atomic.compareAndSet(reference, reference + 20, stamp, stamp + 1)) {
                        System.out.println("充值成功：" + atomic.getReference());
                        break;
                    } else {
                        break;
                    }
                }
            }).start();

        }
        new Thread(() -> {
            //多次消费
            for (int i = 0; i < 200; i++) {
                while (true) {
                    int stampConsume = atomic.getStamp();
                    Integer reference1 = atomic.getReference();
                    if (reference1 > 10) {
                        if (atomic.compareAndSet(reference1, reference1 - 10, stampConsume, stampConsume + 1)) {
                            System.out.println("消费成功：" + atomic.getReference());
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }).start();
       /* for (int i = 0; i < 10; i++) {
            System.out.println(i);
            for (int j = 20; j < 30; j++) {
                if (j > 25) {
                    System.out.println(j);
//                    continue; 开启下一次内层循环
                    break; //结束内层循环，开启下次外层循环
                }
            }
        }*/

        for (int i = 0; i < 50; i++) {
            new Thread(()->{
                System.out.println(integer.getAndIncrement());
            }).start();
        }


    }
}
