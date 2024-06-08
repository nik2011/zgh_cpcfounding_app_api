package com.yitu.cpcFounding.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitu.cpcFounding.api.domain.Config;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 配置表Mapper
 * @author shenjun
 * @date 2021-01-21 11:02:18
 */
@Mapper
public interface ConfigMapper extends BaseMapper<Config>{
    /**
     * 获取积分周榜时间起始列表
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author qixinyi
     * @date 2021/6/9 16:23
     */
    List<String> selectTimes();
}
