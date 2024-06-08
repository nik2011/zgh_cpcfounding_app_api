package com.yitu.cpcFounding.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yitu.cpcFounding.api.domain.QuestionLibraryItem;
import com.yitu.cpcFounding.api.mapper.QuestionLibraryItemMapper;
import com.yitu.cpcFounding.api.service.QuestionLibraryItemService;
import com.yitu.cpcFounding.api.vo.answer.LsQuestionLibraryItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 题库选项表服务实现
 *
 * @author shenjun
 * @date 2021-02-24 10:15:12
 */
@Service
public class QuestionLibraryItemServiceImpl implements QuestionLibraryItemService {

    @Autowired
    private QuestionLibraryItemMapper questionLibraryItemMapper;


    /**
     * @param questionIds
     * @return java.util.List<LsQuestionLibraryItemVO>
     * @Description: 获取题目选项列表
     * @author liuzhaowei
     * @date 2021/2/24
     */
    @Override
    public List<LsQuestionLibraryItemVO> getQuestionLibraryItemList(List<Long> questionIds) {
        List<LsQuestionLibraryItemVO> resultList = new ArrayList<>();
        if (questionIds.size() == 0) {
            return resultList;
        }
        List<QuestionLibraryItem> questionLibraryItemList = questionLibraryItemMapper.selectList(new LambdaQueryWrapper<QuestionLibraryItem>()
                .eq(QuestionLibraryItem::getDeleted, 0)
                .in(QuestionLibraryItem::getQuestionId, questionIds)
        );
        for (QuestionLibraryItem item : questionLibraryItemList) {
            LsQuestionLibraryItemVO libraryItemVO = new LsQuestionLibraryItemVO();
            libraryItemVO.setId(item.getId());
            libraryItemVO.setQuestionId(item.getQuestionId());
            libraryItemVO.setName(item.getName());
            libraryItemVO.setNumber(item.getNumber().toUpperCase());
            resultList.add(libraryItemVO);
        }
        return resultList;
    }

}
