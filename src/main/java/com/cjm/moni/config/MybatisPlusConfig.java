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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

//@Configuration
@MapperScan("com.cjm.moni.mapper*")
public class MybatisPlusConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        // 扫描 com.cjm.boot.demo.mapper xml
        sqlSessionFactory.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*Mapper.xml"));
        // 扫描实体类
        sqlSessionFactory.setTypeAliasesPackage("com.cjm.moni.entity");

        sqlSessionFactory.setPlugins(new Interceptor[] { paginationInterceptor(), optimisticLockerInterceptor() });
        sqlSessionFactory.setGlobalConfig(this.globalConfiguration());
        return sqlSessionFactory.getObject();
    }


    /**
     * 乐观锁插件
     *
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * sql性能分析拦截器
     *
     */
    @Bean
    public SqlExplainInterceptor sqlExplainInterceptor() {
        return new SqlExplainInterceptor();
    }

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * SQL执行效率插件
     */
    @Bean
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
