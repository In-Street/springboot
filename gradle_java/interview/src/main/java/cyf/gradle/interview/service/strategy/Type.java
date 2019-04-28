package cyf.gradle.interview.service.strategy;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Cheng Yufei
 * @create 2019-04-28 15:54
 **/
@Retention(RetentionPolicy.RUNTIME)
public @interface Type {

    OrderType value();

}
