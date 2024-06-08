package com.yitu.cpcFounding.api.service.impl;

import com.yitu.cpcFounding.api.utils.HttpUtil;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.domain.OperationLog;
import com.yitu.cpcFounding.api.enums.OperationLogEnum;
import com.yitu.cpcFounding.api.mapper.OperationLogMapper;
import com.yitu.cpcFounding.api.service.OperationLogService;
import com.yitu.cpcFounding.api.vo.UserVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 操作日志服务
 *
 * @author qinmingtong
 * @date 2021/1/27 9:48
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Resource
    private OperationLogMapper operationLogMapper;

    @Resource
    private LoginUserUtil loginUserUtil;

    @Resource
    private HttpServletRequest request;

    /**
     * 备注最大长度
     */
    private static final int REMARK_MAX_LENGTH = 8000;

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
    @Override
    public int addOperationLog(Long relateId, OperationLogEnum operationLogEnum, String remark) {
        OperationLog operationLog = new OperationLog();
        operationLog.setRelateId(relateId);
        operationLog.setType(operationLogEnum.getCode());
        if(remark == null){
            remark = operationLogEnum.getName();
        }
        if(remark.length() > REMARK_MAX_LENGTH){
            remark = remark.substring(0,REMARK_MAX_LENGTH);
        }
        operationLog.setRemark(remark);
        operationLog.setIp(HttpUtil.getIPAddress(request));
        UserVO userVO = loginUserUtil.getLoginUser();
        operationLog.setAddUser(userVO.getWxUserName());
        operationLog.setAddDate(LocalDateTime.now());
        operationLog.setModifyUser(operationLog.getAddUser());
        operationLog.setModifyDate(operationLog.getAddDate());
        return operationLogMapper.insert(operationLog);
    }

}
