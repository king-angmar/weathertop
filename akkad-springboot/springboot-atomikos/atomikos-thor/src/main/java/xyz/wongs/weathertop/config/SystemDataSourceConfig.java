package xyz.wongs.weathertop.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;

import javax.sql.DataSource;


@Configuration
@MapperScan(basePackages = SystemDataSourceConfig.PACKAGE,markerInterface = BaseMapper.class, sqlSessionFactoryRef = "systemSqlSessionFactory")
public class SystemDataSourceConfig {

	static final String PACKAGE = "xyz.wongs.weathertop.mapper.quanmin";


	@Autowired
	@Qualifier("systemDataSource")
	private DataSource ds;

//	@Bean(name = "systemDataSource")
//	@Primary
//	public DataSource systemDataSource() {
//		AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
//		ds.setXaProperties(PojoUtil.obj2Properties(systemProperties));
//		ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
//		ds.setUniqueResourceName("systemDataSource");
//		ds.setPoolSize(5);
//		ds.setTestQuery("SELECT 1");
//		return ds;
//	}

	@Bean
	@Primary
	public SqlSessionFactory systemSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(ds);
		//指定mapper xml目录
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/system/**/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

//	@Bean(name = "systemSqlSessionTemplate")
//	public SqlSessionTemplate sqlSessionTemplateCarInfo(
//			@Qualifier("systemSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//		return new SqlSessionTemplate(sqlSessionFactory);
//	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate() throws Exception {
		SqlSessionTemplate template = new SqlSessionTemplate(systemSqlSessionFactory()); // 使用上面配置的Factory
		return template;
	}
}
