package cyf.gradle.interview.service.concurrent;

import cyf.gradle.interview.modle.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


/**
 * @author Cheng Yufei
 * @create 2018-09-27 18:17
 **/
@Service
@Slf4j
public class ForkJoinTaskService {

    private static User.UserBuilder builder = User.builder();

    /**
     * 1.ForkJoinTask在不显示使用ForkJoinPool.execute/invoke/submit()方法进行执行的情况下，也可以使用自己的fork/invoke方法进行执行。
     * 2.ForkJoinTask有两个子类，RecursiveAction和RecursiveTask。他们之间的区别是，RecursiveAction没有返回值，RecursiveTask有返回值。
     * <p>
     * -----------------------------
     * <p>
     * ForkJoinPool:
     * 1.可以使用ForkJoinPool.execute(异步，不返回结果)/invoke(同步，返回结果)/submit(异步，返回结果)方法，来执行ForkJoinTask。
     * <p>
     * 2.ForkJoinPool有一个方法commonPool()，这个方法返回一个ForkJoinPool内部声明的静态ForkJoinPool实例。
     * 文档上说，这个方法适用于大多数的应用。这个静态实例的初始线程数，为“CPU核数-1 ”（Runtime.getRuntime().availableProcessors() - 1）。
     * ForkJoinTask自己启动时，使用的就是这个静态实例。
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */

    public User taskCompute() throws ExecutionException, InterruptedException {
        //ForkJoinPool-1-worker-%d
        ForkJoinPool pool = new ForkJoinPool(2);
        TotalTask totalTask = new TotalTask();
//        User user = pool.invoke(totalTask);
        User user = pool.submit(totalTask).get();
//        pool.execute(new TotalTask());

        //fork()之后，利用join阻塞等待任务完成返回结果
//        User user = new TotalTask().join();

        //fork()之后，利用get阻塞等待任务完成返回结果阻塞
//        User user = new TotalTask().get();

        //compute()：task自启动执行任务，请求线程同步执行TotalTask
//        User user = new TotalTask().compute();
        //invoke()：task自启动执行任务，请求线程同步执行TotalTask，等待任务完成返回结果
//        User user = new TotalTask().invoke();

        //fork().join()、fork().get(): 异步执行 ForkJoinPool.commonPool-worker-%d
//        User user = new TotalTask().fork().get();
//        User user = new TotalTask().fork().join();

        return user;
    }

    class TotalTask extends RecursiveTask<User> {
        @Override
        protected User compute() {
            UserIdTask userId = new UserIdTask();
            UserNameTask userName = new UserNameTask();
            //可以执行，并等待这两个任务执行完成才返回;某个任务有异常时会抛出异常，整个请求异常
//            invokeAll(userId,userName);

            //fork()：异步执行，安排在此任务正在运行的线程池中异步执行此任务,加入队列等待被调度 未指定ForkJoinPool时，使用 ForkJoinPool.commonPool-worker-%d 执行; 有异常时不会抛出异常
            userId.fork();
            userName.fork();

            //fork(),可通过join()等待任务完成或isDone() 来获取异常
            /*while (!userId.isDone()) {
                log.info("UserId 任务执行中。。。{}", Thread.currentThread().getName());
            }
            if (userId.isCompletedAbnormally()) {
                System.out.println("《------" + userId.getException() + "------>");
            }*/
            User user = builder.build();
            log.info("TotalTask - {}，User:{}", Thread.currentThread().getName(), user);
            return user;
        }
    }


    class UserIdTask extends RecursiveTask<User> {
        @Override
        protected User compute() {
            builder.id(300);
            Integer.valueOf("AA");
            User user = builder.build();
            log.info("UserIdTask - {}，User:{}", Thread.currentThread().getName(), user);
            return user;
        }
    }

    class UserNameTask extends RecursiveTask<User> {
        @Override
        protected User compute() {
            builder.name("Ariana");
//            Integer.valueOf("AA");
            User user = builder.build();
            log.info("UserNameTask - {}，User:{}", Thread.currentThread().getName(), user);
            return user;
        }
    }
}
