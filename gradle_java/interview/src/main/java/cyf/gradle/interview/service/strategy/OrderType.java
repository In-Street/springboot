package cyf.gradle.interview.service.strategy;

import lombok.AllArgsConstructor;

import java.util.Objects;

/**
 * @author Cheng Yufei
 * @create 2019-04-28 15:49
 **/
@AllArgsConstructor
public enum OrderType {
    //
    COMMON(1,"普通订单"),
    SALES_PROMOTION(2,"促销订单"),
    GROUP_BUY(3,"团购订单");

    private Integer code;
    private String name;

    public  static OrderType get(Integer code) {
        OrderType[] values = OrderType.values();
        for (OrderType value : values) {
            if (Objects.equals(value.code,code)) {
                return value;
            }
        }
        return null;
    }
}
