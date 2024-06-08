package com.yitu.cpcFounding.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitu.cpcFounding.api.domain.Msg;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户短信通知表Mapper
 * @author shenjun
 * @date 2021-01-21 11:02:18
 */
@Mapper
public interface ShareMsgMapper extends BaseMapper<Msg>{

}
