package cyf.gradle.dao.mongodb;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * 第一库的封装
 *
 * @author Cheng Yufei
 * @create 2017-08-07 18:42
 **/
@Configuration
@EnableMongoRepositories(
        mongoTemplateRef = PrimaryMongoConfig.MONGO_TEMPLATE)
public class PrimaryMongoConfig {

    protected static final String MONGO_TEMPLATE = "primaryMongoTemplate";
}
