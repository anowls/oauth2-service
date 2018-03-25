package org.anowls.spring.oauth2.config;

import java.util.Properties;
import javax.sql.DataSource;

import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @author NiBo
 */
@Configuration
public class DataSourceConfiguration {

    private static final String MAPPER_LOCATION = "classpath:mapper/*Mapper.xml";
    private static final String MY_BATIS_CONFIG_LOCATION = "classpath:mybatis/mybatis-config.xml";

    @Bean(name = "dataSource")
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("dataSource") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MAPPER_LOCATION));
        sessionFactory.setConfigLocation(
                new PathMatchingResourcePatternResolver().getResource(MY_BATIS_CONFIG_LOCATION));
        VendorDatabaseIdProvider vendorDatabaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();
        properties.setProperty("Oracle", "Oracle");
        properties.setProperty("MySQL", "MySQL");
        vendorDatabaseIdProvider.setProperties(properties);
        sessionFactory.setDatabaseIdProvider(vendorDatabaseIdProvider);
        return sessionFactory.getObject();
    }

}
