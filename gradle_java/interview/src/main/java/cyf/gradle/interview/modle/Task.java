package cyf.gradle.interview.modle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RecursiveTask;

/**
 * @author Cheng Yufei
 * @create 2019-01-03 16:34
 **/
@AllArgsConstructor
@Builder
@Slf4j
public class Task extends RecursiveTask {

    private Integer start;
    private Integer end;

    public static final int threshold = 10;

    @Override
    protected Integer compute() {
        //任务分割数
        int hold = (end - start) / threshold;
        int sum = 0;
        //最小直接执行
        if (hold <= 1) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            int mid = (end - start) / hold;
            int e = end;
            for (int i = 0; i < hold; i++) {
                log.info("Thread:{},第 {} 个 Task", Thread.currentThread().getName(), i);
                end = start + mid > e ? e : start + mid;
                Task task = new Task(start, end);
                sum += (Integer) task.fork().join();
                log.info("start:{} - end:{}", start, end);
                start = end + 1;
            }
        }
        return sum;
    }
}
