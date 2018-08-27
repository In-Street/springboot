package cyf.gradle.batch.configuration;

import cyf.gradle.dao.model.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.batch.api.BatchProperty;
import javax.sql.DataSource;

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
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;


    @Bean
    public JobRepositoryFactoryBean

    @Bean
    public Step stepOne() {
        return stepBuilderFactory.get("stepOne")
                .<User, User>chunk(10)
                .reader(new JdbcPagingItemReader<User>() {
                    {
                        setDataSource(primaryDataSource);
//                        setFetchSize(3);
                        setPageSize(3);
                    }
                })
                .processor((ItemProcessor<User, User>) process -> {
                    if (process.getId() > 8) {
                        process.setPwd(process.getUsername() + "_A");
                    }
                    return null;
                })
                .writer(new JdbcBatchItemWriter<User>() {
                    {
                        setDataSource(primaryDataSource);
                    }
                })
                .build();
    }

    @Bean(name = "jobOne")
    public Job job() {
        return jobBuilderFactory.get("jobOne")
                .flow(stepOne())
                .end().build();

    }


}
