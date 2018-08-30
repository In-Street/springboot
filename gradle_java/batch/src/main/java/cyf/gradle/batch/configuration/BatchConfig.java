package cyf.gradle.batch.configuration;

import com.google.common.collect.Maps;
import cyf.gradle.dao.model.User;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Cheng Yufei
 * @create 2018-08-27 16:12
 * <p>
 * ItemReader：读操作抽象接口；
 * ItemProcessor：处理逻辑抽象接口；
 * ItemWriter：写操作抽象接口；
 * Step：组成一个Job的各个步骤；
 * Job：可被多次执行的任务，每次执行返回一个JobInstance。
 **/
@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private DataSource primaryDataSource;
    @Autowired
    private SqlSessionFactory primarySqlSessionFactory;

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

        JdbcPagingItemReader<User> itemReader = new JdbcPagingItemReader<>();
        itemReader.setDataSource(primaryDataSource);
        itemReader.setQueryProvider(provider);
        itemReader.setRowMapper(BeanPropertyRowMapper.newInstance(User.class));
        itemReader.afterPropertiesSet();

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


        JdbcBatchItemWriter itemWriter = new JdbcBatchItemWriter();
        itemWriter.setDataSource(primaryDataSource);
        itemWriter.setSql("update t_user set pwd = :pwd where id = :id");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
        itemWriter.afterPropertiesSet();


        Function<User, User> processorFunction = process -> {
            if (process.getId() > 8) {
                process.setPwd(process.getUsername() + "_B");
            }
            return process;
        };


        return stepBuilderFactory.get("stepOne")
                .<User, User>chunk(10)
                .reader(itemReader)
                .processor(processorFunction)
                .writer(itemWriter)
                .build();
    }

    @Bean(name = "jobOne")
    public Job job() throws Exception {
        return jobBuilderFactory.get("jobOne")
                .flow(stepOne())
                .end().build();

    }


}
