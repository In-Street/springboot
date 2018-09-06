package cyf.gradle.batch.configuration;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import cyf.gradle.dao.model.ClubUserDailyStat;
import cyf.gradle.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;

/**
 * @author Cheng Yufei
 * @create 2018-08-27 16:12
 */
@Configuration
@Slf4j
/**
* 使得StepBuilderFactory等基础类可以注入
*/
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private DataSource primaryDataSource;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private SqlSessionFactory primarySqlSessionFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Step stepOne() throws Exception {

        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id,username,pwd");
        provider.setFromClause("t_user");
        Map<String, Order> sortKeyMap = Maps.newHashMapWithExpectedSize(1);
        sortKeyMap.put("id", Order.ASCENDING);
        provider.setSortKeys(sortKeyMap);

        BeanPropertyRowMapper<User> beanPropertyRowMapper = BeanPropertyRowMapper.newInstance(User.class);
        JdbcPagingItemReader<User> itemReader = new JdbcPagingItemReader<>();
        itemReader.setDataSource(primaryDataSource);
        itemReader.setQueryProvider(provider);
        itemReader.setRowMapper(beanPropertyRowMapper);
        itemReader.setPageSize(5);
        itemReader.afterPropertiesSet();

        BeanPropertyItemSqlParameterSourceProvider sourceProvider = new BeanPropertyItemSqlParameterSourceProvider();
        JdbcBatchItemWriter itemWriter = new JdbcBatchItemWriter();
        itemWriter.setDataSource(primaryDataSource);
        itemWriter.setSql("update t_user set pwd = :pwd where id = :id");
        itemWriter.setItemSqlParameterSourceProvider(sourceProvider);
        itemWriter.afterPropertiesSet();


        Function<User, User> processorFunction = process -> {
            if (process.getId() < 7) {
                process.setPwd(process.getUsername() + "_T");
            }
            return process;
        };
        /*.tasklet()*/
        /*.faultTolerant().skip()*/

        return stepBuilderFactory.get("stepOne")

                // 10条数据后交由processor处理（commit-interval的间隔值，减少提交频次，降低资源使用率）,决定CommitCount
                .<User, User>chunk(10)
                .reader(itemReader)
                .processor(processorFunction)
                .writer(itemWriter)
                /* .listener(new ItemWriteListener() {
                     @Override
                     public void beforeWrite(List items) {
                         items.stream().forEach(u -> {
                             System.out.println(((User) u).getId() + "_____" + Thread.currentThread().getName());
                         });
                     }

                     @Override
                     public void afterWrite(List items) {
                     }

                     @Override
                     public void onWriteError(Exception exception, List items) {
                     }
                 })*/
                .build();
    }

    @Bean
    public Step stepTwo() throws Exception {

        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id,username,pwd");
        provider.setFromClause("t_user");
        Map<String, Order> sortKeyMap = Maps.newHashMapWithExpectedSize(1);
        sortKeyMap.put("id", Order.ASCENDING);
        provider.setSortKeys(sortKeyMap);
        provider.setWhereClause("id >= ?");

        BeanPropertyRowMapper<User> beanPropertyRowMapper = BeanPropertyRowMapper.newInstance(User.class);
        JdbcPagingItemReader<User> itemReader = new JdbcPagingItemReader<>();
        itemReader.setDataSource(primaryDataSource);
        itemReader.setQueryProvider(provider);
        itemReader.setRowMapper(beanPropertyRowMapper);
        itemReader.setPageSize(5);
        //provider where条件的传参
        Map ParameterValuesMap = new HashMap(1);
        ParameterValuesMap.put("id", 7);
        itemReader.setParameterValues(ParameterValuesMap);
        itemReader.afterPropertiesSet();

        BeanPropertyItemSqlParameterSourceProvider sourceProvider = new BeanPropertyItemSqlParameterSourceProvider();
        JdbcBatchItemWriter itemWriter = new JdbcBatchItemWriter();
        itemWriter.setDataSource(primaryDataSource);
        itemWriter.setSql("update t_user set pwd = :pwd where id = :id ");
        itemWriter.setItemSqlParameterSourceProvider(sourceProvider);
        itemWriter.afterPropertiesSet();


        Function<User, User> processorFunction = process -> {
            process.setPwd(process.getUsername() + "_S");
            return process;
        };
        TaskletStep step = stepBuilderFactory.get("stepTwo")

                .<User, User>chunk(10)
                .reader(itemReader)
                .processor(processorFunction)
                .writer(itemWriter)
                .listener(new ItemWriteListener() {
                    @Override
                    public void beforeWrite(List items) {
                        items.stream().forEach(u -> {
                            System.out.println(((User) u).getId() + "_____" + Thread.currentThread().getName());
                        });
                    }

                    @Override
                    public void afterWrite(List items) {

                    }

                    @Override
                    public void onWriteError(Exception exception, List items) {

                    }
                })
                //异步线程并发执行
                .taskExecutor(taskExecutor())
                .build();

       /* Tasklet tasklet = new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                int readCount = chunkContext.getStepContext().getStepExecution().getReadCount();
                System.out.println(readCount);
                return RepeatStatus.FINISHED;
            }
        };
        step.setTasklet(tasklet);*/
        return step;
    }

    @Bean
    public Step stepThree() throws Exception {

        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        provider.setSelectClause("id,user_id,user_type,date,recommend_num,be_liked_num,like_num,dislike_num,location,active_time,create_time,modify_time");
        provider.setFromClause("club_user_daily_stat_history");
        Map<String, Order> sortKeyMap = Maps.newHashMapWithExpectedSize(1);
        sortKeyMap.put("id", Order.ASCENDING);
        provider.setSortKeys(sortKeyMap);
//        provider.setWhereClause("date = ?");
        provider.setWhereClause("id < ?");

        BeanPropertyRowMapper<ClubUserDailyStat> beanPropertyRowMapper = BeanPropertyRowMapper.newInstance(ClubUserDailyStat.class);
        JdbcPagingItemReader<ClubUserDailyStat> itemReader = new JdbcPagingItemReader<>();
        itemReader.setDataSource(primaryDataSource);
        itemReader.setQueryProvider(provider);
        itemReader.setRowMapper(beanPropertyRowMapper);
        itemReader.setPageSize(1000);
        //provider where条件的传参
        Map ParameterValuesMap = new HashMap(1);
//        ParameterValuesMap.put("date", "2018-06-21");
        ParameterValuesMap.put("id", 6587);
        itemReader.setParameterValues(ParameterValuesMap);
        itemReader.afterPropertiesSet();

        BeanPropertyItemSqlParameterSourceProvider sourceProvider = new BeanPropertyItemSqlParameterSourceProvider();
        JdbcBatchItemWriter itemWriter = new JdbcBatchItemWriter();
        itemWriter.setDataSource(primaryDataSource);
        itemWriter.setSql("insert into club_user_daily_stat (id,user_id,user_type,date,recommend_num,be_liked_num,like_num,dislike_num,location,active_time,create_time,modify_time)values (:id,:userId,:userType,:date,:recommendNum,:beLikedNum,:likeNum,:dislikeNum,:location,:activeTime,:createTime,:modifyTime) ");
        itemWriter.setItemSqlParameterSourceProvider(sourceProvider);
        itemWriter.afterPropertiesSet();


        TaskletStep step = stepBuilderFactory.get("stepThree")
                .<ClubUserDailyStat, ClubUserDailyStat>chunk(500)
                .reader(itemReader)
                .writer(itemWriter)
                //异步线程并发执行
                .taskExecutor(taskExecutor())
                .build();
        return step;
    }

    @Bean
    public Step stepFour() throws Exception {

        Map readParameter = new HashMap(1);
        readParameter.put("endId", 6587);

        MyBatisPagingItemReader<ClubUserDailyStat> itemReader = new MyBatisPagingItemReader<>();
        itemReader.setSqlSessionFactory(primarySqlSessionFactory);
        itemReader.setQueryId("cyf.gradle.dao.mapper.UDUserDailyMapper.selectByIDRange");
        itemReader.setParameterValues(readParameter);
        // xml中添加 limit #{_skiprows}, #{_pagesize}俩个参数，MyBatisPagingItemReader类中doReadPage()方法
        itemReader.setPageSize(1000);
        itemReader.afterPropertiesSet();

        MyBatisBatchItemWriter<ClubUserDailyStat> itemWriter = new MyBatisBatchItemWriter<>();
        itemWriter.setSqlSessionFactory(primarySqlSessionFactory);
        //SqlSessionTemplate 需开启BATCH模式，默认是SIMPLE
        SqlSessionTemplate primarySqlSessionTemplate = new SqlSessionTemplate(primarySqlSessionFactory, ExecutorType.BATCH);
       /*  修改执行器后获取mapper类执行方法
       UDUserDailyMapper mapper = primarySqlSessionTemplate.getMapper(UDUserDailyMapper.class);
        mapper.insert()*/

        itemWriter.setSqlSessionTemplate(primarySqlSessionTemplate);
        itemWriter.setStatementId("cyf.gradle.dao.mapper.UDUserDailyMapper.insert");
        itemWriter.setAssertUpdates(false);
        itemWriter.afterPropertiesSet();

        TaskletStep step = stepBuilderFactory.get("stepFour")
                .<ClubUserDailyStat, ClubUserDailyStat>chunk(500)
                /*.faultTolerant()
                .skip(RuntimeException.class)
                .skipLimit(1)
                .retry(RuntimeException.class)
                .retryLimit(1)*/
                .reader(itemReader)
                .writer(itemWriter)
                .listener(new ItemWriteListener<ClubUserDailyStat>() {
                    @Override
                    public void beforeWrite(List<? extends ClubUserDailyStat> items) {
                        //抛异常后，因为是开启异步线程并发执行，事务无法传递所以有的数据仍会插入； 异常会导致整个job fail，剩余数据无法插入。可使用faultTolerant()跳过
                        if (items.stream().anyMatch(c->((ClubUserDailyStat) c).getId()==6585)) {
                            throw new RuntimeException("抛异常");
                        }
                    }

                    @Override
                    public void afterWrite(List<? extends ClubUserDailyStat> items) {
                        primarySqlSessionTemplate.clearCache();
                    }

                    @Override
                    public void onWriteError(Exception exception, List<? extends ClubUserDailyStat> items) {

                    }
                })
                //异步线程并发执行,抛异常影响事务
                .taskExecutor(taskExecutor())
                .build();
        return step;

    }

    @Bean
    public Job jobOne() throws Exception {
        Job jobOne = jobBuilderFactory.get("jobOne")
                /* .listener(new JobExecutionListener() {
                     Stopwatch stopwatch = null;

                     @Override
                     public void beforeJob(JobExecution jobExecution) {
                         stopwatch = Stopwatch.createStarted();
                     }

                     @Override
                     public void afterJob(JobExecution jobExecution) {
                         log.info("jobOne耗时：{} s", stopwatch.elapsed(TimeUnit.SECONDS));
                         Collection<StepExecution> executions = jobExecution.getStepExecutions();
                         //每个Step 的读取总数及据chunkSize大小得出的提交次数
                         executions.stream().forEach(stepExecution -> log.debug("Step:{}，ReadCount：{}，CommitCount：{}", stepExecution.getStepName(),stepExecution.getReadCount(), stepExecution.getCommitCount()));
                     }
                 })*/
                .start(stepFour())
//                .next(stepTwo())
                .build();
        return jobOne;
    }


    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setKeepAliveSeconds(60);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(50);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("batch_%d").build());
        executor.initialize();
        return executor;
    }


}
