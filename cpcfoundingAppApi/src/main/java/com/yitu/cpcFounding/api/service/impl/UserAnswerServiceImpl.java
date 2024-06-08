package com.yitu.cpcFounding.api.service.impl;

import com.yitu.cpcFounding.api.mapper.UserAnswerMapper;
import com.yitu.cpcFounding.api.service.CommonService;
import com.yitu.cpcFounding.api.service.UserAnswerService;
import com.yitu.cpcFounding.api.vo.answer.UserAnswerCountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户答题表服务实现
 *
 * @author shenjun
 * @date 2021-02-24 10:15:22
 */
@Service
public class UserAnswerServiceImpl implements UserAnswerService {

    @Autowired
    private UserAnswerMapper userAnswerMapper;
    @Autowired
    private CommonService commonService;

    /**
     * @return com.yitu.mayday.api.vo.answer.UserAnswerCountVO
     * @Description: 获取答题数量和头像
     * @author liuzhaowei
     * @date 2021/4/10
     */
    @Override
    public UserAnswerCountVO getAnswerCountAndHead() {
        UserAnswerCountVO userAnswerCountVO = new UserAnswerCountVO();
        long count = commonService.getUserAnswerCount();
        List<String> headList = commonService.getUserAnswerHeaderList();
        userAnswerCountVO.setCount(count);
        userAnswerCountVO.setHeadList(headList);
        return userAnswerCountVO;
    }
}
