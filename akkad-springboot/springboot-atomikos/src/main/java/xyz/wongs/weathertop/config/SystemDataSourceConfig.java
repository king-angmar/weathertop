package xyz.wongs.weathertop.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import xyz.wongs.weathertop.config.prop.SystemProperties;
import xyz.wongs.weathertop.util.PojoUtil;

import javax.sql.DataSource;


@Configuration
@MapperScan(basePackages = SystemDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "systemSqlSessionFactory")
public class SystemDataSourceConfig {

	static final String PACKAGE = "xyz.wongs.weathertop.mapper.quanmin";

	@Autowired
	private SystemProperties systemProperties;

	@Bean(name = "systemDataSource")
	@Primary
	public DataSource systemDataSource() {
		AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
		ds.setXaProperties(PojoUtil.obj2Properties(systemProperties));
		ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
		ds.setUniqueResourceName("systemDataSource");
		ds.setPoolSize(5);
		ds.setTestQuery("SELECT 1");
		return ds;
	}

	@Bean
	@Primary
	public SqlSessionFactory systemSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(systemDataSource());
		return sqlSessionFactoryBean.getObject();
	}

}
