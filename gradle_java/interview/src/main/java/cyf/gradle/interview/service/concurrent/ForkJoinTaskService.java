package cyf.gradle.interview.service.concurrent;

import cyf.gradle.interview.modle.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.RecursiveTask;


/**
 * @author Cheng Yufei
 * @create 2018-09-27 18:17
 **/
@Service
@Slf4j
public class ForkJoinTaskService {

    private static User.UserBuilder builder = User.builder();


    public void taskCompute() {
        UserIdTask userId = new UserIdTask();
        UserNameTask userName = new UserNameTask();
//        invokeAll(userId,userName);
        userId.join();
        userName.join();
         builder.build();
    }


    class UserIdTask extends RecursiveTask<User> {
        @Override
        protected User compute() {
            builder.id(300);
            User user = builder.build();
            log.info("UserIdTask - {}，User:{}",Thread.currentThread().getName(),user);
            return user;
        }
    }

    class UserNameTask extends RecursiveTask<User> {
        @Override
        protected User compute() {
            User user = builder.build();
            builder.name("Ariana");
            log.info("UserNameTask - {}，User:{}",Thread.currentThread().getName(),user);
            return user;
        }
    }
}
