package cyf.gradle.api.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author Cheng Yufei
 * @create 2018-08-04 上午9:54
 **/
@Service
public class CommandUserForAnnotation {


    /**
     * 注解形式 利用 HystrixCommand 进行熔断降级,指定降级方法，降级的与主方法的返回值与参数需保持一致
     *
     * 需将 HystrixCommandAspect 类已bean形式注入spring ： 参考 HystrixConfiguration 类
     *
     * @param userName
     * @return
     * @throws Exception
     */
    @HystrixCommand(fallbackMethod = "getFallback")
    public String run(String userName) throws Exception {
        TimeUnit.MILLISECONDS.sleep(400);
        System.out.println("username: " + userName);
        throw new Exception("");
//        return "username: " + userName;
    }


    public String getFallback(String userName) {
        return "---------用户模块降级处理：" + userName + " ---------";
    }
}
