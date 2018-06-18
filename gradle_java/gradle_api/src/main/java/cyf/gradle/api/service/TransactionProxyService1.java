package cyf.gradle.api.service;

import cyf.gradle.dao.mapper.Kerr2Mapper;
import cyf.gradle.dao.model.Kerr2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * @author Cheng Yufei
 * @create 2018-01-13 下午8:41
 * <p>
 * Spring事务管理是通过JDK动态代理的方式进行实现的（另一种是使用CGLib动态代理实现)
 * <p>
 * 在test1() 中 ， 通过ApplicationContext上下文获取代理对象来执行 test3（），一个正常提交 成功插入数据库 ， test3（）回滚未提交；
 **/
@Service
@Slf4j
public class TransactionProxyService1 {

    @Autowired
    private Kerr2Mapper kerr2Mapper;

    @Autowired
    private ApplicationContext context;

    private TransactionProxyService1 proxy;

    @PostConstruct
    public void initProxy() {
        proxy = context.getBean(TransactionProxyService1.class);
    }

    /**
     *
     */
    @Transactional(rollbackFor = Exception.class)
    public void test1() {
       /* Kerr2 kerr2 = new Kerr2();
        kerr2.setTitle("事务测试回滚-1");
        kerr2.setPublishtime(new Date());
        kerr2Mapper.insertSelective(kerr2);*/
        test3();
        /*try {
            proxy.test3();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }


    /**
     * Propagation.REQUIRES_NEW的含义 ：如果当前存在事务，则挂起当前事务并且开启一个新事物继续执行，
     * 新事物执行完毕之后，然后在缓刑之前挂起的事务，如果当前不存在事务的话，则开启一个新事物。
     * <p>
     * 如果不添加此参数 则在调用 test5（）时候异常：
     * org.springframework.transaction.UnexpectedRollbackException: Transaction rolled back because it has been marked as rollback-only
     */
//    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
//    @Transactional(rollbackFor = Exception.class)
    public void test3() {
        Kerr2 kerr2 = new Kerr2();
        kerr2.setTitle("事务测试回滚-1");
        kerr2.setPublishtime(new Date());
        kerr2Mapper.insertSelective(kerr2);

        Kerr2 kerr3 = new Kerr2();
        kerr3.setTitle("事务测试回滚-3");
        kerr3.setPublishtime(new Date());
        kerr2Mapper.insertSelective(kerr3);
        throw new RuntimeException("回滚-3 exception");
    }
}
