package com.yitu.cpcFounding.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yitu.cpcFounding.api.domain.Config;

import java.util.List;

/**
 * 配置服务
 *
 * @author pangshihe
 * @date 2021/1/21
 */
public interface ConfigService extends IService<Config> {

    /**
     * 获取配置表数据
     *
     * @param type 类型
     * @return java.util.List<LsConfig>
     * @author pangshihe
     * @date 2021/1/21 11:25
     */
    List<Config> getConfigListByType(int type);

    /**
     * 获取配置表数据
     *
     * @param type  类型
     * @param keyId keyid
     * @return java.util.List<LsConfig>
     * @author shenjun
     * @date 2021/1/23 16:01
     */
    Config getConfigByTypeAndKey(int type, String keyId);

    /**
     * 获取配置
     *
     * @param id id
     * @return LsConfig
     * @author shenjun
     * @date 2021/1/25 9:56
     */
    Config getConfigById(long id);

    /**
     * 获取积分周榜时间起始列表
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author qixinyi
     * @date 2021/6/9 16:23
     */
    List<String> findTimes();
}
