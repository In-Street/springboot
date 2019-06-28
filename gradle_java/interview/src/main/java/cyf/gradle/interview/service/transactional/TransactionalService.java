package cyf.gradle.interview.service.transactional;

import cyf.gradle.dao.mapper.UserMapper;
import cyf.gradle.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author Cheng Yufei
 * @create 2019-06-28 16:07
 * <p>
 * 事务传播方式
 **/
@Service
@Slf4j
public class TransactionalService {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private RegionService regionService;

    /**
     * required:默认模式
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addUser() throws Exception {
        User user = User.builder().username("1").pwd("1").build();
        userMapper.insert(user);

        // addUser 没有 @Transactional 情况，addUser插入成功，addUser_2失败
        //((TransactionalService)AopContext.currentProxy()).addUser_2();
        //addUser 没有 @Transactional 情况，捕获异常时，addUser插入成功，addUser_2失败
       /* try {
            ((TransactionalService) AopContext.currentProxy()).addUser_2();
        } catch (RuntimeException e) {

        }*/



        //同一个事务，两者都回滚,插入失败
       /* regionService.addRegion();
       //捕获异常时，两者都回滚，插入失败，rollback-only
        try {

            regionService.addRegion();
        } catch (Exception e) {

        }*/



       //两个事务，都插入失败
       //regionService.addRegionRequiresNew();
        //两个事务，addUser插入成功，addRegionRequiresNew回滚，插入失败
       /* try {

            regionService.addRegionRequiresNew();
        } catch (Exception e) {

        }*/


       //两个事务，两者都插入失败，回滚同一数据库连接事务
       //regionService.addRegionNested();
        //addRegionNested 内层事务 回滚到savepoint点，插入失败，addUser外部事务正常提交
        try {
            regionService.addRegionNested();

        } catch (Exception e) {

        }

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addUser_2() {
        User user = User.builder().username("2").pwd("2").build();
        userMapper.insert(user);
        throw new RuntimeException();
    }

}
