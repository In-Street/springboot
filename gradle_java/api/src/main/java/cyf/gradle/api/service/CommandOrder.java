package cyf.gradle.api.service;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * @author Cheng Yufei
 * @create 2018-08-02 17:11
 **/
public class CommandOrder extends HystrixCommand<String> {

    private String orderName;

    public CommandOrder(String orderName) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("")));

        this.orderName = orderName;
    }

    @Override
    protected String run() throws Exception {
        return null;
    }
}
