package xyz.wongs.weathertop.palant.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/** 查询Vo对象
 * @ClassName QueryVo
 * @Description 
 * @author WCNGS@QQ.COM
 * @Github <a>https://github.com/rothschil</a>
 * @date 2019/10/25 23:16
 * @Version 1.0.0
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryVo {

    /**
     * 索引名
     */
    private String idxName;
    /**
     * 需要反射的实体类型，用于对查询结果的封装
     */
    private String className;
    /**
     * 具体条件
     */
    private Map<String,Map<String,Object>> query;
}
