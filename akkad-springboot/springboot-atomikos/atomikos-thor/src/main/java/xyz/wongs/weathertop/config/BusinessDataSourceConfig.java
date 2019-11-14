package xyz.wongs.weathertop.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = BusinessDataSourceConfig.PACKAGE,markerInterface = BaseMapper.class, sqlSessionFactoryRef = "businessSqlSessionFactory")
public class BusinessDataSourceConfig {

	static final String PACKAGE = "xyz.wongs.weathertop.mapper.location";

	@Autowired
	@Qualifier("businessDataSource")
	private DataSource ds;

	@Bean(name = "businessSqlSessionFactory")
	public SqlSessionFactory businessSqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(ds);
		//指定mapper xml目录
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mapper/business/**/*.xml"));
		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplateBusiness() throws Exception {
		SqlSessionTemplate template = new SqlSessionTemplate(businessSqlSessionFactory()); // 使用上面配置的Factory
		return template;
	}

}
