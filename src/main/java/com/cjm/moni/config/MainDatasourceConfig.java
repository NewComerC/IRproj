package com.cjm.moni.config;

import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.plugins.SqlExplainInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.cjm.moni.common.status.BaseLogicDeleteStatus;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.cjm.moni.mapper"}, sqlSessionFactoryRef = "mainSqlSessionFactory")
public class MainDatasourceConfig {
    @Primary
    @Bean(name = "mainDataSource")
    @ConfigurationProperties(prefix = "datasource.main")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "mainTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("mainDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "mainSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("mainDataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:/com/cjm/moni/mapper/*Mapper.xml"));


        factoryBean.setTypeAliasesPackage("com.cjm.moni.entity");

        factoryBean.setPlugins(new Interceptor[] { paginationInterceptor(), optimisticLockerInterceptor() ,sqlExplainInterceptor()});
//        ,performanceInterceptor()
        factoryBean.setGlobalConfig(this.globalConfiguration());

        return factoryBean.getObject();
    }


    /**
     * 乐观锁插件
     *
     */

    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * sql性能分析拦截器
     *
     */

    public SqlExplainInterceptor sqlExplainInterceptor() {
        return new SqlExplainInterceptor();
    }

    /**
     * 分页插件
     */

    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * SQL执行效率插件
     */

    // @Profile({ "dev", "debug" }) // 设置 dev test 环境开启
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor intrceptor = new PerformanceInterceptor();
        intrceptor.setMaxTime(20000);
        intrceptor.setFormat(true);
        return intrceptor;
    }

    /**
     * 设置逻辑删除
     *
     * @return
     */
    private GlobalConfiguration globalConfiguration() {
        GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
        conf.setLogicDeleteValue(BaseLogicDeleteStatus.LogicDelete.getValue());
        conf.setLogicNotDeleteValue(BaseLogicDeleteStatus.LogicNotDelete.getValue());
        return conf;
    }
}
