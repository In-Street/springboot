package cyf.gradle.api.service;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import cyf.gradle.dao.model.Kerr2;
import cyf.gradle.dao.model.Region;
import cyf.gradle.dao.model.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 事件监听，当EventBus 发布时，相应的监听器进行业务处理
 *
 * @author Cheng Yufei
 * @create 2018-06-17 上午10:33
 **/
@Component
@Slf4j
public class EventListener<T, K> {

    private AtomicInteger count = new AtomicInteger(1);

    /**
     * 同步总线测试
     * @param t
     * @return
     */
    //如果方法参数不是泛型，则post后订阅哪个方法按形参决定
    @Subscribe
    /**
     允许并发执行:jmeter测试
     10个线程post，触发监听时： 有此注解，每个请求线程结束耗时在601ms左右，能并发处理
     没有此注解，请求线程的结束耗时是累加的，一个线程处于阻塞，必须等待上个线程处理完才能继续，不能并发处理，耗时从 600ms - 5000+ms 递增
     */
    @AllowConcurrentEvents
    @SneakyThrows(value = {InterruptedException.class})
    public T eventBusListener(T t) {

        int increment = count.getAndIncrement();
        //@AllowConcurrentEvents 测试，进行sleep
        TimeUnit.MILLISECONDS.sleep(600);
        if (t instanceof User) {
            User user = (User) t;
            ((User) t).setUsername("Tay_" + increment);
            log.debug("eventBus事件监听器处理：{} - {}", user.toString(), Thread.currentThread().getName());
        }
        return t;
    }

    /**
     * AsyncEventBus: 异步总线测试
     * @param k
     * @return
     */
    @Subscribe
    @SneakyThrows(value = {InterruptedException.class})
    public K asyncEventBusListener(K k) {

        int increment = count.getAndIncrement();
        if (k instanceof Kerr2) {
            TimeUnit.MILLISECONDS.sleep(600);
            Kerr2 kerr2 = (Kerr2) k;
            ((Kerr2) k).setTitle("swi_" + increment);
            log.debug("asyncEventBus事件监听器处理：{} - {}", kerr2.toString(), Thread.currentThread().getName());
        }
        return k;
    }

//---------------------------------参数类型有继承关系,post子类后，子类和父类的 Subscribe 方法都会处理这条消息---------------------------------

    @Subscribe
    public Kerr2 handleKer2(Kerr2 kerr2) {
        kerr2.setTitle("北京市");
        log.info(">>>>kerr2 消息处理");
        return kerr2;
    }

   /* @Subscribe
    public Region handleRegion(Region region) {
        region.name("上海市");
        log.info(">>>>Region 消息处理");
        return region;
    }*/

//--------------------------------- 异常处理   ---------------------------------

    /**
     * eventbus 默认不会对异常处理，只是打印异常信息，异常语句之后的代码不执行，之前的代码设置还有效，然后return ， name属性的设置仍有效
     * @param region
     * @return
     */
    //@Subscribe
    public Region handleRegionException(Region region) {
        region.name("北京市");
        int i = 1 / 0;
        log.info(">>>>Region 消息处理");
        return region;
    }

}
