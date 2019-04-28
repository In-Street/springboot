package cyf.gradle.interview.service.strategy.impl;

import cyf.gradle.interview.service.strategy.Order;
import cyf.gradle.interview.service.strategy.OrderType;
import cyf.gradle.interview.service.strategy.Type;
import org.springframework.stereotype.Service;

/**
 * @author Cheng Yufei
 * @create 2019-04-28 15:46
 **/
@Service
@Type(OrderType.SALES_PROMOTION)
public class SalesPromotionOrder implements Order {
    @Override
    public String handle() {
        return "促销订单处理";
    }
}
