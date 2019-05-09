package cyf.gradle.api.configuration;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * @author Cheng Yufei
 * @create 2019-05-07 16:14
 *
 * 使用线程池线程， requestId 保持和请求线程一致，用于排查一次请求的所有记录
 *
 **/
public class MdcTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> contextMap = MDC.getCopyOfContextMap();
        return () -> {
            try {
                MDC.setContextMap(contextMap);
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
