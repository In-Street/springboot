package cyf.gradle.dao.mongodb;

import lombok.Data;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * mongodb
 *
 * @author Cheng Yufei
 * @create 2017-08-07 18:34
 **/
@Data
@Component
@ConfigurationProperties(prefix = "mongodb")
public class MultipleMongoProperties {




        private MongoProperties primary = new MongoProperties();
        //private MongoProperties secondary = new MongoProperties();
}



