package xyz.wongs.weathertop.war3.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.wongs.weathertop.base.constant.Constants;
import xyz.wongs.weathertop.base.persistence.mybatis.mapper.BaseMapper;
import xyz.wongs.weathertop.base.persistence.mybatis.service.BaseService;
import xyz.wongs.weathertop.base.utils.StringUtils;
import xyz.wongs.weathertop.base.utils.text.Convert;
import xyz.wongs.weathertop.war3.common.constant.UserConstants;
import xyz.wongs.weathertop.war3.common.utils.CacheUtils;
import xyz.wongs.weathertop.war3.system.entity.SysConfig;
import xyz.wongs.weathertop.war3.system.mapper.SysConfigMapper;

import java.util.List;


@Service
public class SysConfigService extends BaseService<SysConfig, Long> {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    /**
     * 清空缓存数据
     */
    public void clearCache() {
        CacheUtils.removeAll(getCacheName());
    }

    /**
     * 批量删除参数配置对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteConfigByIds(String ids) {
        int count = sysConfigMapper.deleteConfigByIds(Convert.toStrArray(ids));
        if (count > 0) {

            CacheUtils.removeAll(getCacheName());
        }
        return count;
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    public String checkConfigKeyUnique(SysConfig config) {
        Long configId = StringUtils.isNull(config.getId()) ? -1L : config.getId();
        SysConfig info = sysConfigMapper.checkConfigKeyUnique(config.getConfigKey());
        if (StringUtils.isNotNull(info) && info.getId().longValue() != configId.longValue()) {
            return UserConstants.CONFIG_KEY_NOT_UNIQUE;
        }
        return UserConstants.CONFIG_KEY_UNIQUE;
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    public List<SysConfig> selectConfigList(SysConfig config) {
        return sysConfigMapper.selectConfigList(config);
    }

    /**
     * @param null
     * @return
     * @throws
     * @Description 获取cache name
     * @date 2020/6/20 12:46
     */
    private String getCacheName() {
        return Constants.SYS_CONFIG_CACHE;
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return Constants.SYS_CONFIG_KEY + configKey;
    }

    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(CacheUtils.get(getCacheName(), getCacheKey(configKey)));
        if (StringUtils.isNotEmpty(configValue)) {
            return configValue;
        }
        SysConfig config = new SysConfig();
        config.setConfigKey(configKey);
        SysConfig retConfig = sysConfigMapper.selectConfig(config);
        if (StringUtils.isNotNull(retConfig)) {
            CacheUtils.put(getCacheName(), getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    @Override
    protected BaseMapper<SysConfig, Long> getMapper() {
        return sysConfigMapper;
    }
}