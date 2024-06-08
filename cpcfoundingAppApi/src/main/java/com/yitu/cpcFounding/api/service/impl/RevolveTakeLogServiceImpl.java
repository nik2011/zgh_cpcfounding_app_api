package com.yitu.cpcFounding.api.service.impl;

import com.yitu.cpcFounding.api.domain.RevolveTakeLog;
import com.yitu.cpcFounding.api.enums.prize.ShakeTakeLogTypeEnum;
import com.yitu.cpcFounding.api.mapper.RevolveTakeLogMapper;
import com.yitu.cpcFounding.api.service.RevolveTakeLogService;
import com.yitu.cpcFounding.api.utils.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 用户摇奖消费日志表服务实现
 *
 * @author shenjun
 * @date 2021-01-21 12:01:28
 */
@Service
public class RevolveTakeLogServiceImpl implements RevolveTakeLogService {

    @Autowired
    private RevolveTakeLogMapper shakeTakeLogMapper;
    @Resource
    private HttpServletRequest request;

    /**
     * @param type
     * @param userPrizeId
     * @param userId
     * @param userName
     * @return int
     * @Description: 添加记录
     * @author liuzhaowei
     * @date 2021/1/21
     */
    @Override
    public int insertRecord(ShakeTakeLogTypeEnum type, int inOrOut, long userPrizeId, long userId, String userName) {
        RevolveTakeLog takeLog = new RevolveTakeLog();
        takeLog.setType(type.getCode());
        takeLog.setUserPrizeId((int) userPrizeId);
        takeLog.setUserId((int) userId);
        takeLog.setUserName(userName);
        takeLog.setInOrOut(inOrOut);
        takeLog.setDeleted(0);
        takeLog.setAddUser(userName);
        takeLog.setAddDate(new Date());
        takeLog.setModifyUser(userName);
        takeLog.setModifyDate(new Date());
        takeLog.setIp(HttpUtil.getIPAddress(request));

        return shakeTakeLogMapper.insert(takeLog);
    }

    /**
     * @param type
     * @param inOrOut
     * @param userPrizeId 用户奖品id
     * @param userId
     * @param userName
     * @return int
     * @Description: 封装实体
     * @author liuzhaowei
     * @date 2021/1/22
     */
    @Override
    public RevolveTakeLog packageEntity(ShakeTakeLogTypeEnum type, int inOrOut, long userPrizeId, long userId, String userName) {
        RevolveTakeLog takeLog = new RevolveTakeLog();
        takeLog.setType(type.getCode());
        takeLog.setUserPrizeId((int) userPrizeId);
        takeLog.setUserId((int) userId);
        takeLog.setUserName(userName);
        takeLog.setInOrOut(inOrOut);
        takeLog.setDeleted(0);
        takeLog.setAddUser(userName);
        takeLog.setAddDate(new Date());
        takeLog.setModifyUser(userName);
        takeLog.setModifyDate(new Date());
        takeLog.setIp(HttpUtil.getIPAddress(request));
        return takeLog;
    }
}
