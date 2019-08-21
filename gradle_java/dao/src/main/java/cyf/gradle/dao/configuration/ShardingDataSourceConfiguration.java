package cyf.gradle.dao.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author Cheng Yufei
 * @create 2019-08-16 11:25
 **/
//@Configuration
@MapperScan(basePackages ="cyf.gradle.dao.mapper",sqlSessionFactoryRef = "sqlSessionFactory")
@Slf4j
public class ShardingDataSourceConfiguration {

   /* @Bean
    @ConfigurationProperties(prefix ="spring.shardingsphere.datasource.master" )
    public DataSource masterDataSource() {
        DataSourceUtil.getDataSource()
    }
*/

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mapper/*.xml"));
        sessionFactory.setTypeAliasesPackage("cyf.gradle.dao.model");
        log.info("-------------------- sqlSessionFactory init ---------------------");
        return sessionFactory.getObject();
    }
}
