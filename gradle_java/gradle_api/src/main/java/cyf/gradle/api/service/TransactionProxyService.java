package cyf.gradle.api.service;

import cyf.gradle.dao.mapper.Kerr2Mapper;
import cyf.gradle.dao.model.Kerr2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author Cheng Yufei
 * @create 2018-01-13 下午8:41
 *
 *   Spring事务管理是通过JDK动态代理的方式进行实现的（另一种是使用CGLib动态代理实现)
 *
 * 在 test1() 中，test3（）抛出异常没有捕获，相当于test1事务中抛出了异常，造成test1（）一起回滚，因为他们本质是同一个方法；
 * 在test4()中，test3（）抛出异常并进行了捕获，test4事务中没有抛出异常，test4（）和 test3（）同时在一个事务里边，所以他们都成功了；
 * 在test5() 中 ， 通过AopProxy上下文获取代理对象来执行 test3（），一个正常提交 成功插入数据库 ， test3（）回滚未提交；
 *
 * 不管你嵌套了多少层方法调用，只有代理对象proxy 直接调用的那一个方法才是真正的走代理的
 *
 **/
@Service
@Slf4j
public class TransactionProxyService {

    @Autowired
    private Kerr2Mapper kerr2Mapper;


    /**
     * 调用test3() ，抛出异常后 1，3 都未提交，插入数据库失败
     */
    @Transactional
    public void test1() {
        Kerr2 kerr2 = new Kerr2();
        kerr2.setTitle("事务测试回滚-1");
        kerr2.setPublishtime(new Date());
        kerr2Mapper.insertSelective(kerr2);
        test3();
    }





    /**
       将 3 的异常 try catch 捕获后
     * 事务测试回滚-1,3 都提交成功 插入数据库
     * 只有 test4 是代理对象执行，test3（）不是代理对象执行的
     *
     */
    @Transactional
    public void test4() {
        Kerr2 kerr2 = new Kerr2();
        kerr2.setTitle("事务测试回滚-1");
        kerr2.setPublishtime(new Date());
        kerr2Mapper.insertSelective(kerr2);
        try {
            test3();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    /**
     *    通过AopProxy上下文获取代理对象
     *    事务测试回滚-1 正常提交 成功插入数据库 ，只有 test3（）回滚未提交
     */
    @Transactional
    public void test5() {
        Kerr2 kerr2 = new Kerr2();
        kerr2.setTitle("事务测试回滚-1");
        kerr2.setPublishtime(new Date());
        kerr2Mapper.insertSelective(kerr2);
        try {

            ((TransactionProxyService)AopContext.currentProxy()).test3();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    /**
     * 未提交成功
     */
    @Transactional
    public void test2()  {
        Kerr2 kerr2 = new Kerr2();
        kerr2.setTitle("事务测试回滚-2");
        kerr2.setPublishtime(new Date());
        kerr2Mapper.insertSelective(kerr2);
        throw new RuntimeException("回滚-2 exception");
    }


    /**
     * Propagation.REQUIRES_NEW的含义 ：如果当前存在事务，则挂起当前事务并且开启一个新事物继续执行，
     *                                                      新事物执行完毕之后，然后在缓刑之前挂起的事务，如果当前不存在事务的话，则开启一个新事物。
     *
     *    如果不添加此参数 则在调用 test5（）时候异常：
     *      org.springframework.transaction.UnexpectedRollbackException: Transaction rolled back because it has been marked as rollback-only
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void test3()  {
        Kerr2 kerr2 = new Kerr2();
        kerr2.setTitle("事务测试回滚-3");
        kerr2.setPublishtime(new Date());
        kerr2Mapper.insertSelective(kerr2);
        throw new RuntimeException("回滚-3 exception");
    }
}
