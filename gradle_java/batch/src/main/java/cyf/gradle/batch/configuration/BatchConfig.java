package cyf.gradle.batch.configuration;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Cheng Yufei
 * @create 2018-08-27 16:12
 *
 * ItemReader：读操作抽象接口；
 * ItemProcessor：处理逻辑抽象接口；
 * ItemWriter：写操作抽象接口；
 * Step：组成一个Job的各个步骤；
 * Job：可被多次执行的任务，每次执行返回一个JobInstance。
 **/
@Configuration
public class BatchConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Step stepOne() {
        stepBuilderFactory.get("stepOne").chunk(10)
                .reader(new JdbcPagingItemReader<>().)
    }

}
