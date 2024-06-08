package com.yitu.cpcFounding.api.service;


import com.yitu.cpcFounding.api.enums.OperationLogEnum;

/**
 * 操作日志服务
 *
 * @author qinmingtong
 * @date 2021/1/27 9:48
 */
public interface OperationLogService {

    /**
     * 添加操作日志
     *
     * @param relateId         关联表外id
     * @param operationLogEnum 操作类型枚举
     * @param remark           备注
     * @return int
     * @author qinmingtong
     * @date 2021/1/27 10:09
     */
    int addOperationLog(Long relateId, OperationLogEnum operationLogEnum, String remark);

}
