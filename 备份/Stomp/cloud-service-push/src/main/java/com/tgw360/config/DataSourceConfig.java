package com.tgw360.config;

/**
 * Created by 危宇 on 2018/1/15 13:42
 */

//import com.alibaba.druid.pool.DruidDataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//
//import javax.sql.DataSource;

/**
 * 业务数据源
 */
//@Configuration
//@MapperScan(basePackages="com.tgw360.mapper")
public class DataSourceConfig {


//    @Bean(name = "DataSource")
//    @ConfigurationProperties(prefix="spring.datasource.master")
//    @Primary
//    public DataSource DataSource() {
//        DruidDataSource dataSource = new DruidDataSource();
//
//        return dataSource;
//
//    }
//
//    @Bean(name = "SqlSessionFactory")
//    @Primary
//    public SqlSessionFactory slaveSqlSessionFactoryBean(@Qualifier("DataSource") DataSource dataSource) throws Exception {
//
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSource);
//
//        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//
//        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mapper/*.xml"));
//
//        return sqlSessionFactoryBean.getObject();
//    }

}
