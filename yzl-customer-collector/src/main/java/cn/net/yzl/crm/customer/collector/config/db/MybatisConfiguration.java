package cn.net.yzl.crm.customer.collector.config.db;

import com.github.pagehelper.PageHelper;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@AutoConfigureAfter(cn.net.yzl.crm.customer.collector.config.db.DataSourceConfiguration.class)
public class MybatisConfiguration {

	private static Logger log = LoggerFactory.getLogger(MybatisConfiguration.class);
	


	//XxxMapper.xml文件所在路径
//	  @Value("${mysql.datasource.mapperLocations}")
      @Value("#{'${mysql.datasource.mapperLocations}'.split(',')}")
      private String[] mapperLocations;

     //  加载全局的配置文件
      @Value("${mysql.datasource.configLocation}")
      private String configLocation;
      
	@Autowired
	@Qualifier("writeDataSource")
	private DataSource writeDataSource;

	
	
    @Bean(name="sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactorys() throws Exception {
        log.info("--------------------  sqlSessionFactory init ---------------------");
        try {
            SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
       //     sessionFactoryBean.setDataSource(roundRobinDataSouce);
            sessionFactoryBean.setDataSource(writeDataSource);
            
            // 读取配置 
            sessionFactoryBean.setTypeAliasesPackage("cn.net.yzl.staff.pojo");
            
            //设置mapper.xml文件所在位置
            for (String mapperLocation : mapperLocations) {
                Resource[] resources = new PathMatchingResourcePatternResolver().getResources(mapperLocation);
                sessionFactoryBean.setMapperLocations(resources);

            }
         //设置mybatis-config.xml配置文件位置
            sessionFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource(configLocation));

            //添加分页插件、打印sql插件
            Interceptor[] plugins = new Interceptor[]{pageHelper(),new SqlPrintInterceptor()};
            sessionFactoryBean.setPlugins(plugins);
            
            return sessionFactoryBean.getObject();
        } catch (IOException e) {
            log.error("mybatis resolver mapper*xml is error",e);
            return null;
        } catch (Exception e) {
            log.error("mybatis sqlSessionFactoryBean create error",e);
            return null;
        }
    }

    /**
     * 分页插件
     * @return
     */
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        p.setProperty("returnPageInfo", "check");
        p.setProperty("params", "count=countSql");
        pageHelper.setProperties(p);
        return pageHelper;
    }



    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
    	return new SqlSessionTemplate(sqlSessionFactory);
    }
    

    
}
