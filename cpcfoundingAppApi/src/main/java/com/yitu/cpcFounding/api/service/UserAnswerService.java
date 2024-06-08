package com.yitu.cpcFounding.api.service;

import com.yitu.cpcFounding.api.vo.answer.UserAnswerCountVO;

/**
 * 用户答题表服务
 *
 * @author shenjun
 * @date 2021-02-24 10:15:22
 */
public interface UserAnswerService {

    /**
     * @param
     * @return com.yitu.mayday.api.vo.answer.UserAnswerCountVO
     * @Description: 获取答题数量和头像
     * @author liuzhaowei
     * @date 2021/4/10
     */
    UserAnswerCountVO getAnswerCountAndHead();
}
