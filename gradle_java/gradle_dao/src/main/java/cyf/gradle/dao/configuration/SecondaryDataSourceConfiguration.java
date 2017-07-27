package cyf.gradle.dao.configuration;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 数据源配置2</><br/>
 * Date: 2017/3/23<br/>
 *
 * @since JDK8
 */

//@Profile({"admin-dev","admin-test","admin-prod"})
//@Configuration
//@EnableTransactionManagement
//@MapperScan(basePackages="com.daishumovie.dao.mapper.worthyvideoadmin",sqlSessionFactoryRef = "secondarySqlSessionFactory")
@Slf4j
public class SecondaryDataSourceConfiguration {

  //  @Bean(name="secondaryDataSource", destroyMethod = "close")
 //   @ConfigurationProperties( prefix="hikari.datasource.secondary")
    public DataSource secondaryDataSource() {
        log.info("-------------------- secondaryDataSource init ---------------------");
        return new HikariDataSource();

    }   

   // @Bean(name = "secondaryTransactionManager")
    public PlatformTransactionManager secondaryTransactionManager(@Qualifier("secondaryDataSource") DataSource secondaryDataSource) {
        log.info("-------------------- secondaryTransactionManager init ---------------------");
    	return new DataSourceTransactionManager(secondaryDataSource);
    }

  //  @Bean(name = "secondarySqlSessionFactory")
    public SqlSessionFactory secondarySqlSessionFactory(@Qualifier("secondaryDataSource") DataSource secondaryDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(secondaryDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath*:mapper/worthyvideoadmin/**/*.xml"));
        sessionFactory.setTypeAliasesPackage("com.daishumovie.dao.model");
        log.info("-------------------- secondarySqlSessionFactory init ---------------------"); 
        return sessionFactory.getObject();
    }

  //  @Bean(name="secondarySqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("secondarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        log.info("-------------------- secondarySqlSessionTemplate init ---------------------");
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}