package com.yitu.cpcFounding.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitu.cpcFounding.api.domain.DangHistory;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

/**
 * 党史Mapper
 * @author shenjun
 * @date 2021-06-03 10:24:25
 */
@Mapper
public interface DangHistoryMapper extends BaseMapper<DangHistory>{


}
