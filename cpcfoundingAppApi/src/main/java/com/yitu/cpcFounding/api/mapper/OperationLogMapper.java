package com.yitu.cpcFounding.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitu.cpcFounding.api.domain.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志持久层
 *
 * @author qinmingtong
 * @date 2021/1/27 9:40
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {

}
