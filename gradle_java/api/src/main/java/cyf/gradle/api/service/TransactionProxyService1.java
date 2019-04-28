package cyf.gradle.api.service;

import cyf.gradle.dao.mapper.Kerr2Mapper;
import cyf.gradle.dao.mapper.UserMapper;
import cyf.gradle.dao.model.Kerr2;
import cyf.gradle.dao.model.User;
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

    @Autowired
    private UserMapper userMapper;

    @PostConstruct
    public void initProxy() {
        proxy = context.getBean(TransactionProxyService1.class);
    }

    /**
     * 一：test1 没有 @Transactional，将数据库操作全部放入test3（有@Transactional）时，3中即使有错误抛出，俩者也会插进数据库，此种做法事务无效【并不是由代理调用，所以事务无效】
     * 二：test1 有 @Transactional，直接调用 3，无论3有无@Transactional，3异常，俩者都未插数据，若在 1中捕获3异常，则数据插入，事务无效
     * 三：test1 有 @Transactional ，用代理调 3，
     * 1. 3 无@Transactional ，效果与二一样
     * 2. 3 有@Transactional及新启事务参数： 在1中未进行捕获，效果与二一样，在1中进行捕获，1 插入 3未插入
     * 四：@Transactional 本工程采用AOP代理类，private方法会使事务注解失效，只有目标方法由外部调用,事务才会生效；
     * 同一类中的其他没有@Transactional 注解的方法内部调用有@Transactional 注解的方法，有@Transactional 注解的方法的事务被忽略，不会发生回滚（自调用问题）
     */
    @Transactional(rollbackFor = Exception.class)
    public void test1() {
        Kerr2 kerr2 = new Kerr2();
        kerr2.setTitle("事务测试回滚-1");
        kerr2.setPublishtime(new Date());
        kerr2Mapper.insertSelective(kerr2);

//        proxy.test3();
        try {
            proxy.test3();
//            test3();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Propagation.REQUIRES_NEW的含义 ：如果当前存在事务，则挂起当前事务并且开启一个新事物继续执行，
     * 新事物执行完毕之后，然后在缓刑之前挂起的事务，如果当前不存在事务的话，则开启一个新事物。
     * <p>
     * 如果不添加此参数 则在调用 test5（）时候异常：
     * org.springframework.transaction.UnexpectedRollbackException: Transaction rolled back because it has been marked as rollback-only
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void test3() {
        Kerr2 kerr3 = new Kerr2();
        kerr3.setTitle("事务测试回滚-3");
        kerr3.setPublishtime(new Date());
        kerr2Mapper.insertSelective(kerr3);
        throw new RuntimeException("回滚-3 exception");
    }

    /**
     * 公号使用
     * @throws Exception
     */
    public void insert() throws Exception {
        //A
        User user = User.builder().id(1).username("Tay").build();
        userMapper.insert(user);
        //自调用事务失效(无论B是否开启新事务),A/B 都插入
        insertAnother();
        //A插入 B回滚(无论B是否开启新事务)
//        ((TransactionProxyService1) AopContext.currentProxy()).insertAnother();

        //A插入 B回滚(无论B是否开启新事务)
        /*try {
            ((TransactionProxyService1) AopContext.currentProxy()).insertAnother();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    @Transactional(rollbackFor = Exception.class)
    public void insertTwo() throws Exception {
        User user = User.builder().id(1).username("Tay").build();
        userMapper.insert(user);
        //A\B 都回滚，无论B是否开启新事务（自调用B事务是失效的,相当于A直接抛出异常）
        insertAnother();

        // 无论B没有挂起当前事务开启新事务，A/B都回滚, B抛出异常，相当于俩个方法都异常
//        ((TransactionProxyService1) AopContext.currentProxy()).insertAnother();

        //B没有开启新事务，A/B都回滚；B开启新事务，A插入B回滚
        /*try {
            ((TransactionProxyService1) AopContext.currentProxy()).insertAnother();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    @Transactional(rollbackFor = Exception.class/*,propagation = Propagation.REQUIRES_NEW*/)
    public void insertAnother() throws Exception {
        //B
        User user = User.builder().id(2).username("Lor").build();
        userMapper.insert(user);
        throw new Exception("插入失败");
    }
}
