package cyf.gradle.interview.service.strategy.impl;

import cyf.gradle.interview.service.strategy.Order;
import cyf.gradle.interview.service.strategy.OrderType;
import cyf.gradle.interview.service.strategy.Type;
import org.springframework.stereotype.Service;

/**
 * @author Cheng Yufei
 * @create 2019-04-28 15:47
 **/
@Service
@Type(OrderType.GROUP_BUY)
public class GroupBuyOrder implements Order {
    @Override
    public String handle() {
        return "团购订单处理";
    }
}
