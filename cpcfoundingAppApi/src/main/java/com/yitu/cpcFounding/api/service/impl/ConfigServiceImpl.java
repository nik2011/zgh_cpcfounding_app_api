package com.yitu.cpcFounding.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.domain.Config;
import com.yitu.cpcFounding.api.enums.DeletedEnum;
import com.yitu.cpcFounding.api.mapper.ConfigMapper;
import com.yitu.cpcFounding.api.redis.RedisUtil;
import com.yitu.cpcFounding.api.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author pangshihe
 * @date 2021/1/21
 */
@Slf4j
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
    @Resource
    ConfigMapper configMapper;
    @Resource
    RedisUtil redisUtil;

    /**
     * 获取配置表数据
     *
     * @param type 类型
     * @return java.util.List<LsConfig>
     * @author pangshihe
     * @date 2021/1/21 11:25
     */
    @Override
    public List<Config> getConfigListByType(int type) {
        List<Config> configList = new ArrayList<>();
        Gson gson = new  GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Object object = redisUtil.get(Configs.CONFIG_TYPE + type);
        if (object != null) {
            try {
                configList = gson.fromJson(object.toString(), new TypeToken<List<Config>>() {
                }.getType());
                return configList;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else {
            QueryWrapper<Config> queryWrapper = new QueryWrapper<Config>().select("id", "type", "key_id", "key_value","order_index")
                                                                              .eq("type", type)
                                                                              .eq("deleted", DeletedEnum.NOT_DELETE.getValue())
                                                                              .orderByAsc("order_index");
            configList = configMapper.selectList(queryWrapper);
            if (CollectionUtils.isNotEmpty(configList)) {
                redisUtil.set(Configs.CONFIG_TYPE + type, gson.toJson(configList));
            }
        }
        return configList;
    }

    /**
     * 获取配置表数据
     *
     * @param type  类型
     * @param keyId id
     * @return java.util.List<LsConfig>
     * @author shenjun
     * @date 2021/1/23 16:03
     */
    @Override
    public Config getConfigByTypeAndKey(int type, String keyId) {
        Config config = null;
        Gson gson = new  GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Object object = redisUtil.get(Configs.CONFIG_TYPE_KEY + type +"_"+ keyId);
        if (object != null) {
            try {
                config = gson.fromJson(object.toString(), new TypeToken<Config>() {
                }.getType());
                return config;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else {
            QueryWrapper<Config> lsConfigQueryWrapper = new QueryWrapper<>();
            lsConfigQueryWrapper.select("id", "type", "key_id", "key_value", "order_index")
                    .eq("deleted", DeletedEnum.NOT_DELETE.getValue())
                    .eq("type", type)
                    .eq("key_id", keyId);
            List<Config> configs = configMapper.selectList(lsConfigQueryWrapper);
            if (CollectionUtils.isNotEmpty(configs)) {
                config = configs.get(0);
                redisUtil.set(Configs.CONFIG_TYPE_KEY + type+"_"+keyId, gson.toJson(config));
            }
        }
        return config;
    }

    /**
     * 获取配置
     *
     * @param id id
     * @return LsConfig
     * @author shenjun
     * @date 2021/1/25 9:56
     */
    @Override
    public Config getConfigById(long id) {
        Config config = null;
        Gson gson = new  GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Object object = redisUtil.get(Configs.CONFIG_ID + id);
        if (object != null) {
            try {
                config = gson.fromJson(object.toString(), new TypeToken<Config>() {
                }.getType());
                return config;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else {
            QueryWrapper<Config> lsConfigQueryWrapper = new QueryWrapper<>();
            lsConfigQueryWrapper.eq("deleted", DeletedEnum.NOT_DELETE.getValue())
                                .eq("id", id);
            List<Config> configs = configMapper.selectList(lsConfigQueryWrapper);
            if (CollectionUtils.isNotEmpty(configs)) {
                config = configs.get(0);
                redisUtil.set(Configs.CONFIG_ID + id, gson.toJson(config));
            }
        }
        return config;
    }

    /**
     * 获取积分周榜时间起始列表
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author qixinyi
     * @date 2021/6/9 16:23
     */
    @Override
    public List<String> findTimes() {
        return configMapper.selectTimes();
    }
}
