package cyf.gradle.batch.configuration;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import cyf.gradle.dao.model.ClubUserDailyStat;
import cyf.gradle.dao.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author Cheng Yufei
 * @create 2018-08-27 16:12
 * <p>
 * <p>
 * ItemReader：读操作抽象接口, 填充对象给ItemProcessor使用
 * ItemProcessor：处理逻辑抽象接口，返回对象给ItemWriter使用
 * ItemWriter：写操作抽象接口；
 * Step：组成一个Job的各个步骤；
 * Job：可被多次执行的任务，每次执行返回一个JobInstance,Batch操作的基础执行单元。
 * Job Launcher：启动Job
 * Tasklet(小任务): Step的一个事务过程，包括重复执行，同步、异步策略
 * chunk：给定数量的Item集合
 * 一个job可以包含0到多个step;一个step可以包含0到多个tasklet;一个tasklet可以包含0到多个chunk
 * <p>
 * ------------------------------------------------------------------------------------------------------------------------------------
 * <p>
 * 1. 组装Step异常跳过：
 * 通过调用FaultTolerantStepBuilder的skip来指定跳过的异常，因为在ItemProcessor中抛出任意异常都会将Job状态变为Failed，
 * 例如我们代码中指定了MoneyNotEnoughException，那么如果抛出该异常，任务会跳过该数据继续执行。
 * <p>
 * 2.   组装Job条件决策：
 * 我们可以实现自己的决策逻辑——实现JobExecutionDecider接口，通过判断程序是正常结束的且跳过了非零条数据（说明有余额不足），
 * 返回自定义的FlowExecutionStatus，在配置任务是通过on(String)来进行条件决策是执行下一个Step还是终结Job。
 * eg:
 * .next((jobExecution, stepExecution) -> {
 * if (stepExecution.getExitStatus().equals(ExitStatus.COMPLETED) &&
 * stepExecution.getSkipCount() > 0) {
 * return new FlowExecutionStatus("NOTICE USER");
 * } else {
 * return new FlowExecutionStatus(stepExecution.getExitStatus().toString());
 * }
 * })
 * .on("COMPLETED").end()
 * .on("NOTICE USER").to(noticeStep)
 * <p>
 * 3.   监听器：
 * 可以给Job、Step指定相应的监听器，来记录日志信息或其他用途，本例通过给Job添加JobExecutionListener，来记录整个任务的执行时间。
 * <p>
 * ------------------------------------------------------------------------------------------------------------------------------------
 * <p>
 * 一个健壮的Job通常需要具备如下的几个特性：
 * 1. 容错性
 * 在Job执行期间非致命的异常，Job执行框架应能够进行有效的容错处理，而不是让整个Job执行失败；通常只有致命的、导致业务不正确的异常才可以终止Job的执行。
 * 2. 可追踪性
 * Job执行期间任何发生错误的地方都需要进行有效的记录，方便后期对错误点进行有效的处理。例如在Job执行期间任何被忽略处理的记录行需要被有效的记录下来，应用程序维护人员可以针对被忽略的记录后续做有效的处理。
 * 3. 可重启性
 * Job执行期间如果因为异常导致失败，应该能够在失败的点重新启动Job；而不是从头开始重新执行Job。
 * 【
 * skip：跳过异常，让Processor能够顺利的处理其余的记录行。
 * retry： 重试
 * restart： 在最后执行失败的地方重启job，而不是从头开始执行，提高Job执行效率。
 * 】
 * <p>
 * ------------------------------------------------------------------------------------------------------------------------------------
 * 框架扩展模式：
 * Multithreaded Step 多线程执行一个Step;
 * Parallel Step 通过多线程并行执行多个Step;
 * Remote Chunking 在远端节点上执行分布式Chunk操作;
 * Partitioning Step 对数据进行分区，并分开执行;
 **/
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
        provider.setWhereClause("id > ?");

        BeanPropertyRowMapper<ClubUserDailyStat> beanPropertyRowMapper = BeanPropertyRowMapper.newInstance(ClubUserDailyStat.class);
        JdbcPagingItemReader<ClubUserDailyStat> itemReader = new JdbcPagingItemReader<>();
        itemReader.setDataSource(primaryDataSource);
        itemReader.setQueryProvider(provider);
        itemReader.setRowMapper(beanPropertyRowMapper);
        itemReader.setPageSize(5);
        //provider where条件的传参
        Map ParameterValuesMap = new HashMap(1);
//        ParameterValuesMap.put("date", "2018-06-23");
        ParameterValuesMap.put("id", "6586");
        itemReader.setParameterValues(ParameterValuesMap);
        itemReader.afterPropertiesSet();

        BeanPropertyItemSqlParameterSourceProvider sourceProvider = new BeanPropertyItemSqlParameterSourceProvider();
        JdbcBatchItemWriter itemWriter = new JdbcBatchItemWriter();
        itemWriter.setDataSource(primaryDataSource);
        itemWriter.setSql("insert into club_user_daily_stat (id,user_id,user_type,date,recommend_num,be_liked_num,like_num,dislike_num,location,active_time,create_time,modify_time)values (:id,:userId,:userType,:date,:recommendNum,:beLikedNum,:likeNum,:dislikeNum,:location,:activeTime,:createTime,:modifyTime) ");
        itemWriter.setItemSqlParameterSourceProvider(sourceProvider);
        itemWriter.afterPropertiesSet();


        /*Function<User, User> processorFunction = process -> {
            process.setPwd(process.getUsername() + "_S");
            return process;
        };*/
        TaskletStep step = stepBuilderFactory.get("stepThree")
                .<ClubUserDailyStat, ClubUserDailyStat>chunk(10)
                .reader(itemReader)
//                .processor(processorFunction)
                .writer(itemWriter)
                //异步线程并发执行
//                .taskExecutor(taskExecutor())
                .build();
        return step;
    }

    @Bean
    public Job jobOne() throws Exception {
        Job jobOne = jobBuilderFactory.get("jobOne")
                .listener(new JobExecutionListener() {
                    Stopwatch stopwatch = null;

                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        stopwatch = Stopwatch.createStarted();
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        log.info("jobOne耗时：{} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
                        Collection<StepExecution> executions = jobExecution.getStepExecutions();
                        //每个Step 的读取总数及据chunkSize大小得出的提交次数
                        executions.stream().forEach(stepExecution -> log.debug("Step:{}，ReadCount：{}，CommitCount：{}", stepExecution.getStepName(),stepExecution.getReadCount(), stepExecution.getCommitCount()));
                    }
                })
                .start(stepThree())
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
        executor.setQueueCapacity(20);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("batch_%d").build());
        executor.initialize();
        return executor;
    }


/* MyBatisBatchItemWriter<User> itemWriter = new MyBatisBatchItemWriter<>();
        itemWriter.setSqlSessionFactory(primarySqlSessionFactory);

        //SqlSessionTemplate 需开启BATCH模式，默认是SIMPLE
        SqlSessionTemplate primarySqlSessionTemplate = new SqlSessionTemplate(primarySqlSessionFactory,ExecutorType.BATCH);
        itemWriter.setSqlSessionTemplate(primarySqlSessionTemplate);
        itemWriter.setStatementId("cyf.gradle.dao.mapper.UserMapper.updateByPrimaryKeySelective");
        itemWriter.afterPropertiesSet();*/

       /* Map<String, Object> map = new HashMap<>();
        map.put("");
        map.put("");

        PreparedStatement ps = new HikariProxyPreparedStatement();
        ColumnMapItemPreparedStatementSetter statementSetter = new ColumnMapItemPreparedStatementSetter();
        statementSetter.setValues();*/
}
