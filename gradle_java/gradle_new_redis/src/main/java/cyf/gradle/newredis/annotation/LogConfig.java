package cyf.gradle.newredis.annotation;


import cyf.gradle.newredis.en.OperaType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 在程序运行时可以对注解进行读取，从而易于软件的测试
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LogConfig {


    OperaType opera();
}
