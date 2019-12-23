package xyz.wongs.weathertop.base.persistence.mybatis.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
@PropertySource("classpath:config.properties")
@ConfigurationProperties(prefix = "page")
public class PaginationInfo {

    private int pageNum;

    private int pageSize;

}
