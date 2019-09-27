package xyz.wongs.weathertop.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="spring.datasource.druid.business-db")
public class BusinessProperties {

	private String url;

	private String username;

	private String password;

	private Integer initialSize;

	private Integer minIdle;

	private Integer maxActive;

	private Integer maxWait;

	private Integer timeBetweenEvictionRunsMillis;

	private Integer minEvictableIdleTimeMillis;

	private String validationQuery;

	private Integer validationQueryTimeout;

	private boolean testWhileIdle;

	private boolean testOnBorrow;

	private boolean testOnReturn;

	private boolean poolPreparedStatements;

	private Integer maxPoolPreparedStatementPerConnectionSize;

	private String connectionProperties;

	private boolean useGlobalDataSourceStat;

	private String filters;

}
