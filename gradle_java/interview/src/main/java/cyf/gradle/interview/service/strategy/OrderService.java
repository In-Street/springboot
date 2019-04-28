package cyf.gradle.interview.service.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Cheng Yufei
 * @create 2019-04-28 17:07
 **/
@Service
@Slf4j
public class OrderService {

    /**
     * 在InitOrder或InitOrder2中完成对HandleContext的容器注册，此处直接引用
     */
    @Resource(name = "orderHandlerContext")
    @Lazy
    private HandleContext handleContext;

    public String orderHandler(OrderType orderType) {
        if (Objects.isNull(orderType)) {
            return "订单类型不符";
        }
        Order order = handleContext.getInstance(orderType);
        return order.handle();
    }
}
