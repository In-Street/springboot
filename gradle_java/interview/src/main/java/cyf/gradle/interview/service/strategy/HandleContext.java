package cyf.gradle.interview.service.strategy;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;

/**
 * @author Cheng Yufei
 * @create 2019-04-28 17:03
 **/
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class HandleContext {

   private HashMap<OrderType, Class> map;

    public Order getInstance(OrderType orderType) {
        Class beanClass = map.get(orderType);

        Order order = null;
        try {
             order = (Order) beanClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return order;
    }
}
